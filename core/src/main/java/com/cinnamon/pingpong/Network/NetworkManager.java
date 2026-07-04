package com.cinnamon.pingpong.Network;

import com.cinnamon.pingpong.Dto.Data;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.net.InetAddress;
import com.badlogic.gdx.Gdx;

public class NetworkManager {
    private final Client client;
    private final Data lastReceivedData;
    private volatile boolean connected = false;
    private InetAddress serverAddress;

    public NetworkManager() {
        this.client = new Client();
        this.lastReceivedData = new Data();
        this.client.getKryo().register(Data.class);
    }

    public void startAndConnectAsync(final Server localServer) {
        // 🔥 PASO 1: Arrancamos el hilo interno dedicado de KryoNet ANTES de conectar.
        // Esto le dice al cliente de KryoNet que cree de forma nativa un hilo background
        // que leerá los paquetes UDP constantemente de forma asíncrona sin congelar la renderización.
        this.client.start();

        final boolean isHost = (localServer != null);

        if (isHost) {
            localServer.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof Data) {
                        Data request = (Data) object;
                        localServer.sendToAllExceptUDP(connection.getID(), request);
                    }
                }
            });
        }

        // Hilo paralelo de emparejamiento (Handshake)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isHost) {
                        Gdx.app.log("NetworkManager", "Host connecting to local loopback...");
                        // Conexión interna del host a su propia máquina
                        client.connect(5000, "127.0.0.1", 54555, 54777);
                        connected = true;
                        Gdx.app.log("NetworkManager", "Host loopback registration success.");
                    } else {
                        Gdx.app.log("NetworkManager", "Client scanning local network via UDP...");
                        serverAddress = client.discoverHost(54777, 35000);

                        if (serverAddress != null) {
                            Gdx.app.log("NetworkManager", "Host found at " + serverAddress + ". Registering TCP/UDP channels...");

                            // Conectamos de forma directa. Como el cliente ya está corriendo en su propio
                            // hilo de fondo (gracias a client.start()), el buffer se vaciará de inmediato.
                            client.connect(5000, serverAddress, 54555, 54777);
                            connected = true;
                            Gdx.app.log("NetworkManager", "Client fully connected and synchronized.");
                        } else {
                            Gdx.app.log("NetworkManager", "Discovery timeout. No host active.");
                        }
                    }
                } catch (Exception e) {
                    Gdx.app.log("NetworkManager", "Handshake network exception: " + e.toString());
                    e.printStackTrace();
                }
            }
        }).start();

        // Escuchador de paquetes entrantes (Se queda igual, pero ahora sí va a recibir datos gracias al paso 1)
        this.client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Data) {
                    Data response = (Data) object;
                    synchronized (lastReceivedData) {
                        lastReceivedData.setClientPaddleX(response.getClientPaddleX());
                        lastReceivedData.setHostPaddleX(response.getHostPaddleX());
                        if (!isHost) {
                            lastReceivedData.setBallX(response.getBallX());
                            lastReceivedData.setBallY(response.getBallY());
                            lastReceivedData.setScoreEnemy(response.getScoreEnemy());
                            lastReceivedData.setScorePlayer(response.getScorePlayer());
                            lastReceivedData.setPaddleHeight(response.getPaddleHeight());
                        }
                    }
                }
            }
        });
    }

    public void sendUDP(Data packet) {
        if (connected) {
            client.sendUDP(packet);
        }
    }

    public Data getLatestData() {
        synchronized (lastReceivedData) {
            return lastReceivedData;
        }
    }

    public boolean isConnected() { return connected; }

    public void close(Server localServer) {
        client.stop();
        if (localServer != null) {
            localServer.stop();
        }
    }
}
