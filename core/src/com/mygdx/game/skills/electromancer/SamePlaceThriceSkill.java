package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class SamePlaceThriceSkill extends PassiveSkill {

	private int spellCounter = 0;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SamePlaceThriceSkill() {
		super(null, false);
	}

	public SamePlaceThriceSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Same Place Thrice";
		desc = "Every third spell stuns enemies hit.";
	}

	public void testfor(Entity targetEntity) {
		if (isLearned()) {
			spellCounter ++;
			if (spellCounter >= 3) {
				spellCounter = 0;
				entity.stun(targetEntity, 1);
			}
		}
	}

}
