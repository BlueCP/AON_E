package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public abstract class MobileControllable extends MobileObject {

	private Array<Integer> ports;

	public MobileControllable(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags,
							  Array<String> ports) {
		super(collisionObject, texture, id, tags);

		this.ports = new Array<>();
		for (String port: ports) {
			this.ports.add(Integer.parseInt(port));
		}
//		this.ports = ports;
		physicsId = Util.getMobileControllerId(this.id);
		collisionObject.setUserValue(physicsId);
	}

}
