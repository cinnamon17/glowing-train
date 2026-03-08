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
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * ServerScreen
 */
public class ServerScreen implements Screen {

    private Stage stage;
    private Sprite backgroundSprite;
    private Image background;
    private MultiplayerLabel label;
    private Server server;
    private Boolean conected;
    private Data data;
	final Main game;

    public ServerScreen(final Main game) {

        this.game = game;
    }

    @Override
    public void show() {

        Gdx.app.log("MainTitleScreen.java", "show method");
        this.stage = new Stage(new ScreenViewport(), game.batch);
        this.backgroundSprite = game.atlas.createSprite("background");
        this.background = new Image(backgroundSprite);
        this.background.setFillParent(true);
        this.background.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
        this.label = new MultiplayerLabel();
        this.stage.addActor(background);
        this.stage.addActor(label);
        this.server = new Server();
        this.server.start();
        this.server.getKryo().register(Data.class);
        this.conected = false;
        this.data = new Data();

        try{
            this.server.bind(54555, 54777);
            Gdx.app.log("ServerScreen.java", "server waiting for connections");
        }catch(IOException e){
            Gdx.app.log("ServerScreen.java", "Error conecting: " + e.toString());
        }


        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Data) {
                    Data request = (Data)object;
                    Gdx.app.log("ServerScreen.java", "received data");
                    conected = true;
                    server.sendToAllExceptUDP(connection.getID(), request);
                }
            }
        });

    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act(delta);
        this.stage.draw();

        if (conected) {
            game.setScreen(new GameScreen(game, this.server));

        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void hide() {

        //game.getMultiplayerLabel().setVisible(false);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }

}
