package com.mygdx.game.rendering;

import com.badlogic.gdx.math.Vector3;

public class CutAction extends LimitedCameraAction {

	private Vector3 pos;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public CutAction() { }
	
	CutAction(float lifetime, Vector3 pos) {
		this.lifetime = lifetime;
		this.pos = pos.cpy();
	}
	
	@Override
	public void update(Camera camera) {
		camera.pos = pos.cpy();
	}

}
