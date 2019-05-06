package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class DivineRetaliationSkill extends PassiveSkill {

	private static final float maxDamageBoost = 5; // The maximum factor that damage can be increased by.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DivineRetaliationSkill() {
		super(null, false);
	}

	public DivineRetaliationSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Divine Retaliation";
		desc = "You deal more damage at lower life.";
	}

	public float damageBoost() {
		if (isLearned()) {
			return ((1 - maxDamageBoost) / entity.getMaxLife()) * entity.getLife() + maxDamageBoost;
		} else {
			return 1;
		}
	}

}
