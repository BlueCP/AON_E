package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.ActivatedSkill;

public class HeatwaveSkill extends ActivatedSkill {

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
		playScreen.projectileManager.addHeatwave(entity, playScreen.physicsManager.getDynamicsWorld(), entity.pos);
		putOnCooldown(3);
	}

}
