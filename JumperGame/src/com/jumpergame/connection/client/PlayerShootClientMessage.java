package com.jumpergame.connection.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jumpergame.constant.ConnectionConstants;

public class PlayerShootClientMessage extends ClientMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mPlayerID;
    public float initVx;
    public float initVy;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlayerShootClientMessage() {

    }

    public PlayerShootClientMessage(final int pBulletID, final float iVx, final float iVy) {
        set(pBulletID, iVx, iVy);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPlayerID, final float iVx,final float iVy) {
        mPlayerID = pPlayerID;
        initVx = iVx;
        initVy = iVy;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_SHOOT;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mPlayerID = pDataInputStream.readInt();
        initVx = pDataInputStream.readFloat();
        initVy = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mPlayerID);
        pDataOutputStream.writeFloat(initVx);
        pDataOutputStream.writeFloat(initVy);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
