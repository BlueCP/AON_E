package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;

public class OptionsScreen extends MyScreen {

	private PlayScreen playScreen;
	
	Stage stage;
	Table table;
	private Label allControls;
	
	OptionsScreen(AON_E game, PlayScreen playScreen) {
		super(game);

		this.playScreen = playScreen;
		
		stage = new Stage(game.viewport);
		
		TextButton resumeGame = new TextButton("Resume game", AON_E.SKIN);
		resumeGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(playScreen);
			}
		});

		TextButton saveGame = new TextButton("Save game", AON_E.SKIN);
		saveGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				save();
			}
		});

		TextButton saveAndExit = new TextButton("Save and exit", AON_E.SKIN);
		saveAndExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveAndExit();
			}
		});

		TextButton controls = new TextButton("Show controls", AON_E.SKIN, "toggle");
		controls.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				allControls.setVisible(controls.isChecked());
			}
		});

		TextButton settings = new TextButton("Settings", AON_E.SKIN);
		settings.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SettingsScreen(game, game.getScreen()));
			}
		});
		
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

		table = new Table();
		table.align(Align.center);
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.add(resumeGame).width(300).padBottom(30).row();
		table.add(saveGame).fillX().padBottom(30).row();
		table.add(saveAndExit).fillX().padBottom(30).row();
		table.add(controls).fillX().padBottom(30).row();
		table.add(settings).fillX().padBottom(30);
		stage.addActor(table);
		stage.addActor(allControls);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	private void save() {
		playScreen.save();
	}
	
	private void saveAndExit() {
		playScreen.saveAndExit();
		game.musicManager.travelMusic.stop();
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
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	void update() {
		universalUpdate();
	}

	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
