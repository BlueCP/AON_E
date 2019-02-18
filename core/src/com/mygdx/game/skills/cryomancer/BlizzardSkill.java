package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class BlizzardSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BlizzardSkill() {
		super(null);
	}

	public BlizzardSkill(Entity entity) {
		super(entity);
		name = "Blizzard";
		desc = "Summon a blizzard at target location which pushes enemies away and damages them.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE, entity.getTargetLocation());
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addBlizzard(entity, playScreen.physicsManager.getDynamicsWorld(), targetPos, 5);
		putOnCooldown(3);
	}

}
