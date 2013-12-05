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
 * @since 12:23:20 - 21.05.2011
 */
public class ConnectionMainServerMessage extends ServerMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private long mTimestamp;

    // ===========================================================
    // Constructors
    // ===========================================================

    @Deprecated
    public ConnectionMainServerMessage() {

    }

    public ConnectionMainServerMessage(final long pTimestamp) {
        this.mTimestamp = pTimestamp;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    public long getTimestamp() {
        return this.mTimestamp;
    }
    
    public void setTimestamp(long pTimestamp) {
        this.mTimestamp = pTimestamp;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_CONNECTION_MAIN;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        this.mTimestamp = pDataInputStream.readLong();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeLong(this.mTimestamp);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
