package com.cinnamon.pingpong.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * MultiplayerLabel
 */
public class MultiplayerLabel extends Table {

    public MultiplayerLabel() {

        super();
        this.setFillParent(true);
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        Label label = new Label("Waiting for connection in LAN", skin, "big");
        label.setColor(skin.getColor("white"));
        this.add(label);
        this.row();
        label = new Label("PORT: 5630", skin, "big");
        label.setColor(skin.getColor("white"));
        this.add(label);
    }

}
