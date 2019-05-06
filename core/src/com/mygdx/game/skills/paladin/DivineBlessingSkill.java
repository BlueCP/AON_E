package com.mygdx.game.skills.paladin;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class DivineBlessingSkill extends PassiveSkill {

	private static final float spiritRegenDuration = 3; // The time for which spirit will be regened after taking damage.
	private static final float spiritPerSecond = 5;

	private float regenCountdown; // The time left to regen.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DivineBlessingSkill() {
		super(null, false);
	}

	public DivineBlessingSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Divine Blessing";
		desc = "You regenerate spirit whilst taking damage.";
	}

	public void recordDamageTaken() {
		regenCountdown = spiritRegenDuration;
	}

	public void update() {
		if (regenCountdown > 0) {
			entity.changeSpirit(spiritPerSecond * Gdx.graphics.getDeltaTime());
			regenCountdown -= Gdx.graphics.getDeltaTime();
		}
	}

}
