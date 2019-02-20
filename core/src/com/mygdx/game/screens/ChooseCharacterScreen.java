package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.AON_E;

public class ChooseCharacterScreen extends MyScreen {

	private Texture chooseCharacterBackground;
	
	Stage stage;
	Table table;

	ChooseCharacterScreen(AON_E game) {
		super(game);

		chooseCharacterBackground = game.manager.get("textures/chooseCharacterBackground.jpg");
		
		Table characterTable = new Table();
		FileHandle[] saves = Gdx.files.local("saves").list();
		for (FileHandle save: saves) {
			TextButton button = new TextButton(save.name(), AON_E.SKIN);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.musicManager.titleMusic.stop();
					game.setScreen(new PlayScreen(game, button.getText().toString()));
				}
			});
			characterTable.add(button).padBottom(10).row();
		}

		ScrollPane charactersPane = new ScrollPane(characterTable, AON_E.SKIN);

		TextButton goBack = new TextButton("Go back", AON_E.SKIN);
		goBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenuScreen(game));
			}
		});

		stage = new Stage(game.viewport);
		characterTable.setWidth(stage.getWidth());
		characterTable.setHeight(stage.getHeight());
		table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.add(charactersPane);
		table.row().padTop(50);
		table.add(goBack);
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

		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		game.batch.draw(chooseCharacterBackground, 0, 0, AON_E.WORLD_WIDTH, AON_E.WORLD_HEIGHT);
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

}
