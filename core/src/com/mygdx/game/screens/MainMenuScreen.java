package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;

public class MainMenuScreen extends MyScreen {

//	private AON_E game;
	//private GlyphLayout gLay;
	
	private Texture titleScreen;
	
//	private Music titleMusic;
	
	private Stage stage;

	public MainMenuScreen(AON_E game) {
		super(game);
		//gLay = new GlyphLayout();

		titleScreen = game.manager.get("textures/titlescreen.jpg", Texture.class);
		
//		titleMusic = game.manager.get("music/titletheme.mp3", Music.class);
		game.musicManager.titleMusic.setLooping(true);
//		game.soundManager.titleMusic.play();

		Label title = new Label("AON_E", new LabelStyle(AON_E.DEFAULT_FONT, Color.MAGENTA));
		title.setFontScale(3);

		TextButton createButton = new TextButton("Create character", AON_E.SKIN);
		createButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new EnterNameScreen(game));
			}
		});
//		createButton.getLabel().setFontScale(AON_E.screenSizeFactor);

		TextButton resumeButton = new TextButton("Resume game", AON_E.SKIN);
		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new ChooseCharacterScreen(game));
			}
		});
//		resumeButton.getLabel().setFontScale(AON_E.screenSizeFactor);

		TextButton deleteButton = new TextButton("Delete character", AON_E.SKIN);
		deleteButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new DeleteCharacterScreen(game));
			}
		});
//		deleteButton.getLabel().setFontScale(AON_E.screenSizeFactor);

		TextButton settingsButton = new TextButton("Settings", AON_E.SKIN);
		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SettingsScreen(game, game.getScreen()));
			}
		});

		TextButton exitButton = new TextButton("Exit game", AON_E.SKIN);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
//		exitButton.getLabel().setFontScale(AON_E.screenSizeFactor);
		
		stage = new Stage(game.viewport);

		Table table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center | Align.top);
		table.padTop(AON_E.WORLD_HEIGHT/2 - 200);
		table.add(title).padBottom(200);
		table.row();
		table.add(createButton).padBottom(30).width(500);
		table.row();
		table.add(resumeButton).padBottom(30).fillX();
		table.row();
		table.add(deleteButton).padBottom(30).fillX();
		table.row();
		table.add(settingsButton).padBottom(30).fillX();
		table.row();
		table.add(exitButton).fillX();

		stage.addActor(table);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	@Override
	public boolean keyDown(int keycode) {
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
//		game.click.play(3f);
		game.soundManager.click.play();
//		Sound sound = game.manager.get("sound/UI/click.wav");
//		sound.play(1, 1, -0.8f);
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
	
	void update() {
		universalUpdate();
	}
	
	@Override
	public void render(float delta) {

		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(game.camera.combined);

		game.batch.begin();
		
//		game.batch.draw(titleScreen, AON_E.leftLimit, AON_E.lowerLimit, AON_E.effectiveScreenWidth, AON_E.effectiveScreenHeight);
		game.batch.draw(titleScreen, 0, 0, AON_E.WORLD_WIDTH, AON_E.WORLD_HEIGHT);
//		game.batch.draw(titleScreen, 0, 0);

		/*
		AON_E.DEFAULT_FONT.getData().setScale(3f);
		gLay.setText(AON_E.DEFAULT_FONT, "AON_E", Color.MAGENTA, 0, Align.center, false);
		AON_E.DEFAULT_FONT.draw(game.batch,
								gLay,
								Gdx.graphics.getWidth() / 2,
								Gdx.graphics.getHeight() / 2 + gLay.height / 2);
		gLay.reset();
		AON_E.DEFAULT_FONT.getData().setScale(1f);
		*/
		
		game.batch.end();
		
		stage.act();
		stage.draw();
		
		game.batch.begin();
//		game.pointer.setSize(AON_E.WORLD_WIDTH, AON_E.WORLD_HEIGHT);
		game.pointer.draw(game.batch);
//		RenderingUtils.renderBlackBars(game.batch);
		game.batch.end();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
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
