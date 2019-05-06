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

public class Blizzard extends StaticProjectile {

	private static final float dps = 1;
	private static final float dragSpeed = 0.5f; // The speed at which entities are pulled into the vortex.
	private static final float radius = 5;
	private static final float height = 1;

	private Array<Integer> hitEntities;

	private Vector3 pushDirection;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public Blizzard() { }

	public Blizzard(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Blizzard";
		hitEntities = new Array<>();

		pushDirection = pos.cpy().sub(entity.pos).nor();

		this.pos.add(0, height/2, 0);

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 1) == 0) {
			for (int i = 0; i < 3; i ++) {
				Vector3 vector = new Vector3();
				Vector3 particlePos = pos.cpy().add(vector.setToRandomDirection().scl(5).set(vector.x, 0, vector.z));
				playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), particlePos, pushDirection.cpy().scl(3), 1, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
			}
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (!hitEntities.contains(entity.id, true) && entity.id != owner) {
			float finalDamage = offender.dealDamage(entity, dps * Gdx.graphics.getDeltaTime() + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.landAbilityDamage(entity, finalDamage, playScreen);
			hitEntities.add(entity.id);
		}

		if (entity.id != owner) {
			Vector3 movementChange = pushDirection.cpy().scl(dragSpeed * Gdx.graphics.getDeltaTime());
			entity.rigidBody.translate(movementChange);
			return true;
		}

		return false;
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
