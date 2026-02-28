
package com.cinnamon.pingpong.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Paddle extends Image {

    private boolean leftMove;
    private boolean rightMove;
    private float WORLD_HEIGHT;
    private float WORLD_WIDTH;

    public Paddle(TextureRegion textureRegion) {
        super(textureRegion);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (getStage() != null) {
            this.WORLD_WIDTH = getStage().getViewport().getWorldWidth();
            this.WORLD_HEIGHT = getStage().getViewport().getWorldHeight();
        }

        if (this.isLeftMoved()) {
            handleLeftMove();
        }

        if (this.isRightMoved()) {
            handleRightMove();
        }

    }

    public void handleLeftMove() {

        this.setX(this.getX() - 200 * Gdx.graphics.getDeltaTime());
        if (isAxisYTouched()) {
            this.setPosition(0, 0);
        }
    }

    public void handleRightMove() {

        this.setX(this.getX() + 200 * Gdx.graphics.getDeltaTime());
        if (isAxisYTouched()) {
            this.setPosition(this.WORLD_WIDTH - this.getWidth(), 0);
        }
    }

    public boolean isAxisYTouched() {
        return this.getX() <= 0 || this.getX() >= this.WORLD_WIDTH - this.getWidth();
    }

    public void setLeftMove(boolean t) {
        if (rightMove && t)
            rightMove = false;
        leftMove = t;
    }

    public void setRightMove(boolean t) {
        if (leftMove && t)
            leftMove = false;
        rightMove = t;
    }

    public boolean isLeftMoved() {
        return this.leftMove;
    }

    public boolean isRightMoved() {
        return this.rightMove;
    }

    public void setIsRightMoved(boolean b) {
        this.rightMove = b;
    }

    public void setIsLeftMoved(boolean b) {
        this.leftMove = b;
    }

}
