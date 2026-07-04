package com.cinnamon.pingpong.Screen;

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
import com.cinnamon.pingpong.Input.PaddleInputProcessor;
import com.cinnamon.pingpong.Logic.PingPongMatch;
import com.cinnamon.pingpong.Logic.MatchRoleController;
import com.cinnamon.pingpong.Network.NetworkManager;
import com.esotericsoftware.kryonet.Server;

public class GameScreen implements Screen {
    private final Main game;
    private final Server server;
    private final NetworkManager network;
    private final PingPongMatch match;

    // La estrategia inyectada que elimina el IF/ELSE
    private final MatchRoleController roleController;

    private float WORLD_HEIGHT = 480;
    private float WORLD_WIDTH = 800;

    private Music music;
    private Paddle paddle;
    private Paddle paddleEnemy;
    private Ball ball;
    private Stage stage;
    private Score score;

    // Recibe el controlador específico por constructor (Inversión de Control real)
    public GameScreen(final Main game, Server server, NetworkManager network, PingPongMatch match, MatchRoleController roleController) {
        this.game = game;
        this.server = server;
        this.network = network;
        this.match = match;
        this.roleController = roleController;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "Starting clean universal match view...");

        Sprite paddleSprite = game.atlas.createSprite("paddleRed");
        Sprite paddleEnemySprite = game.atlas.createSprite("paddleBlu");
        Sprite ballSprite = game.atlas.createSprite("ballBlue");
        Sprite backgroundSprite = game.atlas.createSprite("background");

        this.paddle = new Paddle(paddleSprite);
        this.paddleEnemy = new Paddle(paddleEnemySprite);
        this.ball = new Ball(ballSprite);
        this.score = new Score();

        OrthographicCamera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera), game.batch);

        Image background = new Image(backgroundSprite);
        background.setFillParent(true);
        background.setScaling(com.badlogic.gdx.utils.Scaling.stretch);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        mainTable.add(score).expandY().center().pad(10);

        this.stage.addActor(background);
        this.stage.addActor(ball);
        this.stage.addActor(paddle);
        this.stage.addActor(paddleEnemy);
        this.stage.addActor(mainTable);

        PaddleInputProcessor paddleInputProcessor = new PaddleInputProcessor(game, paddle);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(paddleInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.music = Gdx.audio.newMusic(Gdx.files.internal("mainMusic.wav"));
        this.music.setLooping(true);
        this.music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Si la red no está lista, pintamos el frame congelado
        if (!network.isConnected()) {
            this.stage.draw();
            return;
        }

        float screenWidth = stage.getViewport().getWorldWidth();
        float screenHeight = stage.getViewport().getWorldHeight();

        // 🌟 CERO CONDICIONALES: Delegamos la responsabilidad al rol correspondiente
        roleController.updateAndSync(delta, screenWidth, screenHeight, paddle, paddleEnemy, ball, score, match, network);

        this.score.update();
        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
        this.WORLD_WIDTH = stage.getViewport().getWorldWidth();
        this.WORLD_HEIGHT = stage.getViewport().getWorldHeight();
        paddleEnemy.setY(WORLD_HEIGHT - paddleEnemy.getHeight());
    }

    @Override public void resume() { if (music != null) music.play(); }
    @Override public void hide() { if (music != null) music.pause(); }
    @Override public void pause() {}

    @Override
    public void dispose() {
        if (music != null) music.dispose();
        if (stage != null) stage.dispose();
    }
}
