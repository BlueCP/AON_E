package com.mygdx.game.quests.edgy;

import com.mygdx.game.quests.Objective;
import com.mygdx.game.quests.Quest;
import com.mygdx.game.screens.PlayScreen;

public class Objective2 extends Objective {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public Objective2() { }

	Objective2(Quest quest) {
		super(quest);
		desc = "Fall off the edge of the world.";
	}

	@Override
	public void update(PlayScreen playScreen) {
		if (playScreen.player.pos.y < 0) {
			quest.finishQuest();
		}
	}

}
