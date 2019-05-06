package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class IntangibleGhostSkill extends PassiveSkill {

	private static final float maxLifeReduction = 0.1f; // The min value that life can be multiplied by.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public IntangibleGhostSkill() {
		super(null, false);
	}

	public IntangibleGhostSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Intangible Ghost";
		desc = "You take less damage the lower your health is.";
	}

	public float reduceDamage() {
		if (isLearned()) {
			return ((-(maxLifeReduction - 1) / entity.getMaxLife())*entity.getLife() + maxLifeReduction);
		} else {
			return 1;
		}
		// The formula above was found by forming a generalised linear equation which passes through two points.
		// Point 1: (0, maxLifeReduction). When the player 'has 0 life', apply the largest possible reduction in damage.
		// Point 2: (maxLife, 1). When the player is at max life, do not apply any reduction (i.e. make it 1).
	}

}
