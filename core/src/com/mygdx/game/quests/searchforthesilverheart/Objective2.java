package com.mygdx.game.quests.searchforthesilverheart;

import com.mygdx.game.quests.Objective;
import com.mygdx.game.quests.Quest;
import com.mygdx.game.screens.PlayScreen;

public class Objective2 extends Objective {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private Objective2() { }

	Objective2(Quest quest) {
		super(quest);
		desc = "Give the Silverheart to Deltis.";
	}

	@Override
	public void update(PlayScreen playScreen) {
		// The quest is finished externally.
	}

}
