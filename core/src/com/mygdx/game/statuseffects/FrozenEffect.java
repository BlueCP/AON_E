package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public class FrozenEffect extends BinaryEffect {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public FrozenEffect() { }

	public FrozenEffect(Entity entity) {
		super(entity);
		name = "Frozen";
		desc = "A bit colder than cold.";
	}

}
