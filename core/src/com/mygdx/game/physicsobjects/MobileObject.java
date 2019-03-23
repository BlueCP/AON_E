package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physics.PhysicsManager;

/**
 * Respresents objects in the world which can move, and which are ConstantObjects.
 * E.g. moving platforms, large levers, doors, gates, etc.
 */
public abstract class MobileObject extends ConstantObject {

	private Vector3 linearVelocity;

	public MobileObject(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags) {
		super(collisionObject, texture, id, tags);
//		int physicsId = Util.getPhysicsId(Integer.parseInt(id), "5100");
//		collisionObject.setUserValue(physicsId);
//		this.physicsId = physicsId;

		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		collisionObject.setActivationState(Collision.DISABLE_DEACTIVATION);
		linearVelocity = new Vector3();
	}

	public void update() {
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().translate(linearVelocity.cpy().scl(Gdx.graphics.getDeltaTime())));
		super.update(); // Update the transform after translating this object
	}

	public Vector3 getLinearVelocity() {
		return linearVelocity;
	}

	public void setLinearVelocity(Vector3 linearVelocity) {
		this.linearVelocity = linearVelocity;
	}

}
