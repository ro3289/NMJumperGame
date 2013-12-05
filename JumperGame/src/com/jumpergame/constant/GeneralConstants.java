package com.jumpergame.constant;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface GeneralConstants {
    public static final int FPS = 30;
    
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 480;
    public static final int CAMERA_WIDTH = GAME_WIDTH;
    public static final int CAMERA_HEIGHT = GAME_HEIGHT;
    
    public static final float mGravityX = 0;
    public static final float mGravityY = -10.0f;
    
    public static final int INITIAL_SCORE = 0;
    
    public static final int ENERGY_BAR_POS_X = 50;
    public static final int ENERGY_BAR_POS_Y = 650;
    public static final int ENERGY_BAR_POS_Y_GAP = 40;
    public static final int ENERGY_BAR_HEIGHT = 15;
    
    public static final int FULL_ENERGY = 150;
    public static final int FULL_MAGAZINE_CAPACITY = 30;
    
    public static final int NORMAL_BULLET_VELOCITY = 20;
    public static final int ITEM_BULLET_VELOCITY = 10;
    public static final int BULLET_POWER = 30;
    
    /**
     *   Dialogues
     */
    
    public static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
    public static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
    public static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;
    
    /**
     *   Sprite Names
     */
    
    // Level loader
    public static final String TAG_ENTITY = "entity";
    public static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    public static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    public static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
        
    // Staircase
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
    
    // Stuff
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN        = "coin";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ACID        = "acid";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GLUE        = "glue";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TOOL        = "tool";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENERGY      = "energy";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_INVISIBLE   = "invisible";
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_INVINCIBLE  = "invincible";
    
    // Player
    public static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
    
    /**
     *   Collision Filtering      
     */
    
    /* The categories. */
    public static final short CATEGORYBIT_WALL = 1;
    public static final short CATEGORYBIT_THIS_PLAYER = 2;
    public static final short CATEGORYBIT_OTHER_PLAYER = 4;
    public static final short CATEGORYBIT_BULLET = 8;
//    public static final short CATEGORYBIT_TRIANGLE = 8;

    /* And what should collide with what. */
    public static final short MASKBITS_WALL = CATEGORYBIT_WALL + CATEGORYBIT_THIS_PLAYER + CATEGORYBIT_OTHER_PLAYER + CATEGORYBIT_BULLET;
    public static final short MASKBITS_WALL2 = CATEGORYBIT_WALL ;
    public static final short MASKBITS_THIS_PLAYER = CATEGORYBIT_WALL + CATEGORYBIT_OTHER_PLAYER; // Missing: CATEGORYBIT_CIRCLE
    public static final short MASKBITS_OTHER_PLAYER = CATEGORYBIT_WALL + CATEGORYBIT_THIS_PLAYER + CATEGORYBIT_BULLET; // Missing: CATEGORYBIT_CIRCLE
    public static final short MASKBITS_BULLET = CATEGORYBIT_WALL + CATEGORYBIT_OTHER_PLAYER; // Missing: CATEGORYBIT_BOX
    
    public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
    public static final FixtureDef GROUND_AND_STAIR_FIXTURE_DEF = PhysicsFactory.createFixtureDef(2, 0f, 0.5f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
    public static final FixtureDef THIS_PLAYER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_THIS_PLAYER, MASKBITS_THIS_PLAYER, (short)0);
    public static final FixtureDef BULLET_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0.01f, 0.5f, 0.5f, false, CATEGORYBIT_BULLET, MASKBITS_BULLET, (short)0);
    public static final FixtureDef OTHER_PLAYER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_OTHER_PLAYER, MASKBITS_OTHER_PLAYER, (short)0);
    public static final FixtureDef GROUND_AND_STAIR2_FIXTURE_DEF = PhysicsFactory.createFixtureDef(2f, 0f, 0f, false, CATEGORYBIT_WALL, MASKBITS_WALL2, (short)0);
}
