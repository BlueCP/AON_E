package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
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
import com.mygdx.game.settings.GameplaySettings;

public class GameplaySettingsScreen extends MyScreen {

	private Screen prevScreen;
	Stage stage;

	private Slider textSpeedSlider;

	GameplaySettingsScreen(AON_E game, Screen prevScreen) {
		super(game);

		this.prevScreen = prevScreen;

		// x = the value of the slider, y = the text speed.
		// y = 0.25 * 2^x
		// so x = log2(4y)
		// It is done like this to make the slider feel more natural, with a logarithmic scale.

		Label textSpeedLabel = new Label("Text speed", AON_E.SKIN);
		textSpeedSlider = new Slider(0, 4, 1, false, AON_E.SKIN);
		textSpeedSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameplaySettings.setTextSpeed((float) (0.25f * Math.pow(2, textSpeedSlider.getValue())));
			}
		});

		TextButton resetButton = new TextButton("Reset to default", AON_E.SKIN);
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameplaySettings.reset();
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
		table.add(textSpeedLabel).width(300);
		table.add(textSpeedSlider);
		table.row().padTop(50);
		table.add(resetButton).colspan(2).width(300);
		table.row().padTop(50);
		table.add(backButton).colspan(2).width(300);

		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}

	private void updateWidgets() {
		textSpeedSlider.setValue(MathUtils.log2(4 * GameplaySettings.textSpeed()));
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
