package com.cinnamon.pingpong.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Score
 */
public class Score extends Table {
	private Skin skin;
	private Label playerScore;
	private Label enemyScore;
	private int scorePlayer = 0;
	private int scoreEnemy = 0;

	public Score() {
		super();
        this.center();
		this.setFillParent(true);
		this.skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
		this.playerScore = new Label("Player: " + this.scorePlayer, this.skin);
		this.enemyScore = new Label("Enemy: " + this.scoreEnemy, this.skin);
		this.add(playerScore).fillX().uniformX();
		this.row().pad(10, 0, 10, 0);
		this.add(enemyScore).fillX().uniformX();
		this.row();
	}

	public void update() {

		this.playerScore.setText("Player: " + this.scorePlayer);
		this.enemyScore.setText("Enemy: " + this.scoreEnemy);
	}

	public void setScorePlayer(int scorePlayer) {
		this.scorePlayer = scorePlayer;
	}

	public void setScoreEnemy(int scoreEnemy) {
		this.scoreEnemy = scoreEnemy;
	}

	public int getScorePlayer() {
		return scorePlayer;
	}

	public int getScoreEnemy() {
		return scoreEnemy;
	}

	public void incrementScorePlayer() {
		this.scorePlayer++;
	}

	public void incrementScoreEnemy() {
		this.scoreEnemy++;
	}

}
