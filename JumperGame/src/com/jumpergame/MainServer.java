package com.jumpergame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.protocol.server.SocketServer;
import org.andengine.extension.multiplayer.protocol.server.SocketServer.ISocketServerListener.DefaultSocketServerListener;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.hardware.SensorManager;
import android.util.SparseArray;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Manager.SceneManager;
import com.jumpergame.Scene.MultiplayerGameScene;
import com.jumpergame.connection.client.ConnectionCloseClientMessage;
import com.jumpergame.connection.client.ConnectionEstablishClientMessage;
import com.jumpergame.connection.client.ConnectionPingClientMessage;
import com.jumpergame.connection.client.PlayerJumpClientMessage;
import com.jumpergame.connection.client.PlayerShootClientMessage;
import com.jumpergame.connection.client.PlayerUseItemClientMessage;
import com.jumpergame.connection.server.AddObjectServerMessage;
import com.jumpergame.connection.server.AddPlayerServerMessage;
import com.jumpergame.connection.server.ConnectionCloseServerMessage;
import com.jumpergame.connection.server.ConnectionEstablishedServerMessage;
import com.jumpergame.connection.server.ConnectionMainServerMessage;
import com.jumpergame.connection.server.ConnectionRejectedProtocolMissmatchServerMessage;
import com.jumpergame.connection.server.RemoveObjectServerMessage;
import com.jumpergame.connection.server.UpdateObjectServerMessage;
import com.jumpergame.connection.server.UpdatePlayerServerMessage;
import com.jumpergame.connection.server.UpdateScoreServerMessage;
import com.jumpergame.constant.ConnectionConstants;
import com.jumpergame.constant.GeneralConstants;

public class MainServer extends SocketServer<SocketConnectionClientConnector> implements IUpdateHandler, ContactListener, GeneralConstants, ConnectionConstants {
    
    // ===========================================================
    // Constants
    // ===========================================================
    
    
    // ===========================================================
    // Fields
    // ===========================================================
    
    private SocketServer<SocketConnectionClientConnector> mSocketServer;
    private ServerConnector<SocketConnection> mServerConnector;
    
    private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();
    private final ArrayList<UpdatePlayerServerMessage> mUpdatePlayerServerMessages = new ArrayList<UpdatePlayerServerMessage>();
    
    private final PhysicsWorld mPhysicsWorld;
    private MainActivity mMainActivity;
    
    private final SparseArray<Player_Server> mPlayerBodies = new SparseArray<Player_Server>();
//    private final SparseArray<Body> mBulletBodies = new SparseArray<Body>();
    private final SparseArray<Body> mBodies = new SparseArray<Body>();
    private ArrayList<ArrayList<Fixture>> mStairFixtures;
    private final HashMap<String, Integer> IP_PlayerID = new HashMap<String, Integer>();
    
    // ===========================================================
    // Constructors
    // ===========================================================
    public MainServer(final MainActivity activity, final ISocketConnectionClientConnectorListener pSocketConnectionClientConnectorListener) {
        super(SERVER_PORT, pSocketConnectionClientConnectorListener, new DefaultSocketServerListener<SocketConnectionClientConnector>());
        mMainActivity = activity;
        mStairFixtures = new ArrayList<ArrayList<Fixture>>();
        
        initMessagePool();
        
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
//        mPhysicsWorld = new FixedStepPhysicsWorld(FPS, 2, new Vector2(0, 0), false, 8, 8);
        mPhysicsWorld.setContactListener(this);
        
        initPhysics();
    }
    
    private void initMessagePool() {
        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, ConnectionEstablishedServerMessage.class);
        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_MAIN, ConnectionMainServerMessage.class);
        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class);
//        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_ADD_PLAYER, UpdatePlayerServerMessage.class);
        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE, UpdateScoreServerMessage.class);
