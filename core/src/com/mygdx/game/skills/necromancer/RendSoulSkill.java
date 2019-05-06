package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.Thunderstrike;
import com.mygdx.game.projectiles.necromancer.RendSoul;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class RendSoulSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public RendSoulSkill() {
		super(null);
	}

	public RendSoulSkill(Entity entity) {
		super(entity);
		name = "Rend Soul";
		desc = "Damage the targeted enemy, summoning the might of your trapped souls to deal more damage.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			playScreen.projectileManager.addProjectileNow(new RendSoul(entity, entity.pos, 0.2f, targetEntity), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
