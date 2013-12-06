package com.jumpergame.constant;

public interface ConnectionConstants {
    public static final short PROTOCOL_VERSION = 1;
    public static final int SERVER_PORT = 5566;
    public static final String LOCALHOST_IP = "127.0.0.1";
    
    /* Server Msg Flags */
    public static final short FLAG_MESSAGE_SERVER_CONNECTION_CLOSE = Short.MIN_VALUE;
    public static final short FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED = FLAG_MESSAGE_SERVER_CONNECTION_CLOSE + 1;
    public static final short FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH = FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED + 1;
    public static final short FLAG_MESSAGE_SERVER_CONNECTION_MAIN = FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH + 1;
    
    /* Client Msg Flags */
    public static final short FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE = Short.MIN_VALUE;
    public static final short FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH = FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE + 1;
    public static final short FLAG_MESSAGE_CLIENT_CONNECTION_PING = FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH + 1;
    
    /* Server --> Client */
    public static final short FLAG_MESSAGE_SERVER_ADD_PLAYER = 1;
    public static final short FLAG_MESSAGE_SERVER_ADD_OBJ = FLAG_MESSAGE_SERVER_ADD_PLAYER + 1;
    public static final short FLAG_MESSAGE_SERVER_UPDATE_PLAYER = FLAG_MESSAGE_SERVER_ADD_OBJ + 1;
    public static final short FLAG_MESSAGE_SERVER_UPDATE_OBJ = FLAG_MESSAGE_SERVER_UPDATE_PLAYER + 1;
    public static final short FLAG_MESSAGE_SERVER_REMOVE_OBJ = FLAG_MESSAGE_SERVER_UPDATE_PLAYER + 1;
    public static final short FLAG_MESSAGE_SERVER_UPDATE_SCORE = FLAG_MESSAGE_SERVER_REMOVE_OBJ + 1;
    public static final short FLAG_BULLET_EFFECT_SERVER_MESSAGE = FLAG_MESSAGE_SERVER_UPDATE_SCORE + 1;
    public static final short FLAG_MESSAGE_SERVER_UPDATE_MONEY = FLAG_BULLET_EFFECT_SERVER_MESSAGE + 1;

    /* Client --> Server */
    public static final short FLAG_MESSAGE_CLIENT_SHOOT = 1;
    public static final short FLAG_MESSAGE_CLIENT_JUMP = FLAG_MESSAGE_CLIENT_SHOOT + 1;
    public static final short FLAG_MESSAGE_CLIENT_BUY = FLAG_MESSAGE_CLIENT_JUMP + 1;
    public static final short FLAG_MESSAGE_CLIENT_USE_ITEM = FLAG_MESSAGE_CLIENT_BUY + 1;
    public static final short FLAG_MESSAGE_CLIENT_UPDATE_MONEY = FLAG_MESSAGE_CLIENT_USE_ITEM + 1;
}
