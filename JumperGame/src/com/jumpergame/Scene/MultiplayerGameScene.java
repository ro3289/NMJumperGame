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
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;
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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jumpergame.BulletItem;
import com.jumpergame.Item;
import com.jumpergame.Item.ItemType;
import com.jumpergame.MainActivity;
import com.jumpergame.Player;
import com.jumpergame.Player_Client;
import com.jumpergame.Player_Server;
import com.jumpergame.StoreItem;
import com.jumpergame.rankingSprite;
import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Manager.SceneManager;
import com.jumpergame.Manager.SceneManager.SceneType;
import com.jumpergame.body.Sprite_Body;
import com.jumpergame.connection.client.PlayerJumpClientMessage;
import com.jumpergame.connection.client.PlayerShootClientMessage;
import com.jumpergame.connection.client.PlayerUpdateMoneyClientMessage;
import com.jumpergame.connection.client.PlayerWinClientMessage;
import com.jumpergame.connection.server.ConnectionCloseServerMessage;
import com.jumpergame.constant.GeneralConstants;


public class MultiplayerGameScene extends BaseScene implements IOnSceneTouchListener, IOnAreaTouchListener,GeneralConstants,IAccelerationListener
{
    public HUD gameHUD;
    private final MultiplayerGameScene gc = this;
    
    // Score
    private SparseArray<Text> mScoreTextMap;
    // Money
//    private Text moneyText;
    public SparseArray<Text> mMoneyTextMap;
    
    //bullets
//    private final HashMap<Integer, Rectangle> mBullets = new HashMap<Integer, Rectangle>();
    
//    private final HashMap<Integer, Sprite> mObjects = new HashMap<Integer, Sprite>();
    
    // jump setting
    private Vector2 initVector;
    private Vector2 endVector;

    private SparseArray<Player_Client> mPlayers;
    public int thisID;
    private Sprite mArrow;
    private int mBulletCount = 0;
    
    private Vector2 initBodyVector;
    private Vector2 endBodyVector;
    
//    private float mGravityX = 0;
//    private float mGravityY = -10.0f;
    
    private int textInitY = 720;
    
    public int money = 0;

    // Item
    private Item dragItem;
    private StoreItem  currentDragItem;
    private StoreItem  currentFloatingItem;
    private HashMap<ItemType, StoreItem> itemMap;
    private boolean buyItem = false;
    
    //private ArrayList<Fixture> f;
    public ArrayList<Rectangle> mWalls;
    private ArrayList<Sprite> mSprites;
    public SparseArray<Item> mBullets = new SparseArray<Item>();
    private ArrayList<ArrayList<Fixture>> ff;
    public Rectangle mGround;
    private boolean arrowAttached = false;
    private boolean initJumpState = false;
    
    private rankingSprite rank;
    
    private void createInfoHUD()
    {
        mScoreTextMap = new SparseArray<Text>();
        mMoneyTextMap = new SparseArray<Text>();

        // Set Score 
        /**
         *   energy
         */
        
        /*
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
        */

    }
    // @Bosh
    // Item System
    private void loadItem() {
        itemMap = new HashMap<ItemType, StoreItem>();
        createStoreButton(30, 50, ItemType.BUY_BUTTON, resourcesManager.button_region);
        createAttackItem(90,  50, ItemType.ACID, 300, resourcesManager.acid_region);
        createAttackItem(160, 50, ItemType.GLUE, 500, resourcesManager.glue_region);
        createAttackItem(230, 50, ItemType.TOOL, 800, resourcesManager.tool_region);
        createEffectItem(300, 50, ItemType.ENERGY_DRINK, 200, resourcesManager.energy_region);
        createEffectItem(370, 50, ItemType.INVISIBLE_DRINK, 500, resourcesManager.invisible_region);
        createEffectItem(440, 50, ItemType.INVINCIBLE_DRINK, 1000, resourcesManager.invincible_region);
    }
    
