package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.utils.Util;

public abstract class KinematicObject extends ConstantObject implements Disposable {
	
	private Vector3 linearVelocity;
	
	public KinematicObject(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<Tag> tags,
			int spriteX, int spriteY) {
		super(collisionObject, texture, id, tags);
		int physicsId = Util.getPhysicsId(Integer.parseInt(id), "5100");
		collisionObject.setUserValue(physicsId);
		this.physicsId = physicsId;
		
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | CollisionFlags.CF_KINEMATIC_OBJECT);
		collisionObject.setActivationState(Collision.DISABLE_DEACTIVATION);
		linearVelocity = new Vector3();
	}
	
	public void update() {
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().translate(linearVelocity));
		super.update(); // Update the transform after translating this object
	}
	
	public Vector3 getLinearVelocity() {
		return linearVelocity;
	}

	public void setLinearVelocity(Vector3 linearVelocity) {
		this.linearVelocity = linearVelocity;
	}

}
