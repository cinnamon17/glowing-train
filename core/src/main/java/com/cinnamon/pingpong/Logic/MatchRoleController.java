package com.cinnamon.pingpong.Logic;

import com.cinnamon.pingpong.Actor.Ball;
import com.cinnamon.pingpong.Actor.Paddle;
import com.cinnamon.pingpong.Actor.Score;
import com.cinnamon.pingpong.Network.NetworkManager;

public interface MatchRoleController {
    void updateAndSync(float delta, float screenWidth, float screenHeight,
            Paddle paddle, Paddle paddleEnemy, Ball ball, Score score,
            PingPongMatch match, NetworkManager network);
}
