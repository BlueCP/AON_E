package com.mygdx.game.skills.necromancer;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.PassiveSkill;

public class DrainEssenceSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private DrainEssenceSkill() {
		super(null, false);
	}

	public DrainEssenceSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Drain Essence";
		desc = "You heal over time based on the number of souls you have.";
	}

	public void update() {
		entity.changeLife(entity.soulsEffect.numStacks() * Gdx.graphics.getDeltaTime() * 0.5f);
	}

}
