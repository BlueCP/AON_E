package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class EncaseInIceSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private EncaseInIceSkill() {
		super(null, false);
	}

	public EncaseInIceSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Encase in Ice";
		desc = "At max chill stacks, enemies are frozen.";
	}

}
