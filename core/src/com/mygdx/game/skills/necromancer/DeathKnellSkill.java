package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.Thunderstrike;
import com.mygdx.game.projectiles.necromancer.DeathKnell;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class DeathKnellSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DeathKnellSkill() {
		super(null);
	}

	public DeathKnellSkill(Entity entity) {
		super(entity);
		name = "Death Knell";
		desc = "Deal damage to the targeted enemy. You deal more damage when you have less life.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			playScreen.projectileManager.addProjectileNow(new DeathKnell(entity, entity.pos, 0.2f, targetEntity), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
