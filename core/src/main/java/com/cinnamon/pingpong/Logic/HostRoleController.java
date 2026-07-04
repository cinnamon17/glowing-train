package com.cinnamon.pingpong.Logic;

import com.cinnamon.pingpong.Actor.Ball;
import com.cinnamon.pingpong.Actor.Paddle;
import com.cinnamon.pingpong.Actor.Score;
import com.cinnamon.pingpong.Dto.Data;
import com.cinnamon.pingpong.Network.NetworkManager;

public class HostRoleController implements MatchRoleController {
    @Override
    public void updateAndSync(float delta, float screenWidth, float screenHeight,
            Paddle paddle, Paddle paddleEnemy, Ball ball, Score score,
            PingPongMatch match, NetworkManager network) {

        // 1. Notificar nuestra posición local al modelo lógico
        match.setHostPaddleX(paddle.getX() / screenWidth);

        // 2. Ejecutar físicas y colisiones nativas
        match.updateHostPhysics(delta);
        ball.checkColision(paddle);
        ball.checkColision(paddleEnemy);

        // 3. Suavizar el movimiento de la paleta remota del cliente leyendo la red
        Data netData = network.getLatestData();
        float targetEnemyX = netData.getClientPaddleX() * screenWidth;
        paddleEnemy.setX(match.interpolate(paddleEnemy.getX(), targetEnemyX, 10f * delta));

        // 4. Sincronizar actores visuales con el estado del match
        ball.setPosition(match.getBallX() * screenWidth, match.getBallY() * screenHeight);
        score.setScorePlayer(match.getScorePlayer());
        score.setScoreEnemy(match.getScoreEnemy());

        // 5. Transmitir el estado absoluto al cliente por UDP
        Data packet = new Data();
        packet.setBallX(match.getBallX());
        packet.setBallY(match.getBallY());
        packet.setScorePlayer(match.getScorePlayer());
        packet.setScoreEnemy(match.getScoreEnemy());
        packet.setHostPaddleX(match.getHostPaddleX());
        packet.setPaddleHeight(paddle.getHeight() / screenHeight);
        network.sendUDP(packet);
    }
}
