package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.mobtypes.*;
import com.mygdx.game.utils.NewGameData;

public class ChooseRaceAndClassScreen implements Screen, InputProcessor {

	private AON_E game;
	
	private NewGameData newGame;
	//private String chosenRace = "";
	//private String chosenClass = "";
	
	private Texture chooseRAndCBackground;
	
	private Stage stage;
	private Table table;
	private Label chooseRace;
	
	private ButtonGroup<TextButton> raceGroup;
	private TextButton chooseHuman;
	private TextButton chooseElf;
	private TextButton chooseDwarf;
	
	private Label chooseClass;
	
	private ButtonGroup<TextButton> classGroup;
	private TextButton chooseWarrior;
	private TextButton chooseRanger;
	private TextButton chooseRogue;
	private TextButton chooseMage;
	
	private TextButton okay;
	private TextButton goBack;
	
	public ChooseRaceAndClassScreen(AON_E game) {
		this.game = game;
		
		newGame = new NewGameData();
		
		chooseRAndCBackground = game.manager.get("textures/chooseRAndCBackground.jpg", Texture.class);
		
		raceGroup = new ButtonGroup<>();
		classGroup = new ButtonGroup<>();
		
		chooseRace = new Label("Choose your race.", new LabelStyle(AON_E.DEFAULT_FONT, Color.WHITE));
		
		chooseHuman = new TextButton("Human", AON_E.SKIN, "toggle");
		raceGroup.add(chooseHuman);
		
		chooseElf = new TextButton("Elf", AON_E.SKIN, "toggle");
		raceGroup.add(chooseElf);
		
		chooseDwarf = new TextButton("Dwarf", AON_E.SKIN, "toggle");
		raceGroup.add(chooseDwarf);
		
		chooseClass = new Label("Choose your class.", new LabelStyle(AON_E.DEFAULT_FONT, Color.WHITE));
		
		chooseWarrior = new TextButton("Warrior", AON_E.SKIN, "toggle");
		classGroup.add(chooseWarrior);
		
		chooseRanger = new TextButton("Ranger", AON_E.SKIN, "toggle");
		classGroup.add(chooseRanger);
		
		chooseRogue = new TextButton("Rogue", AON_E.SKIN, "toggle");
		classGroup.add(chooseRogue);
		
		chooseMage = new TextButton("Mage", AON_E.SKIN, "toggle");
		classGroup.add(chooseMage);
		
		okay = new TextButton("Okay", AON_E.SKIN);
		/*okay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				switch (String.valueOf(raceGroup.getChecked().getText())) {
					case "Human":
						newGame.player.setMobRace(new Human());
						break;
					case "Elf":
						newGame.player.setMobRace(new Elf());
						break;
					case "Dwarf":
						newGame.player.setMobRace(new Dwarf());
						break;
				}
				switch (String.valueOf(classGroup.getChecked().getText())) {
					case "Warrior":
						newGame.player.setMobClass(new Warrior());
						break;
					case "Ranger":
						newGame.player.setMobClass(new Ranger());
						break;
					case "Rogue":
						newGame.player.setMobClass(new Rogue());
						break;
					case "Mage":
						newGame.player.setMobClass(new Mage());
						break;
				}
//				game.setScreen(new EnterNameScreen(game, newGame));
			}
		});*/
		
		goBack = new TextButton("Go back", AON_E.SKIN);
		goBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		stage = new Stage(game.viewport);
		table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center | Align.top);
		table.padTop(300);
		table.add(chooseRace).padBottom(50).colspan(4).padLeft(200).padRight(200);
		table.row().padBottom(150);
		table.add(chooseHuman);
		table.add(chooseElf).colspan(2);
		table.add(chooseDwarf);
		table.row();
		table.add(chooseClass).padBottom(50).colspan(4);
		table.row().padBottom(150);
		table.add(chooseWarrior);
		table.add(chooseRanger);
		table.add(chooseRogue);
		table.add(chooseMage);
		table.row().padBottom(50);
		table.add(okay).colspan(4);
		table.row();
		table.add(goBack).colspan(4);
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
	
	private void update() {
		game.pointer.setCenterX(Gdx.input.getX());
		game.pointer.setCenterY(Gdx.graphics.getHeight() - Gdx.input.getY());
	}

	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.batch.draw(chooseRAndCBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.end();
		
		stage.act();
		stage.draw();
		
		game.batch.begin();
		game.pointer.draw(game.batch);
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		//  Auto-generated method stub
		
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
