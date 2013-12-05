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
    public int mX;
    public int mY;
    public int mIsFaceRight;

    // ===========================================================
    // Constructors
    // ===========================================================

    public AddObjectServerMessage() {
    }

    public AddObjectServerMessage(final int pObjectId, final String pTextureName, final int pTiledNumber, final int pX, final int pY, final int pIsFaceRight) {
        this.mObjectId = pObjectId;
        this.mTextureName = pTextureName;
        this.mTiledNumber = pTiledNumber;
        this.mX = pX;
        this.mY = pY;
        this.mIsFaceRight = pIsFaceRight;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pObjectId, final String pTextureName, final int pTiledNumber, final int pX, final int pY, final int pIsFaceRight) {
        this.mObjectId = pObjectId;
        this.mTextureName = pTextureName;
        this.mTiledNumber = pTiledNumber;
        this.mX = pX;
        this.mY = pY;
        this.mIsFaceRight = pIsFaceRight;
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
        this.mX = pDataInputStream.readInt();
        this.mY = pDataInputStream.readInt();
        this.mIsFaceRight = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(this.mObjectId);
        pDataOutputStream.writeUTF(this.mTextureName);
        pDataOutputStream.writeInt(this.mTiledNumber);
        pDataOutputStream.writeInt(this.mX);
        pDataOutputStream.writeInt(this.mY);
        pDataOutputStream.writeInt(this.mIsFaceRight);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
