package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;

/**
 * Represents objects in the world which cannot move, and which are ConstantObjects.
 *
 */
public abstract class ImmobileObject extends ConstantObject {

	private int renderingIndex;

	public ImmobileObject(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags) {
		super(collisionObject, texture, id, tags);
	}

	public void update() {
		super.update();
	}

	public int getRenderingIndex() {
		return renderingIndex;
	}

	public void setRenderingIndex(int renderingIndex) {
		this.renderingIndex = renderingIndex;
	}
}
