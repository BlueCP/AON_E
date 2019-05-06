package com.mygdx.game.projectiles.necromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class UnearthlyMiasmaArea extends StaticProjectile {

	private static final float damage = 2;
	private static final float slowDuration = 2;
	private static final int baseSlowPower = 1;

	private static final float height = 2;
	private static final float radius = 5;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public UnearthlyMiasmaArea() { }

	public UnearthlyMiasmaArea(Entity entity, Vector3 pos) {
		super(entity, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Unearthly Miasma area";

		this.pos.add(0, height/2, 0);
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(radius, height/2, radius)));
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
			offender.landAbility(entity, playScreen);
			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landAbilityDamage(entity, finalDamage, playScreen);
			entity.slowedEffect.add(baseSlowPower * offender.soulsEffect.numStacks(), slowDuration);
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
