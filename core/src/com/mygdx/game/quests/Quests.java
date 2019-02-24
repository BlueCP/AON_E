package com.mygdx.game.quests;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.quests.edgy.EdgyQuest;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;

public class Quests {

	public Array<Quest> quests;
	public Quest currentQuest;

	public Quests() {
		quests = new Array<>();

		quests.add(new EdgyQuest());

		currentQuest = new NullQuest();
		currentQuest.inProgress = true;

		chooseQuest("Edgy");
	}

	public void update(PlayScreen playScreen) {
		if (currentQuest.isCompleted()) {
			currentQuest.setDisplayed(true);
			setNoQuest();
		}

		currentQuest.updateObjectives(playScreen);

		for (Quest quest: quests) {
			quest.updateVisibility(playScreen);
		}
	}

	/**
	 * Returns the quest with the given name.
	 * @param name the name of the quest to be returned.
	 */
	public Quest getQuest(String name) {
		for (Quest quest: quests) {
			if (quest.name.equals(name)) {
				return quest;
			}
		}
		return new NullQuest();
	}

	/**
	 * Sets the current chosen quest to the quest with the given name.
	 * @param name the name of the quest which will be chosen.
	 */
	public void chooseQuest(String name) {
		for (Quest quest: quests) {
			if (quest.name.equals(name)) {
				quest.startQuest();
				currentQuest = quest;
			} else {
				quest.inProgress = false;
			}
		}
	}

	/**
	 * Sets the current chosen quest to the quest with the given name.
	 * @param currentQuest the quest which will be chosen.
	 */
	public void chooseQuest(Quest currentQuest) {
		this.currentQuest.abandonQuest();
		this.currentQuest = currentQuest;
		this.currentQuest.startQuest();

		/*for (Quest quest: quests) {
			if (!quest.name.equals(currentQuest.name)) {
				quest.inProgress = false;
			}
		}*/
	}

	/**
	 * Sets the current quest to NullQuest, i.e. no quest is currently chosen.
	 */
	public void setNoQuest() {
		currentQuest.abandonQuest();

		currentQuest = new NullQuest();
		currentQuest.inProgress = true;

		/*for (Quest quest: quests) {
			if (!quest.name.equals(currentQuest.name)) {
				quest.inProgress = false;
			}
		}*/
	}

	public void save(String dir) {
		try {
			KryoManager.write(this, "saves/" + dir + "/quests.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Quests load(String dir) {
		return KryoManager.read("saves/" + dir + "/quests.txt", Quests.class);
	}

}
