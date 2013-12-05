package com.jumpergame;

import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.view.Menu;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jumpergame.constant.GeneralConstants;

public class Player_Server implements GeneralConstants {
    // ===========================================================
    // Constants
    // ===========================================================
    
    // ===========================================================
    // Fields
    // ===========================================================
    
    private String IP;
    private int id;
    private int score;
    private int energy;
    private int bulletNum;
    
//    private AnimatedSprite appearance;
    private Body body;
//    private Rectangle energyBar;
    
    // ===========================================================
    // Constructors
    // ===========================================================
    
    public Player_Server(String ip) {
        IP = ip;
        score = INITIAL_SCORE;
        energy = FULL_ENERGY;
        bulletNum = FULL_MAGAZINE_CAPACITY;
    }
    
    @Deprecated
    public Player_Server(String ip, AnimatedSprite aSprite) {
        IP = ip;
        score = INITIAL_SCORE;
        energy = FULL_ENERGY;
        bulletNum = FULL_MAGAZINE_CAPACITY;
        
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
    
    public int getEnergy() {
        return energy;
    }
    
    public void setEnergy(int e) {
//        energy = e;
//        EntityModifier eModifier = new EntityModifier();
        
//        energyBar.registerEntityModifier(new ScaleModifier(1, energy, e, 1, 1));
        energy = e;
//        return energyBar;
    }

    public int getScore() {
        return score;
    }
    
    public void setScore(int s) {
        score = s;
    }
    
    public int getID() {
        return id;
    }
    
    public void setID(int s) {
        id = s;
    }
    
    public Body getBody() {
        return body;
    }
    
    public void setBody(Body b) {
        body = b;
    }
    
    /*
    public AnimatedSprite getAppearance() {
        return appearance;
    }
    
    public void setAppearance(AnimatedSprite as) {
        appearance = as;
    }
    */
}
