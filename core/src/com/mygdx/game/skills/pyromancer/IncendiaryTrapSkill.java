package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.IncendiaryTrap;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class IncendiaryTrapSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private IncendiaryTrapSkill() {
		super(null);
	}

	public IncendiaryTrapSkill(Entity entity) {
		super(entity);
		name = "Incendiary Trap";
		desc = "Place a trap at your feet. Explodes when hit by any of your spells.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new IncendiaryTrap(entity, entity.pos, 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
