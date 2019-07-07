package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.BurningBarrier;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class BurningBarrierSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private BurningBarrierSkill() {
		super(null);
	}

	public BurningBarrierSkill(Entity entity) {
		super(entity);
		name = "Burning Barrier";
		desc = "Summon a wall of fire which stuns enemies that walk through it.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 1, 10, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new BurningBarrier(entity, targetPos, 10), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
