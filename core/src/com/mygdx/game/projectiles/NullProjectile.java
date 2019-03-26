package com.mygdx.game.projectiles;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;

public class NullProjectile extends Projectile {

	public NullProjectile() {
		id = -1;
		name = "Null";
	}

	@Override
	void updatePos() {

	}

	@Override
	public void update(float delta, PlayScreen playScreen) {

	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		return false;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

	@Override
	public void destroy(btDynamicsWorld dynamicsWorld, ProjectileManager projectileManager) {

	}

	@Override
	public btCollisionObject getCollisionObject() {
		return null;
	}

	@Override
	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {

	}

	@Override
	protected void loadPhysicsObject() {

	}

}
