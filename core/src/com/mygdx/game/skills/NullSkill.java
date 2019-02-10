package com.mygdx.game.skills;

import com.mygdx.game.entities.Entity;

public class NullSkill extends Skill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public NullSkill() {
		super(null);
	}

	NullSkill(Entity entity) {
		super(entity);
		name = "[No skill]";
		desc = "";
	}

}
