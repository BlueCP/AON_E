package com.mygdx.game.projectiles.cryomancer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class Cryosleep extends StaticProjectile {

	private static final Vector3 halfExtents = new Vector3(0.5f, 1f, 0.5f);
	private static final float healthRegen = 5;
	private static final float spiritRegen = 5;

	private boolean alreadyHit = false;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private Cryosleep() { }

	public Cryosleep(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Cryosleep";

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		Entity entity = playScreen.entities.getEntity(owner, playScreen.player);

		if (entity.id == -1) {
			destroy(playScreen);
		} else if (!alreadyHit) {
			entity.frozenEffect.add(lifetime);
			alreadyHit = true;
		}

		entity.changeLife(healthRegen * Gdx.graphics.getDeltaTime());
		entity.changeSpirit(spiritRegen * Gdx.graphics.getDeltaTime());

		if (Math.floorMod(ticksPast, 6) == 0) {
			playScreen.particleEngine.addFlyUpPoint(playScreen.physicsManager.getDynamicsWorld(), pos, 1, 7, 1.5f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
		}
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
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btBoxShape(halfExtents));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

}
