package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.Fireball;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class FireballSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private FireballSkill() {
		super(null);
	}

	public FireballSkill(Entity entity) {
		super(entity);
		name = "Fireball";
		desc = "For the pyromancer, a simple, effective solution to most problems.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			playScreen.projectileManager.addProjectileNow(new Fireball(entity, entity.pos, playScreen.entities.getEntity(targetEntity, playScreen.player).pos, 5), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
