package com.mygdx.game.projectiles.necromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.DynamicProjectile;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.screens.PlayScreen;

public class DeathBolt extends DynamicProjectile {

	private static final float speed = 5; // m/s
	private static final int spiritGain = 5; // The spirit gained by landing this on an enemy.
	private static final int damage = 1;

	private Vector3 targetPos;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private DeathBolt() { }

	public DeathBolt(Entity entity, Vector3 pos, Vector3 targetPos, float lifetime) {
		super(entity, ProjectileSprite.NO_SPRITE, pos, lifetime);

		name = "Death Bolt";

		this.targetPos = targetPos.cpy();
	}

	protected void loadPhysicsObject() {
		defaultLoadRigidBody(new btSphereShape(0.1f));
		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		calcLinearProjectileMotion(targetPos, speed);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		universalUpdate();

		for (int i = 0; i < 5; i ++) {
			playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), pos.cpy().add(new Vector3().setToRandomDirection().scl(0.05f)), -1f, Particle.Sprite.FIRE, Particle.Behaviour.OSCILLATE_DOWN);
		}

		if (Math.floorMod(ticksPast, 5) == 0) {
			playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), pos, 1f, Particle.Sprite.FIRE, Particle.Behaviour.OSCILLATE_DOWN);
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner) {
			offender.changeSpirit(spiritGain);
//			float finalDamage = entity.dealtDamageBy(offender, damage + offender.getRealDamage());
//			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landBasicAttack(entity, playScreen);
			offender.dealBasicAttackDamage(entity, damage + offender.getRealDamage(), playScreen);

			destroy(playScreen);

			playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 2, 2,
					Particle.Sprite.FIRE, Particle.Behaviour.POOF);

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
