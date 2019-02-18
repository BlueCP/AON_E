package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public class SlowedEffect extends ProcEffect {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SlowedEffect() { }

	public SlowedEffect(Entity entity) {
		super(entity);

		name = "Slowed";
		desc = "A bit slow.";
	}

	/**
	 * Calculates the % dampening on the affected entity's movement.
	 * @return the dampening.
	 */
	public float movementDampening() {
		float totalMovementDampening = 0;
		for (int power: powers) {
			switch (power) {
				case 1:
					totalMovementDampening += 0.05f;
					break;
				case 2:
					totalMovementDampening += 0.1f;
					break;
				case 3:
					totalMovementDampening += 0.15f;
					break;
				case 4:
					totalMovementDampening += 0.2f;
					break;
			}
		}
		return totalMovementDampening;
	}

}
