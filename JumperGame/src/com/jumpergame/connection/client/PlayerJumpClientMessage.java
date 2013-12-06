package com.jumpergame.connection.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jumpergame.constant.ConnectionConstants;

public class PlayerJumpClientMessage extends ClientMessage implements ConnectionConstants {
 // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mPlayerID;
    public float vX;
    public float vY;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlayerJumpClientMessage() {

    }

    public PlayerJumpClientMessage(final int pPlayerID, final float iX, final float iY) {
        set(pPlayerID, iX, iY);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPlayerID, final float iX, final float iY) {
        mPlayerID = pPlayerID;
        vX = iX;
        vY = iY;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_JUMP;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mPlayerID = pDataInputStream.readInt();
        vX = pDataInputStream.readFloat();
        vY = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mPlayerID);
        pDataOutputStream.writeFloat(vX);
        pDataOutputStream.writeFloat(vY);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
