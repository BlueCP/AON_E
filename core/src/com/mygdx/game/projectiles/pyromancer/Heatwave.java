package com.mygdx.game.projectiles.pyromancer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.statuseffects.Effect;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class Heatwave extends StaticProjectile {

	private static final int damage = 2;
	private static final int slowDuration = 3;
	private static final int slowPower = 1;
	private static final float radius = 5;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public Heatwave() { }

	public Heatwave(Entity entity, ProjectileManager projectileEngine, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		super(entity, projectileEngine, ProjectileSprite.FIREBOLT, pos, -1);

		name = "Heatwave";

		loadPhysicsObject();
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btCylinderShape(new Vector3(radius, 1, radius)));

		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
//		particleEngine.addFireWave(dynamicsWorld, pos, 30, 5, Particle.Behaviour.POOF);

		playScreen.particleEngine.addWave(playScreen.physicsManager.getDynamicsWorld(), pos, 30, 5, 2, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);

		playScreen.isoRenderer.camera.screenShake(0.4f, 0.4f);
	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		if (entity.id != owner) {
			entity.dealtDamageBy(playScreen.entities.getEntity(owner, playScreen.player), damage, true);
			playScreen.entities.getEntity(owner, playScreen.player).landAbility(entity);
//		entity.dealDamageOLD(owner, playScreen.player, damage);
//		playScreen.player.flamingBarrage.testfor(owner);
			entity.slowedEffect.add(slowPower, slowDuration);
//		playScreen.player.vikingFuneral.testfor(owner, entity);
//		entity.changeEffect(Effect.EffectType.SLOWED, slowDuration, slowPower);
		}

		return true;
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

}
