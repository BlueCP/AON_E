package com.mygdx.game.skills;

import com.mygdx.game.entities.Entity;

/**
 * Opposite of the Active Skill - a skill which doesn't require any explicit input from the user to work.
 */
public abstract class PassiveSkill extends Skill {

	private boolean learned;

	protected PassiveSkill(Entity entity, boolean learned) {
		super(entity);

		this.learned = learned;
	}

	public boolean isLearned() {
		return learned;
	}

	public void setLearned(boolean learned) {
		this.learned = learned;
	}

}
