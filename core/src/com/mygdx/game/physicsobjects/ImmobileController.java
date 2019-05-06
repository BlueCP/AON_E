package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Util;

public abstract class ImmobileController extends ImmobileObject {

	public Array<Integer> ports; // The 'ports' that this controller has influence over
	public int startingState; // The state that the ports for this controller start in.

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

	/**
	 * This is overridden based on what the functionality of the controller is.
	 * E.g. a three-way switch vs a two-way switch.
	 * @param mouseEvent the mouse event of the click on this object.
	 */
//	public abstract void clicked(PlayScreen.MouseEvent mouseEvent, PhysicsManager physicsManager);

}
