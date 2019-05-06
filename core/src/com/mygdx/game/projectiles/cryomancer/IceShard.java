package com.mygdx.game.projectiles.cryomancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.DynamicProjectile;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.screens.PlayScreen;

public class IceShard extends DynamicProjectile {

	private static final float speed = 5; // m/s
	private static final int damage = 1;

	private long iceShardTravelSoundId;

	private Vector3 targetPos;

	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public IceShard() { }

	public IceShard(Entity entity, Vector3 pos, Vector3 targetPos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Ice Shard";

		this.targetPos = targetPos.cpy();

//		loadPhysicsObject();
//		addToDynamicsWorld(playScreen.physicsManager.getDynamicsWorld(), PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	protected void loadPhysicsObject() {
		defaultLoadRigidBody(new btSphereShape(0.1f));
		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		calcLinearProjectileMotion(targetPos, speed);

		iceShardTravelSoundId = -1;
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		universalUpdate();

		if (iceShardTravelSoundId == -1) {
			iceShardTravelSoundId = playScreen.game.soundManager.fireballTravel.play(0.7f, playScreen.isoRenderer.camera.pos, pos);
		}

		if (Math.floorMod(ticksPast, 15) == 0) {
			playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), pos, 1f, Particle.Sprite.FIRE, Particle.Behaviour.OSCILLATE_DOWN);
		}

		playScreen.game.soundManager.fireballTravel.updateVolumeAndPan(iceShardTravelSoundId, playScreen.isoRenderer.camera.pos, pos);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner) {
			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.landAbilityDamage(entity, finalDamage, playScreen);

			destroy(playScreen);

			playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 3, 2,
					Particle.Sprite.FIRE, Particle.Behaviour.POOF);
			playScreen.isoRenderer.camera.screenShake(0.2f, 0.2f);

			playScreen.game.soundManager.fireballTravel.stop(iceShardTravelSoundId);
			playScreen.game.soundManager.fireballExplosion.play(playScreen.isoRenderer.camera.pos, entity.pos);
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
