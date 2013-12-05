package com.jumpergame.connection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jumpergame.constant.ConnectionConstants;

public class UpdatePlayerServerMessage extends ServerMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mPlayerID;
    public float mX;
    public float mY;

    // ===========================================================
    // Constructors
    // ===========================================================

    public UpdatePlayerServerMessage() {

    }

    public UpdatePlayerServerMessage(final int pPlayerID, final float iVx, final float iVy) {
        set(pPlayerID, iVx, iVy);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPlayerID, final float iVx,final float iVy) {
        mPlayerID = pPlayerID;
        mX = iVx;
        mY = iVy;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_UPDATE_PLAYER;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mPlayerID = pDataInputStream.readInt();
        mX = pDataInputStream.readFloat();
        mY = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mPlayerID);
        pDataOutputStream.writeFloat(mX);
        pDataOutputStream.writeFloat(mY);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
