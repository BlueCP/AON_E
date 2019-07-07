package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public abstract class ImmobileController extends ImmobileObject {

	protected Array<Integer> ports; // The 'ports' that this controller has influence over
	protected int startingState; // The state that the ports for this controller start in.

	public ImmobileController(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags,
							  Array<String> ports, int startingState) {
		super(collisionObject, texture, id, tags);

		this.ports = new Array<>();
		for (String port: ports) {
			this.ports.add(Integer.parseInt(port));
		}
		this.startingState = startingState;
		/*for (int i = 0; i < ports.length; i++) {
			this.ports[i] = Integer.parseInt(ports[i]);
		}*/
//		this.ports = ports;
		physicsId = Util.getImmobileControllerId(this.id);
		collisionObject.setUserValue(physicsId);
	}

}
