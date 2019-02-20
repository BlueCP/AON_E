package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class BitingColdSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BitingColdSkill() {
		super(null, false);
		name = "Biting Cold";
		desc = "Chilled enemies also take damage over time.";
	}

	public BitingColdSkill(Entity entity, boolean learned) {
		super(entity, learned);
	}

}
