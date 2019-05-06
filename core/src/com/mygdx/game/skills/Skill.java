package com.mygdx.game.skills;

import com.mygdx.game.entities.Entity;

public abstract class Skill {

	protected String name;
	protected String desc;
	protected transient Entity entity; // The entity which used this skill.
	protected boolean castWithLife; // Whether or not this skill was cast using life.

	public Skill(Entity entity) {
		this.entity = entity;
	}

	boolean sameAs(Skill skill) {
		return name.equals(skill.name);
	}

	/**
	 * Convenience method.
	 * @param entity the entity using the skill.
	 * @param cost the cost in spirit (or, if there is insufficient spirit, use life instead).
	 * @return whether or not the spell can be cast with the available spirit and life.
	 */
	protected boolean hasResource(Entity entity, int cost) {
		if (entity.getSpirit() >= cost) {
			entity.changeSpirit(-cost);
			castWithLife = false;
			return true;
		} else if (entity.getLife() >= cost) {
			int remainder = (int) (cost - entity.getSpirit());
			entity.setSpirit(0);
			entity.changeLife(-remainder);
			castWithLife = true;
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public boolean isCastWithLife() {
		return castWithLife;
	}

	public void setCastWithLife(boolean castWithLife) {
		this.castWithLife = castWithLife;
	}

}
