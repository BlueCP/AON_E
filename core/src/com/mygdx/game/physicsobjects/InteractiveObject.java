package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.screens.PlayScreen;

public abstract class InteractiveObject extends ConstantObject {

	protected InteractiveObject(btCollisionObject collisionObject, TextureRegion[] TextureRegion, String id, Array<Tag> newTags) {
		super(collisionObject, TextureRegion, id, newTags);
	}

	public abstract void interact(PlayScreen playScreen);
	
}