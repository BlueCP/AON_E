package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public class BlindedEffect extends ProcEffectCont {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private BlindedEffect() { }

	public BlindedEffect(Entity entity) {
		super(entity);

		name = "Blinded";
		desc = "No quite able to see.";
	}

	/**
	 * Calculates the % of the damage that is dealt (more powerful blind = lower % of damage dealt).
	 * @return the %.
	 */
	public float damageDamping() {
		float damping = 1; // 1 = no damage lost.
		for (float power: powers) {
			damping -= power;
		}
		return damping >= 0 ? damping : 0;
	}

}
