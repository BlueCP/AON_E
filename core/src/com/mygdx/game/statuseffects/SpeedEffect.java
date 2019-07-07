package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public class SpeedEffect extends ProcEffectDisc {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private SpeedEffect() { }

	public SpeedEffect(Entity entity) {
		super(entity);

		name = "Speed";
		desc = "Speedy.";
	}

	/**
	 * Calculates the % boost on the affected entity's movement.
	 * @return the boost.
	 */
	public float movementBoost() {
		float totalMovementBoost = 0;
		for (int power: powers) {
			switch (power) {
				case 1:
					totalMovementBoost += 0.1f;
					break;
				case 2:
					totalMovementBoost += 0.2f;
					break;
				case 3:
					totalMovementBoost += 0.3f;
					break;
				case 4:
					totalMovementBoost += 0.4f;
					break;
				case 5:
					totalMovementBoost += 2f;
					break;
			}
		}
		return totalMovementBoost;
	}

}
