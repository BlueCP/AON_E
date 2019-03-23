package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.cryomancer.Hailstorm;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class HailstormSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public HailstormSkill() {
		super(null);
	}

	public HailstormSkill(Entity entity) {
		super(entity);
		name = "Hailstorm";
		desc = "Create a hailstorm in the target area, damaging enemies for the duration.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new Hailstorm(entity, targetPos, 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
