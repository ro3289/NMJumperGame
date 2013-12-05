package com.jumpergame;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jumpergame.Scene.GameScene;
import com.jumpergame.body.Sprite_Body;
import com.jumpergame.constant.GeneralConstants;


public class BulletItem extends Item {

	private PhysicsWorld physicsWorld;
	private final Body bulletBody;
	private final float endX;
	private final float endY;
	private final float velocityConstant;
	private String bodyName;

	public BulletItem(GameScene gc, PhysicsWorld pw, float pX, float pY, ItemType type, 
			ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(gc, pX, pY, type, pTextureRegion, pVertexBufferObjectManager);
		
		// Correct initial bullet position
		this.mX = gameScene.getUser().getX();
		this.mY = gameScene.getUser().getY();
		endX = pX;
		endY = pY;
		
		// Register physics handler
		physicsWorld = pw;
		bulletBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, GeneralConstants.BULLET_FIXTURE_DEF);
		setBodyName();
		bulletBody.setUserData(new Sprite_Body(this, bodyName));
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, bulletBody, true, true));
	    this.setUserData(bulletBody);
	    
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

	public void shoot() {
        gameScene.attachChild(this);
        bulletBody.setLinearVelocity(bulletVelocity());
    }
	
	private Vector2 bulletVelocity() {
        float delX = endX - this.mX;
        System.out.println(delX);
        float delY = endY - this.mY;
        float v = (float) java.lang.Math.pow(java.lang.Math.pow(delX, 2) + java.lang.Math.pow(delY, 2), 0.5);
        return new Vector2(velocityConstant * delX / v, velocityConstant * delY / v);
    }
	public void showBulletEffect()
	{         	 
		switch (itemType)
	 	 {
	 	 	case BULLET:
	 	 		gameScene.setPlayerEnergy(1,0,-10);
	     		 break;
	     	case ACID:
	     		gameScene.setPlayerEnergy(1,0,-50);
	     		 break;
	     	case GLUE:
//	     		gameScene.getOpponent().slowDownEffect(); TODO
	     		 break;
	     	default:
	     		break;
	 	 }
	}
}
