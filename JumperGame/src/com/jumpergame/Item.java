package com.jumpergame;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;

import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Scene.GameScene;
import com.jumpergame.Scene.GameScene.ItemType;

public class Item extends Sprite{
	
	protected GameScene 		gameScene;
	protected ResourcesManager 	resourcesManager;
	protected ItemType 			itemType;
	protected int 				itemAmount;
	protected Text				itemAmountText;

	public Item(GameScene gc, float pX, float pY, ItemType type, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		setType(type);
	}

	public void setType(ItemType type){
		itemType = type;
	}
	
	public ItemType getType(){
		return itemType;
	}
	

}
