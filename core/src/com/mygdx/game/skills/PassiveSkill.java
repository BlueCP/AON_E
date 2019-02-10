package com.mygdx.game.skills;

import com.mygdx.game.entities.Entity;

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
