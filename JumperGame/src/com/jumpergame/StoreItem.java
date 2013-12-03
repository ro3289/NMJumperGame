package com.jumpergame;


import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import com.jumpergame.Scene.GameScene;
import com.jumpergame.Scene.GameScene.ItemType;

public class StoreItem extends Item{
	
	protected int  	itemPrice;
	private   Text 	itemPriceText;
	protected int 	itemAmount;
	protected Text	itemAmountText;
	
	public StoreItem(GameScene gc, float pX, float pY, ItemType type, int price,
			ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager){
		super(gc, pX, pY, type, pTextureRegion, pVertexBufferObjectManager);
		
		itemPrice = price;
		itemPriceText = new Text(pX, pY - 50 , resourcesManager.mPriceFont, String.valueOf(itemPrice), 10 ,new TextOptions(HorizontalAlign.LEFT), resourcesManager.vbom);
		itemPriceText.setAnchorCenter(0, 0);
		gameScene.gameHUD.attachChild(itemPriceText);
		
		itemAmount = 0;
		itemAmountText = new Text(pX, pY + 30 , resourcesManager.mItemAmountFont, "0", 10 ,new TextOptions(HorizontalAlign.LEFT), resourcesManager.vbom);
		itemAmountText.setAnchorCenter(0, 0);
		gameScene.gameHUD.attachChild(itemAmountText);
		
		setAlpha(0.3f);
	}
	
	// Method
	
	public void buyStoreItem()
    {
    	
    	boolean buySuccess = gameScene.minusPlayerMoney(itemPrice);
    	if(buySuccess){
    		plusItem();
    	}
    }
    public int getItemAmount()
    {
    	return itemAmount;
    }
    public void plusItem()
    {
    	itemAmount++;
    	itemAmountText.setText(String.valueOf(itemAmount));
    	if(itemAmount > 0)
    	{
    		setAlpha(1f);
    	}
    }	
    private void minusItem()
    {
    	itemAmount = (itemAmount == 0)? 0 : itemAmount -1;
    	itemAmountText.setText(String.valueOf(itemAmount));
    	if(itemAmount == 0)
    	{
    		setAlpha(0.3f);
    	}
    }

    public void useAttackItem(PhysicsWorld pw, final float pX, final float pY)
    {
    	switch (itemType)
    	{
    	case ACID:
    		// gameScene.setPlayerEnergy(1,0,-100);
    		gameScene.getUser();
    		BulletItem acidBullet = new BulletItem(gameScene, pw, pX ,pY, itemType, resourcesManager.acid_bullet_region, resourcesManager.vbom);
    		acidBullet.shoot();
    		break;
    	case GLUE:
    		BulletItem glueBullet = new BulletItem(gameScene, pw, pX, pY, itemType, resourcesManager.glue_bullet_region, resourcesManager.vbom);
    		glueBullet.shoot();
    		break;
    	case TOOL:
    		
    		break;
		default:
			break;
    	}
    	minusItem();
    }
    
    public void useEffectItem()
    {
    	switch (itemType)
    	{
    	case ENERGY_DRINK:
    		gameScene.setPlayerEnergy(0, 1, +50);
    		break;
    	case INVISIBLE_DRINK:
    		gameScene.getUser().invisibleEffect();
    		break;
    	case INVINCIBLE_DRINK:
    		gameScene.getUser().invincibleEffect();
    		break;
		default:
			break;
    	}
    	minusItem();
    }
    

}
