package com.jumpergame.connection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jumpergame.constant.ConnectionConstants;

public class RemoveObjectServerMessage extends ServerMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mObjectId;

    // ===========================================================
    // Constructors
    // ===========================================================

    public RemoveObjectServerMessage() {
    }

    public RemoveObjectServerMessage(final int pObjectId) {
        this.mObjectId = pObjectId;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pObjectId) {
        this.mObjectId = pObjectId;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_REMOVE_OBJ;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        this.mObjectId = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(this.mObjectId);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
