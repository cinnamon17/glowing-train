package com.cinnamon.pingpong.Screen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cinnamon.pingpong.Main;
import com.cinnamon.pingpong.Actor.Ball;
import com.cinnamon.pingpong.Actor.Paddle;
import com.cinnamon.pingpong.Actor.Score;
import com.cinnamon.pingpong.Dto.Data;
import com.cinnamon.pingpong.Input.PaddleInputProcessor;

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
    private boolean isServer;
    private DataOutputStream serverDataOutputStream, clientDataOutputStream;
    private DataInputStream serverDataInputStream, clientDataInputStream;
    private Json json;
    private Data data;
    private OrthographicCamera camera;

    public GameScreen(final Main game) {
        this.game = game;
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
        //this.paddleEnemy = new Paddle(paddleEnemySprite, WORLD_WIDTH / 2 - paddle.getWidth() / 2, WORLD_HEIGHT - paddle.getHeight());
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
        this.stage.addActor(background);
        this.stage.addActor(ball);
        this.stage.addActor(score);
        this.stage.addActor(paddle);
        this.stage.addActor(paddleEnemy);
        Gdx.input.setInputProcessor(inputMultiplexer);
        this.json = new Json();
        this.data = new Data();
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
        this.updateActors();
        this.stage.act(delta);
        this.stage.draw();
        this.ballCheckCollision();

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
    public void updateActors() {
        this.scoreUpdate();
    }

    public void updateMultiplayerCommunication() {
        try {
            if (this.isServer()) {
                this.handleServerSideCommunication();
            } else {
                this.handleClientSideCommunication();
            }
        } catch (IOException e) {
            this.logError(e);
        }
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

    public void setServerDataOutputStream(OutputStream outputStream) {
        this.serverDataOutputStream = new DataOutputStream(outputStream);
    }

    public void setServerDataInputStream(InputStream inputStream) {
        this.serverDataInputStream = new DataInputStream(inputStream);
    }

    public void setClientDataOutputStream(OutputStream outputStream) {
        this.clientDataOutputStream = new DataOutputStream(outputStream);
    }

    public void setClientDataInputStream(InputStream inputStream) {
        this.clientDataInputStream = new DataInputStream(inputStream);
    }

    public DataInputStream getServerDataInputStream() {
        return serverDataInputStream;
    }

    public DataOutputStream getServerDataOutputStream() {
        return serverDataOutputStream;
    }

    public DataInputStream getClientDataInputStream() {
        return clientDataInputStream;
    }

    public DataOutputStream getClientDataOutputStream() {
        return clientDataOutputStream;
    }

    public boolean isServer() {
        return this.isServer;
    }

    public void setIsServer(boolean b) {
        this.isServer = b;
    }


    public void closeDataStreams() {

        if (this.isServer()) {
            this.closeServerDataStream();
        } else {
            this.closeClientDataStream();
        }
    }

    public void closeServerDataStream() {
        try {
            if (this.serverDataInputStream != null) {
                this.serverDataInputStream.close();
                this.serverDataOutputStream.close();
            }
        } catch (IOException e) {
            Gdx.app.log("GameHandler.java", "Error closing DataStreams", e);
        }
    }

    public void closeClientDataStream() {

        try {
            if (this.clientDataInputStream != null) {
                this.clientDataInputStream.close();
                this.clientDataOutputStream.close();
            }
        } catch (IOException e) {
            Gdx.app.log("GameHandler.java", "Error closing client DataStreams", e);
        }

    }

    private void handleServerSideCommunication() throws IOException {
        updateServerData();
        sendServerDataToClient();
        receiveClientDataAndUpdatePaddle();
    }

    private void handleClientSideCommunication() throws IOException {
        receiveServerDataAndUpdateClient();
        updateClientData();
        sendClientDataToServer();
    }

    private void updateServerData() {
        data.setServerPaddleX(this.paddle.getX());
        data.setServerBallX(this.ball.getX());
        data.setServerBallY(calculateServerBallY());
        data.setScorePlayer(this.score.getScorePlayer());
        data.setScoreEnemy(this.score.getScoreEnemy());
    }

    private float calculateServerBallY() {
        return Gdx.graphics.getHeight() - this.getBallActor().getY() - this.getBallActor().getHeight();
    }

    private void sendServerDataToClient() throws IOException {
        this.getServerDataOutputStream().writeUTF(json.toJson(data));
    }

    private void receiveClientDataAndUpdatePaddle() throws IOException {
        Data clientData = json.fromJson(Data.class, this.getServerDataInputStream().readUTF());
        this.getPaddleActorEnemy().setX(clientData.getClientPaddleX());
        this.paddleEnemy.setIsLeftMoved(clientData.getClientPaddleIsLeftMoved());
        this.paddleEnemy.setIsRightMoved(clientData.getClientPaddleIsRightMoved());
    }

    private void receiveServerDataAndUpdateClient() throws IOException {
        Data serverData = json.fromJson(Data.class, this.getClientDataInputStream().readUTF());
        this.getPaddleActorEnemy().setX(serverData.getServerPaddleX());
        this.getBallActor().setPosition(serverData.getServerBallX(), serverData.getServerBallY());
        this.score.setScoreEnemy(serverData.getScoreEnemy());
        this.score.setScorePlayer(serverData.getScorePlayer());
    }

    private void updateClientData() {
        data.setClientPaddleX(this.getPaddleActor().getX());
        data.setClientPaddleIsRightMoved(this.getPaddleActor().isRightMoved());
        data.setClientPaddleIsLeftMoved(this.getPaddleActor().isLeftMoved());
    }

    private void sendClientDataToServer() throws IOException {
        this.getClientDataOutputStream().writeUTF(json.toJson(data));
    }

    private void logError(IOException e) {
        Gdx.app.log("GameScreen.java", "Error sending Data", e);
    }
}
