package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class ChargeBuildupSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private ChargeBuildupSkill() {
		super(null, false);
	}

	public ChargeBuildupSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Charge Buildup";
		desc = "Hitting enemies with abilities builds up electric charge on them that discharges onto nearby enemies.";
	}

	public void testfor(Entity targetEntity) {
		if (isLearned()) {
			targetEntity.chargedEffect.addOne();
		}
	}

}
