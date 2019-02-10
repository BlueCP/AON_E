package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class FireballSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public FireballSkill() {
		super(null);
	}

	public FireballSkill(Entity entity) {
		super(entity);
		name = "Fireball";
		desc = "For the pyromancer, a simple, effective solution to most problems.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen.entities);
	}

	@Override
	public void finish(PlayScreen playScreen) {
//		System.out.println(targetEntity);
		if (playScreen.entities.getEntity(targetEntity) != null) {
			playScreen.projectileManager.addFireball(entity, playScreen.physicsManager.getDynamicsWorld(), entity.pos, playScreen.entities.getEntity(targetEntity).pos, 5);
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
