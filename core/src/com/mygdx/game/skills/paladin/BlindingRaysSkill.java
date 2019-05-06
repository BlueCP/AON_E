package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.paladin.BlindingRaysArea;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class BlindingRaysSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BlindingRaysSkill() {
		super(null);
	}

	public BlindingRaysSkill(Entity entity) {
		super(entity);
		name = "Blinding Rays";
		desc = "Temporarily blind all nearby enemies.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new BlindingRaysArea(entity, entity.pos), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
