package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.utils.NewGameData;

public class EnterNameScreen extends MyScreen {

	private NewGameData newGame;
	
	private Texture enterNameBackground;
	
	private Stage stage;
	private TextField nameField;

	EnterNameScreen(AON_E game) {
		super(game);

		newGame = new NewGameData();
		
		enterNameBackground = game.manager.get("textures/enterNameBackground.jpg", Texture.class);

		Label enterName = new Label("Enter your character's name.", new LabelStyle(AON_E.DEFAULT_FONT, Color.WHITE));
		nameField = new TextField("", AON_E.SKIN);
		TextButton okay = new TextButton("Okay", AON_E.SKIN);
		okay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if ("".equals(nameField.getText()) ||
					nameField.getText().contains(" ")) {
					return;
				}
				newGame.player.setPlayerName(nameField.getText());
				game.musicManager.titleMusic.stop();
				game.setScreen(new CreatingCharacterScreen(game, newGame));
			}
		});

		TextButton goBack = new TextButton("Go back", AON_E.SKIN);
		goBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenuScreen(game));
			}
		});

		stage = new Stage(game.viewport);
		Table table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center | Align.top);
		table.padTop(AON_E.topBottomBorders + 400);
		table.add(enterName).padBottom(50);
		table.row();
		table.add(nameField).padBottom(150).fill();
		table.row();
		table.add(okay).padBottom(30);
		table.row();
		table.add(goBack);
		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	@Override
	public boolean keyDown(int keycode) {
		//  Auto-generated method stub
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
		game.soundManager.click.play();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//  Auto-generated method stub
		return false;
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
		universalUpdate();
	}

	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		game.batch.draw(enterNameBackground, 0, 0, AON_E.WORLD_WIDTH, AON_E.WORLD_HEIGHT);
		game.batch.end();
		
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
