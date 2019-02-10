package com.mygdx.game.entityactions;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.ActiveSkill;

/**
 * Refers to skills where you just click on it to activate it, no targeting of entities or location required.
 */
public class ActivatedSkillAction extends SkillAction {

//	private Skill skill;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ActivatedSkillAction() { }

	public ActivatedSkillAction(ActiveSkill skill, float lifetime) {
		this.skill = skill;

		name = skill.getName();
		totalLifetime = lifetime;
	}

	@Override
	public void update(Entity entity, PlayScreen playScreen) {
		if (wantsDeletion()) {
			skill.finish(playScreen);
		}
	}

}
