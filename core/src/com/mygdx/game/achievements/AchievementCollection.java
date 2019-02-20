package com.mygdx.game.achievements;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;

public class AchievementCollection {

	private Array<Achievement> achievements = new Array<>();
	/*
	static String[] progOrder = {"Newbie","Adept","Seasoned","Advanced","Expert"};
	static String[] achOrder = {"_Hiker","_Slayer"}; // If the name has an underscore in front of it, that means it's part of the progression achievements (newbie, adept, etc.)
	*/
	
	public AchievementCollection() {

	}
	
	public void savePlayerAchievements(String name) {
		KryoManager.write(this, "saves/" + name + "/playerAchievements.txt");
	}
	
	public static AchievementCollection loadAll(String name) {
		AchievementCollection achievements = loadPlayerAchievements(name);
		achievements.createAllAchievements();
		return achievements;
	}

	private void createAllAchievements() {
		achievements.add(new Walker());
		// Add more achievements here in future.
	}
	
	private static AchievementCollection loadPlayerAchievements(String name) {
		return KryoManager.read("saves/" + name + "/playerAchievements.txt", AchievementCollection.class);
	}
	
	/*private void loadAllAchievements() {
		String fileString = Gdx.files.internal("data/allAchievements.txt").readString();
		Scanner fileAllAchievements = new Scanner(fileString);
		String dataBit;
		Achievement achievement = new Achievement();
		while (fileAllAchievements.hasNextLine()) {
			dataBit = fileAllAchievements.nextLine();
			if (";".equals(dataBit)) {
				this.getAchievements().add(achievement); // Add the completed weapon to items
				if (fileAllAchievements.hasNextLine()) {
					achievement = new Achievement();
					continue;
				} else {
					break;
				}
			}
			achievement.setName(dataBit);
			achievement.setDesc(fileAllAchievements.nextLine());
		}
		fileAllAchievements.close();
	}*/
	
	public void updateAchievements(PlayScreen playScreen) {
		for (Achievement achievement: achievements) {
			achievement.update(playScreen);
		}

		/*for (Achievement achievement: achievements) {
			switch (achievement.getName()) {
				case "Newbie Hiker":
					if (this.timesMoved >= 100) {
						achievement.setCompleted(true);
					}
					break;
				case "Adept Hiker":
					if (this.timesMoved >= 1000) {
						achievement.setCompleted(true);
					}
					break;
				case "Seasoned Hiker":
					if (this.timesMoved >= 5000) {
						achievement.setCompleted(true);
					}
					break;
				case "Advanced Hiker":
					if (this.timesMoved >= 20000) {
						achievement.setCompleted(true);
					}
					break;
				case "Expert Hiker":
					if (this.timesMoved >= 100000) {
						achievement.setCompleted(true);
					}
					break;
				case "Newbie Slayer":
					if (this.enemiesSlain >= 5) {
						achievement.setCompleted(true);
					}
					break;
				case "Adept Slayer":
					if (this.enemiesSlain >= 20) {
						achievement.setCompleted(true);
					}
					break;
				case "Seasoned Slayer":
					if (this.enemiesSlain >= 50) {
						achievement.setCompleted(true);
					}
					break;
				case "Advanced Slayer":
					if (this.enemiesSlain >= 200) {
						achievement.setCompleted(true);
					}
					break;
				case "Expert Slayer":
					if (this.enemiesSlain >= 500) {
						achievement.setCompleted(true);
					}
					break;
			}
			if (tick == 1 && achievement.isCompleted()) {
				achievement.setDisplayed(true);
			}
		}*/
	}

	public Array<Achievement> getAchievements() {
		return achievements;
	}
	
}
