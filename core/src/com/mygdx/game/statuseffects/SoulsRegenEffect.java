package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

/**
 * Increases the rate at which souls are regenerated.
 * Specific to the necromancer's passive ability, Soul Stealer, as that is the only thing that gives the necromancer
 * soul regen.
 * e.g. a power of 2 would double the soul regen speed, and so would halve the time to regen a given number of souls.
 */
public class SoulsRegenEffect extends ProcEffectCont {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SoulsRegenEffect() { }

	public SoulsRegenEffect(Entity entity) {
		super(entity);

		name = "Soul Regen";
		desc = "Faster than usual soul regen.";
	}

}
