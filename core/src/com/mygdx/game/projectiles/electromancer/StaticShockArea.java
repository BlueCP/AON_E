package com.mygdx.game.projectiles.electromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.DynamicProjectile;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.screens.PlayScreen;

public class StaticShockArea extends DynamicProjectile {

	private static final int damage = 1;
	private static final float stunDuration = 1f;

	private Array<Integer> hitEntities;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public StaticShockArea() { }

	public StaticShockArea(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.NO_SPRITE, pos, lifetime);

		name = "Static Shock area";

		hitEntities = new Array<>();
	}

	protected void loadPhysicsObject() {
		defaultLoadRigidBody(new btSphereShape(1f));
		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		Entity entity = playScreen.entities.getEntity(owner, playScreen.player);
		rigidBody.setWorldTransform(rigidBody.getWorldTransform().setTranslation(entity.pos)); // Follow the entity that created this projectile.

		universalUpdate();

		if (Math.floorMod(ticksPast, 3) == 0) {
			Vector3 particlePos = pos.cpy().add(new Vector3().setToRandomDirection());
			playScreen.particleEngine.addFloatingParticles(playScreen.physicsManager.getDynamicsWorld(), particlePos, 1, 1, 2, Particle.Sprite.FIRE);
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (!hitEntities.contains(entity.id, true) && entity.id != owner) {
			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
//			entity.stunnedEffect.add(stunDuration);
			offender.stun(entity, stunDuration);
			offender.landAbility(entity, playScreen);
			offender.landAbilityDamage(entity, finalDamage, playScreen);

			playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 3, 1.5f,
					Particle.Sprite.FIRE, Particle.Behaviour.POOF);

			hitEntities.add(entity.id);

		}

		return true;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

	@Override
	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		super.addToDynamicsWorld(dynamicsWorld, group, mask);

		rigidBody.setGravity(Vector3.Zero);
	}

}
