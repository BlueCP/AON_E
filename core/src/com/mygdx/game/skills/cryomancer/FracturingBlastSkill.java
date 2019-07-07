package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.cryomancer.ShatterExplosion;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.PassiveSkill;

public class FracturingBlastSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private FracturingBlastSkill() {
		super(null, false);
	}

	public FracturingBlastSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Fracturing Blast";
		desc = "Landing basic attacks on frozen enemies will cause them to Shatter.";
	}

	public void testfor(Entity targetEntity, PlayScreen playScreen) {
		if (isLearned() && targetEntity.frozenEffect.isActive()) {
			playScreen.projectileManager.addProjectileNow(new ShatterExplosion(entity, targetEntity.pos, targetEntity.chilledEffect.numStacks()), playScreen.physicsManager.getDynamicsWorld());
			targetEntity.frozenEffect.remove();
			targetEntity.chilledEffect.remove();
		}
	}

}
