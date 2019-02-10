package com.mygdx.game.rendering;

import com.badlogic.gdx.math.MathUtils;

public class ScreenShakeAction extends LimitedCameraAction {

	float fullLifetime;
	float magnitude;

	int arrayLength;
	final int pointsPerSecond = 20; // The number of points in the perlin noise graph for 1s of screen shake.
	float[] slopeAtX;
	float[] slopeAtY;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ScreenShakeAction() { }

	void build(float lifetime, float magnitude) {
		this.lifetime = lifetime;
		this.magnitude = magnitude;
		fullLifetime = lifetime;

		// All this is calculated assuming that 10 points on the perlin noise curve is for 1 second.
		// Multiple lifetime by 10 to find number of integer points on the curve.
		// Add one because casting to int truncates the float. Slightly bigger is better than slightly smaller.
		arrayLength = (int) (lifetime * pointsPerSecond) + 1;

		slopeAtX = new float[arrayLength];
		for (int i = 0; i < arrayLength; i ++) {
			slopeAtX[i] = MathUtils.random(-1000, 1000) / 1000f;
		}

		slopeAtY = new float[arrayLength];
		for (int i = 0; i < arrayLength; i ++) {
			slopeAtY[i] = MathUtils.random(-1000, 1000) / 1000f;
		}
	}

	@Override
	public void update(Camera camera) {
		camera.screenShakeDisplacement.set(noiseX(), 0, noiseY()).scl(magnitude);
	}

	private float noiseX() {
		return noise(slopeAtX);
	}

	private float noiseY() {
		return noise(slopeAtY);
	}

	private float noise(float[] array) {
		float x = lifetime > 0 ? (fullLifetime - lifetime) * pointsPerSecond : arrayLength - 2;
		int lo = (int) Math.floor(x);
		int hi = lo + 1;
		float dist = x - lo;
		float loSlope = array[lo];
		float hiSlope = array[hi];
		float loPos = loSlope * dist;
		float hiPos = -hiSlope * (1 - dist);
		float u = (float) (dist * dist * (3.0 - 2.0 * dist));  // cubic curve
		return (loPos * (1 - u)) + (hiPos * u);  // interpolate
	}

}
