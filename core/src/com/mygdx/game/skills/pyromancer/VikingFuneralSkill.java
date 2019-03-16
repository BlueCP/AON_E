package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class VikingFuneralSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public VikingFuneralSkill() {
		super(null, false);
	}

	public VikingFuneralSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Viking Funeral";
		desc = "Landing abilities on enemies sets them on fire.";
	}

	public void testfor(Entity targetEntity) {
		if (isLearned()) {
			entity.burn(targetEntity, 1, 2);
		}
	}

}
