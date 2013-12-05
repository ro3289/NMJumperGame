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
    public float initX;
    public float initY;
    public float endX;
    public float endY;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlayerJumpClientMessage() {

    }

    public PlayerJumpClientMessage(final int pPlayerID, final float iX, final float iY, final float eX, final float eY) {
        set(pPlayerID, iX, iY, eX, eY);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPlayerID, final float iX, final float iY, final float eX, final float eY) {
        mPlayerID = pPlayerID;
        initX = iX;
        initY = iY;
        endX = eX;
        endY = eY;
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
        initX = pDataInputStream.readFloat();
        initY = pDataInputStream.readFloat();
        endX = pDataInputStream.readFloat();
        endY = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mPlayerID);
        pDataOutputStream.writeFloat(initX);
        pDataOutputStream.writeFloat(initY);
        pDataOutputStream.writeFloat(endX);
        pDataOutputStream.writeFloat(endY);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
