package com.mygdx.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.mygdx.game.dialog.DialogObject;
import com.mygdx.game.dialog.DialogPart;
import com.mygdx.game.dialog.NullDialogObject;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;
import com.mygdx.game.quests.Objective;
import com.mygdx.game.quests.Quest;
import com.mygdx.game.quests.Quests;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.settings.GameplaySettings;

public class HudStage extends OwnStage {

	private PlayScreen playScreen;

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

	private Table respawnTable;

	private Texture blackOverlay;
	private boolean fadeToBlackActive = false; // Whether or not the screen is currently performing a fade to black.
	private float fadeToBlackTimePassed; // That time that has passed since beginning the fade to black.
	private float fadingTime; // The time that the screen should fade to black for.
	private float blackTime; // The time that the screen should remain black for.

	private boolean displayingDialog = false; // Whether or not dialog is currently being displayed.
	private Label dialogCharacterLabel; // The label containing the name of the character speaking.
	private Label dialogSpeechLabel; // The label containing the speech of the character speaking.
	private DialogObject dialogObject; // The current dialog being displayed.
	private int dialogPartCounter = 0; // The index of the current dialog part being displayed.
	private float dialogTimePassed = 0; // The time that has passed since a new dialog part has started.
	private float dialogConstant = 25; // The number of characters per second at default text speed.

	public HudStage(PlayScreen playScreen) {
		name = "hud";

		this.playScreen = playScreen;
		
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

		Label respawnMessageLabel = new Label("Press r to respawn.", AON_E.SKIN);

		respawnTable = new Table();
		respawnTable.setName("respawn table");
		respawnTable.setWidth(stage.getWidth());
		respawnTable.setHeight(stage.getHeight());
		respawnTable.align(Align.center);
		respawnTable.add(respawnMessageLabel).padTop(500);
		respawnTable.setVisible(playScreen.player.getLifeState() == Entity.LifeState.DEAD);
		stage.addActor(respawnTable);

		Pixmap pixmap = new Pixmap((int)AON_E.WORLD_WIDTH, (int)AON_E.WORLD_HEIGHT, Pixmap.Format.RGBA8888);
		pixmap.setColor(0, 0, 0, 1);
		pixmap.fill();
		blackOverlay = new Texture(pixmap);

		dialogObject = new NullDialogObject();
		dialogCharacterLabel = new Label("", AON_E.SKIN);
		dialogSpeechLabel = new Label("", AON_E.SKIN);
		dialogSpeechLabel.setWrap(true);
		Table dialogCharacterTable = new Table();
		dialogCharacterTable.setName("dialog character table");
		dialogCharacterTable.setWidth(stage.getWidth());
		dialogCharacterTable.setHeight(stage.getHeight());
		dialogCharacterTable.align(Align.left);
		dialogCharacterTable.add(dialogCharacterLabel).padTop(500).padLeft(250);
		stage.addActor(dialogCharacterTable);
		Table dialogSpeechTable = new Table();
		dialogSpeechTable.setName("dialog speech table");
		dialogSpeechTable.setWidth(stage.getWidth());
		dialogSpeechTable.setHeight(stage.getHeight());
		dialogSpeechTable.align(Align.left);
		dialogSpeechTable.add(dialogSpeechLabel).padTop(700).padLeft(250).width(1500);
		stage.addActor(dialogSpeechTable);
	}
	
	public void render(PlayScreen playScreen) {
		update(playScreen);
		stage.act();
		stage.draw();

		playScreen.game.batch.setProjectionMatrix(playScreen.game.camera.combined);
		playScreen.game.batch.begin();
		displayCriticalStats(playScreen.game.batch, playScreen.player);
		playScreen.game.batch.end();

		displayFadeToBlack();
	}

	@Override
	public void update(PlayScreen playScreen) {
		timeDisplay.setText("Day " + playScreen.time.getDay() + "   " + String.format("%02d", playScreen.time.getHour()) + ":" + String.format("%02d", playScreen.time.getMinute()));
		fps.setText("FPS = " + Gdx.graphics.getFramesPerSecond());
		playScreen.addMessagesToHud();
		playScreen.addSubMessagesToHud();

		displayQuestInfo(playScreen.quests);

		displayDialog(playScreen.player);

		displayAchievementGets(playScreen.achievements);

		respawnTable.setVisible(playScreen.player.getLifeState() == Entity.LifeState.DEAD);
	}

	private void displayFadeToBlack() {
		if (fadeToBlackActive) {
			fadeToBlackTimePassed += Gdx.graphics.getDeltaTime();
		} else {
			return;
		}

		SpriteBatch spriteBatch = playScreen.game.batch;

		if (fadeToBlackTimePassed <= fadingTime) {
			spriteBatch.begin();
			spriteBatch.setColor(1, 1, 1, fadeToBlackTimePassed / fadingTime);
			spriteBatch.draw(blackOverlay, 0, 0);
			spriteBatch.end();
		} else if (fadeToBlackTimePassed <= fadingTime + blackTime) {
			spriteBatch.begin();
			spriteBatch.draw(blackOverlay, 0, 0);
			spriteBatch.end();
		} else if (fadeToBlackTimePassed <= fadingTime*2 + blackTime) {
			spriteBatch.begin();
			spriteBatch.setColor(1, 1, 1, 1 - (fadeToBlackTimePassed - (fadingTime + blackTime)) / fadingTime);
			spriteBatch.draw(blackOverlay, 0, 0);
			spriteBatch.end();
		} else { // If the fade to black has ended.
			fadeToBlackActive = false;
		}

		spriteBatch.setColor(1, 1, 1, 1);
	}

	public void fadeToBlack(float fadingTime, float blackTime) {
		this.fadingTime = fadingTime;
		this.blackTime = blackTime;
		fadeToBlackActive = true;
		fadeToBlackTimePassed = 0;
	}

	/**
	 * Progresses to the next part of the dialog.
	 */
	public void progressDialog() {
		if (dialogTimePassed * dialogConstant * GameplaySettings.textSpeed() < dialogObject.getPart(dialogPartCounter).getSpeech().length()) {
			dialogTimePassed = 1000; // To make sure the dialog skips to the end of the current part.
			return;
		}
		dialogPartCounter ++;
		dialogTimePassed = 0;
		if (dialogPartCounter >= dialogObject.getDialogParts().size) {
			dialogObject.onCompletion(playScreen);
			dialogObject = new NullDialogObject();
			dialogCharacterLabel.setText("");
			dialogSpeechLabel.setText("");
			displayingDialog = false;
			dialogPartCounter = 0;
		}
	}

	public void startDialog(DialogObject dialogObject) {
		this.dialogObject = dialogObject;
		displayingDialog = true;
	}

	private void displayDialog(Player player) {
		if (dialogObject.getName().equals("Null")) {
			return;
		}
		player.stunnedEffect.addThisTick(); // To keep the player from moving or doing anything.

		DialogPart currentPart = dialogObject.getPart(dialogPartCounter);
		dialogCharacterLabel.setText(currentPart.getCharacter().getName());
		String speechToDisplay;
		if (dialogTimePassed * dialogConstant * GameplaySettings.textSpeed() < currentPart.getSpeech().length()) {
			speechToDisplay = currentPart.getSpeech().substring(0, (int)(dialogTimePassed * dialogConstant * GameplaySettings.textSpeed()));
		} else {
			speechToDisplay = currentPart.getSpeech();
		}
		dialogSpeechLabel.setText(speechToDisplay);

		dialogTimePassed += Gdx.graphics.getDeltaTime();
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

	public boolean isDisplayingDialog() {
		return displayingDialog;
	}

}
