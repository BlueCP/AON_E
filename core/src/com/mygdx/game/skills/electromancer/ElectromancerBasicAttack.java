package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.Electroball;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.BasicAttack;

public class ElectromancerBasicAttack extends BasicAttack {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private ElectromancerBasicAttack() {
		super(null);
	}

	public ElectromancerBasicAttack(Entity entity) {
		super(entity);
		name = "Electroball";
		desc = "Bound to shock everyone you meet.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			playScreen.projectileManager.addProjectileNow(new Electroball(entity, entity.pos, playScreen.entities.getEntity(targetEntity, playScreen.player).pos, 5), playScreen.physicsManager.getDynamicsWorld());
//			putOnCooldown(0);
			nextBasicAttack(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
		} else {
			failResolve();
		}
	}

}
