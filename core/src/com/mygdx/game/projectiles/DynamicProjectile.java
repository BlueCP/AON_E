package com.mygdx.game.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.mygdx.game.entities.Entity;

public abstract class DynamicProjectile extends Projectile {

	private Vector3 linearVelocity;

	protected transient btRigidBody rigidBody;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DynamicProjectile() { }

	protected DynamicProjectile(Entity entity, ProjectileManager projectileEngine, ProjectileSprite sprite, Vector3 pos, float lifetime) {
		super(entity, projectileEngine, sprite, pos, lifetime);
		linearVelocity = new Vector3();
	}

	protected void defaultLoadRigidBody(btCollisionShape shape) {
		this.shape = shape;

		Vector3 localInertia = new Vector3();
		shape.calculateLocalInertia(1, localInertia);
		rigidBody = new btRigidBody(1, null, shape, localInertia);

		rigidBody.setWorldTransform(worldTransform);
		rigidBody.setLinearVelocity(linearVelocity);

		rigidBody.setUserValue(physicsId);
		rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
	}

	protected void universalUpdate() {
		rigidBody.getWorldTransform().getTranslation(pos);
		rigidBody.getWorldTransform(worldTransform);
		linearVelocity.set(rigidBody.getLinearVelocity());
	}

	protected void calcLinearProjectileMotion(Vector3 targetPos, float speed) {
		Vector3 diff = targetPos.sub(pos); // The difference between the caster and the target
		float total = diff.dst(new Vector3()); // The distance from the origin
		float factor = total/speed; // Working out the factor to scale down by so speed is always correct
		rigidBody.setLinearVelocity(new Vector3(diff.x/factor, diff.y/factor, diff.z/factor));
		rigidBody.setWorldTransform(rigidBody.getWorldTransform().setTranslation(pos).translate(rigidBody.getLinearVelocity().scl(0.2f))); // Move the projectile away from the caster so it doesn't immediately collide with them
	}

	void updatePos() {
		rigidBody.getWorldTransform().getTranslation(pos);
	}

	public btCollisionObject getCollisionObject() {
		return rigidBody;
	}

	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		dynamicsWorld.addRigidBody(rigidBody, group, mask);
	}

	public void destroy(btDynamicsWorld dynamicsWorld, ProjectileManager projectileManager) {
		projectileManager.idPool.add(id);
		dynamicsWorld.removeRigidBody(rigidBody);
		projectileManager.projectiles.removeValue(this, false);
		shape.dispose();
		rigidBody.dispose();
	}

}
