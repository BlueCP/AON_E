package com.mygdx.game.projectiles.cryomancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class ShatterExplosion extends StaticProjectile {

	private static final int damagePerStack = 1;
	private static final float radius = 2;

	private float totalDamage;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private ShatterExplosion() { }

	public ShatterExplosion(Entity entity, Vector3 pos, int stacks) {
		super(entity, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Shatter";

		totalDamage = damagePerStack * stacks;

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 4, 1,
				Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
		playScreen.isoRenderer.camera.screenShake(0.2f, 0.2f);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner) {
//			float finalDamage = offender.dealDamage(entity, totalDamage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.dealAbilityDamage(entity, totalDamage + offender.getRealDamage(), playScreen);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btSphereShape(radius));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
	}

}
