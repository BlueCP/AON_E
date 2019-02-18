package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class GlacialWallSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public GlacialWallSkill() {
		super(null);
	}

	public GlacialWallSkill(Entity entity) {
		super(entity);
		name = "Glacial Wall";
		desc = "Summon a great wall of ice that stops foes from passing.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE, entity.getTargetLocation());
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addGlacialWall(entity, playScreen.physicsManager.getDynamicsWorld(), targetPos, 10);
		putOnCooldown(3);
	}

}
