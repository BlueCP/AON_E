package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public class ImmobileTerrain extends ImmobileObject {

	public ImmobileTerrain(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags, int spriteX, int spriteY) {
		super(collisionObject, texture, id, tags, spriteX, spriteY);

		physicsId = Util.getImmobileTerrainId(this.id);
		collisionObject.setUserValue(physicsId);
	}

}
