package com.jumpergame;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.util.debug.Debug;

public class MainClient implements IUpdateHandler {

    public final MainActivity mActivity;
    public final ServerConnector mServerConnector;
    
    private boolean mGameInitialized = false;
    private int[] mControl;

    public MainClient(MainActivity pActivity, ServerConnector pServerConnector) {
        mActivity = pActivity;
        mServerConnector = pServerConnector;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (mGameInitialized) {
            synchronized(mControl) {
                try {
                    // TODO C->S refresh
                } catch (Exception e) {
                    Debug.e(e);
                }
            }
        }
    }

    @Override
    public void reset() {
        
    }
}
