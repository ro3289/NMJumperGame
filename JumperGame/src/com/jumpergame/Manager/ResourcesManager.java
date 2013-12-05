package com.jumpergame.Manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

import com.jumpergame.MainActivity;

public class ResourcesManager {
	//---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
  	public Engine engine;
  	public MainActivity activity;
  	public BoundCamera camera;
  	public VertexBufferObjectManager vbom;
    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    
   // Splash Texture
  	public ITextureRegion splash_region;
  	private BitmapTextureAtlas splashTextureAtlas;
    
  	// Menu Texture Atlas
  	private BuildableBitmapTextureAtlas menuTextureAtlas;
  	
  	// Menu Texture Regions
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;
    
   // Game Texture Atlas
    public BuildableBitmapTextureAtlas gameTextureAtlas;
        
    // Background Texture Regions
    public BuildableBitmapTextureAtlas background1TextureAtlas;
    public BuildableBitmapTextureAtlas background2TextureAtlas;
    public BuildableBitmapTextureAtlas background3TextureAtlas;
    public BuildableBitmapTextureAtlas background4TextureAtlas;
    public ITextureRegion background1_region;
    public ITextureRegion background2_region;
    public ITextureRegion background3_region;
    public ITextureRegion background4_region;
    
    public ITextureRegion platform1_region;
    public ITextureRegion platform2_region;
    public ITextureRegion platform3_region;
    
    // Item & Money Texture Regions
    public ITextureRegion coin_region;
    public ITextureRegion acid_region;
    public ITextureRegion glue_region;
    public ITextureRegion tool_region;
    public ITextureRegion energy_region;
    public ITextureRegion invisible_region;
    public ITextureRegion invincible_region;
    public ITextureRegion button_region;
    
    // Bullet Texture Region
    public ITextureRegion normal_bullet_region;
    public ITextureRegion acid_bullet_region;
    public ITextureRegion glue_bullet_region;
    
    // Player TextureRegion
    public ITiledTextureRegion  player_region;
    public ITextureRegion 		energy_bar_region;
  	
  	// Font
  	public Font font;
  	public Font mScoreFont;
  	public Font mPriceFont;
  	public Font mItemAmountFont;
  	
  	// Direction
  	public BuildableBitmapTextureAtlas mDirectionTextureAtlas;
    public ITextureRegion mDirectionTextureRegion;


    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }
    
    private void loadMenuGraphics()
    {
    	System.out.println("Loading menu graphics...");
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

    	menu_background_region 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
    	play_region 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
    	options_region 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
    	       

    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    	this.menuTextureAtlas.load();
    }
    
    private void loadMenuAudio()
    {
        
    }

    private void loadMenuFonts()
    {
    	
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "The_Rainmaker.otf", 50, true, 100, 5, 50);
        font.load();
    }

    private void loadGameGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas 		= new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        background1TextureAtlas 	= new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        background2TextureAtlas 	= new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        background3TextureAtlas 	= new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        background4TextureAtlas 	= new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        
        
        // Load Background Texture
        background1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(background1TextureAtlas, activity, "background.png");
        background2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(background2TextureAtlas, activity, "background2.png");
        background3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(background3TextureAtlas, activity, "background1.png");
        background4_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(background4TextureAtlas, activity, "background2.png");

        
        // Load staircase texture
        platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
        platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
        platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        
        // Load item texture
        coin_region 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        acid_region			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "acid.png");
        glue_region			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "glue.png");
        tool_region 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "tool.png");
        energy_region		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "energy.png");
        invisible_region 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "invisible.png");
        invincible_region 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "invincible.png");
        button_region 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "buy.png");
        
        // Load bullet texture
        acid_bullet_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "acid_bullet.png");
        glue_bullet_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "glue_bullet.png");
        normal_bullet_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "normal_bullet.png");
        
        // Load player texture
        player_region 		= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player1.png", 8, 1);
        energy_bar_region    = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "energy_bar.png");
        
        // Load jump direction texture
        mDirectionTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "up_arrow.png");
        
        System.out.println("2");
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
            this.background1TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.background1TextureAtlas.load();
            this.background2TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.background2TextureAtlas.load();
            this.background3TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.background3TextureAtlas.load();
            this.background4TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.background4TextureAtlas.load();

        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }
    
    private void loadGameFonts()
    {
    	FontFactory.setAssetBasePath("font/");
    	
    	final ITexture scoreFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        mScoreFont = FontFactory.createFromAsset(activity.getFontManager(), scoreFontTexture, activity.getAssets(), "Tabaquera.ttf", 36, true, Color.BLACK);
        mScoreFont.load();
        
        final ITexture priceFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        mPriceFont = FontFactory.createFromAsset(activity.getFontManager(), priceFontTexture, activity.getAssets(), "Erasaur.otf", 17, true, Color.BLACK);
        mPriceFont.load();
        
        final ITexture itemAmountFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        mItemAmountFont = FontFactory.createFromAsset(activity.getFontManager(), itemAmountFontTexture, activity.getAssets(), "Erasaur.otf", 17, true, Color.BLACK);
        mItemAmountFont.load();
        
    }
    
    private void loadGameAudio()
    {
        
    }
    
    public void loadSplashScreen()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
    	splashTextureAtlas.load();
    }
    
    public void unloadSplashScreen()
    {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, MainActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
    
    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }
    
    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    }

    public MainActivity getActivity() {
        // TODO Auto-generated method stub
        return activity;
    }

}
