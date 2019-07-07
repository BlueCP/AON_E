package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.achievements.Achievement;

public class AchievementsScreen extends MyScreen {

//	AON_E game;
	private PlayScreen playScreen;
	
	Stage stage;
	Table table;

	AchievementsScreen(AON_E game, PlayScreen playScreen) {
		super(game);

//		this.game = game;
		this.playScreen = playScreen;
		
		stage = new Stage(game.viewport);

		Table achievementsTable = new Table();
		achievementsTable.setWidth(stage.getWidth());
		achievementsTable.setHeight(stage.getHeight());
		achievementsTable.align(Align.center | Align.top);
		
//		ButtonGroup<TextButton> achievButtonGroup = new ButtonGroup<>();
		for (Achievement achievement: playScreen.achievements.getAchievements()) {
			TextButton button = new TextButton(achievement.getName(), AON_E.SKIN);
			if (achievement.isCompleted()) {
				button.getLabel().setColor(Color.GREEN);
			}
			button.addListener(new ClickListener() {

				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					if (pointer != -1) {
						return;
					}
					Table infoTable = new Table();
					infoTable.setName("infoTable");
					infoTable.setWidth(stage.getWidth());
					infoTable.setHeight(stage.getHeight());
					infoTable.align(Align.right);

//					infoTable.add(new Label("Name:", AON_E.SKIN)).row();
					infoTable.add(new Label(achievement.getName(), AON_E.SKIN)).padBottom(30).row();

//					infoTable.add(new Label("Description:", AON_E.SKIN)).row();
					Label label = new Label(achievement.getDesc(), AON_E.SKIN);
//					label.setWrap(true);
					infoTable.add(label).padBottom(30).row();

					if (achievement.isCompleted()) {
						infoTable.add(new Label("Completed!", new LabelStyle(AON_E.DEFAULT_FONT, Color.GREEN)));
					}

//					infoTable.add(new Label(achievement.isCompleted() ? "Completed!" : "Not completed.",
//							achievement.isCompleted() ? new LabelStyle(AON_E.DEFAULT_FONT, Color.GREEN) : new LabelStyle(AON_E.DEFAULT_FONT, Color.YELLOW)));

					stage.addActor(infoTable);
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					if (pointer != -1) {
						return;
					}
					for (Actor actor: stage.getActors()) {
						if ("infoTable".equals(actor.getName())) {
							actor.remove();
							break;
						}
					}
				}
			});
//			achievButtonGroup.add(button);
			achievementsTable.add(button).padBottom(10).row();
		}

		ScrollPane achievementsPane = new ScrollPane(achievementsTable, AON_E.SKIN);
		
		table = new Table();
		table.align(Align.center);
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.add(achievementsPane);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	@Override
	public void show() {

	}
	
	void update() {
		universalUpdate();
	}

	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
		
		game.batch.begin();
		game.pointer.draw(game.batch);
//		RenderingUtils.renderBlackBars(game.batch);
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
//		game.pointer.setRegion(game.pointerDown);
//		game.click.play();
		game.soundManager.click.play();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		game.pointer.setRegion(game.pointerUp);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		game.pointer.setRegion(game.pointerUp);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
