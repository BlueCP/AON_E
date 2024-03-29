package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class VoltaicOverloadSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private VoltaicOverloadSkill() {
		super(null, false);
	}

	public VoltaicOverloadSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Voltaic Overload";
		desc = "Charged enemies take bonus damage. Basic attacks charge enemies.";
	}

	public void charge(Entity targetEntity) {
		if (isLearned()) {
			targetEntity.chargedEffect.addOne();
		}
	}

	public float damage(Entity targetEntity) {
		if (isLearned() && targetEntity.chargedEffect.isActive()) {
			return 1;
		} else {
			return 0;
		}
	}

}
