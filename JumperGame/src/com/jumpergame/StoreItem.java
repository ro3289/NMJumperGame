package com.jumpergame;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Scene.GameScene;
import com.jumpergame.Scene.GameScene.ItemType;

public class StoreItem extends Item{
	
	private int  itemPrice;
	private Text itemPriceText;
	private ResourcesManager resourcesManager;
	
	public StoreItem(GameScene gc, float pX, float pY, ItemType type, int price,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, type, pTextureRegion, pVertexBufferObjectManager);
		
		gameScene = gc;
		resourcesManager = ResourcesManager.getInstance();
		
		setType(type);
		
		itemPrice = price;
		itemPriceText = new Text(pX, pY - 50 , resourcesManager.mPriceFont, String.valueOf(itemPrice), 10 ,new TextOptions(HorizontalAlign.LEFT), resourcesManager.vbom);
		itemPriceText.setAnchorCenter(0, 0);
		gameScene.gameHUD.attachChild(itemPriceText);
		
		itemAmount = 0;
		itemAmountText = new Text(pX, pY + 30 , resourcesManager.mItemAmountFont, "0", 10 ,new TextOptions(HorizontalAlign.LEFT), resourcesManager.vbom);
		itemAmountText.setAnchorCenter(0, 0);
		gameScene.gameHUD.attachChild(itemAmountText);
	}
	public void buyStoreItem()
    {
    	
    	boolean buySuccess = gameScene.minusPlayerMoney(itemPrice);
    	if(buySuccess){
    		plusStoreItem();
    	}
    }
    public int getItemAmount()
    {
    	return itemAmount;
    }
    private void plusStoreItem()
    {
    	itemAmount++;
    	itemAmountText.setText(String.valueOf(itemAmount));
    }	
    private void minusStoreItem()
    {
    	itemAmount = (itemAmount >= 0)? 0 : itemAmount -1;
    	itemAmountText.setText(String.valueOf(itemAmount));
    }
    public void useStoreItem()
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
    	minusStoreItem();
    }
    

}
