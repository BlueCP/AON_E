package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.BasicAttack;

public class CryomancerBasicAttack extends BasicAttack {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public CryomancerBasicAttack() {
		super(null);
	}

	public CryomancerBasicAttack(Entity entity) {
		super(entity);
		name = "Frostbolt";
		desc = "Cold.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			playScreen.projectileManager.addFrostbolt(entity, playScreen.physicsManager.getDynamicsWorld(), entity.pos, playScreen.entities.getEntity(targetEntity, playScreen.player).pos, 5);
			putOnCooldown(0);
		} else {
			failResolve();
		}
	}

}
