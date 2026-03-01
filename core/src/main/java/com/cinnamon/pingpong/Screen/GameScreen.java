package com.cinnamon.pingpong.Screen;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cinnamon.pingpong.Main;
import com.cinnamon.pingpong.Actor.Ball;
import com.cinnamon.pingpong.Actor.Paddle;
import com.cinnamon.pingpong.Actor.Score;
import com.cinnamon.pingpong.Dto.Data;
import com.cinnamon.pingpong.Input.PaddleInputProcessor;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameScreen implements Screen {
    private final Main game;
    private float WORLD_HEIGHT;
    private float WORLD_WIDTH;
    private Music music;
    private Sprite paddleSprite;
    private Sprite paddleEnemySprite;
    private Paddle paddle;
    private Paddle paddleEnemy;
    private Sprite backgroundSprite;
    private Image background;
    private Sprite ballSprite;
    private Ball ball;
    private InputMultiplexer inputMultiplexer;
    private PaddleInputProcessor paddleInputProcessor;
    private Stage stage;
    private Score score;
    private boolean conected;
    private Data data;
    private OrthographicCamera camera;
    private Table mainTable;
    private Server server;
    private Boolean isHost;
    private Client client;
    private InetAddress address;

    public GameScreen(final Main game, Server server ) {
        this.game = game;
        this.server = server;
        this.isHost =  (server!= null);
    }

    @Override
    public void show() {

        Gdx.app.log("GameScreen.java", "show");
        this.WORLD_HEIGHT = 480;
        this.WORLD_WIDTH = 800;
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mainMusic.wav"));
        this.paddleSprite = game.atlas.createSprite("paddleRed");
        this.paddleEnemySprite = game.atlas.createSprite("paddleBlu");
        this.ballSprite = game.atlas.createSprite("ballBlue");
        this.backgroundSprite = game.atlas.createSprite("background");
        this.paddle = new Paddle(paddleSprite);
        this.paddleEnemy = new Paddle(paddleEnemySprite);
        this.ball = new Ball(ballSprite);
        this.score = new Score();
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera), game.batch);
        this.setMusicLooping(true);
        this.musicPlay();
        this.paddleInputProcessor = new PaddleInputProcessor(game, paddle);
        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(getStage());
        this.inputMultiplexer.addProcessor(paddleInputProcessor);
        this.background = new Image(backgroundSprite);
        this.background.setFillParent(true);
        this.background.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
        this.mainTable = new Table();
        this.mainTable.setFillParent(true);
        this.mainTable.center();
        this.mainTable.add(score).expandY().center().pad(10);
        this.mainTable.row();
        this.stage.addActor(background);
        this.stage.addActor(ball);
        this.stage.addActor(paddle);
        this.stage.addActor(paddleEnemy);
        this.stage.addActor(mainTable);
        Gdx.input.setInputProcessor(inputMultiplexer);
        this.data = new Data();

        this.client = new Client();
        this.client.start();
        this.client.getKryo().register(Data.class);
        this.address = client.discoverHost(54777, 5000);

        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Data) {
                    Data response = (Data)object;
                    data.setClientPaddleX(response.getClientPaddleX());
                    data.setHostPaddleX(response.getHostPaddleX());

                    if (!isHost) {
                        data.setBallX(response.getBallX());
                        data.setBallY(response.getBallY());
                        data.setScoreEnemy(response.getScoreEnemy());
                        data.setScorePlayer(response.getScorePlayer());
                    }
                }
            }
        });

        if (this.address != null) {
            try {
                client.connect(5000, this.address.getHostAddress(), 54555, 54777);
                this.conected = true;
                Gdx.app.log("GameScreen.java", "connected to server");
            } catch (Exception e) {
                Gdx.app.log("GameScreen.java", "could not stablish a connection: " + e.toString());
            }
        }

    }

    @Override
    public void resize(int width, int height) {

        Gdx.app.log("GameScreen.java", "resize method");
        Gdx.app.log("GameScreen.java", "width: " + width + " Height: " + height);
        this.stage.getViewport().update(width, height, false);
        this.WORLD_WIDTH = stage.getViewport().getWorldWidth();
        this.WORLD_HEIGHT = stage.getViewport().getWorldHeight();

        paddleEnemy.setY(WORLD_HEIGHT - paddleEnemy.getHeight());
    }

    @Override
    public void resume() {

        Gdx.app.log("GameScreen.java", "resume method");
        this.musicPlay();
    }

    @Override
    public void dispose() {
        this.musicDispose();
        this.stageDispose();
    }

    @Override
    public void hide() {
        this.musicPause();
        Gdx.app.log("GameScreen.java", "hide");
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen.java", "pause");
    }

    @Override
    public void render(float delta) {

        this.clearScreen();
        if (conected) {

            float anchoActual = stage.getViewport().getWorldWidth();
            float altoActual = stage.getViewport().getWorldHeight();

            if (isHost) {

                float targetX = this.data.getClientPaddleX() * anchoActual;
                float currentX = paddleEnemy.getX();
                paddleEnemy.setX(com.badlogic.gdx.math.MathUtils.lerp(currentX, targetX, 10f * delta));
                this.ball.act(delta);
                this.ballCheckCollision();
                this.scoreUpdate();

                Data packet = new Data();
                packet.setBallX(this.ball.getX() / anchoActual);
                packet.setBallY(this.ball.getY() / altoActual);
                packet.setScorePlayer(this.score.getScorePlayer());
                packet.setScoreEnemy(this.score.getScoreEnemy());
                packet.setHostPaddleX(this.paddle.getX() / anchoActual);
                this.client.sendUDP(packet);

            } else {

                float targetX = this.data.getHostPaddleX() * anchoActual;
                float currentX = paddleEnemy.getX();
                paddleEnemy.setX(com.badlogic.gdx.math.MathUtils.lerp(currentX, targetX, 10f * delta));
                float ballTargetX = data.getBallX() * anchoActual;
                float ballTargetY = data.getBallY() * altoActual;

                float bX = com.badlogic.gdx.math.MathUtils.lerp(ball.getX(), ballTargetX, 15f * delta);
                float bY = com.badlogic.gdx.math.MathUtils.lerp(ball.getY(), ballTargetY, 15f * delta);
                ball.setPosition(bX, bY);

                this.score.setScorePlayer(data.getScorePlayer());
                this.score.setScoreEnemy(data.getScoreEnemy());
                this.score.update();

                Data packetClient = new Data();
                packetClient.setClientPaddleX(this.paddle.getX() / anchoActual);
                this.client.sendUDP(packetClient);
            }

            Gdx.app.log("DEBUG", "Bola recibida: " + data.getBallX());
        }

        this.stage.act(delta);
        this.stage.draw();

    }

    public void setMusicLooping(boolean b) {
        this.music.setLooping(b);
    }

    public void musicPlay() {
        this.music.play();
    }


    public void musicPause() {
        this.music.pause();
    }

    public void musicDispose() {
        this.music.dispose();
    }
    public void stageDispose() {
        this.stage.dispose();
    }

    public Stage getStage() {
        return this.stage;
    }
    public void updateCamera() {

        this.clearScreen();
        this.cameraUpdate();
        this.setProjectionMatrixCombined();
    }

    public void ballCheckCollision() {
        this.ball.checkColision(this.paddle);
        this.ball.checkColision(this.paddleEnemy);
    }

    public Paddle getPaddleActor() {
        return this.paddle;
    }

    public Paddle getPaddleActorEnemy() {
        return this.paddleEnemy;
    }


    public Ball getBallActor() {
        return this.ball;
    }

    public Score getScoreActor() {
        return this.score;
    }

    public void setMusic() {
        this.setMusicLooping(true);
        this.musicPlay();
    }
    public void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void cameraUpdate() {
        this.camera.update();
    }

    public void setProjectionMatrixCombined() {
        this.game.batch.setProjectionMatrix(this.getCamera().combined);
    }

    public void scoreUpdate() {

        if (this.ball.isBallTouchingTopOfScreen()) {
            this.score.incrementScorePlayer();
            this.ball.setPosition(this.WORLD_WIDTH / 2, this.WORLD_HEIGHT / 2);
        }

        if (this.ball.isBallTouchingBottomOfScreen()) {
            this.score.incrementScoreEnemy();
            this.ball.setPosition(this.WORLD_WIDTH / 2, this.WORLD_HEIGHT / 2);
        }
        this.score.update();
    }

}
