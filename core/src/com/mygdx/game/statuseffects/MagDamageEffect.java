package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

/**
 * Buffs magical damage.
 */
public class MagDamageEffect extends ProcEffectCont {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public MagDamageEffect() { }

	public MagDamageEffect(Entity entity) {
		super(entity);

		name = "Magical Damage Buff";
		desc = "Increased magical damage.";
	}

	public float totalBuff() {
		float totalBuff = 1;
		for (float power: powers) {
			totalBuff += power;
		}
		return totalBuff;
	}

}
