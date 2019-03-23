package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.Fireball;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class IceShardSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public IceShardSkill() {
		super(null);
	}

	public IceShardSkill(Entity entity) {
		super(entity);
		name = "Ice Shard";
		desc = "Warning: excessive use may be indicative of an icy personality.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.7f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
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
