package com.mygdx.game.projectiles.paladin;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class DetainArea extends StaticProjectile {

	private static final float baseDamage = 2;
	private static final float rootDuration = 2;

	private int targetEntity;
	private boolean alreadyHit = false;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public DetainArea() { }

	public DetainArea(Entity entity, Vector3 pos, float lifetime, Entity targetEntity) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Detain area";
		this.targetEntity = targetEntity.id;
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btSphereShape(1f));
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
			float finalDamage = offender.dealDamage(target, baseDamage + offender.getRealDamage());
			offender.landAbility(target, playScreen);
			offender.landAbilityDamage(target, finalDamage, playScreen);
			target.rootedEffect.add(rootDuration);
			alreadyHit = true;
			playScreen.particleEngine.addFlyUpPoint(playScreen.physicsManager.getDynamicsWorld(), pos, 10, 7, 1.5f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
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
