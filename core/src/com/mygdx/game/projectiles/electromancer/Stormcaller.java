package com.mygdx.game.projectiles.electromancer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class Stormcaller extends StaticProjectile {

	private static final float radius = 5;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private Stormcaller() { }

	public Stormcaller(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Stormcaller";

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (Math.floorMod(ticksPast, 60) == 0) {
			Vector3 lightningBoltPos = pos.cpy().add(new Vector3(MathUtils.random(-1f, 1f), 0, MathUtils.random(-1f, 1f)).nor().scl(MathUtils.random(0, radius)));
			playScreen.projectileManager.addProjectileInFuture(new LightningBolt(playScreen.entities.getEntity(owner, playScreen.player),
					lightningBoltPos, 0.5f));

			playScreen.isoRenderer.camera.screenShake(0.3f, 0.3f);
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
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(radius, 0.1f, radius)));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

}
