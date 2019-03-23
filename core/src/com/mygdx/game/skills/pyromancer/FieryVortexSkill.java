package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.FieryVortex;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class FieryVortexSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public FieryVortexSkill() {
		super(null);
	}

	public FieryVortexSkill(Entity entity) {
		super(entity);
		name = "Fiery Vortex";
		desc = "Create an area of fire which burns and pulls enemies towards you.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new FieryVortex(entity, targetPos, 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
