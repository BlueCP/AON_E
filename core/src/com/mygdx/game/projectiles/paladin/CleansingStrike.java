package com.mygdx.game.projectiles.paladin;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class CleansingStrike extends StaticProjectile {

	private static final float baseDamage = 1;

	private int targetEntity;
	private float range;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public CleansingStrike() { }

	public CleansingStrike(Entity entity, Vector3 pos, Entity targetEntity) {
		super(entity, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Cleansing Strike";
		this.targetEntity = targetEntity.id;

		range = entity.equipped().getWeapon().getRange();
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btSphereShape(range));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);

		if (entity.id == targetEntity) {
			float finalDamage = offender.dealDamage(entity, baseDamage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.landAbilityDamage(entity, finalDamage, playScreen);
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
