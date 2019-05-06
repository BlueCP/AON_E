package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class DivineProtectionSkill extends PassiveSkill {

	public static final float defenseInc = 3f; // The amount defense is to be multiplied by.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DivineProtectionSkill() {
		super(null, false);
	}

	public DivineProtectionSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Divine Protection";
		desc = "You have a permanent increase in defense.";
	}

	public float defenseBoost() {
		if (isLearned()) {
			return defenseInc;
		} else {
			return 1;
		}
	}

}
