package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public abstract class ImmobileControllable extends ImmobileObject {

	public Array<Integer> ports;

	public ImmobileControllable(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags,
								Array<String> ports) {
		super(collisionObject, texture, id, tags);

		this.ports = new Array<>();
		for (String port: ports) {
			this.ports.add(Integer.parseInt(port));
		}
		/*for (int i = 0; i < ports.length; i++) {
			this.ports[i] = Integer.parseInt(ports[i]);
		}*/
//		this.ports = ports;
		physicsId = Util.getImmobileControllableId(this.id);
		collisionObject.setUserValue(physicsId);
	}

	/**
	 * This is overridden to execute some behaviour based on the states of the ports.
	 */
//	public abstract void update(ObjectMap<Integer, Integer> objectMap);

}
