package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

/**
 * Buffs physical damage.
 */
public class DamageEffect extends ProcEffectCont {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DamageEffect() { }

	public DamageEffect(Entity entity) {
		super(entity);

		name = "Damage Buff";
		desc = "Increased damage.";
	}

	public float totalBuff() {
		float totalBuff = 1;
		for (float power: powers) {
			totalBuff += power;
		}
		return totalBuff;
	}

}
