package com.mygdx.game.rendering;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class SoftPanAction extends LimitedCameraAction {

	private Vector3 startingPos;
	private Vector3 displacement;

	private float fullLifetime;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	private SoftPanAction() { }
	
	SoftPanAction(float lifetime, Vector3 startingPos, Vector3 targetPos) {
		this.lifetime = lifetime;
		this.startingPos = startingPos.cpy();
		displacement = targetPos.sub(startingPos);

		fullLifetime = lifetime;
	}
	
	@Override
	public void update(Camera camera) {
		float value = ((fullLifetime - lifetime) / fullLifetime) * 180; // First 180 degrees of the inverse cos curve
		float distanceFromStart = (-MathUtils.cosDeg(value) + 1) / 2;
		Vector3 displacementNow = displacement.cpy().scl(distanceFromStart);
		camera.pos = startingPos.cpy().add(displacementNow);
	}

}
