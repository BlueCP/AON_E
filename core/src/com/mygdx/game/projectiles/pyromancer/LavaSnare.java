package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class LavaSnare extends StaticProjectile {

	private static final int burnPower = 1;
	private static final Vector3 halfExtents = new Vector3(0.5f, 1f, 0.5f);

	private int targetEntity;
	private boolean alreadyHit = false;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public LavaSnare() { }

	public LavaSnare(Entity entity, ProjectileManager projectileEngine, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime, Entity targetEntity) {
		super(entity, projectileEngine, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Lava Snare";
		this.targetEntity = targetEntity.id;

		loadPhysicsObject();
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btBoxShape(halfExtents));

		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		Entity target = playScreen.entities.getEntity(targetEntity, playScreen.player);

		if (target.id == -1) {
			destroy(playScreen.physicsManager.getDynamicsWorld(), playScreen.projectileManager);
		} else if (!alreadyHit) {
			playScreen.entities.getEntity(owner, playScreen.player).landAbility(target, playScreen);
			target.burnedBy(playScreen.entities.getEntity(owner, playScreen.player), burnPower, lifetime);
			target.rootedEffect.add(lifetime);
			alreadyHit = true;
		}

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

}
