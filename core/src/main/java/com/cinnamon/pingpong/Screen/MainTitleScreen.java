package com.cinnamon.pingpong.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cinnamon.pingpong.Main;
import com.cinnamon.pingpong.Actor.MainTitleMenu;

public class MainTitleScreen implements Screen {

    private Stage stage;
    private MainTitleMenu mainTitleMenuButton;
    private Sprite backgroundSprite;
    private Image background;
	final Main game;

	public MainTitleScreen(final Main game) {
		this.game = game;
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log("MainTitleScreen.java", "resize method");
        this.stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		Gdx.app.log("MainTitleScreen.java", "pause method");
	}

	@Override
	public void show() {

		Gdx.app.log("MainTitleScreen.java", "show method");
        this.stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);
        this.mainTitleMenuButton = new MainTitleMenu(game);
        this.backgroundSprite = game.atlas.createSprite("background");
        this.background = new Image(backgroundSprite);
        this.background.setFillParent(true);
        this.background.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
		this.mainTitleMenuButton.create();
        this.stage.addActor(background);
        this.stage.addActor(mainTitleMenuButton);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(delta);
        this.stage.draw();
	}

	@Override
	public void hide() {
		Gdx.app.log("MainTitleScreen.java", "hide method");
        this.mainTitleMenuButton.removeEventListeners();
	}

	@Override
	public void dispose() {
		Gdx.app.log("MainTitleScreen.java", "dispose method");
        this.stage.dispose();
	}

	@Override
	public void resume() {
		Gdx.app.log("MainTitleScreen.java", "resume method");
	}
}
