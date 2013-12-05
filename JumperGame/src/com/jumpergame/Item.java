package com.jumpergame;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Scene.GameScene;

public class Item extends Sprite {
    
    public enum ItemType
    {
        // Money
        COIN,
        // Bullet
        BULLET,
        // Attack items
        ACID,
        GLUE,
        TOOL,
        // Self items
        ENERGY_DRINK,
        INVISIBLE_DRINK,
        INVINCIBLE_DRINK,
        // Buy button
        BUY_BUTTON;
    }
	
	protected Scene 				    gameScene;
	protected static ResourcesManager 	resourcesManager;
	protected ItemType 					itemType;
	protected boolean                   isOnline;

	public Item(Scene gc, float pX, float pY, ItemType type, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, boolean online) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		setType(type);
		resourcesManager = ResourcesManager.getInstance();
		gameScene = gc;
		isOnline = online;
	}

	public void setType(ItemType type){
		itemType = type;
	}
	
	public ItemType getType(){
		return itemType;
	}

}
