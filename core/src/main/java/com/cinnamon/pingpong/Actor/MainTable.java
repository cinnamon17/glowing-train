package com.cinnamon.pingpong.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cinnamon.pingpong.Main;

public class MainTable extends Table{

    private Skin uiSkin;
    public MainTable(final Main game){

        super();
        this.setFillParent(true);
		uiSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        TextButton menuButton = new TextButton("Menu", uiSkin);
        this.add(menuButton).expandX().right();
        this.row();

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Yendo al menú...");
                game.setScreen(game.mainTitleScreen);
            }
        });

    }
}
