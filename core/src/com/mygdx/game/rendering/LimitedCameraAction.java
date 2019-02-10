package com.mygdx.game.rendering;

public abstract class LimitedCameraAction {

	protected float lifetime;
	
	public abstract void update(Camera camera);

	public float getLifetime() {
		return lifetime;
	}

	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}
	
}
