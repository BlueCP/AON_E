package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Util;

public abstract class MobileTerrain extends MobileObject {

	public MobileTerrain(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags, int spriteX, int spriteY) {
		super(collisionObject, texture, id, tags, spriteX, spriteY);

		physicsId = Util.getMobileTerrainId(this.id);
		collisionObject.setUserValue(physicsId);
	}

	/**
	 * This is overridden based on how the mobile terrain to behave.
	 * E.g. if the terrain only moves when the player is near.
	 * @param playScreen the entire playscreen because the info needed could cover such a broad range of things.
	 */
	public abstract void update(PlayScreen playScreen);

}
