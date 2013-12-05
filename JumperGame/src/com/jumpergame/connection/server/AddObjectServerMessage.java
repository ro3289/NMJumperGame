package com.jumpergame.connection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jumpergame.constant.ConnectionConstants;

public class AddObjectServerMessage extends ServerMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mObjectId;
    public String mTextureName;
    public int mTiledNumber;
    public float mX;
    public float mY;

    // ===========================================================
    // Constructors
    // ===========================================================

    public AddObjectServerMessage() {
    }

    public AddObjectServerMessage(final int pObjectId, final String pTextureName, final int pTiledNumber, final float pX, final float pY) {
        this.mObjectId = pObjectId;
        this.mTextureName = pTextureName;
        this.mTiledNumber = pTiledNumber;
        this.mX = pX;
        this.mY = pY;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pObjectId, final String pTextureName, final int pTiledNumber, final float pX, final float pY) {
        this.mObjectId = pObjectId;
        this.mTextureName = pTextureName;
        this.mTiledNumber = pTiledNumber;
        this.mX = pX;
        this.mY = pY;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_ADD_OBJ;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        this.mObjectId = pDataInputStream.readInt();
        this.mTextureName = pDataInputStream.readUTF();
        this.mTiledNumber = pDataInputStream.readInt();
        this.mX = pDataInputStream.readFloat();
        this.mY = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(this.mObjectId);
        pDataOutputStream.writeUTF(this.mTextureName);
        pDataOutputStream.writeInt(this.mTiledNumber);
        pDataOutputStream.writeFloat(this.mX);
        pDataOutputStream.writeFloat(this.mY);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
