package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class SoothingLightSkill extends SimpleSkill {

	private static final float percLifeHeal = 0.5f; // The % of the entity's life that will be healed.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private SoothingLightSkill() {
		super(null);
	}

	public SoothingLightSkill(Entity entity) {
		super(entity);
		name = "Soothing Light";
		desc = "Heal a portion of your life.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		entity.changeLife(entity.getMaxLife() * percLifeHeal);
		putOnCooldown(3);
	}

}
