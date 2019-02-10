package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.VideoSettings;

public class SupernovaExplosion extends StaticProjectile {

	private static final int damage = 3;
	private static final float radius = 2;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SupernovaExplosion() { }

	public SupernovaExplosion(Entity entity, ProjectileManager projectileEngine, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		super(entity, projectileEngine, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Supernova Explosion";

		loadPhysicsObject();
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btSphereShape(radius));

		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		playScreen.particleEngine.addBurst(playScreen.physicsManager.getDynamicsWorld(), pos, 20, 5, 1,
											Particle.Sprite.FIRE, Particle.Behaviour.POOF);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		if (entity.id != owner) {
			entity.dealtDamageBy(playScreen.entities.getEntity(owner, playScreen.player), damage, true);
			playScreen.entities.getEntity(owner, playScreen.player).landAbility(entity);
		}

		return true;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

}
