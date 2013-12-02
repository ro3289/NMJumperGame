package com.jumpergame;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.jumpergame.Scene.GameScene.ItemType;

public class Item extends Sprite{
	
	private ItemType 	itemType;

	public Item(float pX, float pY, ItemType type, ITextureRegion pTextureRegion,
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
