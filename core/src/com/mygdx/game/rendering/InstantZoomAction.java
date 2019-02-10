package com.mygdx.game.rendering;

import com.mygdx.game.screens.PlayScreen;

public class InstantZoomAction extends UnlimitedCameraAction {

	private float zoom;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public InstantZoomAction() { }
	
	InstantZoomAction(float zoom) {
		this.zoom = zoom;
	}
	
	@Override
	public void update(PlayScreen playScreen) {
		playScreen.isoRenderer.camera.setZoom(zoom);
	}

}
