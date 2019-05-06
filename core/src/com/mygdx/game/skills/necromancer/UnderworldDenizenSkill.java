package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class UnderworldDenizenSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public UnderworldDenizenSkill() {
		super(null, false);
	}

	public UnderworldDenizenSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Underworld Denizen";
		desc = "Spells cast with health deal additional damage.";
	}

	public float damage(float damage) {
		if (isLearned() && entity.currentSkill.isCastWithLife()) {
			return damage;
		} else {
			return 0;
		}
	}

}
