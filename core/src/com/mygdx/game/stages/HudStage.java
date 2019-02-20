package com.mygdx.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.mygdx.game.entities.Player;
import com.mygdx.game.screens.PlayScreen;

public class HudStage extends OwnStage {

	private Texture fullPlayerLifeBar;
	private TextureRegion lifeBar;
	
	private Texture fullPlayerSpiritBar;
	private TextureRegion spiritBar;
	
	private Label timeDisplay;
	private Label fps;
//	public TextButton isoMovement;
	public TextButton fastTime;
	private TextButton FPS24;
	private TextButton FPS12;
	private TextButton FPS18;
	
	public HudStage(AON_E game, PlayScreen playScreen) {
		name = "hud";
		
		fullPlayerLifeBar = game.manager.get("sprites/fullPlayerLifeBar.png");
		lifeBar = new TextureRegion(fullPlayerLifeBar);
		fullPlayerSpiritBar = game.manager.get("sprites/fullSpiritBar.png");
		spiritBar = new TextureRegion(fullPlayerSpiritBar);
		
		/*
		isoMovement = new TextButton("Isometric movement", AON_E.SKIN, "toggle");
		isoMovement.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isoMovement.isChecked()) {
					playScreen.isoMovement = true;
				} else {
					playScreen.isoMovement = false;
				}
			}
		});
		*/
		
		fastTime = new TextButton("Fast time", AON_E.SKIN, "toggle");

		fps = new Label("FPS = " + Gdx.graphics.getFramesPerSecond(), new LabelStyle(AON_E.DEFAULT_FONT, Color.WHITE));

		timeDisplay = new Label("Day " + playScreen.time.getDay() + "   " + String.format("%02d", playScreen.time.getHour()) + ":" + String.format("%02d", playScreen.time.getMinute()), new LabelStyle(AON_E.DEFAULT_FONT, Color.WHITE));

		ButtonGroup<TextButton> group = new ButtonGroup<>();
		
		FPS24 = new TextButton("24 FPS", AON_E.SKIN, "toggle");
		FPS24.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (FPS24.isChecked()) {
					PlayScreen.foo = 1;
				}
			}
		});
		FPS24.setChecked(true);

		FPS12 = new TextButton("12 FPS", AON_E.SKIN, "toggle");
		FPS12.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (FPS12.isChecked()) {
					PlayScreen.foo = 2;
				}
			}
		});

		FPS18 = new TextButton("18 FPS", AON_E.SKIN, "toggle");
		FPS18.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (FPS18.isChecked()) {
					PlayScreen.foo = 3;
				}
			}
		});

		group.add(FPS12, FPS18, FPS24);
		
		stage = new Stage(game.viewport);
		Table table = new Table();
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.align(Align.center | Align.top);
		
		table.padTop(120).row();
		
		//table.add(fps).spaceRight(200 * AON_E.screenSizeFactor).align(Align.center);
		//table.add(fastTime).spaceRight(200 * AON_E.screenSizeFactor).align(Align.center);
		//table.add(timeDisplay).align(Align.center);
		table.add(fps).center().uniform();
		table.add(fastTime).padLeft(50).padRight(50).center().uniform();
		table.add(timeDisplay).center().uniform();
		
		table.row().padTop(30);
		//table.add(isoMovement).colspan(2).padBottom(30).row();
		//table.add(fastTime).padRight(100 * AON_E.screenSizeFactor);
		
		table.add(FPS12).center();
		table.add(FPS18).center();
		table.add(FPS24).center();
		
		stage.addActor(table);
	}
	
	public void render(PlayScreen playScreen) {
		update(playScreen);
		stage.act();
		stage.draw();

		playScreen.game.batch.setProjectionMatrix(playScreen.game.camera.combined);
		playScreen.game.batch.begin();
		displayCriticalStats(playScreen.game.batch, playScreen.player);
		playScreen.game.batch.end();
	}

	@Override
	public void update(PlayScreen playScreen) {
		timeDisplay.setText("Day " + playScreen.time.getDay() + "   " + String.format("%02d", playScreen.time.getHour()) + ":" + String.format("%02d", playScreen.time.getMinute()));
		fps.setText("FPS = " + Gdx.graphics.getFramesPerSecond());
		playScreen.addMessagesToHud();
		playScreen.addSubMessagesToHud();
	}
	
	private void displayCriticalStats(SpriteBatch spriteBatch, Player player) {
		{
			float percLife = player.getLife() / player.getMaxLife();
			int barLength = (int)(fullPlayerLifeBar.getTextureData().getWidth() * percLife);
			lifeBar.setRegionWidth(barLength);
			spriteBatch.draw(lifeBar,
							 AON_E.WORLD_WIDTH/2 - (50 + lifeBar.getRegionWidth()),
							 AON_E.WORLD_HEIGHT - (50 + lifeBar.getRegionHeight()),
							 lifeBar.getRegionWidth() + 50, lifeBar.getRegionHeight()/2f, // Origin x and y
							 lifeBar.getRegionWidth(), lifeBar.getRegionHeight(), // Width and height
							 1, 1, 0); // Scale and rotation
		}
		
		{
			float percSpirit = player.getSpirit() / player.getMaxSpirit();
			int barLength = (int)(fullPlayerSpiritBar.getTextureData().getWidth() * percSpirit);
			spiritBar.setRegionWidth(barLength);
			spriteBatch.draw(spiritBar,
					 		 AON_E.WORLD_WIDTH/2 + 50,
					 		 AON_E.WORLD_HEIGHT - (50 + spiritBar.getRegionHeight()),
					 		 -50, spiritBar.getRegionHeight()/2f,
					 		 spiritBar.getRegionWidth(), spiritBar.getRegionHeight(),
					 		 1, 1, 0);
		}
	}
	
}
