package com.mygdx.game.projectiles.paladin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class EnforcementZone extends StaticProjectile {

	private static final float healPerSecond = 5;
	private static final float damageBuff = 0.5f;

	private static final float height = 2;
	private static final float radius = 5;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public EnforcementZone() { }

	public EnforcementZone(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Enforcement Zone";

		this.pos.add(0, height/2, 0);
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(radius, height/2, radius)));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 1) == 0) {
			for (int i = 0; i < 3; i ++) {
				Vector3 vector = new Vector3();
				Vector3 particlePos = pos.cpy().add(vector.setToRandomDirection().scl(radius).set(vector.x, 0, vector.z));
				playScreen.particleEngine.addFlyUpPoint(playScreen.physicsManager.getDynamicsWorld(), particlePos, 1, 1,
						1, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
			}
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		if (entity.id == owner) {
			entity.changeLife(healPerSecond * Gdx.graphics.getDeltaTime());
			entity.damageEffect.addThisTick(damageBuff);
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
