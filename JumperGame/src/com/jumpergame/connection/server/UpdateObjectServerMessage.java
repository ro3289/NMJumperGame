package com.jumpergame.connection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jumpergame.constant.ConnectionConstants;

public class UpdateObjectServerMessage extends ServerMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mObjID;
    public float mX;
    public float mY;

    // ===========================================================
    // Constructors
    // ===========================================================

    public UpdateObjectServerMessage() {

    }

    public UpdateObjectServerMessage(final int pPlayerID, final float iVx, final float iVy) {
        set(pPlayerID, iVx, iVy);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPlayerID, final float iVx,final float iVy) {
        mObjID = pPlayerID;
        mX = iVx;
        mY = iVy;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_UPDATE_OBJ;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mObjID = pDataInputStream.readInt();
        mX = pDataInputStream.readFloat();
        mY = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mObjID);
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
