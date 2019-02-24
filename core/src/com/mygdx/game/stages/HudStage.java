package com.mygdx.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.AON_E;
import com.mygdx.game.achievements.Achievement;
import com.mygdx.game.achievements.AchievementCollection;
import com.mygdx.game.entities.Player;
import com.mygdx.game.quests.Objective;
import com.mygdx.game.quests.Quest;
import com.mygdx.game.quests.Quests;
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

	private Table questInfoTable;
	private Array<Label> objectiveLabels;
	private Table questCompleteTable;
	private float questCompleteTimeLeft;
	private Quest currentQuest;

	private Queue<Achievement> completedAchievements;
	private float achievementCompleteTimeLeft;
	private Table achievementCompleteTable;
	private Label achievementGetLabel;
	
	public HudStage(PlayScreen playScreen) {
		name = "hud";
		
		fullPlayerLifeBar = playScreen.game.manager.get("sprites/fullPlayerLifeBar.png");
		lifeBar = new TextureRegion(fullPlayerLifeBar);
		fullPlayerSpiritBar = playScreen.game.manager.get("sprites/fullSpiritBar.png");
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
		
		stage = new Stage(playScreen.game.viewport);

		Table topTable = new Table();
		topTable.setWidth(stage.getWidth());
		topTable.setHeight(stage.getHeight());
		topTable.align(Align.center | Align.top);
		
		topTable.padTop(120).row();
		
		//table.add(fps).spaceRight(200 * AON_E.screenSizeFactor).align(Align.center);
		//table.add(fastTime).spaceRight(200 * AON_E.screenSizeFactor).align(Align.center);
		//table.add(timeDisplay).align(Align.center);
		topTable.add(fps).center().uniform();
		topTable.add(fastTime).padLeft(50).padRight(50).center().uniform();
		topTable.add(timeDisplay).center().uniform();
		
		topTable.row().padTop(30);
		//table.add(isoMovement).colspan(2).padBottom(30).row();
		//table.add(fastTime).padRight(100 * AON_E.screenSizeFactor);
		
		topTable.add(FPS12).center();
		topTable.add(FPS18).center();
		topTable.add(FPS24).center();
		
		stage.addActor(topTable);

		currentQuest = playScreen.quests.currentQuest;
		resetQuestInfoTable(playScreen.quests);

		Label questCompleteLabel = new Label("Quest Complete!", AON_E.SKIN);
		questCompleteLabel.setFontScale(3);

		questCompleteTable = new Table();
		questCompleteTable.setName("quest complete table");
		questCompleteTable.setWidth(stage.getWidth());
		questCompleteTable.setHeight(stage.getHeight());
		questCompleteTable.align(Align.center);
		questCompleteTable.add(questCompleteLabel);

		completedAchievements = new Queue<>();
		achievementCompleteTable = new Table();
		achievementCompleteTable.setName("achievement get");
		achievementCompleteTable.setWidth(stage.getWidth());
		achievementCompleteTable.setHeight(stage.getHeight());
		achievementCompleteTable.align(Align.center);
		achievementGetLabel = new Label("", AON_E.SKIN); // Make text blank since it will be changed depending on the name of the achievement.
		achievementGetLabel.setFontScale(3);
		achievementCompleteTable.add(achievementGetLabel).padTop(400);
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

		displayQuestInfo(playScreen.quests);

		displayAchievementGets(playScreen.achievements);
	}

	private void displayQuestInfo(Quests quests) {
		if (quests.currentQuest.getName().equals(currentQuest.getName())) { // If the quest hasn't changed, just update the details.
			for (int i = 0; i < quests.currentQuest.objectives.size; i ++) {
				if (quests.currentQuest.objectives.get(i).isInProgress()) {
					objectiveLabels.get(i).setColor(Color.YELLOW);
				} else if (quests.currentQuest.objectives.get(i).isCompleted()) {
					objectiveLabels.get(i).setColor(Color.GREEN);
				} else {
					objectiveLabels.get(i).setColor(Color.WHITE);
				}
			}
		} else { // If the quest has changed, reset the table with the new quest info.
			resetQuestInfoTable(quests);
		}

		currentQuest = quests.currentQuest;

		updateQuestCompleteLabel(quests.currentQuest);
	}

	private void updateQuestCompleteLabel(Quest quest) {
		if (quest.isCompleted() && !quest.isDisplayed()) {
			stage.addActor(questCompleteTable);
			questCompleteTimeLeft = 2;
		}

		if (questCompleteTimeLeft > 0) {
			questCompleteTimeLeft -= Gdx.graphics.getDeltaTime();
		} else {
			for (Actor actor: stage.getActors()) {
				if ("quest complete table".equals(actor.getName())) {
					actor.remove();
				}
			}
		}
	}

	private void displayAchievementGets(AchievementCollection achievementCollection) {
		for (Achievement achievement : achievementCollection.getAchievements()) {
			if (achievement.isCompleted() && !achievement.isDisplayed()) {
				if (completedAchievements.size == 0) { // If this is the first achievement in line, reset the duration.
					achievementCompleteTimeLeft = 2;
				}
				completedAchievements.addLast(achievement);
			}
		}

		if (achievementCompleteTimeLeft == 2) { // If a new achievement is to be displayed on the screen.
			achievementCompleteTimeLeft -= Gdx.graphics.getDeltaTime();
			achievementGetLabel.setText("Achievement get: " + completedAchievements.first().getName());
			stage.addActor(achievementCompleteTable);
		} else if (achievementCompleteTimeLeft > 0) { // If an achievement is displayed on the screen, decrease its duration.
			achievementCompleteTimeLeft -= Gdx.graphics.getDeltaTime();
		} else if (completedAchievements.size > 0) { // If an achievement is due to be removed from the screen.
			for (Actor actor: stage.getActors()) {
				if ("achievement get".equals(actor.getName())) {
					actor.remove();
				}
			}
			completedAchievements.removeFirst();
			if (completedAchievements.size > 0) { // If there is another achievement in line to be displayed, reset the duration.
				achievementCompleteTimeLeft = 2;
			}
		}
	}

	private void resetQuestInfoTable(Quests quests) {
		if (questInfoTable == null) { // If this is the initialisation of the table.
			questInfoTable = new Table();
		} else { // If a new quest has been accepted and the table needs to be reset.
			questInfoTable.reset();
		}

		questInfoTable.setWidth(stage.getWidth());
		questInfoTable.setHeight(stage.getHeight());
		questInfoTable.align(Align.left);

		Label questNameLabel = new Label(quests.currentQuest.getName(), AON_E.SKIN);
		questNameLabel.setName(quests.currentQuest.getName());
		objectiveLabels = new Array<>();
		for (Objective objective: quests.currentQuest.objectives) {
			LabelStyle labelStyle;
			if (objective.isInProgress()) {
				labelStyle = new LabelStyle(AON_E.DEFAULT_FONT, Color.YELLOW);
			} else if (objective.isCompleted()) {
				labelStyle = new LabelStyle(AON_E.DEFAULT_FONT, Color.GREEN);
			} else { // If the quest is just sitting there.
				labelStyle = new LabelStyle(AON_E.DEFAULT_FONT, Color.WHITE);
			}
			objectiveLabels.add(new Label(objective.getDesc(), labelStyle));
		}

		questInfoTable.add(questNameLabel).padBottom(20).row();
		for (Label objectiveLabel: objectiveLabels) {
			questInfoTable.add(objectiveLabel).padBottom(10).row();
		}

		stage.addActor(questInfoTable);
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
