package com.cinnamon.pingpong.Actor;

import java.io.IOException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cinnamon.pingpong.Main;
import com.cinnamon.pingpong.Dto.Data;
import com.cinnamon.pingpong.Logic.ClientRoleController;
import com.cinnamon.pingpong.Logic.HostRoleController;
import com.cinnamon.pingpong.Logic.MatchRoleController;
import com.cinnamon.pingpong.Logic.PingPongMatch;
import com.cinnamon.pingpong.Network.NetworkManager;
import com.cinnamon.pingpong.Screen.GameScreen;
import com.cinnamon.pingpong.Screen.LobbyScreen;
import com.esotericsoftware.kryonet.Server;

public class MainTitleMenu extends Table {
    private final TextButton newGame;
    private final TextButton connect;
    private final TextButton exit;
    private final Skin skin;
    private final Main game;

    public MainTitleMenu(final Main game) {
        this.game = game;
        this.setFillParent(true);
        this.center();

        this.skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        this.newGame = new TextButton("Host Game (Play)", this.skin);
        this.connect = new TextButton("Join Game (Connect)", this.skin);
        this.exit = new TextButton("Exit", this.skin);

        this.add(newGame).fillX().uniformX();
        this.row().pad(10, 0, 10, 0);
        this.add(connect).fillX().uniformX();
        this.row();
        this.add(exit).fillX().uniformX();
    }

    public void create() {
        this.setVisible(true);

        // BOTÓN PARA SER HOST (Hospedar la partida)
        this.newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("MainTitleMenu", "Opening Lobby as Host...");
                game.setScreen(new LobbyScreen(game, true)); // true = es Host
            }
        });

        // BOTÓN PARA SER CLIENTE (Unirse a una partida existente)
        this.connect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("MainTitleMenu", "Opening Lobby as Client...");
                game.setScreen(new LobbyScreen(game, false)); // false = es Cliente
            }
        });

        this.exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    public void removeEventListeners() { this.setVisible(false); }
    public TextButton getnewGameButton() { return this.newGame; }
    public TextButton getConnect() { return connect; }
}
