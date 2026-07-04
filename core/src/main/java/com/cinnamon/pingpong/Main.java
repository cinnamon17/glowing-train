package com.cinnamon.pingpong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cinnamon.pingpong.Logic.PingPongMatch;
import com.cinnamon.pingpong.Network.NetworkManager;
import com.cinnamon.pingpong.Screen.MainTitleScreen;

public class Main extends Game {
    public SpriteBatch batch;
    public MainTitleScreen mainTitleScreen;
    public TextureAtlas atlas;

    // 🌟 NUEVOS MOTORES SOBERANOS (Persisten durante todo el juego)
    public NetworkManager network;
    public PingPongMatch match;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.atlas = new TextureAtlas(Gdx.files.internal("ping_pong.atlas"));

        // Inicialización única de red y físicas en memoria RAM global
        this.network = new NetworkManager();
        this.match = new PingPongMatch();

        this.mainTitleScreen = new MainTitleScreen(this);
        this.setScreen(mainTitleScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameHandler.java", "dispose method - Cleaning global resources");

        // 1. Limpieza de las pantallas y assets visuales nativos
        if (this.batch != null) {
            this.batch.dispose();
        }
        if (this.mainTitleScreen != null) {
            this.mainTitleScreen.dispose();
        }
        if (this.atlas != null) {
            this.atlas.dispose();
        }

        // 2. Apagado definitivo y seguro de los sockets de red al cerrar la app
        if (this.network != null) {
            Gdx.app.log("GameHandler.java", "Closing network manager sockets.");
            this.network.close(null); // Pasa null porque el servidor se gestiona de forma independiente en el lobby/screen
        }
    }
}
