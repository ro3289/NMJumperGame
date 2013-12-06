package com.jumpergame;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.jumpergame.Manager.ResourcesManager;
import com.jumpergame.Manager.SceneManager;
import com.jumpergame.Scene.MultiplayerGameScene;
import com.jumpergame.connection.client.ConnectionEstablishClientMessage;
import com.jumpergame.connection.server.AddObjectServerMessage;
import com.jumpergame.connection.server.AddPlayerServerMessage;
import com.jumpergame.connection.server.BulletEffectServerMessage;
import com.jumpergame.connection.server.ConnectionCloseServerMessage;
import com.jumpergame.connection.server.ConnectionEstablishedServerMessage;
import com.jumpergame.connection.server.ConnectionMainServerMessage;
import com.jumpergame.connection.server.ConnectionRejectedProtocolMissmatchServerMessage;
import com.jumpergame.connection.server.RemoveObjectServerMessage;
import com.jumpergame.connection.server.UpdateMoneyServerMessage;
import com.jumpergame.connection.server.UpdateObjectServerMessage;
import com.jumpergame.connection.server.UpdatePlayerServerMessage;
import com.jumpergame.connection.server.UpdateScoreServerMessage;
import com.jumpergame.constant.ConnectionConstants;
import com.jumpergame.constant.GeneralConstants;

public class MainActivity extends BaseGameActivity implements GeneralConstants, ConnectionConstants {
	private ResourcesManager resourcesManager;
	private BoundCamera mCamera;
	
	public MainServer mServer;
//	public MainClient mClient;
	private String mServerIP = LOCALHOST_IP;
    public MainServerConnector mServerConnector;
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
//	    showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
	    
		this.mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), this.mCamera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException 
	{
		ResourcesManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());
	    resourcesManager = ResourcesManager.getInstance();
	    pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException 
	{
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException 
	{
	/* It will display the splash screen until different tasks have been executed 
	 * (Loading the menu resources, the menu scene and setting the scene to menu scene.) 
	 */
	 mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                // load menu resources, create menu scene
	                // set menu scene using scene manager
	                // disposeSplashScene();
	                // READ NEXT ARTICLE FOR THIS PART.
	            	System.out.println("HERE!!!");
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                SceneManager.getInstance().createMenuScene();
	            }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}
	
	@Override
    public Dialog onCreateDialog(final int pID) {
        switch(pID) {
            case DIALOG_SHOW_SERVER_IP_ID:
                try {
                    return new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Your Server-IP ...")
                    .setCancelable(false)
                    .setMessage("The IP of your Server is:\n" + WifiUtils.getWifiIPv4Address(this))
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
                } catch (final UnknownHostException e) {
                    return new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Your Server-IP ...")
                    .setCancelable(false)
                    .setMessage("Error retrieving IP of your Server: " + e)
                    .setPositiveButton(android.R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface pDialog, final int pWhich) {
                            MainActivity.this.finish();
                        }
                    })
                    .create();
                }
            case DIALOG_ENTER_SERVER_IP_ID:
                final EditText ipEditText = new EditText(this);
                return new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Enter Server-IP ...")
                .setCancelable(false)
                .setView(ipEditText)
                .setPositiveButton("Connect", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                        MainActivity.this.mServerIP = ipEditText.getText().toString();
                        MainActivity.this.initClient();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                        MainActivity.this.finish();
                    }
                })
                .create();
            case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
                return new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Be Server or Client ...")
                .setCancelable(false)
                .setPositiveButton("Client", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                        MainActivity.this.showDialog(DIALOG_ENTER_SERVER_IP_ID);
                    }
                })
                .setNeutralButton("Server", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                        MainActivity.this.toastOnUiThread("You can add and move sprites, which are only shown on the clients.");
                        MainActivity.this.initServer();
                        MainActivity.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
                    }
                })
                .setNegativeButton("Both", new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                        MainActivity.this.toastOnUiThread("You can add sprites and move them, by dragging them.");
                        MainActivity.this.initServerAndClient();
                        MainActivity.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
                    }
                })
                .create();
            default:
                return super.onCreateDialog(pID);
        }
    }
	
	@Override
	protected void onDestroy()
	{
        if(this.mServer != null) {
            try {
                this.mServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
            } catch (final IOException e) {
                Debug.e(e);
            }
            this.mServer.terminate();
        }
        
        if(this.mServerConnector != null) {
            this.mServerConnector.terminate();
        }
/*
        if (this.mClient != null) {
            if(this.mClient.mServerConnector != null) {
                this.mClient.mServerConnector.terminate();
            }
  1      }
*/	    
		super.onDestroy();
	    System.exit(0);	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}
	
    private void initServerAndClient() {
        MainActivity.this.initServer();

        /* Wait some time after the server has been started, so it actually can start up. */
        try {
            Thread.sleep(500);
        } catch (final Throwable t) {
            Debug.e(t);
        }

        MainActivity.this.initClient();
    }

    private void initServer() {
        Debug.d("initServer");
        this.mServer = new MainServer(this, new MainClientConnectorListener());

        this.mServer.start();

        this.mEngine.registerUpdateHandler(mServer);
    }
