package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.PassiveSkill;

public class FracturingBlastSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public FracturingBlastSkill() {
		super(null, false);
	}

	public FracturingBlastSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Fracturing Blast";
		desc = "Landing basic attacks on frozen enemies will cause them to Shatter.";
	}

	public void testfor(Entity targetEntity, PlayScreen playScreen) {
		if (isLearned() && targetEntity.frozenEffect.isActive()) {
			playScreen.projectileManager.addShatterExplosion(entity, playScreen.physicsManager.getDynamicsWorld(), targetEntity.pos, targetEntity.chilledEffect.numStacks());
			targetEntity.frozenEffect.remove();
			targetEntity.chilledEffect.remove();
		}
	}

}
