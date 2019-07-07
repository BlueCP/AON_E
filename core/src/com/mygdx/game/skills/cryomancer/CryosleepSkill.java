package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.cryomancer.Cryosleep;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class CryosleepSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private CryosleepSkill() {
		super(null);
	}

	public CryosleepSkill(Entity entity) {
		super(entity);
		name = "Cryosleep";
		desc = "Freeze yourself in a protective block of ice, healing life and spirit for the duration";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new Cryosleep(entity, entity.pos, 3), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(5);
	}
}
