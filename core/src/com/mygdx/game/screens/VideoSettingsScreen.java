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
import com.mygdx.game.settings.VideoSettings;

public class VideoSettingsScreen extends MyScreen {

	private Screen prevScreen;
	Stage stage;

	private TextButton vSyncButton;
	private TextButton fullScreenButton;
	private TextButton screenShakeButton;
	private TextButton particlesButton;

	VideoSettingsScreen(AON_E game, Screen prevScreen) {
		super(game);

		this.prevScreen = prevScreen;

		vSyncButton = new TextButton("VSync", AON_E.SKIN, "toggle");
		vSyncButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (vSyncButton.isChecked()) {
					VideoSettings.enableVSync();
				} else {
					VideoSettings.disableVSync();
				}
			}
		});

		fullScreenButton = new TextButton("Fullscreen", AON_E.SKIN, "toggle");
		fullScreenButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (fullScreenButton.isChecked()) {
					VideoSettings.enableFullScreen();
				} else {
					VideoSettings.disableFullScreen();
				}
			}
		});

		screenShakeButton = new TextButton("Screen shake", AON_E.SKIN, "toggle");
		screenShakeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (screenShakeButton.isChecked()) {
					VideoSettings.enableScreenShake();
				} else {
					VideoSettings.disableScreenShake();
				}
			}
		});

		particlesButton = new TextButton("Particles", AON_E.SKIN, "toggle");
		particlesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (particlesButton.isChecked()) {
					VideoSettings.enableParticles();
				} else {
					VideoSettings.disableParticles();
				}
			}
		});

		TextButton resetButton = new TextButton("Reset to default", AON_E.SKIN);
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				VideoSettings.reset();
				updateWidgets();
			}
		});

		TextButton backButton = new TextButton("Back", AON_E.SKIN);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(prevScreen);
			}
		});

		updateWidgets();

		stage = new Stage(game.viewport);

		Table table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center);
		table.add(vSyncButton).width(400).padBottom(50).row();
		table.add(fullScreenButton).fillX().padBottom(50).row();
		table.add(screenShakeButton).fillX().padBottom(50).row();
		table.add(particlesButton).fillX().padBottom(50).row();
		table.add(resetButton).fillX().padBottom(50).row();
		table.add(backButton).fillX();

		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}

	private void updateWidgets() {
		vSyncButton.setChecked(VideoSettings.isVSyncEnabled());
		fullScreenButton.setChecked(VideoSettings.isFullscreen());
		screenShakeButton.setChecked(VideoSettings.isScreenShakeEnabled());
		particlesButton.setChecked(VideoSettings.isParticlesEnabled());
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
