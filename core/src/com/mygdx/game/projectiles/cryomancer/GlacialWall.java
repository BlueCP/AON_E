package com.mygdx.game.projectiles.cryomancer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class GlacialWall extends StaticProjectile {

	private static final Vector3 halfExtents = new Vector3(3, 2, 0.5f);

	private Vector3 startPos;
	private float rotation; // The rotation around the y axis, anticlockwise.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public GlacialWall() { }

	public GlacialWall(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Glacial Wall";
		startPos = entity.pos.cpy();

		this.pos.add(0, halfExtents.y, 0);
//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 1) == 0) {
			Vector3 randomPos = new Vector3(MathUtils.random(-halfExtents.x, halfExtents.x), MathUtils.random(-halfExtents.y, halfExtents.y), MathUtils.random(-halfExtents.z, halfExtents.z)).rotate(Vector3.Y, -rotation).add(pos);
			playScreen.particleEngine.addFloatingParticles(playScreen.physicsManager.getDynamicsWorld(), randomPos,
					1, 0.1f, 2f, Particle.Sprite.FIRE);
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

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btBoxShape(halfExtents));

		Vector3 diff = pos.cpy().sub(startPos);
		rotation = (float) Math.toDegrees(MathUtils.atan2(diff.z, diff.x));

		rotation -= 90;
		if (rotation < 0) {
			rotation += 360;
		}

		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setToRotation(Vector3.Y, -rotation));

		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

}
