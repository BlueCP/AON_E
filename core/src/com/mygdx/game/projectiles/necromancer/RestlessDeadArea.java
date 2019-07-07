package com.mygdx.game.projectiles.necromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class RestlessDeadArea extends StaticProjectile {

	private static final int baseWitherPower = 1;
	private static final float radius = 5;
	private static final float height = 1;

	private int realWitherPower = baseWitherPower;
	private Array<Integer> hitEntities;

//	private Array<Integer> entitiesHitThisFrame;
//	private Array<Integer> entitiesHitLastFrame;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private RestlessDeadArea() { }

	public RestlessDeadArea(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Restless Dead area";
		hitEntities = new Array<>();
//		entitiesHitThisFrame = new Array<>();
//		entitiesHitLastFrame = new Array<>();

		this.pos.add(0, height/2, 0);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		/*for (int entity: entitiesHitThisFrame) {
			if (entitiesHitLastFrame.contains(entity, true)) {
			}
		}*/

		realWitherPower = baseWitherPower;
		realWitherPower *= playScreen.entities.getEntity(owner, playScreen.player).soulsEffect.numStacks() + 1;
		// The +1 is so that there is an effect when the entity has only one soul stack.

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
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (!hitEntities.contains(entity.id, true) && entity.id != owner) {
			offender.landAbility(entity, playScreen);
			hitEntities.add(entity.id);
		}
		if (entity.id != owner) {
			entity.witherEffect.addThisTick(realWitherPower);
		}
//		entitiesHitThisFrame.add(entity.id);
		return true;
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
