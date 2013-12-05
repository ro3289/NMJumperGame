package com.jumpergame;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.constant.GeneralConstants;
import com.jumpergame.body.Sprite_Body;

public abstract class Player extends AnimatedSprite implements GeneralConstants
{
    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    
    public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, String name, int type)
    {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
        userdata=name;
        playerType=type;
        createPhysics(camera, physicsWorld);
        // camera.setChaseEntity(this);
        score = INITIAL_SCORE;
        energy = FULL_ENERGY;
        
    }
    
     // ---------------------------------------------
     // VARIABLES
     // ---------------------------------------------
         
     private String userdata;
     private Body body;
     private int  velocityFactor = JUMP_VELOCITY_FACTOR;
     private boolean invincibleState = false;
     
     public abstract void onDie();

     private boolean canRun = false;
     
     private int energy;
     private int score = 0;
     private int money = 0;
     private int playerType;
     
     private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
     {        
         if(playerType==1)
             body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, THIS_PLAYER_FIXTURE_DEF);
         else
             body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, OTHER_PLAYER_FIXTURE_DEF);
         body.setUserData(new Sprite_Body(this, userdata));
         body.setFixedRotation(true);
         
         physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
         
         /*{
             @Override
             public void onUpdate(float pSecondsElapsed)
             {
                 super.onUpdate(pSecondsElapsed);
                 camera.onUpdate(0.1f);
                 
                 if (getY() <= 0)
                 {                    
                     onDie();
                 }
                 
                 if (canRun)
                 {    
                     body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y)); 
                 }
             }

         });
     }

	     });*/
	     animate(new long[]{ 100, 100, 100, 100, 100, 100, 100, 100}, 0, 7, true);
	     setUserData(body);
	 }
	 

     public void jump()
     {
         body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 8)); 
     }
     
     public int getEnergy() {
         return energy;
     }
        
     public void setEnergy(int e) {
         energy = e;
     }

     public int getScore() {
         return score;
     }
        
     public void setScore(int s) {
         score = s;
     }
     
     public int getMoney() {
         return money;
     }
        
     public void setMoney(int m) {
         money = m;
     }
     
     public Body returnBody()
     {
         return body;
     }
     
     public int getVelocityFactor()
     {
         return velocityFactor;
     }
     
     public void slowDownEffect()
     {
         velocityFactor = JUMP_VELOCITY_FACTOR_SLOW;
         registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() 
        {
            
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                unregisterUpdateHandler(pTimerHandler);
                velocityFactor = JUMP_VELOCITY_FACTOR;
            }
        }));
     }
     public void invisibleEffect()
     {  
         setAlpha(0.3f);
         registerUpdateHandler(new TimerHandler(4f, new ITimerCallback() 
        {
            
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                unregisterUpdateHandler(pTimerHandler);
                setAlpha(1f);
            }
        }));
     }
     public void invincibleEffect()
     {  // Not corrected yet
        invincibleState = true;
         registerUpdateHandler(new TimerHandler(4f, new ITimerCallback() 
        {
            
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                unregisterUpdateHandler(pTimerHandler);
                invincibleState = false;
            }
        }));
     }

}
