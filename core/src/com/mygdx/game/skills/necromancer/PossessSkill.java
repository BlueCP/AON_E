package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.Thunderstrike;
import com.mygdx.game.projectiles.necromancer.PossessArea;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class PossessSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public PossessSkill() {
		super(null);
	}

	public PossessSkill(Entity entity) {
		super(entity);
		name = "Possess";
		desc = "Possess the targeted enemy, causing them to take damage over time and increasing your spirit regen rate.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			playScreen.projectileManager.addProjectileNow(new PossessArea(entity, targetEntity, 5f), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
