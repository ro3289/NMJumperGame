package com.jumpergame.connection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jumpergame.constant.ConnectionConstants;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:23:25 - 21.05.2011
 */
public class ConnectionEstablishedServerMessage extends ServerMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    
    public int pID;
    
    // ===========================================================
    // Constructors
    // ===========================================================

    public ConnectionEstablishedServerMessage() {
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void setID(final int id) {
        pID = id;
    }
    
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        pID = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(pID);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
