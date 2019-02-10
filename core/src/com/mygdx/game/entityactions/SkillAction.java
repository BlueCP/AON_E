package com.mygdx.game.entityactions;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.Skill;
import com.mygdx.game.skills.ActiveSkill;

public class SkillAction extends EntityAction {

//	protected Entity entity;
	protected ActiveSkill skill;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SkillAction() {
		super("", null);
	}

	public SkillAction(ActiveSkill skill, Entity.AnimationType animationType, float lifetime) {
		super(skill.getName(), animationType);

		this.skill = skill;

		name = skill.getName();
		totalLifetime = lifetime;
	}

//	public Entity getEntity() {
//		return entity;
//	}

	public Skill getSkill() {
		return skill;
	}

	@Override
	public void update(Entity entity, PlayScreen playScreen) {
		if (wantsDeletion()) {
			skill.finish(playScreen);
		}
	}

}
