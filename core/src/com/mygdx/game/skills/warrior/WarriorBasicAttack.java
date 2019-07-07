package com.mygdx.game.skills.warrior;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.warrior.WarriorSlash;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.BasicAttack;

public class WarriorBasicAttack extends BasicAttack {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private WarriorBasicAttack() {
		super(null);
	}

	public WarriorBasicAttack(Entity entity) {
		super(entity);
		name = "Slash";
		desc = "The path to peace is paved in blood and shattered skulls.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			playScreen.projectileManager.addProjectileNow(new WarriorSlash(entity, entity.pos, playScreen.entities.getEntity(targetEntity, playScreen.player)), playScreen.physicsManager.getDynamicsWorld());
			nextBasicAttack(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
		} else {
			failResolve();
		}
	}

}
