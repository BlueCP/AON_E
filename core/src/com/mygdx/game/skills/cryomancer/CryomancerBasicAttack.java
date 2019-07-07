package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.cryomancer.Frostbolt;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.BasicAttack;

public class CryomancerBasicAttack extends BasicAttack {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private CryomancerBasicAttack() {
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
			playScreen.projectileManager.addProjectileNow(new Frostbolt(entity, entity.pos, playScreen.entities.getEntity(targetEntity, playScreen.player).pos, 5), playScreen.physicsManager.getDynamicsWorld());
//			putOnCooldown(0);
			nextBasicAttack(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
		} else {
			failResolve();
		}
	}

}
