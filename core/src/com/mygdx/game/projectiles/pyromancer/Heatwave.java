package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class Heatwave extends StaticProjectile {

	private static final int damage = 2;
	private static final int slowDuration = 3;
	private static final int slowPower = 1;
	private static final float radius = 5;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public Heatwave() { }

	public Heatwave(Entity entity, Vector3 pos) {
		super(entity, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Heatwave";

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(radius, 1, radius)));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		playScreen.particleEngine.addWave(playScreen.physicsManager.getDynamicsWorld(), pos, 30, 5, 2, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);

		playScreen.isoRenderer.camera.screenShake(0.4f, 0.4f);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner) {
			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.landAbilityDamage(entity, finalDamage, playScreen);
			entity.slowedEffect.add(slowPower, slowDuration);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

}
