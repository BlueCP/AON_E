package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Util;

public abstract class MobileController extends MobileObject {

	int[] ports;

	public MobileController(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags,
							int spriteX, int spriteY, int[] ports) {
		super(collisionObject, texture, id, tags, spriteX, spriteY);

		this.ports = ports;
		physicsId = Util.getMobileControllerId(this.id);
		collisionObject.setUserValue(physicsId);
	}

	/**
	 * This is overridden based on what the functionality of the controller is.
	 * E.g. a three-way switch vs a two-way switch.
	 * @param mouseEvent the mouse event of the click on this object.
	 */
	public abstract void clicked(PlayScreen.MouseEvent mouseEvent, PhysicsManager physicsManager);

}
