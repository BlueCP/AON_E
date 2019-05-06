package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.RepulsionField;
import com.mygdx.game.projectiles.necromancer.SpiritSnareArea;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class SpiritSnareSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SpiritSnareSkill() {
		super(null);
	}

	public SpiritSnareSkill(Entity entity) {
		super(entity);
		name = "Spirit Snare";
		desc = "Root nearby enemies for a duration based on the number of souls in your possession.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new SpiritSnareArea(entity, entity.pos), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
