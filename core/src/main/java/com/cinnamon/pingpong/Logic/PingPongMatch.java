package com.cinnamon.pingpong.Logic;

import com.badlogic.gdx.math.MathUtils;

public class PingPongMatch {
    private float ballX = 0.5f;
    private float ballY = 0.5f;
    private float ballSpeedX = 0.3f;
    private float ballSpeedY = 0.3f;

    private float hostPaddleX = 0.5f;
    private float clientPaddleX = 0.5f;
    private float paddleHeightRatio = 0.05f; // Altura relativa de la pala

    private int scorePlayer = 0;
    private int scoreEnemy = 0;

    public void updateHostPhysics(float delta) {
        // 1. Mover la bola
        ballX += ballSpeedX * delta;
        ballY += ballSpeedY * delta;

        // 2. Rebotar en los laterales de la pantalla (X)
        if (ballX <= 0 || ballX >= 1f) {
            ballSpeedX = -ballSpeedX;
        }

        // 3. Lógica de Puntuación (Tocar arriba o abajo)
        if (ballY >= 1.0f) {
            scorePlayer++;
            resetBall();
        } else if (ballY <= 0.0f) {
            scoreEnemy++;
            resetBall();
        }
    }

    public void resetBall() {
        this.ballX = 0.5f;
        this.ballY = 0.5f;
        this.ballSpeedY = -ballSpeedY; // Invierte dirección al sacar
    }

    public float interpolate(float current, float target, float alpha) {
        return MathUtils.lerp(current, target, alpha);
    }

    public float getBallX() { return ballX; }
    public void setBallX(float ballX) { this.ballX = ballX; }
    public float getBallY() { return ballY; }
    public void setBallY(float ballY) { this.ballY = ballY; }
    public float getHostPaddleX() { return hostPaddleX; }
    public void setHostPaddleX(float hostPaddleX) { this.hostPaddleX = hostPaddleX; }
    public float getClientPaddleX() { return clientPaddleX; }
    public void setClientPaddleX(float clientPaddleX) { this.clientPaddleX = clientPaddleX; }
    public int getScorePlayer() { return scorePlayer; }
    public void setScorePlayer(int scorePlayer) { this.scorePlayer = scorePlayer; }
    public int getScoreEnemy() { return scoreEnemy; }
    public void setScoreEnemy(int scoreEnemy) { this.scoreEnemy = scoreEnemy; }
    public float getPaddleHeightRatio() { return paddleHeightRatio; }
    public void setPaddleHeightRatio(float ratio) { this.paddleHeightRatio = ratio; }
}
