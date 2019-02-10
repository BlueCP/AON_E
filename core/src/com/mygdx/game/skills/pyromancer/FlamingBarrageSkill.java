package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.skills.ActiveSkill;
import com.mygdx.game.skills.PassiveSkill;

public class FlamingBarrageSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public FlamingBarrageSkill() {
		super(null, false);
	}

	public FlamingBarrageSkill(Entity entity, boolean learned) {
		super(entity, learned);
	}

	public void testfor() {
		if (isLearned()) {
			for (ActiveSkill skill: entity.getSkills()) {
				skill.reduceCooldown(1);
			}
		}
	}

}
