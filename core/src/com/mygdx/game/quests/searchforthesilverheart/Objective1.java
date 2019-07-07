package com.mygdx.game.quests.searchforthesilverheart;

import com.mygdx.game.quests.Objective;
import com.mygdx.game.quests.Quest;
import com.mygdx.game.screens.PlayScreen;

public class Objective1 extends Objective {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private Objective1() { }

	Objective1(Quest quest) {
		super(quest);
		desc = "Obtain the Silverheart.";
	}

	@Override
	public void update(PlayScreen playScreen) {
		if (playScreen.player.inventory.contains("Silverheart")) {
			quest.ontoNextObjective();
		}
	}

}
