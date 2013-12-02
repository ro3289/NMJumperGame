package com.jumpergame.Scene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.andengine.util.math.MathUtils;
import org.xml.sax.Attributes;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.util.SparseArray;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import com.jumpergame.Item;
import com.jumpergame.Player;
import com.jumpergame.StoreItem;
import com.jumpergame.Manager.SceneManager;
import com.jumpergame.Manager.SceneManager.SceneType;
import com.jumpergame.body.Sprite_Body;
import com.jumpergame.constant.GeneralConstants;


public class GameScene extends BaseScene implements IOnSceneTouchListener, IOnAreaTouchListener,GeneralConstants,IAccelerationListener
{
	public HUD gameHUD;
	final GameScene gc = this;
	
	// Score
	private Text scoreText;
	private int score = 0;
	private SparseArray<Text> mScoreTextMap;
	// Money
	private Text moneyText;
	private boolean buyItem = false;
	//energy
	private ArrayList<Rectangle> mPlayerEnergies;
	//bullets
	private final HashMap<Integer, Rectangle> mBullets = new HashMap<Integer, Rectangle>();
	
	// jump setting
    private Vector2 initVector;
    private Vector2 endVector;
    private boolean jumpState = false;
	
	// Physics 
	private PhysicsWorld physicsWorld;
	
	// Level loader
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	    
	// Staircase
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
	
	// Stuff
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN 		= "coin";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ACID 		= "acid";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GLUE 		= "glue";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TOOL 		= "tool";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENERGY 		= "energy";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_INVISIBLE 	= "invisible";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_INVINCIBLE 	= "invincible";
	
	// Player
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";

	private Player player;
	private Player dummy;
	private ArrayList<Player> mPlayers;
	private int thisID;
	private int dummyID;
	private Sprite mArrow;
	private int mBulletCount = 0;
	
	private Vector2 initBodyVector;
    private Vector2 endBodyVector;
    
    private float mGravityX = 0;
    private float mGravityY = -10.0f;
	

	// Item
	private Item dragItem;
	private StoreItem  currentDragItem;
	private StoreItem  currentFloatingItem;
	private HashMap<ItemType, StoreItem> itemMap;

	public enum ItemType
	{
		// Money
		COIN,
		// Attack items
		ACID,
		GLUE,
		TOOL,
		// Self items
		ENERGY_DRINK,
		INVISIBLE_DRINK,
		INVINCIBLE_DRINK,
		BUY_BUTTON;
	}
	
