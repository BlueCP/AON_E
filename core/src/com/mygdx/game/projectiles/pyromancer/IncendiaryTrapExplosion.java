package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class IncendiaryTrapExplosion extends StaticProjectile {

	private static final int damage = 3;
	private static final float radius = 2;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private IncendiaryTrapExplosion() { }

	public IncendiaryTrapExplosion(Entity entity, Vector3 pos) {
		super(entity, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Incendiary Trap Explosion";

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btSphereShape(radius));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner) {
//			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.dealAbilityDamage(entity, damage + offender.getRealDamage(), playScreen);
		}

		return true;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

}
