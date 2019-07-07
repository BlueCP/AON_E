package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.cryomancer.Blizzard;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class BlizzardSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private BlizzardSkill() {
		super(null);
	}

	public BlizzardSkill(Entity entity) {
		super(entity);
		name = "Blizzard";
		desc = "Summon a blizzard at target location which pushes enemies away and damages them.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new Blizzard(entity, targetPos, 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
