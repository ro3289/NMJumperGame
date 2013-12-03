package com.jumpergame;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jumpergame.Scene.GameScene;
import com.jumpergame.Scene.GameScene.ItemType;
import com.jumpergame.body.Sprite_Body;
import com.jumpergame.constant.GeneralConstants;


public class BulletItem extends Item{

	private PhysicsWorld physicsWorld;
	private final Body bulletBody;
	private final float endX;
	private final float endY;

	public BulletItem(GameScene gc,PhysicsWorld pw,float pX, float pY, ItemType type, 
			ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(gc, pX, pY, type, pTextureRegion, pVertexBufferObjectManager);
		// Should correct initial bullet position
		this.mX = gameScene.getUser().getX();
		this.mY = gameScene.getUser().getY();
		endX = pX;
		endY = pY;
		physicsWorld = pw;
		bulletBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, GeneralConstants.BULLET_FIXTURE_DEF);
		bulletBody.setUserData(new Sprite_Body(this, "Bullet"));
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, bulletBody, true, true));
	    this.setUserData(bulletBody);
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
        return new Vector2(GeneralConstants.BULLET_VELOCITY * delX / v, GeneralConstants.BULLET_VELOCITY * delY / v);
    }
	
}