/*
    private void initClient() {
        Debug.d("initClient, mServerIP = " + this.mServerIP);
        try {
            mClient = new MainClient(this, new MainServerConnector(mServerIP, new MainServerConnectorListener()));

            mClient.mServerConnector.getConnection().start();
            getEngine().registerUpdateHandler(mClient);
        } catch (final Throwable t) {
            Debug.e(t);
        }
    }	
*/
    private void initClient() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity.this.mServerConnector = new MainServerConnector(MainActivity.this.mServerIP, new MainServerConnectorListener());

                    MainActivity.this.mServerConnector.getConnection().start();
                } catch (final Throwable t) {
                    Debug.e(t);
                }
            }
        };
        
        new Thread(r).start();
    }
    
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public class MainServerConnector extends ServerConnector<SocketConnection> implements ConnectionConstants { // TODO S->C protocols
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================

        // ===========================================================
        // Constructors
        // ===========================================================

        private static final short FLAG_UPDATE_MONEY_SERVER_MESSAGE = 0;

        public MainServerConnector(final String pServerIP, final ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
            super(new SocketConnection(new Socket(pServerIP, SERVER_PORT)), pSocketConnectionServerConnectorListener);

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    System.out.println("GG!!");
                    MainActivity.this.finish();
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, ConnectionEstablishedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    Debug.d("CLIENT: Connection established.");
                    System.out.println("Connected!!");
                    
                    final ConnectionEstablishedServerMessage connectionEstablishedServerMessage = (ConnectionEstablishedServerMessage) pServerMessage;
                    final int pID = connectionEstablishedServerMessage.pID;
                    ((MultiplayerGameScene) SceneManager.getInstance().multiplayerScene).setPlayerID(pID);
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH, ConnectionRejectedProtocolMissmatchServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final ConnectionRejectedProtocolMissmatchServerMessage connectionRejectedProtocolMissmatchServerMessage = (ConnectionRejectedProtocolMissmatchServerMessage)pServerMessage;
                    if(connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() > PROTOCOL_VERSION) {
                        //                      Toast.makeText(context, text, duration).show();
                    } else if(connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() < PROTOCOL_VERSION) {
                        //                      Toast.makeText(context, text, duration).show();
                    }
                    MainActivity.this.finish();
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_MAIN, ConnectionMainServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final ConnectionMainServerMessage connectionPongServerMessage = (ConnectionMainServerMessage) pServerMessage;
                    final long roundtripMilliseconds = System.currentTimeMillis() - connectionPongServerMessage.getTimestamp();
                    Debug.v("Ping: " + roundtripMilliseconds / 2 + "ms");
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_ADD_PLAYER, AddPlayerServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final AddPlayerServerMessage addPlayerServerMessage = (AddPlayerServerMessage) pServerMessage;
                    System.out.println("Added!!");
                    ((MultiplayerGameScene) SceneManager.getInstance().multiplayerScene).addPlayer(
                        addPlayerServerMessage.mPlayerID,
                        addPlayerServerMessage.initX, 
                        addPlayerServerMessage.initY
                    );
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE, UpdateScoreServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final UpdateScoreServerMessage updateScoreServerMessage = (UpdateScoreServerMessage) pServerMessage;
                    ((MultiplayerGameScene) SceneManager.getInstance().multiplayerScene).updateScore(updateScoreServerMessage.mPlayerID, updateScoreServerMessage.mScore);
                }
            });

            this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_PLAYER, UpdatePlayerServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final UpdatePlayerServerMessage updateObjectServerMessage = (UpdatePlayerServerMessage) pServerMessage;
                    ((MultiplayerGameScene) SceneManager.getInstance().multiplayerScene).updatePlayer(
                        updateObjectServerMessage.mPlayerID,
                        updateObjectServerMessage.mX,
                        updateObjectServerMessage.mY
                    );
                }
            });
            
            this.registerServerMessage(FLAG_MESSAGE_SERVER_REMOVE_OBJ, RemoveObjectServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final RemoveObjectServerMessage removeObjectServerMessage = (RemoveObjectServerMessage) pServerMessage;
//                    MainActivity.this.updateBullet(updateBulletServerMessage.mBulletID, updateBulletServerMessage.mX, updateBulletServerMessage.mY); TODO
                }
            });
