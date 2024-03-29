package com.mygdx.game.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.screens.PlayScreen;

public class RazeZone extends StaticProjectile {

	/*
	 * No-arg constructor for serialisation purposes.
	 */
	private RazeZone() { }
	
	RazeZone(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.RAZE_ZONE, pos, lifetime);
		
		//btGhostPairCallback ghostCall = new btGhostPairCallback();
		//dynamicsWorld.getBroadphase().getOverlappingPairCache().setInternalGhostPairCallback(ghostCall);

		loadPhysicsObject();

		collisionObject.setWorldTransform(collisionObject.getWorldTransform().trn(pos.cpy()));
		
		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	protected void loadPhysicsObject() {
		shape = new btCylinderShape(new Vector3(10, 0.3f, 10));
		collisionObject = new btCollisionObject();

		collisionObject.setCollisionShape(shape);

		collisionObject.setWorldTransform(worldTransform);

		collisionObject.setUserValue(physicsId);
		collisionObject.setActivationState(Collision.DISABLE_DEACTIVATION);
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}
	
	@Override
	public void update(float delta, PlayScreen playScreen) {
		collisionObject.getWorldTransform().getTranslation(pos);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
//		entity.changeEffect(EffectType.BURNING, 2, 1);
		return false;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

	@Override
	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		super.addToDynamicsWorld(dynamicsWorld, group, mask);
	}

}
