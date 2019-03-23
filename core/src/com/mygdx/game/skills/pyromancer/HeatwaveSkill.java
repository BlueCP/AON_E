package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.Heatwave;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class HeatwaveSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public HeatwaveSkill() {
		super(null);
	}

	public HeatwaveSkill(Entity entity) {
		super(entity);
		name = "Heatwave";
		desc = "Create a pulsating heatwave that damages and slows enemies.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new Heatwave(entity, entity.pos), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
