package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class BurningBarrierSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BurningBarrierSkill() {
		super(null);
	}

	public BurningBarrierSkill(Entity entity) {
		super(entity);
		name = "Burning Barrier";
		desc = "Summon a wall of fire which stuns enemies that walk through it.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 1, 10, Entity.AnimationType.SHOOT_PROJECTILE, playScreen.player.getTargetLocation());
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addBurningBarrier(entity, playScreen.physicsManager.getDynamicsWorld(), targetPos, 10);
		putOnCooldown(3);
	}

}
