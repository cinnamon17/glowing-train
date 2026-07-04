package com.cinnamon.pingpong.Dto;

/**
 * Data
 */
public class Data {

    private float clientPaddleX;
    private float hostPaddleX;
    private float hostPaddleHeight;
    public float ballX;
    public float ballY;
    public int scorePlayer;
    public int scoreEnemy;

    public Data(){}

    public float getClientPaddleX() {
        return clientPaddleX;
    }

    public Data setClientPaddleX(float pos) {
        this.clientPaddleX = pos;
        return this;
    }

    public float getHostPaddleX() {
        return hostPaddleX;
    }

    public void setHostPaddleX(float hostPaddleX) {
        this.hostPaddleX = hostPaddleX;
    }

    public float getPaddleHeight() {
        return hostPaddleHeight;
    }

    public void setPaddleHeight(float hostPaddleHeight) {
        this.hostPaddleHeight = hostPaddleHeight;
    }

    public float getBallX() {
        return ballX;
    }

    public void setBallX(float ballX) {
        this.ballX = ballX;
    }

    public float getBallY() {
        return ballY;
    }

    public void setBallY(float ballY) {
        this.ballY = ballY;
    }
    public int getScoreEnemy() {
        return scoreEnemy;
    }
    public void setScoreEnemy(int scoreEnemy) {
        this.scoreEnemy = scoreEnemy;
    }

    public int getScorePlayer() {
        return scorePlayer;
    }

    public void setScorePlayer(int scorePlayer) {
        this.scorePlayer = scorePlayer;
    }

}
