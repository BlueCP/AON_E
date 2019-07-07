package com.mygdx.game.quests.searchforthesilverheart;

import com.mygdx.game.quests.Quest;
import com.mygdx.game.screens.PlayScreen;

public class SearchForTheSilverheartQuest extends Quest {

	public SearchForTheSilverheartQuest() {
		name = "Search for the Silverheart";
		desc = "A fellow Continuum Breaker, Deltis, is stranded here. Render aid and help him escape.";
		questType = QuestType.MAIN_QUEST;
		visible = true;

		objectives.add(new Objective1(this));
		objectives.add(new Objective2(this));
	}

	@Override
	public void onCompletion(PlayScreen playScreen) {
		playScreen.player.inventory.removeOtherItem("Silverheart");
		playScreen.player.inventory.addOtherItem("Continuum Fragment");
		playScreen.player.speedEffect.add(5, 5);
	}

	@Override
	public void updateVisibility(PlayScreen playScreen) {

	}

}
