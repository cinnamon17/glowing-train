package com.cinnamon.pingpong.Logic;

import com.cinnamon.pingpong.Actor.Ball;
import com.cinnamon.pingpong.Actor.Paddle;
import com.cinnamon.pingpong.Actor.Score;
import com.cinnamon.pingpong.Dto.Data;
import com.cinnamon.pingpong.Network.NetworkManager;

public class ClientRoleController implements MatchRoleController {
    @Override
    public void updateAndSync(float delta, float screenWidth, float screenHeight,
            Paddle paddle, Paddle paddleEnemy, Ball ball, Score score,
            PingPongMatch match, NetworkManager network) {

        // 1. Notificar nuestra posición local al modelo lógico
        match.setClientPaddleX(paddle.getX() / screenWidth);

        // 2. Leer los datos absolutos que envió el Host
        Data netData = network.getLatestData();

        // 3. Mover los actores replicando e interpolando los datos de red para evitar tirones
        float targetEnemyX = netData.getHostPaddleX() * screenWidth;
        paddleEnemy.setX(match.interpolate(paddleEnemy.getX(), targetEnemyX, 10f * delta));
        paddleEnemy.setHeight(netData.getPaddleHeight() * screenHeight);

        float targetBallX = netData.getBallX() * screenWidth;
        float targetBallY = netData.getBallY() * screenHeight;
        ball.setPosition(
                match.interpolate(ball.getX(), targetBallX, 15f * delta),
                match.interpolate(ball.getY(), targetBallY, 15f * delta)
                );

        score.setScorePlayer(netData.getScorePlayer());
        score.setScoreEnemy(netData.getScoreEnemy());

        // 4. Responder al Host enviando únicamente nuestra coordenada de paleta
        Data clientPacket = new Data();
        clientPacket.setClientPaddleX(match.getClientPaddleX());
        network.sendUDP(clientPacket);
    }
}
