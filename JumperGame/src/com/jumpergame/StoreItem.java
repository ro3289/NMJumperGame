package com.jumpergame;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.jumpergame.Scene.GameScene.ItemType;

public class StoreItem extends Item{

	public StoreItem(float pX, float pY, ItemType type,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, type, pTextureRegion, pVertexBufferObjectManager);
		setType(type);
	}

}
