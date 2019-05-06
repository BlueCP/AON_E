package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class Flamethrower extends StaticProjectile {

	private static final float damage = 2;
	private static final float range = 5;

	private float rotation; // Anticlockwise rotation

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public Flamethrower() { }

	public Flamethrower(Entity entity, Vector3 pos, float rotation) {
		super(entity, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Flamethrower";
		this.rotation = rotation;

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(range, 1, range))); // TODO: Ask Will to make a model for a conical flamethrower shape.

		collisionObject.setWorldTransform(collisionObject.getWorldTransform().rotate(Vector3.Y, -rotation));

		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);

		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		playScreen.particleEngine.addDirectionalWave(playScreen.physicsManager.getDynamicsWorld(), pos, 30, 5, 1, rotation,90, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);

		playScreen.isoRenderer.camera.screenShake(0.4f, 0.4f);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner) {
			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.landAbilityDamage(entity, finalDamage, playScreen);
		}
		return true;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

}
