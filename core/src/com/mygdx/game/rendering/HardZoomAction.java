package com.mygdx.game.rendering;

import com.badlogic.gdx.math.MathUtils;

public class HardZoomAction extends LimitedCameraAction {

	private float initialZoom;
	private float targetZoom;
	
	private float fullLifetime;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public HardZoomAction() { }
	
	HardZoomAction(float lifetime, float initialZoom, float targetZoom) {
		this.lifetime = lifetime;
		this.initialZoom = initialZoom;
		this.targetZoom = targetZoom;
		
		this.fullLifetime = lifetime;
	}
	
	@Override
	public void update(Camera camera) {
		float value = ((fullLifetime - lifetime) / fullLifetime) * 90; // First 90 degrees of the sin curve
		float newDisp = (MathUtils.sinDeg(value)) * (targetZoom - initialZoom);
		//float disp = ((camera.getZoom() - initialZoom) / (targetZoom - initialZoom)) * 180;
		camera.setZoom(initialZoom + newDisp);
	}

}
