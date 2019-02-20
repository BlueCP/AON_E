package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;
import com.mygdx.game.settings.ControlSettings;

public class ControlSettingsScreen extends MyScreen {

	private Screen prevScreen;
	Stage stage;

	Table table;
	private TextButton basicAttackButton;
	private Array<TextButton> abilityButtons;
	private TextButton openInventoryButton;

	private ButtonGroup<TextButton> keyBindingsGroup;

	ControlSettingsScreen(AON_E game, Screen prevScreen) {
		super(game);

		this.prevScreen = prevScreen;

		keyBindingsGroup = new ButtonGroup<>();

		Label basicAttackLabel = new Label("Basic attack:", AON_E.SKIN);
		basicAttackButton = new TextButton(Input.Keys.toString(ControlSettings.basicAttackKey()), AON_E.SKIN, "toggle");
		basicAttackButton.setName("basic attack");
		keyBindingsGroup.add(basicAttackButton);

		Array<Label> abilityLabels = new Array<>();
		abilityButtons = new Array<>();
		for (int i = 0; i < 8; i ++) {
			Label abilityLabel = new Label("Ability " + (i + 1) + ":", AON_E.SKIN);
			abilityLabels.add(abilityLabel);

			TextButton abilityButton = new TextButton(Input.Keys.toString(ControlSettings.abilityKey(i + 1)), AON_E.SKIN, "toggle");
			abilityButton.setName("ability " + (i + 1));
			keyBindingsGroup.add(abilityButton);
			abilityButtons.add(abilityButton);
		}

		Label openInventoryLabel = new Label("Open inventory:", AON_E.SKIN);
		openInventoryButton = new TextButton(Input.Keys.toString(ControlSettings.openInventoryKey()), AON_E.SKIN, "toggle");
		openInventoryButton.setName("open inventory");
		keyBindingsGroup.add(openInventoryButton);

		TextButton resetButton = new TextButton("Reset to default", AON_E.SKIN);
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ControlSettings.resetKeyBindings();
				updateButtonText();
			}
		});

		TextButton backButton = new TextButton("Back", AON_E.SKIN);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(prevScreen);
			}
		});

		keyBindingsGroup.uncheckAll();

		stage = new Stage(game.viewport);

		table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center);
		table.add(basicAttackLabel).width(300);
		table.add(basicAttackButton).width(100);
		table.row().padTop(30);

		for (int i = 0; i < 8; i ++) {
			table.add(abilityLabels.get(i)).fillX();
			table.add(abilityButtons.get(i)).fillX();
			table.row().padTop(30);
		}
		table.add(openInventoryLabel).fillX();
		table.add(openInventoryButton).fillX();
		table.row().padTop(30);
		table.add(resetButton).colspan(2).fillX();
		table.row().padTop(50);
		table.add(backButton).colspan(2).fillX();

		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	private void updateButtonText() {
		basicAttackButton.setText(Input.Keys.toString(ControlSettings.basicAttackKey()));

		for (int i = 0; i < 8; i ++) {
			abilityButtons.get(i).setText(Input.Keys.toString(ControlSettings.abilityKey(i + 1)));
		}

		openInventoryButton.setText(Input.Keys.toString(ControlSettings.openInventoryKey()));
	}

	private void updateKeybinding(String key) {
		TextButton button = keyBindingsGroup.getChecked();

		if (button.getText().equals(key)) {
			return;
		}

		if (button.getName().equals("basic attack")) {
			if (ControlSettings.setBasicAttackKey(Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		} else if (button.getName().contains("ability")) {
			String abilityIndex = button.getName().substring(button.getName().length() - 1);
			if (ControlSettings.setAbilityKey(Integer.parseInt(abilityIndex), Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		} else if (button.getName().equals("open inventory")) {
			if (ControlSettings.setOpenInventoryKey(Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		}
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
		}

		String key = Input.Keys.toString(keycode);

		if (key.length() == 1) {
			updateKeybinding(key);
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
