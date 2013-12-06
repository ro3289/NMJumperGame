package com.jumpergame.connection.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jumpergame.constant.ConnectionConstants;

public class PlayerUpdateMoneyClientMessage extends ClientMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mPlayerID;
    public int deltaMoney;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlayerUpdateMoneyClientMessage() {

    }

    public PlayerUpdateMoneyClientMessage(final int pPlayerID, final int del) {
        set(pPlayerID, del);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPlayerID, final int del) {
        mPlayerID = pPlayerID;
        deltaMoney = del;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_UPDATE_MONEY;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mPlayerID = pDataInputStream.readInt();
        deltaMoney = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mPlayerID);
        pDataOutputStream.writeInt(deltaMoney);
    }
    
    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}