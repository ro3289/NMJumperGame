package com.jumpergame.Scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite; 
import org.andengine.opengl.util.GLState;

import com.jumpergame.Manager.SceneManager.SceneType; // Directly import the enumerator of SceneManager

public class SplashScene extends BaseScene
{
	private Sprite splash;
	protected GLState pGLState;
	
    @Override
    public void createScene()
    {
    	// Initialize sprite scene
    	splash = new Sprite(0, 0, resourcesManager.splash_region, vbom)
    	{	
    		@Override
    	    protected void preDraw(GLState pGLState, Camera pCamera) 
    	    {
    	       super.preDraw(pGLState, pCamera);
    	       pGLState.enableDither();
    	    }
    	};
    	
    	splash.setScale(1.0f, 1.5f);
    	splash.setPosition(400,240); // in the middle of the screen
    	attachChild(splash);  // BaseScene extend Scene
    }

    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneType getSceneType()
    {
    	return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene()
    {
    	splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }
    
    // Method
    
    
}