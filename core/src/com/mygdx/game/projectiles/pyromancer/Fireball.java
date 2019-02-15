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
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.screens.PlayScreen;

public class Fireball extends DynamicProjectile {

	private static final float speed = 5; // m/s
	private static final int spiritGain = 5; // The spirit gained by landing this on an enemy.
	private static final int damage = 1;
	
//	private btCollisionShape shape;
//	private btRigidBody rigidBody;
//	private Vector3 targetPos;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public Fireball() { }
	
	public Fireball(Entity entity, ProjectileManager projectileEngine, btDynamicsWorld dynamicsWorld, Vector3 pos, Vector3 targetPos, float lifetime) {
		super(entity, projectileEngine, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Fireball";
		
//		this.targetPos = targetPos.cpy();

		loadPhysicsObject();
		calcLinearProjectileMotion(targetPos, speed);
		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}
	
	protected void loadPhysicsObject() {
		defaultLoadRigidBody(new btSphereShape(0.1f));

		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		universalUpdate();
		
		if (Math.floorMod(ticksPast, 15) == 0) {
//			particleEngine.addFireParticle(dynamicsWorld, pos);
			playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), pos, 1f, Particle.Sprite.FIRE, Behaviour.OSCILLATE_DOWN);
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		/*if (owner == 0) {
			playScreen.player.changeSpirit(spiritGain); // Gaining spirit from landing a successful attack
		} else {
			playScreen.entities.getEntity(owner).changeSpirit(spiritGain);
		}*/

		if (entity.id != owner) {
			entity.dealtDamageBy(playScreen.entities.getEntity(owner, playScreen.player), damage, true);
			playScreen.entities.getEntity(owner, playScreen.player).landAbility(entity);
//		entity.dealDamageOLD(owner, playScreen.player, damage);
//		playScreen.player.flamingBarrage.testfor(owner);
//		playScreen.player.vikingFuneral.testfor(owner, entity);

			destroy(playScreen.physicsManager.getDynamicsWorld(), playScreen.projectileManager);

//		playScreen.particleEngine.addFireBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 3, Behaviour.POOF);
			playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 3, 2,
					Particle.Sprite.FIRE, Behaviour.POOF);
			playScreen.isoRenderer.camera.screenShake(0.2f, 0.2f);
			playScreen.game.soundManager.fireball.play(playScreen.isoRenderer.camera.pos, entity.pos);
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
