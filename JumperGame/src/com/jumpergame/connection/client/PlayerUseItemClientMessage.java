package com.jumpergame.connection.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jumpergame.constant.ConnectionConstants;

public class PlayerUseItemClientMessage extends ClientMessage implements ConnectionConstants {
 // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int userID;
    public int TargetID;
    public int itemID;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlayerUseItemClientMessage() {

    }

    public PlayerUseItemClientMessage(final int uID, final int tID, final int iID) {
        set(uID, tID, iID);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int uID, final int tID, final int iID) {
        userID = uID;
        TargetID = tID;
        itemID = iID;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_USE_ITEM;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        userID = pDataInputStream.readInt();
        TargetID = pDataInputStream.readInt();
        itemID = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(userID);
        pDataOutputStream.writeInt(TargetID);
        pDataOutputStream.writeInt(itemID);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
