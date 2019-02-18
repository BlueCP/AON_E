package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public class RootedEffect extends BinaryEffect {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public RootedEffect() { }

	public RootedEffect(Entity entity) {
		super(entity);

		name = "Rooted";
		desc = "Can't move.";
	}

}
