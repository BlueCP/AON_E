package com.mygdx.game.projectiles.warrior;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;
import javafx.util.Pair;

public class WarriorSlash extends StaticProjectile {

	private static final int damage = 2;
	private static final int spiritGain = 5; // The spirit gained by landing this on an enemy.

	private int targetEntity;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private WarriorSlash() { }

	public WarriorSlash(Entity entity, Vector3 pos, Entity target) {
		super(entity, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Warrior slash";

		targetEntity = target.id;
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btSphereShape(1)); // Collision shape is irrelevant as collision is calculated using ray picking.
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		Entity target = playScreen.entities.getEntity(targetEntity, playScreen.player);
		if (offender.id != -1) {
			Pair<Integer, Vector3> pair = playScreen.physicsManager.rayTestFirst(offender.pos, target.pos, PhysicsManager.CONST_OBJ_FLAG | PhysicsManager.HITBOX_FLAG | PhysicsManager.PROJECTILE_FLAG);
			if (target.physicsId == pair.getKey()) { // If the first object in the way is the target entity
				offender.changeSpirit(spiritGain);
				offender.landBasicAttack(target, playScreen);
				offender.dealBasicAttackDamage(target, damage + offender.getRealDamage(), playScreen);
			}
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

}
