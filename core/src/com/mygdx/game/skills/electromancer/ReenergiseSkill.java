package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class ReenergiseSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private ReenergiseSkill() {
		super(null);
	}

	public ReenergiseSkill(Entity entity) {
		super(entity);
		name = "Reenergise";
		desc = "Regen some spirit and gain movement speed temporarily.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		entity.changeSpirit(10);
		entity.speedEffect.add(5, 2);
		putOnCooldown(3);
	}

}
