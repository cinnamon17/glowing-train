package com.cinnamon.pingpong.Input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import com.cinnamon.pingpong.Main;
import com.cinnamon.pingpong.Actor.Paddle;

/**
 * Input handler for Paddle
 */
public class PaddleInputProcessor implements InputProcessor {

    private Paddle paddle;
    private Main game;
    private float width;

    public PaddleInputProcessor(Main game, Paddle paddle) {
        this.game = game;
        this.paddle = paddle;
        this.width = Gdx.graphics.getWidth();
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.LEFT:
                paddle.setLeftMove(false);
                break;
            case Keys.RIGHT:
                paddle.setRightMove(false);
                break;
            case Keys.UP:
                break;
            case Keys.DOWN:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Keys.LEFT:
                paddle.setLeftMove(true);
                break;
            case Keys.RIGHT:
                paddle.setRightMove(true);
                break;
            case Keys.UP:
                break;
            case Keys.DOWN:
                break;
            case Keys.ESCAPE:
                this.game.setScreen(this.game.mainTitleScreen);
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenX < width / 2) {
           paddle.setLeftMove(false);
        }else{
           paddle.setRightMove(false);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (screenX < width / 2) {
           paddle.setLeftMove(true);
        }else{
           paddle.setRightMove(true);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'touchCancelled'");
	}

}
