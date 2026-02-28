package com.cinnamon.pingpong.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cinnamon.pingpong.Main;
import com.cinnamon.pingpong.Screen.GameScreen;

/**
 * Menu buttons items
 */
public class MainTitleMenu extends Table {

	private TextButton newGame;
	private TextButton multiplayer;
	private TextButton connect;
	private TextButton exit;
	private Skin skin;
	private Main game;
    private GameScreen gameScreen;

	public MainTitleMenu(final Main game) {

		this.game = game;

		this.setFillParent(true);
        this.center();
		this.skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

		this.newGame = new TextButton("Play", this.skin);
		this.multiplayer = new TextButton("Multiplayer", this.skin);
		this.connect = new TextButton("Connect", this.skin);
		this.exit = new TextButton("Exit", this.skin);
		this.add(newGame).fillX().uniformX();
		this.row().pad(10, 0, 10, 0);
		this.add(multiplayer).fillX().uniformX();
		this.row();
		this.add(connect).fillX().uniformX();
		this.row().pad(10, 0, 10, 0);
		this.add(exit).fillX().uniformX();

	}

	public void create() {

		this.setVisible(true);
		this.newGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("MainTitleScreen.java", "new game button pressed");
                if (gameScreen == null) {
                    gameScreen = new GameScreen(game);
				    game.setScreen(gameScreen);
                }
			}

		});

		this.multiplayer.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("MainTitleScreen.java", "Starting Server Socket");

				ServerSocketHints serverSocketHints = new ServerSocketHints();
				serverSocketHints.acceptTimeout = 0;
				ServerSocket server = Gdx.net.newServerSocket(Net.Protocol.TCP, "localhost",
						8080,
						serverSocketHints);

				// Thread serverListener = new Thread() {
				// 	@Override
				// 	public void run() {
				// 		Gdx.app.log("MainTitleScreen.java", "Waiting for connection");
				// 		Socket socket = server.accept(new SocketHints());
				// 		Gdx.app.log("MainTitleScreen.java", "client connected");
				// 		game.setServerDataInputStream(socket.getInputStream());
				// 		game.setServerDataOutputStream(socket.getOutputStream());
				// 		game.setIsServer(true);
				// 		game.setScreen(game.getGameScreen());
				// 		newGame.setDisabled(false);
				// 		connect.setDisabled(true);
				// 		multiplayer.setDisabled(true);
				// 	}
				// };
				//serverListener.start();
				//game.setScreen(game.getServerScreen());
			}

		});

		this.connect.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				newGame.setDisabled(false);
				connect.setDisabled(true);
				multiplayer.setDisabled(true);
				//game.setScreen(game.getClientScreen());
			}

		});
		this.exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("MainTitleScreen.java", "exit button pressed");
				Gdx.app.exit();
			}
		});
	}

	public void removeEventListeners() {
		this.setVisible(false);
	}

	public TextButton getnewGameButton() {
		return this.newGame;
	}

	public TextButton getConnect() {
		return connect;
	}

	public TextButton getMultiplayer() {
		return multiplayer;
	}

}
