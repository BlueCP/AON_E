package com.mygdx.game.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.cryomancer.Frostbolt;
import com.mygdx.game.projectiles.cryomancer.IceShard;
import com.mygdx.game.projectiles.pyromancer.*;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;

public class ProjectileManager {

	Array<Integer> idPool = new Array<>();
	
	public Array<Projectile> projectiles;

	// This is a list of projectiles which will be added next frame.
	public Array<Projectile> futureProjectiles;
	
	public ProjectileManager() {
		projectiles = new Array<>();
		futureProjectiles = new Array<>();
	}

	public void removeProjectile(Projectile projectile, btDynamicsWorld dynamicsWorld) {

	}
	
	public void update(float delta, PlayScreen playScreen) {
		for (Projectile projectile: futureProjectiles) {
			projectiles.add(projectile);
		}
		futureProjectiles.clear();

		for (int i = 0; i < projectiles.size; i ++) {
			Projectile projectile = projectiles.get(i);

			projectile.universalUpdate(delta);

			/*if (projectile.hitEntity != -1) {
				if (projectile.onHitEntity(playScreen.entities.getEntity(projectile.hitEntity), playScreen)) {
					// If the projectile was destroyed (onHitEntity returns true if destroyed), decrement the counter to adjust.
					i--;
				}
			}*/
			if (projectile.lifetime <= -1) { // If the projectile was to die immediately
				projectile.lifetime = 0; // Give it a chance to do its thing
				projectile.update(delta, playScreen);
			} else if (projectiles.get(i).getLifetime() <= 0) {
				projectile.destroy(playScreen.physicsManager.getDynamicsWorld(), this);
				i --;
			} else {
				projectile.update(delta, playScreen);
			}
		}
	}
	
	public void saveAndExit(String dir) {
		try {
			for (Projectile projectile: projectiles) {
				projectile.prepareForSaveAndExit();
			}
			
			KryoManager.write(this, "saves/" + dir + "/projectiles.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(String dir) {
		Array<btCollisionObject> colObjs = new Array<>();
		Array<btRigidBody> rigidBodies = new Array<>();

		for (Projectile projectile: projectiles) {
			if (projectile instanceof DynamicProjectile) {
				rigidBodies.add(((DynamicProjectile) projectile).prepareForSave());
			} else if (projectile instanceof StaticProjectile) {
				colObjs.add(((StaticProjectile) projectile).prepareForSave());
			}
		}

		try {
			KryoManager.write(this, "saves/" + dir + "/projectiles.txt");
			int dynamicCounter = 0;
			int staticCounter = 0;
			for (int i = 0; i < projectiles.size; i ++) {
				Projectile projectile = projectiles.get(i);
				if (projectile instanceof DynamicProjectile) {
					((DynamicProjectile) projectile).rigidBody = rigidBodies.get(dynamicCounter);
					projectile.shape = rigidBodies.get(dynamicCounter).getCollisionShape();
					dynamicCounter ++;
				} else if (projectile instanceof StaticProjectile) {
					((StaticProjectile) projectile).collisionObject = colObjs.get(staticCounter);
					projectile.shape = colObjs.get(staticCounter).getCollisionShape();
					staticCounter ++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ProjectileManager load(String dir) {
		try {
			ProjectileManager projectileManager = KryoManager.read("saves/" + dir + "/projectiles.txt", ProjectileManager.class);
			
			for (Projectile projectile: projectileManager.projectiles) {
				projectile.processAfterLoading();
			}
			return projectileManager;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Projectile get(int id) {
		for (Projectile projectile: projectiles) {
			if (projectile.getId() == id) {
				return projectile;
			}
		}
		return null;
	}

	public void addFirebolt(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new Firebolt(entity, this, dynamicsWorld, pos, targetPos, lifetime));
	}
	
	public void addFireball(Entity entity, PlayScreen playScreen, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new Fireball(entity, playScreen, pos, targetPos, lifetime));
	}

	public void addIncendiaryTrap(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new IncendiaryTrap(entity, this, dynamicsWorld, pos, lifetime));
	}

	public void addIncendiaryTrapExplosion(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new IncendiaryTrapExplosion(entity, this, dynamicsWorld, pos));
	}

	public void addFieryVortex(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new FieryVortex(entity, this, dynamicsWorld, pos, lifetime));
	}

	public void addHeatwave(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new Heatwave(entity, this, dynamicsWorld, pos));
	}

	public void addBurningBarrier(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new BurningBarrier(entity, this, dynamicsWorld, pos, lifetime));
	}

	public void addFlamethrower(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float rotation) {
		projectiles.add(new Flamethrower(entity, this, dynamicsWorld, pos, rotation));
	}

	public void addLavaSnare(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime, Entity targetEntity) {
		projectiles.add(new LavaSnare(entity, this, dynamicsWorld, pos, lifetime, targetEntity));
	}

	public void addSupernovaExplosion(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new SupernovaExplosion(entity, this, dynamicsWorld, pos));
	}

	public void addFrostbolt(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new Frostbolt(entity, this, dynamicsWorld, pos, targetPos, lifetime));
	}

	public void addIceShard(Entity entity, PlayScreen playScreen, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new IceShard(entity, playScreen, pos, targetPos, lifetime));
	}
	
	public void addRazeZone(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new RazeZone(entity, this, dynamicsWorld, pos, lifetime));
	}
	
	public void addImmolateZone(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new ImmolateZone(entity, this, dynamicsWorld, pos));
	}
	
	public void dispose() {
		for (Projectile projectile: projectiles) {
			projectile.getCollisionObject().dispose();
		}
	}
	
}
