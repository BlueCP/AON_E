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

public class CracklingOrb extends DynamicProjectile {

	private static final float speed = 5; // m/s
	private static final int damage = 3;

	private long travelSoundId;

	private Vector3 targetPos;

	private Array<Integer> hitIds;

	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public CracklingOrb() { }

	public CracklingOrb(Entity entity, Vector3 pos, Vector3 targetPos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Crackling Orb";

		hitIds = new Array<>();

		this.targetPos = targetPos.cpy();
	}

	protected void loadPhysicsObject() {
		defaultLoadRigidBody(new btSphereShape(0.1f));
		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		calcLinearProjectileMotion(targetPos, speed);

		travelSoundId = -1;
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		universalUpdate();

		if (travelSoundId == -1) {
			travelSoundId = playScreen.game.soundManager.fireballTravel.play(0.7f, playScreen.isoRenderer.camera.pos, pos);
		}

		if (Math.floorMod(ticksPast, 15) == 0) {
			playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), pos, 1f, Particle.Sprite.FIRE, Particle.Behaviour.OSCILLATE_DOWN);
		}

		playScreen.game.soundManager.fireballTravel.updateVolumeAndPan(travelSoundId, playScreen.isoRenderer.camera.pos, pos);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner && !hitIds.contains(entity.id, true)) {
			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.landAbilityDamage(entity, finalDamage, playScreen);

			playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 3, 2,
					Particle.Sprite.FIRE, Particle.Behaviour.POOF);
			playScreen.isoRenderer.camera.screenShake(0.2f, 0.2f);

//			playScreen.game.soundManager.fireballTravel.stop(travelSoundId);
			playScreen.game.soundManager.fireballExplosion.play(playScreen.isoRenderer.camera.pos, entity.pos);

			hitIds.add(entity.id);
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
