package com.mygdx.game.projectiles.cryomancer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class Hailstorm extends StaticProjectile {

	private static final int dps = 1;
	private static final float radius = 5;
	private static final float height = 10;

	private Array<Integer> hitEntities;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public Hailstorm() { }

	public Hailstorm(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Hailstorm";
		hitEntities = new Array<>();

		this.pos.add(0, height/2, 0);
//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 1) == 0) {
			for (int i = 0; i < 2; i ++) {
				Vector3 vector = new Vector3();
				Vector3 particlePos = pos.cpy().add(vector.setToRandomDirection().scl(radius).add(0, 10, 0));
				playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), particlePos,
						new Vector3(-1, -8, 0), 3, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
			}
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (!hitEntities.contains(entity.id, true) && entity.id != owner) {
			offender.landAbility(entity, playScreen);
			hitEntities.add(entity.id);
		}

		if (entity.id != owner) {
			entity.dealtDamageBy(offender, dps * Gdx.graphics.getDeltaTime() + offender.equipped().getWeapon().getMagDamage());
			offender.landAbilityDamage(entity, dps * Gdx.graphics.getDeltaTime() + offender.equipped().getWeapon().getMagDamage(), playScreen);
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
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(radius, height/2, radius)));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

}
