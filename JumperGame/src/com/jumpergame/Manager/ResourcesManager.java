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
    
  	// Menu Texture
  	private BuildableBitmapTextureAtlas menuTextureAtlas;
  	
  	// Menu Texture Regions
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;
    
   // Game Texture
    public BuildableBitmapTextureAtlas gameTextureAtlas;
        
    // Game Texture Regions
    public ITextureRegion platform1_region;
    public ITextureRegion platform2_region;
    public ITextureRegion platform3_region;
    
    // Stuff Texture Regions
    public ITextureRegion coin_region;
    public ITextureRegion acid_region;
    public ITextureRegion glue_region;
    public ITextureRegion tool_region;
    public ITextureRegion energy_region;
    public ITextureRegion invisible_region;
    public ITextureRegion invincible_region;
    
    // Player TextureRegion
    public ITiledTextureRegion player_region;
  	
  	// Font
  	public Font font;
  	public Font mScoreFont;
  	
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

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, 100, 5, 50);
        font.load();
    }

    private void loadGameGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        
        // Load stair texture
        platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
        platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
        platform3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        
        // Load stuff texture
        coin_region 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        acid_region			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "acid.png");
        glue_region			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "glue.png");
        tool_region 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "tool.png");
        energy_region		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "energy.png");
        invisible_region 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "invisible.png");
        invincible_region 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "invincible.png");
        
        // Load player texture
        player_region 		= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        
//        mDirectionTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
        mDirectionTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "up_arrow.jpg");
        
        System.out.println("2");
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();

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
        mScoreFont = FontFactory.createFromAsset(activity.getFontManager(), scoreFontTexture, activity.getAssets(), "LCD.ttf", 32, true, Color.WHITE);
        mScoreFont.load();
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

}
