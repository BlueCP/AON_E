package com.mygdx.game.entityactions;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;

public abstract class EntityAction {

	protected String name;
	private Entity.AnimationType animationType;
	
	float totalLifetime; // Maxiumum lifetime of action
	private float countUp; // Time passed since creation of action

	EntityAction(String name, Entity.AnimationType animationType) {
		this.name = name;
		this.animationType = animationType;

		countUp = 0;
	}

	public abstract void update(Entity entity, PlayScreen playScreen);
	
	/*
	 * Returns true if countUp >= totalLifetime. Returns false otherwise.
	 */
	public boolean wantsDeletion() {
		return countUp >= totalLifetime;
	}

	public float getCountUp() {
		return countUp;
	}

	public void setCountUp(float countUp) {
		this.countUp = countUp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Entity.AnimationType getAnimationType() {
		return animationType;
	}

	public void setAnimationType(Entity.AnimationType animationType) {
		this.animationType = animationType;
	}

}
