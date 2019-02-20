package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;

public class SettingsScreen extends MyScreen {

	private Screen prevScreen;
	Stage stage;

	SettingsScreen(AON_E game, Screen prevScreen) {
		super(game);

		this.prevScreen = prevScreen;

		TextButton videoSettingsButton = new TextButton("Video settings", AON_E.SKIN);
		videoSettingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new VideoSettingsScreen(game, game.getScreen()));
			}
		});

		TextButton audioSettingsButton = new TextButton("Audio settings", AON_E.SKIN);
		audioSettingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new AudioSettingsScreen(game, game.getScreen()));
			}
		});

		TextButton controlSettingsButton = new TextButton("Controls", AON_E.SKIN);
		controlSettingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new ControlSettingsScreen(game, game.getScreen()));
			}
		});

		TextButton backButton = new TextButton("Back", AON_E.SKIN);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(prevScreen);
			}
		});

		stage = new Stage(game.viewport);

		Table table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center);
		table.add(videoSettingsButton).width(400).padBottom(50).row();
		table.add(audioSettingsButton).fillX().padBottom(50).row();
		table.add(controlSettingsButton).fillX().padBottom(50).row();
		table.add(backButton).fillX();

		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}

	@Override
	void update() {
		universalUpdate();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			game.setScreen(prevScreen);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		game.soundManager.click.play();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}

	@Override
	public void render(float delta) {
		update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(game.camera.combined);

		stage.act();
		stage.draw();

		game.batch.begin();
		game.pointer.draw(game.batch);
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		updateViewport(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
