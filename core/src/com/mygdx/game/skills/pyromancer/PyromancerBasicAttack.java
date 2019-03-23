package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.Firebolt;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.BasicAttack;

public class PyromancerBasicAttack extends BasicAttack {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public PyromancerBasicAttack() {
		super(null);
	}

	public PyromancerBasicAttack(Entity entity) {
		super(entity);
		name = "Firebolt";
		desc = "A fast, simple way to make peace with someone.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			playScreen.projectileManager.addProjectileNow(new Firebolt(entity, entity.pos, playScreen.entities.getEntity(targetEntity, playScreen.player).pos, 5), playScreen.physicsManager.getDynamicsWorld());
//			putOnCooldown(0);
			nextBasicAttack(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
		} else {
			failResolve();
		}
	}

}