    private void createAttackItem(final float x, final float y, final ItemType type, final int price, final ITextureRegion itemTextureRegion)
    {   
        StoreItem item= new StoreItem(this, x, y, type, price, itemTextureRegion, vbom, true)
        {
             @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
            {
                if (pSceneTouchEvent.isActionDown() && dragItem == null)
                {   
                    if(buyItem){
                        this.buyStoreItem();
                    }
                    else
                    {
                        if(getItemAmount() > 0 )
                        {
                            currentDragItem = this;
                            dragItem = new Item(gc, x, y, type, itemTextureRegion, vbom, true);
                            gameHUD.attachChild(dragItem);
                        }                   
                    }
                }
                else if (pSceneTouchEvent.isActionUp() && initJumpState)
                {
                    refreshArrow();
                }
                return true;
            };
                
        };
        itemMap.put(type, item);
        gameHUD.registerTouchArea(item);
        gameHUD.attachChild(item);
    }
    private void createEffectItem(final float x, final float y, final ItemType type,final int p, final ITextureRegion itemTextureRegion)
    {   
        StoreItem item= new StoreItem(this, x, y, type, p, itemTextureRegion, vbom, true)
        {
             @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
            {
                if (pSceneTouchEvent.isActionDown())
                {
                    if(buyItem){
                        this.buyStoreItem();
                    }
                    else{
                        if (this.getItemAmount() > 0)
                        {
                            this.useEffectItem();
                        }
                    }
                }
                else if (pSceneTouchEvent.isActionUp() && initJumpState)
                {
                    refreshArrow();
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
        Item item= new Item(this, x, y, type, itemTextureRegion, vbom, true)

        {
             @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
            {
                if (pSceneTouchEvent.isActionDown())
                {
                    if(buyItem){
                        buyItem = false;
                        // scale down modifier of button
                        registerEntityModifier(new ScaleModifier((float) 0.1, 1, 0.8f));
                    }
                    else{
                        if (money > 0)
                        {
                            buyItem = true;
                            registerEntityModifier(new ScaleModifier((float) 0.1, 1, 1.2f));
                        }
                        // scale up modifier of button
                        
                    }
                }
                else if (pSceneTouchEvent.isActionUp() && initJumpState)
                {
                    refreshArrow();
                }
                return true;
            };
                
        };
        gameHUD.registerTouchArea(item);
        gameHUD.attachChild(item);
    }
    
    private void createBackground() {
        Sprite background1 = new Sprite(240, 400, resourcesManager.background1_region, vbom);
        Sprite background2 = new Sprite(240, 1200, resourcesManager.background2_region, vbom);
        Sprite background3 = new Sprite(240, 2000, resourcesManager.background3_region, vbom);
        Sprite background4 = new Sprite(240, 2800, resourcesManager.background4_region, vbom);
    //  SpriteBackground spriteBackground = new SpriteBackground(background);
        attachChild(background1);
        attachChild(background2);
        attachChild(background3);
        attachChild(background4);
    }
    
    @Override
    public void createScene()
    {
        ResourcesManager.getInstance().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ResourcesManager.getInstance().getActivity().showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
            }
        });
                
        
        createBackground();
        gameHUD = new HUD();
        loadItem();
        loadLevel(1);
        createInfoHUD();
        camera.setHUD(gameHUD);
        rank=new rankingSprite(vbom);
        
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
        
        MainActivity a = ResourcesManager.getInstance().activity;
        if(a.mServer != null) {
            try {
                a.mServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
            } catch (final IOException e) {
                Debug.e(e);
            }
            a.mServer.terminate();
        }
        
        if(a.mServerConnector != null) {
            a.mServerConnector.terminate();
        }

        // TODO code responsible for disposing scene
        // removing all game scene objects.
    }
    
