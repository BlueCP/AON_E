package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.utils.Util;

public class ControllerObject extends ConstantObject {

	public ControllerObject(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<Tag> tags,
							int spriteX, int spriteY) {
		super(collisionObject, texture, id, tags, spriteX, spriteY);
		int physicsId = Util.getPhysicsId(Integer.parseInt(id), "2100");
		collisionObject.setUserValue(physicsId);
		this.physicsId = physicsId;
	}

}
