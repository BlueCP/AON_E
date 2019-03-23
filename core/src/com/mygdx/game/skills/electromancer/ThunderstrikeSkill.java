package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.Thunderstrike;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class ThunderstrikeSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ThunderstrikeSkill() {
		super(null);
	}

	public ThunderstrikeSkill(Entity entity) {
		super(entity);
		name = "Thunderstrike";
		desc = "Summon a bolt of lightning to fire at the targeted enemy.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			playScreen.projectileManager.addProjectileNow(new Thunderstrike(entity, entity.pos, 0.2f, targetEntity), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
