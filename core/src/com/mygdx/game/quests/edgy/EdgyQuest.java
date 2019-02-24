package com.mygdx.game.quests.edgy;

import com.mygdx.game.quests.Quest;
import com.mygdx.game.screens.PlayScreen;

public class EdgyQuest extends Quest {

	public EdgyQuest() {
		name = "Edgy";
		desc = "Fall off the edge of the world.";
		questType = QuestType.MAIN_QUEST;
		visible = true;

		objectives.add(new Objective1(this));
		objectives.add(new Objective2(this));
	}

	@Override
	public void updateVisibility(PlayScreen playScreen) {

	}

}