//        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_UPDATE_BULLET, UpdateBulletServerMessage.class);
//        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_UPDATE_PLAYER, UpdatePlayerServerMessage.class);
        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_ADD_OBJ, AddObjectServerMessage.class);
        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_UPDATE_PLAYER, UpdatePlayerServerMessage.class);
        mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_REMOVE_OBJ, RemoveObjectServerMessage.class);
    }
    
    private void initPhysics() {
        final Vector2 gravity = Vector2Pool.obtain(mGravityX, mGravityY);
        this.mPhysicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
        
        Rectangle ground = ((MultiplayerGameScene)SceneManager.getInstance().multiplayerScene).mGround;
        Body groundBody = PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody, GROUND_FIXTURE_DEF);
        groundBody.setUserData("Wall");
        System.out.println("SERVER adds ground, Body_x = " + groundBody.getPosition().x + ", Body_y = " + groundBody.getPosition().y);
        
        for (Rectangle wall : ((MultiplayerGameScene)SceneManager.getInstance().multiplayerScene).mWalls) {
            Body wallBody = PhysicsFactory.createBoxBody(mPhysicsWorld, wall, BodyType.StaticBody, WALL_FIXTURE_DEF);
            wallBody.setUserData("Wall");
            System.out.println("SERVER adds wall, Body_x = " + wallBody.getPosition().x + ", Body_y = " + wallBody.getPosition().y);
        }
        
        for (Sprite sprite : ((MultiplayerGameScene)SceneManager.getInstance().multiplayerScene).getSprites()) {
            String type = (String) sprite.getUserData();
            Body body;
            
            if (type.equals("platform1")) {
                body = PhysicsFactory.createBoxBody(mPhysicsWorld, sprite, BodyType.StaticBody, STAIR_FIXTURE_DEF);
                body.setUserData("Wall");
                mStairFixtures.add(body.getFixtureList()) ;
            }
            
            
        }
        
        /*
        for (Ladder ladder : mMainActivity.getLadders()) {
            Body ladderBody = PhysicsFactory.createBoxBody(mPhysicsWorld, ladder.getRect(), BodyType.StaticBody, GROUND_AND_STAIR_FIXTURE_DEF);
            ladderBody.setUserData(new Sprite_Body(ladder.getRect(), "Wall"));
            System.out.println("SERVER adds ladder, Body_x = " + ladderBody.getPosition().x + ", Body_y = " + ladderBody.getPosition().y);
        }
        */
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    
    @Override
    public void onUpdate(float pSecondsElapsed) {
        // TODO updateFrame
        
        mPhysicsWorld.onUpdate(pSecondsElapsed);
        
        final SparseArray<Player_Server> players = mPlayerBodies;
        for(int j = 0; j < players.size(); j++) {
            final int key = players.keyAt(j);
            final Player_Server player = players.get(key);
            final Body pBody = player.getBody();
            
            Filter fil = pBody.getFixtureList().get(0).getFilterData();
            if (pBody.getLinearVelocity().y > 0) {
                fil.maskBits = MASKBITS_PLAYER_IGNORE_LADDER;
            }
            else {
                fil.maskBits = MASKBITS_PLAYER;
            }
            pBody.getFixtureList().get(0).setFilterData(fil);
        }
        
        /* Prepare UpdatePlayerServerMessage. */
        ArrayList<UpdatePlayerServerMessage> updatePlayerServerMessages = mUpdatePlayerServerMessages;
//        System.out.println("Player Num = " + updatePlayerServerMessages.size());
        
        for(int j = 0; j < players.size(); j++) {
            final int key = players.keyAt(j);
//            System.out.println("key = " + key);
            final Player_Server player = players.get(key); 
            final Body playerBody = player.getBody();
            final Vector2 playerPosition = playerBody.getPosition();

            final float playerX = playerPosition.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;// - ResourcesManager.getInstance().player_region.getWidth()/2;  //PADDLE_WIDTH_HALF;
            final float playerY = playerPosition.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;// - ResourcesManager.getInstance().player_region.getHeight()/2;//PADDLE_HEIGHT_HALF;
            
            System.out.println("update player, x = " + playerX + ", y = " + playerY);

            final UpdatePlayerServerMessage updatePlayerServerMessage = (UpdatePlayerServerMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_UPDATE_PLAYER);
            updatePlayerServerMessage.set(player.getPlayerID(), playerX, playerY);

            updatePlayerServerMessages.add(updatePlayerServerMessage);
        }
        
        try {

            /* Update Players */
            for(int j = 0; j < updatePlayerServerMessages.size(); ++j) {
//                System.out.println("update player XDDDDDDDDDDDDDDDD... size = " + updatePlayerServerMessages.size());
                sendBroadcastServerMessage(updatePlayerServerMessages.get(j));
            }
        } catch (final IOException e) {
            Debug.e(e);
        }
        
        /* Recycle messages. */
//        mMessagePool.recycleMessages(updateBulletServerMessages);
        mMessagePool.recycleMessages(updatePlayerServerMessages);
//        updateBulletServerMessages.clear();
        updatePlayerServerMessages.clear();

    }

    @Override
    public void reset() {
        // TODO leave it blank?
        
    }
    
    @Override
    public void beginContact(Contact pContact) {
        System.out.println("contacted!!");
        
        final Fixture x1 = pContact.getFixtureA();
        final Fixture x2 = pContact.getFixtureB();
        final Body BodyA = x1.getBody();
        final Body BodyB = x2.getBody();
        final float densityA = x1.getDensity();
        final float densityB = x2.getDensity();
//        Sprite_Body userDataA = (Sprite_Body) BodyA.getUserData();
//        Sprite_Body userDataB = (Sprite_Body) BodyB.getUserData();
        
        /*
        if(userDataA.getName() == "Wall" && userDataB.getName() == "Bullet") {
            MainActivity.this.removeEntity((IEntity) mBullets.get(userDataB.getEntity().getUserData()));
        } else if(userDataA.getName() == "Bullet" && userDataB.getName() == "Wall") {
            MainActivity.this.removeEntity((IEntity) mBullets.get(userDataA.getEntity().getUserData()));
        }
        
        if(userDataA.getName() == "otherPlayer" && userDataB.getName() == "Bullet") {
            MainActivity.this.removeEntity((IEntity) mBullets.get(userDataB.getEntity().getUserData()));
            MainActivity.this.setPlayerEnergy(1, 0, -10);
        } else if(userDataA.getName() == "Bullet" && userDataB.getName() == "otherPlayer") {
            MainActivity.this.removeEntity((IEntity) mBullets.get(userDataA.getEntity().getUserData()));
            MainActivity.this.setPlayerEnergy(1, 0, -10);
        }
        
        if (x1.getBody() != null && x2.getBody() != null && jumpState) {
            if(densityA ==2 || densityB == 2) {
                computeScore();
                System.out.println("Contact");
                Vector2 bodyVector = new Vector2(thisPlayer.getBody().getPosition().x, thisPlayer.getBody().getPosition().y);
                Vector2 forceVector = new Vector2(0,0);
                thisPlayer.getBody().applyForce(forceVector, bodyVector);
                Vector2 velocity = new Vector2(0,0);
                thisPlayer.getBody().setLinearVelocity(velocity);
            //  Vector2Pool.recycle(velocity); // Error message: more items recycled than obtained
                jumpState = false;
            }
        }
        */
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    @Override
    protected SocketConnectionClientConnector newClientConnector(
            SocketConnection pSocketConnection) throws IOException { // TODO C->S protocols

        final SocketConnectionClientConnector clientConnector = new SocketConnectionClientConnector(pSocketConnection);
        
        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, ConnectionCloseClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                pClientConnector.terminate();
            }
        });

        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, ConnectionEstablishClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                final ConnectionEstablishClientMessage connectionEstablishClientMessage = (ConnectionEstablishClientMessage) pClientMessage;
                final ConnectionEstablishedServerMessage connectionEstablishedServerMessage = (ConnectionEstablishedServerMessage) MainServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED);
                String newIP = pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress();
                
                if(connectionEstablishClientMessage.getProtocolVersion() == PROTOCOL_VERSION && !IP_PlayerID.keySet().contains(newIP)) {
                    try {
                        final float initX = MathUtils.random(10, CAMERA_WIDTH-10);
                        final float initY = 500;
                        final int newPlayerID = addPlayer(newIP, initX, initY);
                        
                        connectionEstablishedServerMessage.setID(newPlayerID);
                        pClientConnector.sendServerMessage(connectionEstablishedServerMessage);
                        System.out.println("sent connectionEstablishedServerMessage");
                        
//                        final int newObjectID = mPlayerBodies.get(newPlayerID).getObjectID();
                        pClientConnector.sendServerMessage(new AddPlayerServerMessage(newPlayerID, initX, initY));
                        sendBroadcastServerMessage(new AddPlayerServerMessage(newPlayerID, initX, initY));
                        System.out.println("sent AddPlayerServerMessage" + newPlayerID);
                        
                        for (Integer ID : IP_PlayerID.values()) {
                            if (ID != newPlayerID) {
                                Player_Server p = mPlayerBodies.get(ID);
                                pClientConnector.sendServerMessage(new AddPlayerServerMessage(ID, p.getBody().getPosition().x, p.getBody().getPosition().y));
                                System.out.println("sent AddPlayerServerMessage" + ID);
                            }
                        }
                        
                    } catch (IOException e) {
                        Debug.e(e);
                    }
                    MainServer.this.mMessagePool.recycleMessage(connectionEstablishedServerMessage);
                } else {
                    final ConnectionRejectedProtocolMissmatchServerMessage connectionRejectedProtocolMissmatchServerMessage = (ConnectionRejectedProtocolMissmatchServerMessage) MainServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH);
                    connectionRejectedProtocolMissmatchServerMessage.setProtocolVersion(PROTOCOL_VERSION);
                    try {
                        pClientConnector.sendServerMessage(connectionRejectedProtocolMissmatchServerMessage);
                    } catch (IOException e) {
                        Debug.e(e);
                    }
                    MainServer.this.mMessagePool.recycleMessage(connectionRejectedProtocolMissmatchServerMessage);
                }
            }
        });

        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PING, ConnectionPingClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                final ConnectionMainServerMessage connectionMainServerMessage = (ConnectionMainServerMessage) MainServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_MAIN);
                try {
                    pClientConnector.sendServerMessage(connectionMainServerMessage);
                } catch (IOException e) {
                    Debug.e(e);
                }
                MainServer.this.mMessagePool.recycleMessage(connectionMainServerMessage);
            }
        });
        
        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_JUMP, PlayerJumpClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                final PlayerJumpClientMessage playerJumpClientMessage = (PlayerJumpClientMessage)pClientMessage;
                // TODO jump
            }
        });
        
        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_SHOOT, PlayerShootClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                // TODO shoot
            }
        });
        
        clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_USE_ITEM, PlayerUseItemClientMessage.class, new IClientMessageHandler<SocketConnection>() {
            @Override
            public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
                // TODO useItem
            }
        });
        
        return clientConnector;
    }
    
    // ===========================================================
    // Methods
    // ===========================================================
    
    private synchronized int addPlayer(final String newIP, final float initX, final float initY) {
        Player_Server newPlayer = new Player_Server(newIP);
        
        AnimatedSprite appearance = new AnimatedSprite(initX, initY, ResourcesManager.getInstance().player_region, ResourcesManager.getInstance().vbom);
        Body newPlayerBody = PhysicsFactory.createBoxBody(mPhysicsWorld, appearance, BodyType.DynamicBody, PLAYER_FIXTURE_DEF);
        newPlayerBody.setUserData(newIP);
        
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(appearance, newPlayerBody, true, false));
        
        final int newID = IP_PlayerID.size();
        newPlayer.setPlayerID(newID);
        newPlayer.setBody(newPlayerBody);
        IP_PlayerID.put(newIP, newID);
        mPlayerBodies.append(newID, newPlayer);
        
        final int newObjectID = mBodies.size();
        newPlayer.setObjectID(newObjectID);
        mBodies.append(newObjectID, newPlayerBody);
        
        return newID;
    }
    
    private synchronized int shoot() {
        return 0;
    }
    
    private synchronized void jump() {
        
    }
    
    private synchronized void removeBody(Body body) {
        
    }
}
