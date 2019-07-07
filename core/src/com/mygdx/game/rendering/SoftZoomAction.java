package com.mygdx.game.rendering;

import com.badlogic.gdx.math.MathUtils;

public class SoftZoomAction extends LimitedCameraAction {

	private float initialZoom;
	private float targetZoom;
	
	private float fullLifetime;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	private SoftZoomAction() { }
	
	SoftZoomAction(float lifetime, float initialZoom, float targetZoom) {
		this.lifetime = lifetime;
		this.initialZoom = initialZoom;
		this.targetZoom = targetZoom;
		
		this.fullLifetime = lifetime;
	}
	
	@Override
	public void update(Camera camera) {
		float value = ((fullLifetime - lifetime) / fullLifetime) * 180; // First 180 degrees of the inverse cos curve
		float newDisp = ((-MathUtils.cosDeg(value) + 1) / 2) * (targetZoom - initialZoom);
		//float disp = ((camera.getZoom() - initialZoom) / (targetZoom - initialZoom)) * 180;
		camera.setZoom(initialZoom + newDisp);
	}

}
