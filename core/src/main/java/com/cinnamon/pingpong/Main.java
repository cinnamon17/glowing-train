package com.cinnamon.pingpong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cinnamon.pingpong.Screen.MainTitleScreen;

public class Main extends Game {

    public SpriteBatch batch;
    public MainTitleScreen mainTitleScreen;
    public TextureAtlas atlas;

    @Override
    public void create() {

        this.batch = new SpriteBatch();
        this.atlas = new TextureAtlas(Gdx.files.internal("ping_pong.atlas"));
        this.mainTitleScreen = new MainTitleScreen(this);
        this.setScreen(mainTitleScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameHandler.java", "dispose method");
        this.batch.dispose();
        this.mainTitleScreen.dispose();
        this.atlas.dispose();
    }
}