    private void loadLevel(int levelID)
    {
        final Rectangle ground = new Rectangle(240, 50, 480, 100, vbom);
        final Rectangle left = new Rectangle(5, 800*5, 10, 800*10, vbom);
        final Rectangle right = new Rectangle(480 - 5, 800*5, 10, 800*10, vbom);
        ff=new ArrayList<ArrayList<Fixture>>();
        mPlayers = new SparseArray<Player_Client>();
        
        mWalls = new ArrayList<Rectangle>();
        
        mGround = ground;
//        mWalls.add(ground);
        mWalls.add(left);
        mWalls.add(right);
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

                return MultiplayerGameScene.this;
            }
        });
        System.out.println("7");
        
        mSprites = new ArrayList<Sprite>();
        
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
//                    System.out.println("platform1");
                } 
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    /*
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData(new Sprite_Body(levelObject,"platform2"));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                    */
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    /*
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData(new Sprite_Body(levelObject,"platform3"));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                    */
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
                {
                    levelObject = createFloatingItem(x, y, ItemType.COIN, resourcesManager.coin_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
//                    System.out.println("coin");
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ACID))
                {
//                    System.out.println("acid before");
                    levelObject = createFloatingItem(x, y, ItemType.ACID, resourcesManager.acid_region);
//                    System.out.println("acid after");
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
                /*
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
                    camera.setChaseEntity(player);
                    thisID = mPlayers.size();
                    mPlayers.add(player);
                    levelObject = player;
                }
                */
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CROWN))
                {
                    levelObject = createFloatingItem( x, y, ItemType.CROWN, resourcesManager.crown_region);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else
                {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);
                levelObject.setUserData(type);
                mSprites.add(levelObject);

                return levelObject;
            }

            private Sprite createFloatingItem( int x, int y, final ItemType type, ITextureRegion region) {
                Sprite object = new Item(gc, x, y, type, region, vbom, true)
                
                {
                     @Override
                     protected void onManagedUpdate(float pSecondsElapsed)
                     {
                         super.onManagedUpdate(pSecondsElapsed);
                         
                         if (gc.getThisPlayer() != null) {
                         if (gc.getThisPlayer().getAppearance().collidesWith(this))
                         {
                             if(type == ItemType.COIN)
                             {
//                                 gc.plusPlayerMoney(500);
                                 money += 500;
                                 PlayerUpdateMoneyClientMessage pClientMessage = new PlayerUpdateMoneyClientMessage();
                                 pClientMessage.set(thisID, 500);
                                 try {
                                     System.out.println("money sent!!");
                                     ResourcesManager.getInstance().activity.mServerConnector.sendClientMessage(pClientMessage);
                                 } catch (IOException e) {
                                     Debug.e(e);
                                 }
                                 
                             }
                             else if(type == ItemType.CROWN)
                             {
                                 PlayerWinClientMessage pClientMessage = new PlayerWinClientMessage();
                                 try {
                                     System.out.println("crown sent!!");
                                     ResourcesManager.getInstance().activity.mServerConnector.sendClientMessage(pClientMessage);
                                 } catch (IOException e) {
                                     Debug.e(e);
                                 }
                             }
                             else
                             {
                                currentFloatingItem = itemMap.get(type);
                                currentFloatingItem.plusItem();
                             }
                             this.setVisible(false);
                             this.setIgnoreUpdate(true);
                         }
                         }
                     }
                };
                
                
                return object;
            }

        });
        System.out.println("5");
        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
        System.out.println("6");
        
        mArrow = new Sprite(240, 400, resourcesManager.mDirectionTextureRegion, vbom);
    }
    public Player_Client getThisPlayer() {
        return mPlayers.get(thisID);
    }
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

        // Convert touch event to camera screen touch event first
        if(dragItem != null){
            camera.convertSceneTouchEventToCameraSceneTouchEvent(pSceneTouchEvent);
        }
        
        
            
            if(pSceneTouchEvent.isActionDown() && dragItem == null) {   
                // Record initial position
                initVector = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                // Show Direction
                mArrow.setPosition(getThisPlayer().getAppearance().getX(), getThisPlayer().getAppearance().getY() +40 );
                activity.runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        attachChild(mArrow);
                    }
                });
                arrowAttached = true;
                initJumpState = true;
            }
            
            else if (pSceneTouchEvent.isActionUp()) {

                    if(dragItem == null)
                    {
                        // Eject object
                        final float deltaX = initVector.x - pSceneTouchEvent.getX();
                        final float deltaY = initVector.y - pSceneTouchEvent.getY();
                        if (deltaX < 20.0 && deltaY < 20.0) {
                            // shoot(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                            BulletItem bullet = new BulletItem(this, null, pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 
                                                               ItemType.BULLET, resourcesManager.normal_bullet_region,  vbom, true);
                            
                            final int bID = mBulletCount++;
                            mBullets.append(bID, bullet);
                            bullet.id = bID;
                            
                            try {
                                bullet.shoot();
                            } catch (IOException e) {
                                Debug.e(e);
                            }
                        } else {
                            endVector = new Vector2(initVector.x - pSceneTouchEvent.getX(), initVector.y - pSceneTouchEvent.getY());
                            final float velocityX = endVector.x;
                            final float velocityY = endVector.y;
                            final int velocityFactor = JUMP_VELOCITY_FACTOR;
                            final Vector2 velocity = Vector2Pool.obtain(velocityFactor * velocityX *0.01f, velocityFactor * velocityY * 0.01f*0.35f);
                            
                            IClientMessage pClientMessage = new PlayerJumpClientMessage(thisID, velocity.x, velocity.y);
                            
                            try {
                                ResourcesManager.getInstance().activity.mServerConnector.sendClientMessage(pClientMessage);
                            } catch (IOException e) {
                                Debug.e(e);
                            }
                            
//                            getThisPlayer().returnBody().setLinearVelocity(velocity);
                            Vector2Pool.recycle(velocity);
                        // Record initial jump position
//                            initBodyVector = new Vector2(getThisPlayer().returnBody().getPosition().x, getThisPlayer().returnBody().getPosition().y);
                            initJumpState = false;
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
                if(arrowAttached)
                {
                    activity.runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                            detachChild(mArrow);
                            arrowAttached = false;
                        }
                    });
                }
            }
            else if(pSceneTouchEvent.isActionMove()) {
                if(dragItem == null) // Problem here when store items used!!!!!
                {
                    System.out.print(arrowAttached);
                    if(!arrowAttached)
                    {
                        activity.runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                attachChild(mArrow);
                                arrowAttached = true;
                            }
                        });
                    }
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

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
            ITouchArea pTouchArea, float pTouchAreaLocalX,
            float pTouchAreaLocalY) {
        
        return false;
    }
    
    // ===========================================================
    // Methods
    // ===========================================================
    
    public void addPlayer(final int playerID, final float initX, final float initY) {
        if (mPlayers.get(playerID) != null) return;
        System.out.println("Add player, id = "+playerID+"initX = "+initX+"initY = "+initY);
        
        final float centerX = CAMERA_WIDTH / 2;
        final float centerY = CAMERA_HEIGHT / 2;
        AnimatedSprite appearance;
        
        if (thisID == playerID) {
            appearance = new AnimatedSprite(initX, initY, ResourcesManager.getInstance().player1_region, ResourcesManager.getInstance().vbom) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
                {
                  if (pSceneTouchEvent.isActionUp() && initJumpState)
                    {
                      refreshArrow();
                    }
                    return true;
                };
            };
            appearance.animate(new long[]{ 200, 200 }, 0, 1, true);
            registerTouchArea(appearance);
            attachChild(appearance);
            
            ResourcesManager.getInstance().camera.setChaseEntity(appearance);
        }
        else {
            appearance = new AnimatedSprite(initX, initY, ResourcesManager.getInstance().player2_region, ResourcesManager.getInstance().vbom){
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
                {
                  if (pSceneTouchEvent.isActionUp() && initJumpState)
                    {
                      refreshArrow();
                    }
                    return true;
                };
            };
            appearance.animate(new long[]{ 200, 200 }, 0, 1, true);
            registerTouchArea(appearance);
            attachChild(appearance);
        }
        
        mPlayers.append(playerID, new Player_Client(appearance, playerID));
        
