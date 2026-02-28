package com.cinnamon.pingpong.Screen;

import com.badlogic.gdx.Screen;
import com.cinnamon.pingpong.Main;

/**
 * ServerScreen
 */
public class ServerScreen implements Screen {

    private Main game;

    public ServerScreen(final Main game) {

        this.game = game;
    }

    @Override
    public void show() {

        //game.getMultiplayerLabel().setVisible(true);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        //game.stageDraw();
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

    }

}
