package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.quests.Quest;

public class QuestScreen extends MyScreen {

	private PlayScreen playScreen;

	private Stage stage;

	QuestScreen(AON_E game, PlayScreen playScreen) {
		super(game);

		this.playScreen = playScreen;

		stage = new Stage(game.viewport);

		ButtonGroup<TextButton> questsButtonGroup = new ButtonGroup<>();
		questsButtonGroup.setMinCheckCount(0);

		Table questsTable = new Table();
		questsTable.setWidth(stage.getWidth());
		questsTable.setHeight(stage.getHeight());
		questsTable.align(Align.center);

		for (Quest quest: playScreen.quests.quests) {
			if (!quest.isVisible()) {
				continue;
			}
			TextButton button = new TextButton(quest.getName(), AON_E.SKIN, "toggle");
			if (quest.isCompleted()) {
				button.getLabel().setColor(Color.GREEN);
//				button.setColor(Color.GREEN);
				button.setChecked(false);
			} else if (quest.isInProgress()) {
				button.setChecked(true);
			} else {
//				button.setColor(Color.WHITE);
				button.getLabel().setColor(Color.WHITE);
				button.setChecked(false);
			}
			Label conditionLabel = new Label("", AON_E.SKIN);

			button.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (button.isChecked() && !quest.isCompleted()) {
						playScreen.quests.chooseQuest(quest);

						conditionLabel.setText("In progress.");
						conditionLabel.setColor(Color.YELLOW);
					} else if (!button.isChecked() && !quest.isCompleted()) {
						playScreen.quests.setNoQuest();
						questsButtonGroup.uncheckAll();

						conditionLabel.setText("");
					} else {
						button.setChecked(false); // If the quest has been completed, stop the button from being checked (no reason to let it be checked).
					}
				}

				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					if (pointer != -1) {
						return;
					}
					Table infoTable = new Table();
					infoTable.setName("info table");
					infoTable.setWidth(stage.getWidth());
					infoTable.setHeight(stage.getHeight());
					infoTable.align(Align.right);

					Label nameLabel = new Label(quest.getName(), AON_E.SKIN);
					nameLabel.setWrap(true);
					Label descLabel = new Label(quest.getDesc(), AON_E.SKIN);
					descLabel.setWrap(true);
					infoTable.add(nameLabel).width(400).padBottom(30).row();
					infoTable.add(descLabel).width(400).padBottom(30).row();
//					Label conditionLabel;
					if (quest.isCompleted()) {
//						infoTable.add(new Label("Completed!", new Label.LabelStyle(AON_E.DEFAULT_FONT, Color.GREEN)));
						conditionLabel.setText("Completed!");
						conditionLabel.setColor(Color.GREEN);
					} else if (quest.isInProgress()) {
//						infoTable.add(new Label("In progress.", new Label.LabelStyle(AON_E.DEFAULT_FONT, Color.YELLOW)));
						conditionLabel.setText("In progress.");
						conditionLabel.setColor(Color.YELLOW);
					}
					infoTable.add(conditionLabel).width(400);

					stage.addActor(infoTable);
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					if (pointer != -1) {
						return;
					}
					for (Actor actor: stage.getActors()) {
						if ("info table".equals(actor.getName())) {
							actor.remove();
							return;
						}
					}
				}

			});
			questsButtonGroup.add(button);
			questsTable.add(button).padBottom(10).row();
		}

		ScrollPane questsPane = new ScrollPane(questsTable, AON_E.SKIN);

		Table table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center);

		table.add(questsPane);

		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}

	@Override
	void update() {
		universalUpdate();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.ESCAPE:
				game.setScreen(playScreen);
				return true;
			default:
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
