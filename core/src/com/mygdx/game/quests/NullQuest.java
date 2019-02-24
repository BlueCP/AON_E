package com.mygdx.game.quests;

import com.mygdx.game.screens.PlayScreen;

public class NullQuest extends Quest {

	public NullQuest() {
		name = "No quest";
		desc = "No quest chosen.";
		questType = QuestType.NO_QUEST;
		visible = true;
	}

	@Override
	void updateObjectives(PlayScreen playScreen) {
		// There are no objectives to update, so just don't do anything.
	}

	@Override
	public void updateVisibility(PlayScreen playScreen) {

	}

}
