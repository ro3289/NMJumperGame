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
 * @since 02:02:12 - 01.03.2011
 */
public class UpdateScoreServerMessage extends ServerMessage implements ConnectionConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    public int mPlayerID;
    public int mScore;

    // ===========================================================
    // Constructors
    // ===========================================================

    public UpdateScoreServerMessage() {

    }

    public UpdateScoreServerMessage(final int pPaddleID, final int pScore) {
        this.mPlayerID = pPaddleID;
        this.mScore = pScore;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void set(final int pPaddleID, final int pScore) {
        this.mPlayerID = pPaddleID;
        this.mScore = pScore;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_UPDATE_SCORE;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        this.mPlayerID = pDataInputStream.readInt();
        this.mScore = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(this.mPlayerID);
        pDataOutputStream.writeInt(this.mScore);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}