//        Text thisScoreText = new Text(20, textInitY, resourcesManager.mScoreFont, "Score: 0", 50, new TextOptions(HorizontalAlign.LEFT), vbom);
//        thisScoreText.setAnchorCenter(0, 0); 
//        gameHUD.attachChild(thisScoreText);
//        mScoreTextMap.put(thisID, thisScoreText);
        // Set Money
//        Text thisMoneyText = new Text(20, textInitY-50, resourcesManager.mScoreFont, "Money: 0", 20, new TextOptions(HorizontalAlign.RIGHT), vbom);
//        thisMoneyText.setAnchorCenter(0, 0);
//        gameHUD.attachChild(thisMoneyText);
//        mMoneyTextMap.put(thisID, thisMoneyText);
        
//        textInitY -= 50;
    }
    
    private void addObject(final int mObjectId, final String mTextureName, final float initX, final float initY) { // TODO maybe we don't need it XD 
        /*
        if (mTextureName.equals("platform1")) {
            
        } else if (mTextureName.equals("platform2")) {
            
        } else if (mTextureName.equals("platform3")) {
            
        } else if (mTextureName.equals("")) {
            
        }
        */
    }

    private void computeScore() {
        /*
        final float mBodyVelocityY = (getThisPlayer().returnBody().getLinearVelocity().y > 0 )? getThisPlayer().returnBody().getLinearVelocity().y : -getThisPlayer().returnBody().getLinearVelocity().y ;
        
        endBodyVector = new Vector2(getThisPlayer().returnBody().getPosition().x, getThisPlayer().returnBody().getPosition().y);
        final float jumpHeight = (endBodyVector.y - initBodyVector.y > 0) ? endBodyVector.y - initBodyVector.y:  initBodyVector.y - endBodyVector.y;
        final float jumpLength = (endBodyVector.x - initBodyVector.x > 0) ? endBodyVector.x - initBodyVector.x:  initBodyVector.x - endBodyVector.x;
        // Set score base
        final int scoreMultiplier;
        if(mBodyVelocityY <= 3) scoreMultiplier = 10;
        else if(mBodyVelocityY <= 5) scoreMultiplier = 5;
        else if(mBodyVelocityY <= 8) scoreMultiplier = 3;
        else if(mBodyVelocityY <= 11) scoreMultiplier = 1;
        else scoreMultiplier = 0;
        
        int newScore = (int) (scoreMultiplier * jumpHeight * jumpLength/10) + getThisPlayer().getScore();
        getThisPlayer().setScore(newScore);
        System.out.println("VeclocityY: "+mBodyVelocityY+'\n'+ "Jump height: " + jumpHeight + '\n'+ "Score: " + getThisPlayer().getScore() );
        
        updateScore(thisID, newScore);
        */
    }
    
    public void updateScore(final int userID, final int deltaPoints) {
        final Text score = this.mScoreTextMap.get(userID);
        score.setText("Score: " + String.valueOf(deltaPoints));
    }
    
    private void refreshArrow()
    {
        if(arrowAttached)
        {
         activity.runOnUpdateThread(new Runnable() {
             @Override
             public void run() {
                 gc.detachChild(mArrow);
             }
         });
         arrowAttached = false;
        }
        initJumpState = false;
    }
    
    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    public ArrayList<Rectangle> getWalls() {
        return mWalls;
    }
    
    public ArrayList<Sprite> getSprites() {
        return mSprites;
    }
    
    public void setObjectPosition(final int objID, final float mX, final float mY) {
        mSprites.get(objID).setPosition(mX, mY);
    }
    
    /*
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
    */
    
    public void setPlayerEnergy(final int id, final int originalEnergy, final int newEnergy) {
        Player_Client player = mPlayers.get(id);
        
//        int originalEnergy = player.getEnergy();
//        int newEnergy = originalEnergy + deltaEnergy;
        if (newEnergy <= 0) {
//            player.setEnergy(FULL_ENERGY);
            
            player.getEnergyBar().registerEntityModifier(new SequenceEntityModifier(
                new ScaleModifier(1, 
                    (float) originalEnergy / FULL_ENERGY, 0.001f, 
                    1, 1
                ),
                new ScaleModifier(1, 0.001f, 1, 1, 1)
            ));
        }
        
        else {
//            player.setEnergy(newEnergy);
            player.getEnergyBar().registerEntityModifier(new ScaleModifier(1, 
                (float) originalEnergy / FULL_ENERGY, (float) newEnergy / FULL_ENERGY, 
                1, 1
            ));            
        }       
    }
    /*
    public void plusPlayerMoney(final int deltaMoney)
    {
        Player_Client player1 = mPlayers.get(0);
        int original = player1.getMoney();
        int newPlayerMoney = original + deltaMoney;
        player1.setMoney(newPlayerMoney);
        moneyText.setText("Money: "+ String.valueOf(newPlayerMoney));
    }
    public boolean minusPlayerMoney(final int deltaMoney)
    {
        boolean buySuccess;
        Player_Client player1 = mPlayers.get(0);
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
    */
    
    public void setMoney(final int pID, final int newMoney) {
//        mMoneyTextMap.get(pID).setText("Money: "+ String.valueOf(newMoney));
        if (mScoreTextMap.get(pID) != null) {
            mScoreTextMap.get(pID).setText("Player " + pID + " Score: "+ String.valueOf(newMoney));
            mPlayers.get(pID).setMoney(newMoney);
        }
        else {
            Text thisScoreText = new Text(20, textInitY, resourcesManager.mScoreFont, "Player " + pID + "Score: "+ String.valueOf(newMoney), 50, new TextOptions(HorizontalAlign.LEFT), vbom);
            thisScoreText.setAnchorCenter(0, 0); 
            gameHUD.attachChild(thisScoreText);
            mScoreTextMap.put(pID, thisScoreText);
            
            textInitY -= 50;
        }
    }
    
    /*
    public Player getUser()
    {
        return mPlayers.get(0);
    }
    public Player getOpponent()
    {
        return mPlayers.get(1);
    }
    */
    @Override
    public void onAccelerationChanged(final AccelerationData pAccelerationData) {

    }
    
    public void removeBullet(final IEntity pIEntity) {
        /* TODO: remove bullet
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
        */
    }
    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
        
    }
    public void setPlayerID(int pID) {
        thisID = pID;
    }
    public void updatePlayer(int mPlayerID, float mX, float mY) {
        try {
            mPlayers.get(mPlayerID).getAppearance().setPosition(mX, mY);
        } catch (NullPointerException e) {
            Debug.e(e);
        }
    }
    public void gameEnd() {
        rank = new rankingSprite(vbom);
        rank.display(gc,camera,true);
    }
    
    public ArrayList<Player_Client> getPlayers() {
        ArrayList<Player_Client> players = new ArrayList<Player_Client>();
        
        for(int j = 0; j < mPlayers.size(); j++) {
            final int key = mPlayers.keyAt(j);
            final Player_Client player = mPlayers.get(key);
            players.add(player);
        }
        
        return players;
    }
}