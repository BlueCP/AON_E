package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.Thunderstrike;
import com.mygdx.game.projectiles.necromancer.SiphoningStrike;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class SiphoningStrikeSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SiphoningStrikeSkill() {
		super(null);
	}

	public SiphoningStrikeSkill(Entity entity) {
		super(entity);
		name = "Siphoning Strike";
		desc = "Drain the life of the targeted enemy, healing yourself based on the number of souls you have.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			playScreen.projectileManager.addProjectileNow(new SiphoningStrike(entity, entity.pos, 0.2f, targetEntity), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
