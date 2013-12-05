package com.jumpergame;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;

import com.jumpergame.constant.GeneralConstants;

public class Player_Client implements GeneralConstants {
    // ===========================================================
    // Constants
    // ===========================================================
    
    // ===========================================================
    // Fields
    // ===========================================================
    
//    private String IP;
    private int ID; // id recorded in MainServer 
//    private int score;
//    private int energy;
//    private int bulletNum;
    
    private AnimatedSprite appearance;
//    private Body body;
    private Rectangle energyBar;
    private Text moneyText;
    
    // ===========================================================
    // Constructors
    // ===========================================================
    
    public Player_Client(int id) {
        setID(id);
//        ID = id;
//        appearance = aSprite;
//        energyBar = eBar;
//        energyBar = new Rectangle();
    }
    /*
    public AnimatedSprite getASprite() {
        return appearance;
    }
    */

    // ===========================================================
    // Getters & Setters
    // ===========================================================
    
    public int getID() {
        return ID;
    }
    
    public void setID(int id) {
        ID = id;
    }
    
    public AnimatedSprite getAppearance() {
        return appearance;
    }
    
    public void setAppearance(AnimatedSprite as) {
        appearance = as;
    }
    
    public Rectangle getEnergyBar() {
        return energyBar;
    }
    
    public void setEnergyBar(Rectangle eBar) {
        energyBar = eBar;
    }
    
    public void setMoney(int money) {
        moneyText.setText(String.valueOf(money));
    }
    
    public void setMoneyText(Text t) {
        moneyText = t;
    }
    
    // ===========================================================
    // Methods
    // ===========================================================
    
    public void setPos(final float x, final float y) {
        appearance.setPosition(x, y);
    }
}
