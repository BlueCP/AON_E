package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class BurningBarrier extends StaticProjectile {

	private static final int damage = 3;
	private static final float stunDuration = 1;
	private static final Vector3 halfExtents = new Vector3(3, 2, 0.5f);

	private Vector3 startPos;
	private float rotation; // The rotation around the y axis, anticlockwise.

	private Array<Integer> hitEntities; // Entities which have been damaged by this barrier, so will not be damaged again.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BurningBarrier() { }

	public BurningBarrier(Entity entity, ProjectileManager projectileEngine, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		super(entity, projectileEngine, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Burning Barrier";
		startPos = entity.pos.cpy();
		hitEntities = new Array<>();

		this.pos.add(0, halfExtents.y, 0);
		loadPhysicsObject();
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btBoxShape(halfExtents));

		Vector3 diff = pos.cpy().sub(startPos);
		rotation = (float) Math.toDegrees(MathUtils.atan2(diff.z, diff.x));

		rotation -= 90;
		if (rotation < 0) {
			rotation += 360;
		}
//		System.out.println(rotation);
//		angle = (float) (Math.round(angle/22.5) * 22.5); // Round to nearest 22.5 degrees (which is 360/number of directions)
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setToRotation(Vector3.Y, -rotation));

		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 1) == 0) {
//			Vector3 randomX = pos.cpy().add(MathUtils.random(-3, 3), 0, 0).rot;
			Vector3 randomPos = new Vector3(MathUtils.random(-halfExtents.x, halfExtents.x), halfExtents.y, MathUtils.random(-halfExtents.z, halfExtents.z)).rotate(Vector3.Y, -rotation).add(pos);
			playScreen.particleEngine.addParticle(playScreen.physicsManager.getDynamicsWorld(), randomPos, 1.5f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
		}
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		if (!hitEntities.contains(entity.id, true) && entity.id != owner) {
			entity.dealtDamageBy(playScreen.entities.getEntity(owner, playScreen.player), damage);
			playScreen.entities.getEntity(owner, playScreen.player).landAbility(entity, playScreen);
//			entity.dealDamageOLD(owner, playScreen.player, 3);
//			playScreen.player.flamingBarrage.testfor(owner);
			entity.stunnedEffect.add(stunDuration);
//			playScreen.player.vikingFuneral.testfor(owner, entity);

			hitEntities.add(entity.id);
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
