package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class DivinePunishmentSkill extends PassiveSkill {

	private static final float damageMultiplier = 1.5f;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DivinePunishmentSkill() {
		super(null, false);
	}

	public DivinePunishmentSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Divine Punishment";
		desc = "You deal more damage to blind, burning (with light fire), or rooted enemies.";
	}

	public float damage(Entity entity) {
		if (isLearned() && (entity.blindedEffect.isActive() || entity.lightFireEffect.isActive() ||
				entity.rootedEffect.isActive())) {
			return damageMultiplier;
		} else {
			return 1;
		}
	}

}
