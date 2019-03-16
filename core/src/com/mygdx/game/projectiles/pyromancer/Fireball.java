package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.particles.Particle.Behaviour;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.DynamicProjectile;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.screens.PlayScreen;

public class Fireball extends DynamicProjectile {

	private static final float speed = 5; // m/s
	private static final int damage = 1;

	private long fireballTravelSoundId;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public Fireball() { }
	
	public Fireball(Entity entity, PlayScreen playScreen, Vector3 pos, Vector3 targetPos, float lifetime) {
		super(entity, playScreen.projectileManager, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Fireball";
		
//		this.targetPos = targetPos.cpy();

		loadPhysicsObject();
		calcLinearProjectileMotion(targetPos, speed);
		addToDynamicsWorld(playScreen.physicsManager.getDynamicsWorld(), PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}
	
	protected void loadPhysicsObject() {
		defaultLoadRigidBody(new btSphereShape(0.1f));

		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | CollisionFlags.CF_NO_CONTACT_RESPONSE);

		fireballTravelSoundId = -1;
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		universalUpdate();

		if (fireballTravelSoundId == -1) {
			fireballTravelSoundId = playScreen.game.soundManager.fireballTravel.play(0.7f, playScreen.isoRenderer.camera.pos, pos);
		}
		
		if (Math.floorMod(ticksPast, 15) == 0) {
			playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), pos, 1f, Particle.Sprite.FIRE, Behaviour.OSCILLATE_DOWN);
		}

		playScreen.game.soundManager.fireballTravel.updateVolumeAndPan(fireballTravelSoundId, playScreen.isoRenderer.camera.pos, pos);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner) {
			entity.dealtDamageBy(offender, damage + offender.equipped().getWeapon().getMagDamage());
			offender.landAbility(entity, playScreen);

			destroy(playScreen.physicsManager.getDynamicsWorld(), playScreen.projectileManager);

			playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 3, 2,
					Particle.Sprite.FIRE, Behaviour.POOF);
			playScreen.isoRenderer.camera.screenShake(0.2f, 0.2f);

			playScreen.game.soundManager.fireballTravel.stop(fireballTravelSoundId);
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
