package com.mygdx.game.entityactions;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.ActiveSkill;

/**
 * Refers to skills which require an entity target.
 */
public class TargettedSkillAction extends SkillAction {

//	private Entity entity;
//	private int targetEntity;
//	private Skill skill;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public TargettedSkillAction() { }

	public TargettedSkillAction(Entity entity, ActiveSkill skill, float lifetime) {
//		this.entity = entity;
		this.skill = skill;
//		targetEntity = entity.getTargetEntity();

		name = skill.getName();
		totalLifetime = lifetime;
	}

	@Override
	public void update(Entity entity, PlayScreen playScreen) {
//		if (wantsDeletion() && playScreen.entities.getEntity() != null) {
//			skill.finish(playScreen);
//		}
	}

//	public Entity getEntity() {
//		return entity;
//	}

//	public int getTargetEntity() {
//		return targetEntity;
//	}

}
