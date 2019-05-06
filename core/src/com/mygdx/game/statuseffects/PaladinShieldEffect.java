package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public class PaladinShieldEffect extends BinaryEffect {

	private static final float percReflection = 0.5f; // The % of damage that is reflected. The rest is taken as normal.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public PaladinShieldEffect() { }

	public PaladinShieldEffect(Entity entity) {
		super(entity);

		name = "Shielded";
		desc = "Protected by the light of the paladin's patron.";
	}

	public float reflectDamage(Entity targetEntity, float damage) {
		if (isActive()) {
			entity.dealDamage(targetEntity, damage * percReflection); // Reflect some damage onto the offending entity.
			return damage * (1 - percReflection); // Take the remainder of the damage as normal.
		} else {
			return damage;
		}
	}

}
