package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.achievements.Achievement;
import com.mygdx.game.rendering.RenderingUtils;

public class AchievementsScreen implements Screen, InputProcessor {

	AON_E game;
	private PlayScreen playScreen;
	
	Stage stage;
	Table table;

	public AchievementsScreen(AON_E game, PlayScreen playScreen) {
		this.game = game;
		this.playScreen = playScreen;
		
		stage = new Stage(game.viewport);
		Table achievementsTable = new Table();
		achievementsTable.align(Align.center | Align.top);
		
		ButtonGroup<TextButton> achievButtonGroup = new ButtonGroup<>();
		for (Achievement achievement: playScreen.achievements.getAchievements()) {
			TextButton button = new TextButton(achievement.getName(), AON_E.SKIN, "toggle");
			button.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					for (Actor actor: stage.getActors()) {
						if ("infoTable".equals(actor.getName())) {
							actor.remove();
							break;
						}
					}
					Table infoTable = new Table();
					infoTable.setName("infoTable");
					infoTable.align(Align.center | Align.right);
					infoTable.setWidth(stage.getWidth());
					infoTable.setHeight(stage.getHeight());
					
					infoTable.add(new Label("Name:", AON_E.SKIN)).row();
					infoTable.add(new Label(achievement.getName(), AON_E.SKIN)).width(500).row();
					
					infoTable.add(new Label("Description:", AON_E.SKIN)).row();
					Label label = new Label(achievement.getDesc(), AON_E.SKIN); label.setWrap(true);
					infoTable.add(label).width(500).row();
					
					infoTable.add(new Label(achievement.isCompleted() ? "Completed!" : "Not completed.",
											achievement.isCompleted() ? new LabelStyle(AON_E.DEFAULT_FONT, Color.GREEN) : new LabelStyle(AON_E.DEFAULT_FONT, Color.YELLOW)));
					
					stage.addActor(infoTable);
				}
				
			});
			achievButtonGroup.add(button);
			achievementsTable.add(button).padBottom(10).row();
		}

		ScrollPane achievementsPane = new ScrollPane(achievementsTable, AON_E.SKIN);
		
		table = new Table();
		table.align(Align.center | Align.top);
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.add(achievementsPane).padTop(50);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	@Override
	public void show() {

	}
	
	private void update() {
		game.pointer.setCenterX(Gdx.input.getX());
		game.pointer.setCenterY(Gdx.graphics.getHeight() - Gdx.input.getY());
	}

	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
		
		game.batch.begin();
		game.pointer.draw(game.batch);
		RenderingUtils.renderBlackBars(game.batch);
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {

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
			case Keys.ESCAPE:
				game.setScreen(playScreen);
				break;
		}
		return false;
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
		game.pointer.setRegion(game.pointerDown);
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
