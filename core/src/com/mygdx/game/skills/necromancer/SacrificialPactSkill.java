package com.mygdx.game.skills.necromancer;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class SacrificialPactSkill extends SimpleSkill {

	private static final float soulRegenPower = 3;
	private static final float damageBuff = 0.5f;

	private float timeAlive; // Time since the skill had finished casting, i.e. the time since the skill was put on standby.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private SacrificialPactSkill() {
		super(null);
	}

	public SacrificialPactSkill(Entity entity) {
		super(entity);
		name = "Sacrificial Pact";
		desc = "Gain a damage and soul regeneration buff, but deals increasing damage over time to you.";
	}

	@Override
	public void update(PlayScreen playScreen) {
		if (state == State.ON_STANDBY) { // If the pact is active
			entity.soulsRegenEffect.addThisTick(soulRegenPower);
			entity.damageEffect.addThisTick(damageBuff);

			entity.takeDamageNoEntity((float) (Math.pow(1.2, timeAlive) / 60f));

			timeAlive += Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 1f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		timeAlive = 0;
		removeAction();
		putOnStandby();
	}

	@Override
	public void stop(PlayScreen playScreen) {
		putOnCooldown(3);
	}

}