	// Create Item HUD
	private void createHUD()
	{
		mScoreTextMap = new SparseArray<Text>();
		mPlayerEnergies = new ArrayList<Rectangle>();
		System.out.println("3");
		gameHUD = new HUD();
		
		// Load Item
		loadItem();
		
		// Set Score 
		Text thisScoreText = new Text(20, 720, resourcesManager.mScoreFont, "Score: 0", 50, new TextOptions(HorizontalAlign.LEFT), vbom);
        Text dummyScoreText = new Text(20, 680, resourcesManager.mScoreFont, "Player2 Score: 0", 50, new TextOptions(HorizontalAlign.LEFT), vbom);
        thisScoreText.setAnchorCenter(0, 0);    
        dummyScoreText.setAnchorCenter(0, 0);    
        gameHUD.attachChild(thisScoreText);
        gameHUD.attachChild(dummyScoreText);
        
        mScoreTextMap.put(thisID, thisScoreText);
        mScoreTextMap.put(dummyID, dummyScoreText);
        // Set Money
        moneyText = new Text(250, 720, resourcesManager.mScoreFont, "Money: 0", 20, new TextOptions(HorizontalAlign.RIGHT), vbom);
        moneyText.setAnchorCenter(0, 0);
        gameHUD.attachChild(moneyText);
        
        /**
         *   energy
         */
        
        System.out.println("3");
        int i = 0;
        for (Player p : mPlayers) {
            Rectangle energy = new Rectangle(ENERGY_BAR_POS_X, ENERGY_BAR_POS_Y-ENERGY_BAR_POS_Y_GAP*i, p.getEnergy(), ENERGY_BAR_HEIGHT, vbom);
            energy.setAnchorCenterX(0);
            energy.setColor(Color.RED);
            mPlayerEnergies.add(energy);
            gameHUD.attachChild(energy);
            
            i++;
        }

	    camera.setHUD(gameHUD);
	}
	// @Bosh
	// Item System
	private void loadItem() {
		itemMap = new HashMap<ItemType, StoreItem>();
		createAttackItem(30,  50, ItemType.ACID, 300, resourcesManager.acid_region);
		createAttackItem(100, 50, ItemType.GLUE, 500, resourcesManager.glue_region);
		createAttackItem(170, 50, ItemType.TOOL, 800, resourcesManager.tool_region);
		createStoreItem(240, 50, ItemType.ENERGY_DRINK, 200, resourcesManager.energy_region);
		createStoreItem(310, 50, ItemType.INVISIBLE_DRINK, 500, resourcesManager.invisible_region);
		createStoreItem(380, 50, ItemType.INVINCIBLE_DRINK, 1000, resourcesManager.invincible_region);
		createStoreButton(450, 50, ItemType.BUY_BUTTON, resourcesManager.button_region);
	}
	private void createAttackItem(final float x, final float y, final ItemType type, final int price, final ITextureRegion itemTextureRegion)
	{	
		StoreItem item= new StoreItem(this, x, y, type, price, itemTextureRegion, vbom)
		{
			 @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionDown() && dragItem == null)
		        {	
		        	if(buyItem){
	        			this.buyStoreItem();
		        	buyItem = false;
		        	// scale down modifier of button
		        	}
		        	else
		        	{
		        		if(getItemAmount() > 0 )
			        	{
			        		currentDragItem = this;
				        	dragItem = new Item(gc, x, y, type, itemTextureRegion, vbom);
				            gameHUD.attachChild(dragItem);
			        	}		        	
		        	}
		        }
		        return true;
		    };
			    
		};
		itemMap.put(type, item);
		gameHUD.registerTouchArea(item);
		gameHUD.attachChild(item);
	}
	private void createStoreItem(final float x, final float y, final ItemType type,final int p, final ITextureRegion itemTextureRegion)
	{	
		StoreItem item= new StoreItem(this, x, y, type, p, itemTextureRegion, vbom)
		{
			 @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionDown())
		        {
		        	if(buyItem){
		        			this.buyStoreItem();
			        	buyItem = false;
			        	// scale down modifier of button
		        	}
		        	else{
		        		if (this.getItemAmount() > 0)
		        		{
	        				this.useStoreItem();
		        		}
		        	}
		        }
		        return true;
		    };
			    
		};
		itemMap.put(type, item);
		gameHUD.registerTouchArea(item);
		gameHUD.attachChild(item);
	}
	private void createStoreButton(final float x, final float y, final ItemType type,final ITextureRegion itemTextureRegion)
	{	
		Item item= new Item(this, x, y, type, itemTextureRegion, vbom)
		{
			 @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionDown())
		        {
		        	if(buyItem){
			        	buyItem = false;
			        	// scale down modifier of button
		        	}
		        	else{
		        		buyItem = true;
		        		// scale up modifier of button
		        	}
		        }
		        return true;
		    };
			    
		};
		gameHUD.registerTouchArea(item);
		gameHUD.attachChild(item);
	}
	private void createPhysics()
	{
	    physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -SensorManager.GRAVITY_EARTH), false); 
	    physicsWorld.setContactListener(contactListener());
	    registerUpdateHandler(physicsWorld);
	}
	
    private void createBackground() {
    	setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
	}
	
    @Override
    public void createScene()
    {
    	createBackground();	    
	    createPhysics();
	    loadLevel(1);
	    createHUD();
	    
	    setOnSceneTouchListener(this);
	    setOnAreaTouchListener(this);
    }

    @Override
    public void onBackKeyPressed()
    {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	camera.setHUD(null);
        camera.setCenter(240, 400);

        // TODO code responsible for disposing scene
        // removing all game scene objects.
    }
    
    private void loadLevel(int levelID)
    {
    	final Rectangle ground = new Rectangle(240, 50, 480, 100, vbom);
        final Rectangle left = new Rectangle(5, 480*5, 10, 480*10, vbom);
        final Rectangle right = new Rectangle(480 - 5, 450*5, 10, 480*10, vbom);

        mPlayers = new ArrayList<Player>();
        PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, GROUND_AND_STAIR_FIXTURE_DEF).setUserData(new Sprite_Body(ground, "Wall"));
        PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, WALL_FIXTURE_DEF).setUserData(new Sprite_Body(left, "Wall"));
        PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, WALL_FIXTURE_DEF).setUserData(new Sprite_Body(right, "Wall"));
        attachChild(ground);
        attachChild(left);
        attachChild(right);
    	System.out.println("4");
    	
    	final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
        
        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
        
        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
            {
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
                
                camera.setBounds(0, 0, width, height); // here we set camera bounds
                camera.setBoundsEnabled(true);

                return GameScene.this;
            }
        });
        System.out.println("7");
        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
                
                final Sprite levelObject;
                
                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, GROUND_AND_STAIR_FIXTURE_DEF);
                    body.setUserData(new Sprite_Body(levelObject, "Wall"));
                } 
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData(new Sprite_Body(levelObject,"platform2"));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData(new Sprite_Body(levelObject,"platform3"));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
                {
                    levelObject = new Item(gc, x, y, ItemType.COIN, resourcesManager.coin_region, vbom);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ACID))
                {
                	levelObject = createFloatingItem(x, y, ItemType.ACID, resourcesManager.acid_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GLUE))
                {
                	levelObject = createFloatingItem(x, y, ItemType.GLUE, resourcesManager.glue_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TOOL))
                {
                	levelObject = createFloatingItem( x, y, ItemType.TOOL, resourcesManager.tool_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENERGY))
                {
                	levelObject = createFloatingItem( x, y, ItemType.ENERGY_DRINK, resourcesManager.energy_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_INVISIBLE))
                {
                	levelObject = createFloatingItem(x, y, ItemType.INVISIBLE_DRINK, resourcesManager.invisible_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_INVINCIBLE))
                {
                	levelObject = createFloatingItem(x, y, ItemType.INVINCIBLE_DRINK, resourcesManager.invincible_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
                {
                    player = new Player(x, y, vbom, camera, physicsWorld,"thisPlayer",1)
                    {
                        @Override
                        public void onDie()
                        {
                            // TODO Latter we will handle it.
                        }
                        
                    };
                    // registerTouchArea(player);
                    // camera.setChaseEntity(player);
                    thisID = mPlayers.size();
                    mPlayers.add(player);
                    levelObject = player;
                }
                else
                {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }

			private Sprite createFloatingItem( int x, int y, ItemType type, ITextureRegion region) {
				Sprite object = new Item(gc, x, y, type, region, vbom);
				if(type != ItemType.COIN){
					currentFloatingItem = itemMap.get(type);
	                currentFloatingItem.plusItem();
				}
				return object;
			}

        });
        System.out.println("5");
        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
        System.out.println("6");
        
        mArrow = new Sprite(240, 400, resourcesManager.mDirectionTextureRegion, vbom);
		dummy = new Player(200, 200, vbom, camera, physicsWorld,"dummy",2)
		{

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}
			@Override 
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionUp() && dragItem != null)
		        {
		        	System.out.println("Dummy attacked!");
		        	currentDragItem.useStoreItem();
		        	currentDragItem = null;
		        	gameHUD.detachChild(dragItem);
		        	dragItem.dispose();
		        	dragItem = null;
		        //	DRAG_ITEM = false;
		        }
		        return true;
		    };
		};
		this.registerTouchArea(dummy);
		this.attachChild(dummy);
		System.out.println("7");
        dummyID = mPlayers.size();
        System.out.println("8");
        mPlayers.add(dummy);
        System.out.println("9");
    }
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		if(physicsWorld != null ) {
            if(pSceneTouchEvent.isActionDown() && dragItem == null) {   
                // Record initial position
                initVector = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                // Show Direction
                mArrow.setPosition(player.getX(), player.getY() +40 );
                activity.runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        attachChild(mArrow);
                    }
                });
            }
            else if (pSceneTouchEvent.isActionUp()) {
                if(!jumpState) {
                	if(dragItem == null)
                	{
	                    // Eject object
	                    final float deltaX = initVector.x - pSceneTouchEvent.getX();
	                    final float deltaY = initVector.y - pSceneTouchEvent.getY();
	                    if (deltaX < 50.0 && deltaY < 50.0) {
	                        shoot(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
	                    } else {
	                        endVector = new Vector2(initVector.x - pSceneTouchEvent.getX(), initVector.y - pSceneTouchEvent.getY());
	                        final float velocityX = endVector.x;
	                        final float velocityY = endVector.y;
	                        final int velocityFactor = getUser().getVelocityFactor();
	                        final Vector2 velocity = Vector2Pool.obtain(velocityFactor * velocityX *0.01f, velocityFactor * velocityY * 0.01f);
	                        player.returnBody().setLinearVelocity(velocity);
	                        Vector2Pool.recycle(velocity);
	                    // Record initial jump position
	                        initBodyVector = new Vector2(player.returnBody().getPosition().x, player.returnBody().getPosition().y);
	                        jumpState 	 = true;
	                    }
                	}
                }
                // Item 
                if(dragItem != null)
    			{
                	System.out.println("release item!!");
    				gameHUD.detachChild(dragItem);
    				dragItem.dispose();
    				dragItem = null;
    			}
                // Detach arrow 
                activity.runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        detachChild(mArrow);
                        System.out.println("aaaa");
                    }
                });
                return true;
            }
            else if(pSceneTouchEvent.isActionMove()) {
            	if(dragItem == null) // Problem here when store items used!!!!!
            	{
	                endVector = new Vector2(initVector.x - pSceneTouchEvent.getX(), initVector.y - pSceneTouchEvent.getY());
	                final float dX = endVector.x;
	                final float dY = endVector.y;
	                final float angle = (float) Math.atan2( dX, dY);
	                final float rotation = MathUtils.radToDeg(angle);
	                mArrow.setRotation(rotation);
            	}
            	else
    			{
    				dragItem.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
    			}
            }
            
            return true;
        }
        return false;
	}

	private ContactListener contactListener()
	{
	    ContactListener contactListener = new ContactListener()
	    {
	        public void beginContact(Contact contact)
	        {
	            final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();
                final Body BodyA = x1.getBody();
                final Body BodyB = x2.getBody();
                final float densityA = x1.getDensity();
                final float densityB = x2.getDensity();

                Sprite_Body userDataA = (Sprite_Body) BodyA.getUserData();
                Sprite_Body userDataB = (Sprite_Body) BodyB.getUserData();
                if(userDataA.getName() != null)
                {
                	System.out.println("a");
                System.out.println(userDataA.getName());
                }
                if(userDataB.getName() != null)
                {
                	System.out.println("b");
                    System.out.println(userDataB.getName());
                }
                if(BodyA.getUserData() != null)
                {
                	System.out.println("c");
                	System.out.println(BodyA.getUserData());
                }
                if(BodyB.getUserData() != null)
                {
                	System.out.println("d");
                    System.out.println(BodyB.getUserData());
                }
                if(userDataA.getName() == "dummy"||userDataB.getName()=="dummy")
                {
                	System.out.println("dummy");
                }
                if(userDataA.getName() == "thisPlayer"||userDataB.getName()=="thisPlayer")
                {
                	System.out.println("player");
                }
                
                if(userDataA.getName() == "Wall" && userDataB.getName() == "Bullet") {
                    removeEntity((IEntity)userDataB.getEntity());
                    System.out.println("wb");
                } else if(userDataA.getName() == "Bullet" && userDataB.getName() == "Wall") {
                    removeEntity((IEntity)userDataA.getEntity());
                    System.out.println("bw");
                }
                
                if(userDataA.getName() == "dummy" && userDataB.getName() == "Bullet") {
                    removeEntity((IEntity)userDataB.getEntity());
                    System.out.println("db");
                    setPlayerEnergy(1, 0, -10);
                } else if(userDataA.getName() == "Bullet" && userDataB.getName() == "dummy") {
                    removeEntity((IEntity)userDataA.getEntity());
                    System.out.println("bd");
                    setPlayerEnergy(1, 0, -10);
                }
                
                if (x1.getBody() != null && x2.getBody() != null && jumpState) {
                    if(densityA ==2 || densityB == 2) {
                        computeScore();
                        System.out.println("Contact");                        
                        	
                        Vector2 bodyVector = new Vector2(player.returnBody().getPosition().x, player.returnBody().getPosition().y);
                        Vector2 forceVector = new Vector2(0,0);
                        player.returnBody().applyForce(forceVector, bodyVector);
                        Vector2 velocity = new Vector2(0,0);
                        player.returnBody().setLinearVelocity(velocity);
                    //  Vector2Pool.recycle(velocity); // Error message: more items recycled than obtained
                        jumpState = false;
                    }
                }
	        }

	        public void endContact(Contact contact)
	        {
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();

	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("player"))
	                {
	                  //  player.decreaseFootContacts();
	                }
	            }
	        }

	        public void preSolve(Contact contact, Manifold oldManifold)
	        {

	        }

	        public void postSolve(Contact contact, ContactImpulse impulse)
	        {

	        }
	    };
	    return contactListener;
	}
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	// ===========================================================
    // Methods
    // ===========================================================
	
	private void shoot(float x, float y) {
        final Rectangle bullet;
        final Body body;
        
        bullet = new Rectangle(player.getX(), player.getY(), 10, 10, vbom);
        bullet.setColor(Color.RED);
        body = PhysicsFactory.createCircleBody(physicsWorld, bullet, BodyType.DynamicBody, BULLET_FIXTURE_DEF);
        body.setUserData(new Sprite_Body(bullet, "Bullet"));
        
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(bullet, body, true, true));
        
        bullet.setUserData(body);
        bullet.setTag(thisID);
        mBullets.put(mBulletCount++, bullet);
        attachChild(bullet);
        
        body.setLinearVelocity(bulletVelocity(player.getX(), player.getY(), x, y));
    }
	
	private Vector2 bulletVelocity(float initX, float initY, float finX, float finY) {
        float delX = finX-initX;
        float delY = finY-initY;
        float v = (float) java.lang.Math.pow(java.lang.Math.pow(delX, 2) + java.lang.Math.pow(delY, 2), 0.5);
        return new Vector2(BULLET_VELOCITY * delX / v, BULLET_VELOCITY * delY / v);
    }
	
	private void computeScore() {
        final float mBodyVelocityY = (player.returnBody().getLinearVelocity().y > 0 )? player.returnBody().getLinearVelocity().y : -player.returnBody().getLinearVelocity().y ;
        
        endBodyVector = new Vector2(player.returnBody().getPosition().x, player.returnBody().getPosition().y);
        final float jumpHeight = (endBodyVector.y - initBodyVector.y > 0) ? endBodyVector.y - initBodyVector.y:  initBodyVector.y - endBodyVector.y;
        final float jumpLength = (endBodyVector.x - initBodyVector.x > 0) ? endBodyVector.x - initBodyVector.x:  initBodyVector.x - endBodyVector.x;
        // Set score base
        final int scoreMultiplier;
        if(mBodyVelocityY <= 3) scoreMultiplier = 10;
        else if(mBodyVelocityY <= 5) scoreMultiplier = 5;
        else if(mBodyVelocityY <= 8) scoreMultiplier = 3;
        else if(mBodyVelocityY <= 11) scoreMultiplier = 1;
        else scoreMultiplier = 0;
        
        int newScore = (int) (scoreMultiplier * jumpHeight * jumpLength/10) + player.getScore();
        player.setScore(newScore);
        System.out.println("VeclocityY: "+mBodyVelocityY+'\n'+ "Jump height: " + jumpHeight + '\n'+ "Score: " + player.getScore() );
        
        updateScore(thisID, newScore);
    }
    
    public void updateScore(final int userID, final int deltaPoints) {
        final Text score = this.mScoreTextMap.get(userID);
        score.setText("Score: " + String.valueOf(deltaPoints));
    }
    
    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    public void setPlayerEnergy(final int id1, final int id2, final int deltaEnergy) {
        Player player1 = mPlayers.get(id1);
        Player player2 = mPlayers.get(id2);
        
        int originalEnergy = player1.getEnergy();
        int newEnergy = originalEnergy + deltaEnergy;
        if (newEnergy <= 0) {
            player1.setEnergy(FULL_ENERGY);
            int player2NewScore = player2.getScore() + 1000;
            player2.setScore(player2NewScore);
            updateScore(id2, player2NewScore);
            
            mPlayerEnergies.get(id1).registerEntityModifier(new SequenceEntityModifier(
                new ScaleModifier(1, 
                    (float) originalEnergy / FULL_ENERGY, 0.001f, 
                    1, 1
                ),
                new ScaleModifier(1, 0.001f, 1, 1, 1)
            ));
        }
        
        else {
            player1.setEnergy(newEnergy);
            mPlayerEnergies.get(id1).registerEntityModifier(new ScaleModifier(1, 
                (float) originalEnergy / FULL_ENERGY, (float) newEnergy / FULL_ENERGY, 
                1, 1
            ));            
        }
    }
    public void plusPlayerMoney(final int deltaMoney)
    {
    	Player player1 = mPlayers.get(0);
    	int original = player1.getMoney();
    	int newPlayerMoney = original + deltaMoney;
    	player1.setMoney(newPlayerMoney);
    	moneyText.setText("Money: "+ String.valueOf(newPlayerMoney));
    }
    public boolean minusPlayerMoney(final int deltaMoney)
    {
    	boolean buySuccess;
    	Player player1 = mPlayers.get(0);
    	int original = player1.getMoney();
    	int result = original - deltaMoney;
    	int newPlayerMoney;
    	if(result >= 0)
    	{
    		newPlayerMoney = result;
    		buySuccess = true;
    	}
    	else
    	{
    		newPlayerMoney = original;
    		buySuccess = false;
    	}
    	player1.setMoney(newPlayerMoney);
    	moneyText.setText("Money: "+ String.valueOf(newPlayerMoney));
    	return buySuccess;
    }
    public Player getUser()
    {
    	return mPlayers.get(0);
    }
    public Player getOpponent()
    {
    	return mPlayers.get(1);
    }
    @Override
    public void onAccelerationChanged(final AccelerationData pAccelerationData) {
        /*
        this.mGravityX = pAccelerationData.getX();
        this.mGravityY = pAccelerationData.getY();

        final Vector2 gravity = Vector2Pool.obtain(this.mGravityX, this.mGravityY);
        this.mPhysicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
        */
        final Vector2 gravity = Vector2Pool.obtain(this.mGravityX, this.mGravityY);
        physicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
    }
    
    private void removeEntity(final IEntity pIEntity) {
        activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(pIEntity);
                if (facePhysicsConnector == null) return;

                physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
                physicsWorld.destroyBody(facePhysicsConnector.getBody());

                detachChild(pIEntity);
                
                System.gc();
            }
        });
    }
	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
		
	}
}