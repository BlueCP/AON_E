package com.mygdx.game.skills.necromancer;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class SoulStealerSkill extends PassiveSkill {

	private static final float baseSoulRegenSpeed = 5; // The time it takes for a soul to naturally regen.

	private float timeSinceLastSoul = 0; // The time since the last soul was added to the player's soul effect.
	private float realSoulRegenSpeed = baseSoulRegenSpeed;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SoulStealerSkill() {
		super(null, false);
	}

	public SoulStealerSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Soul Stealer";
		desc = "You capture souls over time and from dead enemies. Souls increase your damage and grant lifesteal to basic attacks.";
	}

	public void update() {
		if (isLearned()) {
			updateRegenSpeed();
			timeSinceLastSoul += Gdx.graphics.getDeltaTime();
			if (timeSinceLastSoul >= realSoulRegenSpeed) {
				entity.soulsEffect.addOne();
				timeSinceLastSoul = 0;
			}
		}
	}

	private void updateRegenSpeed() {
		realSoulRegenSpeed = baseSoulRegenSpeed;
		for (float power: entity.soulsRegenEffect.powers) {
			realSoulRegenSpeed *= 1/power; // If power is 2, time to regen is halved, so 'speed' is doubled.
		}
	}

	public void enemyKill() {
		if (isLearned()) {
			entity.soulsEffect.addOne();
		}
	}

	public float damage(float damage) {
		if (isLearned()) {
			return damage * entity.soulsEffect.numStacks();
		} else {
			return 0;
		}
	}

	public void basicAttackLifesteal(float damage) {
		if (isLearned()) {
			entity.changeLife(damage * 0.5f);
		}
	}

}
