package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class LastingChillSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public LastingChillSkill() {
		super(null, false);
		name = "Lasting Chill";
		desc = "Landing abilities on enemies chills them. Chilled enemies are slowed for each chill stack.";
	}

	public LastingChillSkill(Entity entity, boolean learned) {
		super(entity, learned);
	}

	public void testfor(Entity targetEntity) {
		if (isLearned()) {
			entity.chill(targetEntity, 1, 3);
		}
	}

}
