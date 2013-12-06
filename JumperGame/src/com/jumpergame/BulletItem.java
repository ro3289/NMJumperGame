package com.jumpergame;

import java.io.IOException;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Scene.GameScene;
import com.jumpergame.Scene.MultiplayerGameScene;
import com.jumpergame.body.Sprite_Body;
import com.jumpergame.connection.client.PlayerShootClientMessage;
import com.jumpergame.constant.GeneralConstants;


public class BulletItem extends Item {
    public int id;
    
	private PhysicsWorld physicsWorld;
	private final Body bulletBody;
	private final float endX;
	private final float endY;
	private final float velocityConstant;
	private String bodyName;

	public BulletItem(Scene gc, PhysicsWorld pw, float pX, float pY, ItemType type, 
			ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, boolean online) {
		super(gc, pX, pY, type, pTextureRegion, pVertexBufferObjectManager, online);
		
		// Correct initial bullet position
		if (!isOnline) {
	        this.mX = ((GameScene)gameScene).getUser().getX();
	        this.mY = ((GameScene)gameScene).getUser().getY();  
		
		
        // Register physics handler
	        physicsWorld = pw;
	        bulletBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, GeneralConstants.BULLET_FIXTURE_DEF);
	        setBodyName();
	        bulletBody.setUserData(new Sprite_Body(this, bodyName));
	        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, bulletBody, true, true));
	        this.setUserData(bulletBody);
		} else {
		    bulletBody = null;
		}
		
        endX = pX;
        endY = pY;
		// Determine velocity by type
        velocityConstant = (type == ItemType.BULLET) ? GeneralConstants.NORMAL_BULLET_VELOCITY : GeneralConstants.ITEM_BULLET_VELOCITY;
	}

	private void setBodyName() {
		switch (itemType)
		{
			case BULLET:
				bodyName = new String("BULLET");
				break;
			case ACID:
				bodyName = new String("ACID");
				break;
			case GLUE:
				bodyName = new String("GLUE");
				break;
			default:
				bodyName = new String("");
				break;
		}
	}

	public void shoot() throws IOException {
	    if (!isOnline) {
	        ((GameScene)gameScene).attachChild(this);
	        bulletBody.setLinearVelocity(bulletVelocity());
	        resourcesManager.mShoot.play();
	    }
	    else {
	        final int pID = ((MultiplayerGameScene)gameScene).thisID;
	        final int type;
	        switch (itemType)
	        {
	            case BULLET:
	                type = 0;
	                break;
	            case ACID:
	                type = 1;
	                break;
	            case GLUE:
	                type = 2;
	                break;
	            default:
	                type = 0;
	                break;
	        }
	        
	        Vector2 v = bulletVelocity();
	        PlayerShootClientMessage pClientMessage = new PlayerShootClientMessage();
	        pClientMessage.set(pID, id, type, v.x, v.y);
            ResourcesManager.getInstance().activity.mServerConnector.sendClientMessage(pClientMessage);
	    }
    }
	
	private Vector2 bulletVelocity() {
        float delX = endX - this.mX;
        System.out.println(delX);
        float delY = endY - this.mY;
        float v = (float) java.lang.Math.pow(java.lang.Math.pow(delX, 2) + java.lang.Math.pow(delY, 2), 0.5);
        return new Vector2(velocityConstant * delX / v, velocityConstant * delY / v);
    }
	public void showBulletEffect() {
//	    if (!isOnline) {
	        switch (itemType)
	        {
	                case BULLET:
	                    ((GameScene)gameScene).setPlayerEnergy(1,0,-10);
	                    break;
	           case ACID:
	               ((GameScene)gameScene).setPlayerEnergy(1,0,-50);
	                    break;
	           case GLUE:
	               ((GameScene)gameScene).getOpponent().slowDownEffect();
	                    break;
	           default:
	                   break;
	        }
//	    }
	}
	
	public void showBulletEffect(String type) {
	    if (type.equals("BULLET"))
            ((MultiplayerGameScene)gameScene).setPlayerEnergy(1,0,-10);
        else if (type.equals("ACID"))
           ((MultiplayerGameScene)gameScene).setPlayerEnergy(1,0,-50);
//        else if (type.equals("GLUE")) {
//           ((MultiplayerGameScene)gameScene).getOpponent().slowDownEffect();
	}
}