/*
            this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_PLAYER, UpdatePlayerServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final UpdatePlayerServerMessage updatePlayerServerMessage = (UpdatePlayerServerMessage) pServerMessage;
//                    MainActivity.this.updatePlayer(updatePlayerServerMessage.mPlayerID, updatePlayerServerMessage.mX, updatePlayerServerMessage.mY); TODO
                }
            });
            
            
*/          
            this.registerServerMessage(FLAG_BULLET_EFFECT_SERVER_MESSAGE, BulletEffectServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final BulletEffectServerMessage bulletEffectServerMessage = (BulletEffectServerMessage) pServerMessage;
                    
                    final int key = bulletEffectServerMessage.mBulletID;
                    final String type = bulletEffectServerMessage.mType;
                    BulletItem bullet = (BulletItem)((MultiplayerGameScene) SceneManager.getInstance().multiplayerScene).mBullets.get(key);
                    
                }
            });
            
            this.registerServerMessage(FLAG_UPDATE_MONEY_SERVER_MESSAGE, UpdateMoneyServerMessage.class, new IServerMessageHandler<SocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                    final UpdateMoneyServerMessage updateMoneyServerMessage = (UpdateMoneyServerMessage) pServerMessage;
                    
                    final int key = updateMoneyServerMessage.mPlayerID;
                    final int money = updateMoneyServerMessage.deltaMoney;
                    
                    ((MultiplayerGameScene) SceneManager.getInstance().multiplayerScene).setMoney(key, money);
                }
            });
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================

        // ===========================================================
        // Methods
        // ===========================================================

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
    }
    
    private class MainServerConnectorListener implements ISocketConnectionServerConnectorListener {
        @SuppressWarnings("deprecation")
        @Override
        public void onStarted(final ServerConnector<SocketConnection> pServerConnector) {
            try {
                pServerConnector.sendClientMessage(new ConnectionEstablishClientMessage(PROTOCOL_VERSION));
                Debug.d("Send ConnectionEstablishClientMessage");
            }
            catch(Exception e) {
                Debug.e(e);
            }
            MainActivity.this.toastOnUiThread("CLIENT: Connected to server.", Toast.LENGTH_SHORT);
        }

        @Override
        public void onTerminated(final ServerConnector<SocketConnection> pServerConnector) {
            MainActivity.this.toastOnUiThread("CLIENT: Disconnected from Server.", Toast.LENGTH_SHORT);
            MainActivity.this.finish();
        }
    }

    private class MainClientConnectorListener implements ISocketConnectionClientConnectorListener {
        @Override
        public void onStarted(final ClientConnector<SocketConnection> pClientConnector) {
            MainActivity.this.toastOnUiThread("SERVER: Client connected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress(), Toast.LENGTH_SHORT);
        }

        @Override
        public void onTerminated(final ClientConnector<SocketConnection> pClientConnector) {
            MainActivity.this.toastOnUiThread("SERVER: Client disconnected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress(), Toast.LENGTH_SHORT);
        }
    }
}
