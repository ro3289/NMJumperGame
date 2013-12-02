package com.jumpergame;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Scene.GameScene;
import com.jumpergame.Scene.GameScene.ItemType;

public class AttackItem extends Item{

	public AttackItem(GameScene gc, float pX, float pY, ItemType type,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, type, pTextureRegion, pVertexBufferObjectManager);

		gameScene = gc;
		resourcesManager = ResourcesManager.getInstance();
		
		setType(type);
		itemAmount = 0;
		itemAmountText = new Text(pX, pY + 30 , resourcesManager.mItemAmountFont, "0", 10 ,new TextOptions(HorizontalAlign.LEFT), resourcesManager.vbom);
		itemAmountText.setAnchorCenter(0, 0);
		gameScene.gameHUD.attachChild(itemAmountText);
	}
	
	// Method
	
    public int getItemAmount()
    {
    	return itemAmount;
    }
    private void plusAttackItem()
    {
    	itemAmount++;
    	itemAmountText.setText(String.valueOf(itemAmount));
    }	
    private void minusAttackItem()
    {
    	itemAmount = (itemAmount == 0)? 0 : itemAmount -1;
    	itemAmountText.setText(String.valueOf(itemAmount));
    }
    public void useAttackItem()
    {
    	switch (itemType)
    	{
    	case ENERGY_DRINK:
    		gameScene.setPlayerEnergy(0, 1, +50);
    		break;
    	case INVISIBLE_DRINK:
    		break;
    	case INVINCIBLE_DRINK:
    		break;
		default:
			break;
    	}
    	minusAttackItem();
    }

}
