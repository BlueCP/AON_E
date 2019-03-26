package com.mygdx.game.projectiles.electromancer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.screens.PlayScreen;

public class PowerGridConductor extends StaticProjectile {

	private static final Vector3 halfExtents = new Vector3(0.5f, 1f, 0.5f);
	private static final float range = 5; // Beyond this range, conductors will not connect together.

//	private boolean initialised = false;
	private Array<Projectile> connectedConductors; // The other conductors this conductor is connected to.
	private Array<Projectile> energyBeams; // The beams of energy that this conductor has created between itself and other conductors.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public PowerGridConductor() { }

	public PowerGridConductor(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.FIREBOLT, pos, lifetime);

		name = "Power Grid Conductor";

		this.pos.add(0, halfExtents.y, 0);

		connectedConductors = new Array<>();
		energyBeams = new Array<>();
	}

	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btBoxShape(halfExtents));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		outerLoop:
		for (int i = 0; i < playScreen.projectileManager.projectiles.size; i++) {
			Projectile projectile = playScreen.projectileManager.projectiles.get(i);
			if (projectile.name.equals("Power Grid Conductor") && projectile.getOwner() == owner && projectile.id != id && projectile.pos.dst(pos) <= range) {
				Vector3 midPoint = pos.cpy().lerp(projectile.pos, 0.5f);
				for (int j = 0; j < playScreen.projectileManager.projectiles.size; j++) {
					Projectile projectile1 = playScreen.projectileManager.projectiles.get(j);
					if (projectile1.name.equals("Power Grid Beam") && projectile1.pos.dst(midPoint) < 0.01f) {
						continue outerLoop;
					}
				}
				Projectile beam = new PowerGridBeam(playScreen.entities.getEntity(owner, playScreen.player), pos, projectile.pos,
						this, projectile, lifetime, 2);
				playScreen.projectileManager.addProjectileNow(beam, playScreen.physicsManager.getDynamicsWorld());
				connectedConductors.add(projectile);
				energyBeams.add(beam);
			}
		}

		if (Math.floorMod(ticksPast, 3) == 0) {
			Vector3 particlePos = pos.cpy().add(new Vector3(MathUtils.random(-halfExtents.x, halfExtents.x),
					MathUtils.random(-halfExtents.y, halfExtents.y), MathUtils.random(-halfExtents.z, halfExtents.z)));
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
	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		super.addToDynamicsWorld(dynamicsWorld, group, mask);
	}

}
