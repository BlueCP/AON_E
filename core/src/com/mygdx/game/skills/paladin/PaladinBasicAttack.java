package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.paladin.PaladinSwing;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.BasicAttack;

public class PaladinBasicAttack extends BasicAttack {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private PaladinBasicAttack() {
		super(null);
	}

	public PaladinBasicAttack(Entity entity) {
		super(entity);
		name = "Paladin swing";
		desc = "One normal swing, with extra righteousness, please.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			playScreen.projectileManager.addProjectileNow(new PaladinSwing(entity, entity.pos, playScreen.entities.getEntity(targetEntity, playScreen.player)), playScreen.physicsManager.getDynamicsWorld());
			nextBasicAttack(0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
		} else {
			failResolve();
		}
	}

}
