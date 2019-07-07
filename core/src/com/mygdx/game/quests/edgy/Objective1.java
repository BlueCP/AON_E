package com.mygdx.game.quests.edgy;

import com.mygdx.game.entities.Entity;
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
		desc = "Get into the air in any way.";
	}

	@Override
	public void update(PlayScreen playScreen) {
		if (playScreen.player.getAnimationType() == Entity.AnimationType.MIDAIR) {
			quest.ontoNextObjective();
		}
	}

}
