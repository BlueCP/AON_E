package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.LavaSnare;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class LavaSnareSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public LavaSnareSkill() {
		super(null);
	}

	public LavaSnareSkill(Entity entity) {
		super(entity);
		name = "Lava Snare";
		desc = "Trap an enemy in place, burning them for the duration.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 1, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (entity.getTargetEntity() != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			playScreen.projectileManager.addProjectileNow(new LavaSnare(entity, targetEntity.pos.cpy(), 3, targetEntity), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(3);
		} else {
			failResolve();
		}
	}

}
