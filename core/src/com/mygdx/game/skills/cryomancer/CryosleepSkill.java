package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.ActivatedSkill;

public class CryosleepSkill extends ActivatedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public CryosleepSkill() {
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
		playScreen.projectileManager.addCryosleep(entity, playScreen.physicsManager.getDynamicsWorld(), entity.pos, 3);
		putOnCooldown(5);
	}
}
