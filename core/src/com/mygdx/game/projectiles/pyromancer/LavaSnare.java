package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
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
	private LavaSnare() { }

	public LavaSnare(Entity entity, Vector3 pos, float lifetime, Entity targetEntity) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Lava Snare";
		this.targetEntity = targetEntity.id;

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btBoxShape(halfExtents));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		Entity target = playScreen.entities.getEntity(targetEntity, playScreen.player);

		if (target.id == -1) {
			destroy(playScreen);
		} else if (!alreadyHit) {
			playScreen.entities.getEntity(owner, playScreen.player).landAbility(target, playScreen);
			offender.burn(target, burnPower, lifetime);
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
