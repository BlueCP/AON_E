package com.mygdx.game.rendering;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class HardPanAction extends LimitedCameraAction {

	private Vector3 startingPos;
	private Vector3 displacement;

	private float fullLifetime;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public HardPanAction() { }
	
	HardPanAction(float lifetime, Vector3 startingPos, Vector3 targetPos) {
		this.lifetime = lifetime;
		this.startingPos = startingPos.cpy();
		displacement = targetPos.sub(startingPos);

		fullLifetime = lifetime;
	}
	
	@Override
	public void update(Camera camera) {
		float value = ((fullLifetime - lifetime) / fullLifetime) * 90; // First 90 degrees of the sin curve
		float distanceFromStart = MathUtils.sinDeg(value);
		Vector3 displacementNow = displacement.cpy().scl(distanceFromStart);
		camera.pos = startingPos.cpy().add(displacementNow);
	}

}
