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
    public int mBulletID;
    public int mType;
    public float initVx;
    public float initVy;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlayerShootClientMessage() {

    }

    public PlayerShootClientMessage(final int pPlayerID, final int pBulletID, final int type, final float iVx, final float iVy) {
        set(pPlayerID, pBulletID, type, iVx, iVy);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPlayerID, final int pBulletID, final int type, final float iVx, final float iVy) {
        mPlayerID = pPlayerID;
        mBulletID = pBulletID;
        mType = type;
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
        mBulletID = pDataInputStream.readInt();
        mType = pDataInputStream.readInt();
        initVx = pDataInputStream.readFloat();
        initVy = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mPlayerID);
        pDataOutputStream.writeInt(mBulletID);
        pDataOutputStream.writeInt(mType);
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
