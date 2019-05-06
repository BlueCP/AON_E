package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.paladin.CleansingStrike;
import com.mygdx.game.projectiles.paladin.DetainArea;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class DetainSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DetainSkill() {
		super(null);
	}

	public DetainSkill(Entity entity) {
		super(entity);
		name = "Detain";
		desc = "Root and damage the targeted enemy.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			playScreen.projectileManager.addProjectileNow(new DetainArea(entity, targetEntity.pos, 2, targetEntity), playScreen.physicsManager.getDynamicsWorld());
			putOnCooldown(2);
		} else {
			failResolve();
		}
	}

}
