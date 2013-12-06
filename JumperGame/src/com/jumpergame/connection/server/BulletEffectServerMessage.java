package com.jumpergame.connection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jumpergame.constant.ConnectionConstants;

public class BulletEffectServerMessage extends ServerMessage implements ConnectionConstants {
    public int mBulletID;
    public String mType;
    
    public BulletEffectServerMessage() {
        
    }
    
    public void set(final int id, final String bulletAData) {
        mBulletID = id;
        mType = bulletAData;
    }

    @Override
    public short getFlag() {
        // TODO Auto-generated method stub
        return FLAG_BULLET_EFFECT_SERVER_MESSAGE;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream)
            throws IOException {
        mBulletID = pDataInputStream.readInt();
        mType = pDataInputStream.readUTF();
    }

    @Override
    protected void onWriteTransmissionData(DataOutputStream pDataOutputStream)
            throws IOException {
        pDataOutputStream.writeInt(mBulletID);
        pDataOutputStream.writeUTF(mType);
    }
}
