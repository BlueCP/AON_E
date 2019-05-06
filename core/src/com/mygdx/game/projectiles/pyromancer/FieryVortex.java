package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class FieryVortex extends StaticProjectile {

	private static final int burnPower = 1;
	private static final float dragSpeed = 0.5f; // The speed at which entities are pulled into the vortex.
	private static final float radius = 5;
	private static final float height = 1;

	private Array<Integer> hitEntities;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public FieryVortex() { }

	public FieryVortex(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Fiery Vortex";
		hitEntities = new Array<>();

		this.pos.add(0, height/2, 0);
//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(radius, height/2, radius)));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 1) == 0) {
			for (int i = 0; i < 3; i ++) {
				Vector3 vector = new Vector3();
				// We add 0.1 to the y coord to make sure the particles spawn above ground level.
				Vector3 particlePos = pos.cpy().add(vector.setToRandomDirection().scl(radius).set(vector.x, 0, vector.z));
				playScreen.particleEngine.addFlyUpPoint(playScreen.physicsManager.getDynamicsWorld(), particlePos, 1, 1,
						1, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
			}
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (!hitEntities.contains(entity.id, true) && entity.id != owner) {
			offender.burn(entity, burnPower, lifetime);
			offender.landAbility(entity, playScreen);
			hitEntities.add(entity.id);
		}

		if (entity.id != owner) {
			Vector3 movementChange = pos.cpy().sub(entity.pos).nor().scl(dragSpeed * Gdx.graphics.getDeltaTime());
			entity.rigidBody.translate(movementChange);
			return true;
//		entity.addAdditionalMovementVector(pos.cpy().sub(entity.pos).nor().scl(dragSpeed));
//		entity.addMovementVector(pos.cpy().sub(entity.pos).nor().scl(dragSpeed)); // Move the entity towards the center of the vortex.
		} else {
			return false;
		}
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
