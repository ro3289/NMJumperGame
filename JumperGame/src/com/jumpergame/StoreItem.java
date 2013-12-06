package com.jumpergame;


import java.io.IOException;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;

import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Scene.GameScene;
import com.jumpergame.Scene.MultiplayerGameScene;
import com.jumpergame.connection.client.PlayerUpdateMoneyClientMessage;

public class StoreItem extends Item {
	
	protected int  	itemPrice;
	private   Text 	itemPriceText;
	protected int 	itemAmount;
	protected Text	itemAmountText;
	
	public StoreItem(Scene gc, float pX, float pY, ItemType type, int price,
			ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, boolean online){
		super(gc, pX, pY, type, pTextureRegion, pVertexBufferObjectManager, online);
		
		itemPrice = price;
		itemPriceText = new Text(pX, pY - 50 , resourcesManager.mPriceFont, String.valueOf(itemPrice), 10 ,new TextOptions(HorizontalAlign.LEFT), resourcesManager.vbom);
		itemPriceText.setAnchorCenter(0, 0);
		
		if (isOnline) {
		    ((MultiplayerGameScene)gameScene).gameHUD.attachChild(itemPriceText);
		} else {
		    ((GameScene)gameScene).gameHUD.attachChild(itemPriceText);
		}
		
		itemAmount = 0;
		itemAmountText = new Text(pX, pY + 30 , resourcesManager.mItemAmountFont, "0", 10 ,new TextOptions(HorizontalAlign.LEFT), resourcesManager.vbom);
		itemAmountText.setAnchorCenter(0, 0);
		
		if (isOnline) {
            ((MultiplayerGameScene)gameScene).gameHUD.attachChild(itemAmountText);
        } else {
            ((GameScene)gameScene).gameHUD.attachChild(itemAmountText);
        }
		
		setAlpha(0.3f);
	}
	
	// Method
	
	public void buyStoreItem()
    {
    	if (!isOnline) {
            boolean buySuccess = ((GameScene)gameScene).minusPlayerMoney(itemPrice);
            if(buySuccess){
                plusItem();
            }
    	}
    	else {
    	    PlayerUpdateMoneyClientMessage pClientMessage = new PlayerUpdateMoneyClientMessage();
            pClientMessage.set(((MultiplayerGameScene)gameScene).thisID, -itemPrice);
            try {
               ResourcesManager.getInstance().activity.mServerConnector.sendClientMessage(pClientMessage);
           } catch (IOException e) {
               Debug.e(e);
           }
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

    public void useAttackItem(PhysicsWorld pw, final float pX, final float pY) throws IOException
    {
        if (!isOnline) {
            switch (itemType)
            {
            case ACID:
                    // gameScene.setPlayerEnergy(1,0,-100);
                ((GameScene)gameScene).getUser();
                    BulletItem acidBullet = new BulletItem(((GameScene)gameScene), pw, pX ,pY, itemType, resourcesManager.acid_bullet_region, resourcesManager.vbom, false);
                    acidBullet.shoot();
                    break;
            case GLUE:
                    BulletItem glueBullet = new BulletItem(((GameScene)gameScene), pw, pX, pY, itemType, resourcesManager.glue_bullet_region, resourcesManager.vbom, false);
                    glueBullet.shoot();
                    break;
            case TOOL:
                    
                    break;
                default:
                        break;
            }
            
        }
        minusItem();
    }
    
    public void useEffectItem()
    {
        if (!isOnline) {
            switch (itemType)
            {
            case ENERGY_DRINK:
                ((GameScene)gameScene).setPlayerEnergy(0, 1, +50);
                    break;
            case INVISIBLE_DRINK:
                ((GameScene)gameScene).getUser().invisibleEffect();
                    break;
            case INVINCIBLE_DRINK:
                ((GameScene)gameScene).getUser().invincibleEffect();
                    break;
                default:
                        break;
            }
            
        }
        minusItem();
    }
    

}
