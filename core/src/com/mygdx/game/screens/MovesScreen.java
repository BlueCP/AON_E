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
import com.mygdx.game.playerattributes.Move;
import com.mygdx.game.rendering.RenderingUtils;

public class MovesScreen implements Screen, InputProcessor {

	AON_E game;
	private PlayScreen playScreen;
	
	Stage stage;
	Table table;
	private Label infoLabel;
	private ScrollPane movesPane;
	private ButtonGroup<TextButton> movesGroup;
	
	public MovesScreen(AON_E game, PlayScreen playScreen) {
		this.game = game;
		this.playScreen = playScreen;
		
		stage = new Stage(game.viewport);
		
		infoLabel = new Label("Press a key on the number pad to equip the chosen spell to that slot.", new LabelStyle(AON_E.DEFAULT_FONT, Color.CYAN));
		
		Table spellsTable = new Table();
		spellsTable.align(Align.center | Align.top);
		
		movesGroup = new ButtonGroup<>();
		/*for (Move move: playScreen.moveCollection.getAllMoves()) {
			TextButton button = new TextButton(move.getName(), AON_E.SKIN, "toggle");
			button.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					for (Actor actor: stage.getActors()) {
						if ("infoTable".equals(actor.getName()))
							actor.remove();
					}
					Table infoTable = new Table();
					infoTable.setName("infoTable");
					infoTable.align(Align.center | Align.right);
					infoTable.setWidth(stage.getWidth());
					infoTable.setHeight(stage.getHeight());
					
					infoTable.add(new Label("Name:", AON_E.SKIN)).row();
					infoTable.add(new Label(move.getName(), AON_E.SKIN)).width(500 * AON_E.screenSizeFactor).row();
					
					infoTable.add(new Label("Description:", AON_E.SKIN)).row();
					Label label = new Label(move.getDesc(), AON_E.SKIN); label.setWrap(true);
					infoTable.add(label).width(500 * AON_E.screenSizeFactor).row();
					
					infoTable.add(new Label(move.isLearned() ? "Learned!" : "Not learned.",
											move.isLearned() ? new LabelStyle(AON_E.DEFAULT_FONT, Color.GREEN) : new LabelStyle(AON_E.DEFAULT_FONT, Color.YELLOW)));
					
					if (playScreen.player.moveBar.contains(move)) {
						Label position = new Label(String.format("Assigned to position %s.", playScreen.player.moveBar.positionOf(move)),
												   new LabelStyle(AON_E.DEFAULT_FONT, Color.CYAN));
						infoTable.row();
						infoTable.add(position);
					}
					
					stage.addActor(infoTable);
				}
				
			});
			movesGroup.add(button);
			spellsTable.add(button).padBottom(10 * AON_E.screenSizeFactor).row();
		}*/
		
		movesPane = new ScrollPane(spellsTable, AON_E.SKIN);
		
		table = new Table();
		table.align(Align.center | Align.top);
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.add(infoLabel).padTop(30 * AON_E.screenSizeFactor).row();
		table.add(movesPane).padTop(20 * AON_E.screenSizeFactor);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
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
		
		stage.act();
		stage.draw();
		
		game.batch.begin();
		game.pointer.draw(game.batch);
		RenderingUtils.renderBlackBars(game.batch);
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

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.ESCAPE:
				game.setScreen(playScreen);
				break;
		}
		/*if (keycode >= Keys.NUM_1 && keycode <= Keys.NUM_9) {
			playScreen.player.moveBar.setMove(playScreen.moveCollection.getMove(String.valueOf(movesGroup.getChecked().getText())),
											  keycode - 7);
			// keycode - 7 will give 1 for num 1, 2 for num 2, etc. and 9 for num 9.
		} else if (keycode == Keys.NUM_0) {
			// Separate conditional statement because 0 is at the end of the number row, unlike the number pad where 0 is below every other key.
			playScreen.player.moveBar.setMove(playScreen.moveCollection.getMove(String.valueOf(movesGroup.getChecked().getText())), 10);
		}*/
		return true;
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

}
