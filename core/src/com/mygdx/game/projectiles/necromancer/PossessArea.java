package com.mygdx.game.projectiles.necromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.DynamicProjectile;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.screens.PlayScreen;

public class PossessArea extends DynamicProjectile {

	private static final int witherPower = 1;
	private static final float soulRegen = 2;

	private int possessedEntity;
	private boolean effectsApplied = false; // Whether or not the effects of this spell have been applied (i.e. has the first tick passed or not).
	private int witherEffectId; // The id of the wither effect on the defending entity.
	private int soulsRegenId; // The id of the souls regen effect on the offending entity.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private PossessArea() { }

	public PossessArea(Entity entity, Entity possessed, float lifetime) {
		super(entity, ProjectileSprite.NO_SPRITE, possessed.pos, lifetime);

		possessedEntity = possessed.id;

		name = "Possess area";
	}

	protected void loadPhysicsObject() {
		defaultLoadRigidBody(new btSphereShape(1f));
		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		Entity defender = playScreen.entities.getEntity(possessedEntity, playScreen.player);
		rigidBody.setWorldTransform(rigidBody.getWorldTransform().setTranslation(defender.pos)); // Follow the entity that created this projectile.

		universalUpdate();

		if (!effectsApplied) {
			soulsRegenId = offender.soulsRegenEffect.add(soulRegen, Integer.MAX_VALUE); // If you want to know why I used MAX_VALUE, look at the ProcEffectCont class.
			witherEffectId = defender.witherEffect.add(witherPower, Integer.MAX_VALUE);
			effectsApplied = true;
		}
		if (defender.id == -1) { // If the original possessed entity has died (so defender is just a NullEntity).
			destroy(playScreen); // Destroy this projectile - 'end the possession'.
		}

		if (Math.floorMod(ticksPast, 3) == 0) {
			Vector3 particlePos = pos.cpy().add(new Vector3().setToRandomDirection());
			playScreen.particleEngine.addFloatingParticles(playScreen.physicsManager.getDynamicsWorld(), particlePos, 1, 1, 2, Particle.Sprite.FIRE);
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
	public void destroy(PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		Entity defender = playScreen.entities.getEntity(possessedEntity, playScreen.player);

		offender.soulsRegenEffect.remove(soulsRegenId);
		defender.witherEffect.remove(witherEffectId);

		super.destroy(playScreen);
	}

	@Override
	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		super.addToDynamicsWorld(dynamicsWorld, group, mask);

		rigidBody.setGravity(Vector3.Zero);
	}

}
