package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.PowerGridConductor;
import com.mygdx.game.projectiles.electromancer.Stormcaller;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class PowerGridSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public PowerGridSkill() {
		super(null);
	}

	public PowerGridSkill(Entity entity) {
		super(entity);
		name = "Power Grid";
		desc = "Place down an orb of energy in target location. Orbs are connected by beams of energy which are harmful to enemies.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new PowerGridConductor(entity, targetPos, 10), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(2);
	}

}
