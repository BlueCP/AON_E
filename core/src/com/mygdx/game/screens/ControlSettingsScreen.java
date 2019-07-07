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
	private TextButton respawnButton;
	private TextButton optionsScreenButton;
	private TextButton cancelMovementButton;
	private TextButton questScreenButton;
	private TextButton achievementsScreenButton;
	private TextButton interactButton;

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

		Label respawnLabel = new Label("Respawn:", AON_E.SKIN);
		respawnButton = new TextButton(Input.Keys.toString(ControlSettings.respawnKey()), AON_E.SKIN, "toggle");
		respawnButton.setName("respawn");
		keyBindingsGroup.add(respawnButton);

		Label optionsScreenLabel = new Label("Options screen:", AON_E.SKIN);
		optionsScreenButton = new TextButton(Input.Keys.toString(ControlSettings.optionsScreenKey()), AON_E.SKIN, "toggle");
		optionsScreenButton.setName("options screen");
		keyBindingsGroup.add(optionsScreenButton);

		Label cancelMovementLabel = new Label("Cancel movement:", AON_E.SKIN);
		cancelMovementButton = new TextButton(Input.Keys.toString(ControlSettings.cancelMovementKey()), AON_E.SKIN, "toggle");
		cancelMovementButton.setName("cancel movement");
		keyBindingsGroup.add(cancelMovementButton);

		Label questScreenLabel = new Label("Quest screen:", AON_E.SKIN);
		questScreenButton = new TextButton(Input.Keys.toString(ControlSettings.questScreenKey()), AON_E.SKIN, "toggle");
		questScreenButton.setName("quest screen");
		keyBindingsGroup.add(questScreenButton);

		Label achievementsScreenLabel = new Label("Achievements screen:", AON_E.SKIN);
		achievementsScreenButton = new TextButton(Input.Keys.toString(ControlSettings.achivementsScreenKey()), AON_E.SKIN, "toggle");
		achievementsScreenButton.setName("achievements screen");
		keyBindingsGroup.add(achievementsScreenButton);

		Label interactLabel = new Label("Interact:", AON_E.SKIN);
		interactButton = new TextButton(Input.Keys.toString(ControlSettings.interactKey()), AON_E.SKIN, "toggle");
		interactButton.setName("interact");
		keyBindingsGroup.add(interactButton);

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
		table.add(respawnLabel).fillX();
		table.add(respawnButton).fillX();
		table.row().padTop(30);
		table.add(optionsScreenLabel).fillX();
		table.add(optionsScreenButton).fillX();
		table.row().padTop(30);
		table.add(cancelMovementLabel).fillX();
		table.add(cancelMovementButton).fillX();
		table.row().padTop(30);
		table.add(questScreenLabel).fillX();
		table.add(questScreenButton).fillX();
		table.row().padTop(30);
		table.add(achievementsScreenLabel).fillX();
		table.add(achievementsScreenButton).fillX();
		table.row().padTop(30);
		table.add(interactLabel).fillX();
		table.add(interactButton).fillX();
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
		respawnButton.setText(Input.Keys.toString(ControlSettings.respawnKey()));
		optionsScreenButton.setText(Input.Keys.toString(ControlSettings.optionsScreenKey()));
		cancelMovementButton.setText(Input.Keys.toString(ControlSettings.cancelMovementKey()));
		questScreenButton.setText(Input.Keys.toString(ControlSettings.questScreenKey()));
		achievementsScreenButton.setText(Input.Keys.toString(ControlSettings.achivementsScreenKey()));
		interactButton.setText(Input.Keys.toString(ControlSettings.interactKey()));
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
		} else if (button.getName().equals("respawn")) {
			if (ControlSettings.setRespawnKey(Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		} else if (button.getName().equals("options screen")) {
			if (ControlSettings.setOptionsScreenKey(Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		} else if (button.getName().equals("cancel movement")) {
			if (ControlSettings.setCancelMovementKey(Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		} else if (button.getName().equals("quest screen")) {
			if (ControlSettings.setQuestScreenKey(Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		} else if (button.getName().equals("achievements screen")) {
			if (ControlSettings.setAchivementsScreenKey(Input.Keys.valueOf(key))) {
				button.setText(key);
				keyBindingsGroup.uncheckAll();
			}
		} else if (button.getName().equals("interact")) {
			if (ControlSettings.setInteractKey(Input.Keys.valueOf(key))) {
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
