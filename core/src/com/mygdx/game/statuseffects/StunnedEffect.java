package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public class StunnedEffect extends BinaryEffect {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private StunnedEffect() { }

	public StunnedEffect(Entity entity) {
		super(entity);

		name = "Stunned";
		desc = "Seeing stars.";
	}

}
