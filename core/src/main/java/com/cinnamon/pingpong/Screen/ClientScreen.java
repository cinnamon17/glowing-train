package com.cinnamon.pingpong.Screen;

import com.badlogic.gdx.Screen;
import com.cinnamon.pingpong.Main;

/**
 * ClientScreen
 */
public class ClientScreen implements Screen {
    private Main game;

    public ClientScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        //game.getWindowInput().setVisible(true);
    }

    @Override
    public void hide() {

        //game.getWindowInput().setVisible(false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(float delta) {

        //game.stageDraw();
    }
}
