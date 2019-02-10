package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public abstract class ImmobileControllable extends ImmobileObject {

	int[] ports;

	public ImmobileControllable(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags,
								int spriteX, int spriteY, int[] ports) {
		super(collisionObject, texture, id, tags, spriteX, spriteY);

		this.ports = ports;
		physicsId = Util.getImmobileControllableId(this.id);
		collisionObject.setUserValue(physicsId);
	}

	/**
	 * This is overridden to execute some behaviour based on the states of the ports.
	 */
	public abstract void update(ObjectMap<Integer, Integer> objectMap);

}
