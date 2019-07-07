package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.settings.AudioSettings;

public class AudioSettingsScreen extends MyScreen {

	private Screen prevScreen;
	Stage stage;

	private Slider masterVolumeSlider;
	private Slider musicVolumeSlider;
	private Slider environmentalVolumeSlider;
	private Slider soundFXVolumeSlider;
	private Slider creatureVolumeSlider;
	private Slider UIVolumeSlider;

	AudioSettingsScreen(AON_E game, Screen prevScreen) {
		super(game);

		this.prevScreen = prevScreen;

		Label masterVolumeLabel = new Label("Master volume", AON_E.SKIN);
		masterVolumeSlider = new Slider(0, 100, 1, false, AON_E.SKIN);
		masterVolumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AudioSettings.setMasterVolume(masterVolumeSlider.getValue() / 100);
			}
		});

		Label musicVolumeLabel = new Label("Music", AON_E.SKIN);
		musicVolumeSlider = new Slider(0, 100, 1, false, AON_E.SKIN);
		musicVolumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AudioSettings.setMusicVolume(musicVolumeSlider.getValue() / 100);
			}
		});

		Label environmentalVolumeLabel = new Label("Environmental", AON_E.SKIN);
		environmentalVolumeSlider = new Slider(0, 100, 1, false, AON_E.SKIN);
		environmentalVolumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AudioSettings.setEnvironmentalVolume(environmentalVolumeSlider.getValue() / 100);
			}
		});

		Label soundFXVolumeLabel = new Label("Sound FX", AON_E.SKIN);
		soundFXVolumeSlider = new Slider(0, 100, 1, false, AON_E.SKIN);
		soundFXVolumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AudioSettings.setSoundFXVolume(soundFXVolumeSlider.getValue() / 100);
			}
		});

		Label creatureVolumeLabel = new Label("Creature sounds", AON_E.SKIN);
		creatureVolumeSlider = new Slider(0, 100, 1, false, AON_E.SKIN);
		creatureVolumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AudioSettings.setCreatureVolume(creatureVolumeSlider.getValue() / 100);
			}
		});

		Label UIVolumeLabel = new Label("UI sounds", AON_E.SKIN);
		UIVolumeSlider = new Slider(0, 100, 1, false, AON_E.SKIN);
		UIVolumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AudioSettings.setUIVolume(UIVolumeSlider.getValue() / 100);
			}
		});

		TextButton resetButton = new TextButton("Reset to default", AON_E.SKIN);
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AudioSettings.reset();
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
		table.add(masterVolumeLabel).width(300);
		table.add(masterVolumeSlider);
		table.row().padTop(30);
		table.add(musicVolumeLabel).fillX();
		table.add(musicVolumeSlider).fillX();
		table.row().padTop(30);
		table.add(environmentalVolumeLabel).fillX();
		table.add(environmentalVolumeSlider).fillX();
		table.row().padTop(30);
		table.add(soundFXVolumeLabel).fillX();
		table.add(soundFXVolumeSlider).fillX();
		table.row().padTop(30);
		table.add(creatureVolumeLabel).fillX();
		table.add(creatureVolumeSlider).fillX();
		table.row().padTop(30);
		table.add(UIVolumeLabel).fillX();
		table.add(UIVolumeSlider).fillX();
		table.row().padTop(50);
		table.add(resetButton).colspan(2).width(300);
		table.row().padTop(50);
		table.add(backButton).colspan(2).width(300);

		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}

	private void updateWidgets() {
		masterVolumeSlider.setValue(AudioSettings.masterVolume() * 100);
		musicVolumeSlider.setValue(AudioSettings.musicVolume() * 100);
		environmentalVolumeSlider.setValue(AudioSettings.environmentalVolume() * 100);
		soundFXVolumeSlider.setValue(AudioSettings.soundFXVolume() * 100);
		creatureVolumeSlider.setValue(AudioSettings.creatureVolume() * 100);
		UIVolumeSlider.setValue(AudioSettings.UIVolume() * 100);
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
