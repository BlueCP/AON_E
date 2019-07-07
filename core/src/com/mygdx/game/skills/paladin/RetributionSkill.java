package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class RetributionSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private RetributionSkill() {
		super(null);
	}

	public RetributionSkill(Entity entity) {
		super(entity);
		name = "Retribution";
		desc = "Gain a temporary shield which will reflect a portion of the damage you take.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		entity.paladinShieldEffect.add(10); // Give this entity a shield.
		putOnCooldown(3);
	}

}
