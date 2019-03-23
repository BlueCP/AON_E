package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public class ImmobileTerrain extends ImmobileObject {

	public ImmobileTerrain(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags) {
		super(collisionObject, texture, id, tags);

		physicsId = Util.getImmobileTerrainId(this.id);
		collisionObject.setUserValue(physicsId);
	}

	/**
	 * By default, nothing is saved. Subclasses can override if they wish.
	 */
	@Override
	public void save(Kryo kryo, Output output) {

	}

	/**
	 * By default, no data is loaded. Subclasses can override if they wish.
	 */
	@Override
	public void load(Kryo kryo, Input input) {

	}

}
