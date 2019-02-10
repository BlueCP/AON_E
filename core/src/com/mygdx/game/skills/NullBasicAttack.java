package com.mygdx.game.skills;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;

public class NullBasicAttack extends BasicAttack {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public NullBasicAttack() {
		super(null);
	}

	NullBasicAttack(Entity entity) {
		super(entity);
		name = "[No basic attack]";
		desc = "";
	}

	@Override
	public void start(PlayScreen playScreen) {

	}

	@Override
	public void finish(PlayScreen playScreen) {

	}

}
