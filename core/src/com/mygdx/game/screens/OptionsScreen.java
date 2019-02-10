package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.rendering.RenderingUtils;

public class OptionsScreen extends MyScreen implements InputProcessor {

//	private AON_E game;
	private PlayScreen playScreen;
	
	Stage stage;
	Table table;
	private Label allControls;
	
	OptionsScreen(AON_E game, PlayScreen playScreen) {
		super(game);

//		this.game = game;
		this.playScreen = playScreen;
		
		stage = new Stage(game.viewport);
		
		TextButton resumeGame = new TextButton("Resume game", AON_E.SKIN);
		resumeGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(playScreen);
			}
		});
//		resumeGame.getLabel().setFontScale(AON_E.screenSizeFactor);
		
		TextButton saveGame = new TextButton("Save game", AON_E.SKIN);
		saveGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				save();
			}
		});
//		saveGame.getLabel().setFontScale(AON_E.screenSizeFactor);
		
		TextButton saveAndExit = new TextButton("Save and exit", AON_E.SKIN);
		saveAndExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveAndExit();
			}
		});
//		saveAndExit.getLabel().setFontScale(AON_E.screenSizeFactor);
		
		TextButton controls = new TextButton("Show controls", AON_E.SKIN, "toggle");
		controls.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				allControls.setVisible(controls.isChecked());
			}
		});
//		controls.getLabel().setFontScale(AON_E.screenSizeFactor);
		
		allControls = new Label("[ESCAPE]: Options menu.\n"
							  + "w: Move up.\n"
							  + "a: Move left.\n"
							  + "s: Move down.\n"
							  + "d: Move right.\n"
							  + "w+a: Move up-left.\n"
							  + "w+d: Move up-right.\n"
							  + "s+a: Move down-left.\n"
							  + "s+d: Move down-right.\n"
							  + "q: View all achievements.\n"
							  + "k: View all moves.\n"
							  + "l: View all spells.\n"
								 ,AON_E.SKIN);
		allControls.setWrap(true);
		allControls.setWidth(500);
		allControls.setHeight(700);
		allControls.setX((AON_E.WORLD_WIDTH / 1.3f), Align.center);
		allControls.setY(AON_E.WORLD_HEIGHT / 2f, Align.center);
		allControls.setVisible(false);
//		allControls.setFontScale(AON_E.screenSizeFactor);
		
		table = new Table();
		table.align(Align.center);
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.add(resumeGame).row();
		table.add(saveGame).padTop(30).row();
		table.add(saveAndExit).padTop(30).row();
		table.add(controls).padTop(30).row();
		stage.addActor(table);
		stage.addActor(allControls);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	private void save() {
		playScreen.save();
	}
	
	private void saveAndExit() {
		playScreen.saveAndExit();
		playScreen.travelMusic.stop();
		game.setScreen(new MainMenuScreen(game));
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.ESCAPE:
				game.setScreen(playScreen);
				return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		game.pointer.setRegion(game.pointerDown);
		game.click.play();
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
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		//  Auto-generated method stub
		
	}
	
	void update() {
		updateVirtualCoords();
		updatePointer();
	}

	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
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
		//  Auto-generated method stub
		
	}

	@Override
	public void resume() {
		//  Auto-generated method stub
		
	}

	@Override
	public void hide() {
		//  Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
