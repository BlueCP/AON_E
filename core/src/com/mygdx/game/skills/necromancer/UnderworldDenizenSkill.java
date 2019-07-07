package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class UnderworldDenizenSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private UnderworldDenizenSkill() {
		super(null, false);
	}

	public UnderworldDenizenSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Underworld Denizen";
		desc = "Spells cast with health deal additional damage.";
	}

	public float damage() {
		if (isLearned() && entity.currentSkill.isCastWithLife()) {
			return 1;
		} else {
			return 0;
		}
	}

}
