package com.mygdx.game.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;

public abstract class StaticProjectile extends Projectile {

	protected transient btCollisionObject collisionObject;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public StaticProjectile() { }

	protected StaticProjectile(Entity entity, ProjectileSprite sprite, Vector3 pos, float lifetime) {
		super(entity, sprite, pos, lifetime);
	}

	void updatePos() {
		collisionObject.getWorldTransform().getTranslation(pos);
		collisionObject.getWorldTransform(worldTransform);
	}

	public btCollisionObject getCollisionObject() {
		return collisionObject;
	}

	protected void defaultLoadCollisionObject(btCollisionShape shape) {
		this.shape = shape;
		collisionObject = new btCollisionObject();
		collisionObject.setCollisionShape(shape);

		collisionObject.setWorldTransform(worldTransform);

		collisionObject.setUserValue(physicsId);
		collisionObject.setActivationState(Collision.DISABLE_DEACTIVATION);
	}

	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		dynamicsWorld.addCollisionObject(collisionObject, group, mask);
	}

	public void destroy(btDynamicsWorld dynamicsWorld, ProjectileManager projectileManager) {
		isDestroyed = true;
		projectileManager.idPool.add(id);
		dynamicsWorld.removeCollisionObject(collisionObject);
		projectileManager.projectiles.removeValue(this, false);
		shape.dispose();
		collisionObject.dispose();
	}

}
