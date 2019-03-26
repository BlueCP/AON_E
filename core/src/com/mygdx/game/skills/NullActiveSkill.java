package com.mygdx.game.skills;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;

public class NullActiveSkill extends ActiveSkill {

	public NullActiveSkill() {
		super(null);
		name = "[No active skill]";
		desc = "";
	}

	public NullActiveSkill(Entity entity) {
		super(entity);
		name = "[No active skill]";
		desc = "";
	}

	@Override
	public void start(PlayScreen playScreen) {

	}

	@Override
	public void finish(PlayScreen playScreen) {

	}
}
