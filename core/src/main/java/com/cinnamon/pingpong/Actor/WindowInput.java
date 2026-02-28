package com.cinnamon.pingpong.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cinnamon.pingpong.Main;

/**
 * Window
 */

public class WindowInput extends Window {
    private TextButton buttonOK;
    private TextButton buttonCancel;
    private TextField textField;
    private Label title;

    public WindowInput(final Main game) {
        super("", new Skin(Gdx.files.internal("skin/glassy-ui.json")));
        this.title = new Label("Enter ip to connect", new Skin(Gdx.files.internal("skin/glassy-ui.json")), "big-black");
        this.add(title);
        this.row().pad(10, 0, 10, 0);
        this.textField = new TextField("", new Skin(Gdx.files.internal("skin/glassy-ui.json")), "default");
        this.add(textField).fillX();
        this.row().pad(10, 0, 10, 0);
        Table pivotTable = new Table();
        this.buttonOK = new TextButton("Ok", new Skin(Gdx.files.internal("skin/glassy-ui.json")));
        this.buttonCancel = new TextButton("cancel", new Skin(Gdx.files.internal("skin/glassy-ui.json")));
        pivotTable.add(this.buttonOK);
        pivotTable.add(this.buttonCancel);
        this.add(pivotTable);
        this.sizeBy(this.getPrefWidth(), this.getPrefHeight() - 40f);
        this.setX((Gdx.graphics.getWidth() - this.getWidth()) / 2);
        this.setY((Gdx.graphics.getHeight() - this.getHeight()) / 2);

        this.buttonOK.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Gdx.app.log("ClientScreen.java", "connecting to server");
                String text = textField.getText();
                // try {
                //     socket = Gdx.net.newClientSocket(Net.Protocol.TCP, text, 8080,
                //             new SocketHints());
                //     OutputStream outputStream = socket.getOutputStream();
                //     InputStream inputStream = socket.getInputStream();
                //     game.setClientDataInputStream(inputStream);
                //     game.setClientDataOutputStream(outputStream);
                //     game.getWindowInput().remove();
                //     game.setScreen(game.getGameScreen());
                // } catch (GdxRuntimeException e) {
                //     Gdx.app.log("ClientScreen.java", "Error connecting to socket", e);
                //     game.getWindowInput().title.setText("Ip not valid");
                //     game.setScreen(game.getClientScreen());
                // }

            }
        });

        this.buttonCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // game.getWindowInput().setVisible(false);
                // game.getMainTitleMenu().getnewGameButton().setDisabled(true);
                // game.getMainTitleMenu().getMultiplayer().setDisabled(false);
                // game.getMainTitleMenu().getConnect().setDisabled(false);
                // game.setScreen(game.getMainTitleScreen());
            }
        });
    }
}
