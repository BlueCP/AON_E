package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class IncendiaryTrap extends StaticProjectile {

	private static final Vector3 halfExtents = new Vector3(0.5f, 1f, 0.5f);

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public IncendiaryTrap() { }

	public IncendiaryTrap(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Incendiary Trap";

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btBoxShape(halfExtents));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 6) == 0) {
			playScreen.particleEngine.addFlyUpPoint(playScreen.physicsManager.getDynamicsWorld(), pos, 1, 5, 1.5f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		return false;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		// If the hit projectile is associated with fire, destroy both this trap and the projectile that triggered it.
		if (projectile.name.equals("Fireball") || projectile.name.equals("Firebolt")) {
			destroy(playScreen.physicsManager.getDynamicsWorld(), playScreen.projectileManager);
			projectile.destroy(playScreen.physicsManager.getDynamicsWorld(), playScreen.projectileManager);

			playScreen.projectileManager.addProjectileInFuture(new IncendiaryTrapExplosion(playScreen.entities.getEntity(owner, playScreen.player), pos));

			playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 4, 2,
												Particle.Sprite.FIRE, Particle.Behaviour.POOF);
			playScreen.isoRenderer.camera.screenShake(0.4f, 0.4f);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		super.addToDynamicsWorld(dynamicsWorld, group, mask);
	}

}
