package com.cinnamon.pingpong.Screen;

import java.io.IOException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cinnamon.pingpong.Main;
import com.cinnamon.pingpong.Actor.MultiplayerLabel;
import com.cinnamon.pingpong.Dto.Data;
import com.cinnamon.pingpong.Logic.MatchRoleController;
import com.cinnamon.pingpong.Logic.HostRoleController;
import com.cinnamon.pingpong.Logic.ClientRoleController;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class LobbyScreen implements Screen {
    private Stage stage;
    private Sprite backgroundSprite;
    private Image background;
    private MultiplayerLabel label;

    private Server server;
    private final boolean isHost;
    private volatile boolean connected;
    private final Main game;

    public LobbyScreen(final Main game, boolean isHost) {
        this.game = game;
        this.isHost = isHost;
        this.connected = false;
    }

    @Override
    public void show() {
        Gdx.app.log("LobbyScreen", "Show method. Mode: " + (isHost ? "HOST" : "CLIENT"));

        this.stage = new Stage(new ScreenViewport(), game.batch);
        this.backgroundSprite = game.atlas.createSprite("background");
        this.background = new Image(backgroundSprite);
        this.background.setFillParent(true);
        this.background.setScaling(com.badlogic.gdx.utils.Scaling.stretch);

        this.label = new MultiplayerLabel();
        this.stage.addActor(background);
        this.stage.addActor(label);

        if (isHost) {
            Gdx.app.log("LobbyScreen", "Spinning up local KryoNet server...");
            this.server = new Server();
            this.server.start();
            this.server.getKryo().register(Data.class);

            try {
                this.server.bind(54555, 54777);
                Gdx.app.log("LobbyScreen", "Server waiting for external client connection.");
            } catch (IOException e) {
                Gdx.app.log("LobbyScreen", "Error binding server ports: " + e.toString());
            }

            server.addListener(new Listener() {
                @Override
                public void connected(Connection connection) {
                    String remoteIp = connection.getRemoteAddressTCP().getAddress().getHostAddress();
                    Gdx.app.log("LobbyScreen", "Server detected a connection from: " + remoteIp);

                    if (!remoteIp.equals("127.0.0.1") && !remoteIp.equals("localhost")) {
                        Gdx.app.log("LobbyScreen", "Rival player detected! Starting match...");
                        connected = true;
                    }
                }
            });

            // Usamos la red global del juego
            game.network.startAndConnectAsync(server);
        } else {
            Gdx.app.log("LobbyScreen", "Connecting client to discovery service...");
            game.network.startAndConnectAsync(null);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act(delta);
        this.stage.draw();

        if (!isHost && game.network.isConnected()) {
            connected = true;
        }

        if (connected) {
            Gdx.app.log("LobbyScreen", "Switching screen to GameScreen.");
            MatchRoleController roleController = isHost ? new HostRoleController() : new ClientRoleController();

            // Pasamos las instancias globales limpias sin destrucciones intermedias
            game.setScreen(new GameScreen(game, this.server, game.network, game.match, roleController));
        }
    }

    @Override public void resize(int width, int height) { this.stage.getViewport().update(width, height, true); }
    @Override public void resume() {}
    @Override public void pause() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) this.stage.dispose();
        // 🌟 YA NO SE DESTRUYE LA RED AQUÍ. Pertenece a Main.java
    }
}
