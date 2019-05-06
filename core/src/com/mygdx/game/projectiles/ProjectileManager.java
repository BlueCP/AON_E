package com.mygdx.game.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ProjectileManager implements Disposable {

	Array<Integer> idPool = new Array<>();
	
	public Array<Projectile> projectiles;

	// This is a list of projectiles which will be added next frame.
	public Array<Projectile> futureProjectiles;
	
	public ProjectileManager() {
		projectiles = new Array<>();
		futureProjectiles = new Array<>();
	}
	
	public void update(float delta, PlayScreen playScreen) {
		for (Projectile projectile: futureProjectiles) {
			projectile.loadPhysicsObject();
			projectile.addToDynamicsWorld(playScreen.physicsManager.getDynamicsWorld(), PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
			projectiles.add(projectile);
		}
		futureProjectiles.clear();

		for (int i = 0; i < projectiles.size; i ++) {
			Projectile projectile = projectiles.get(i);

			projectile.universalUpdate(delta);

			if (projectile.lifetime <= -1) { // If the projectile was to die immediately
				projectile.lifetime = 0; // Give it a chance to do its thing
				projectile.update(delta, playScreen);
			} else if (projectiles.get(i).getLifetime() <= 0) {
				projectile.destroy(playScreen);
//				i --;
			} else {
				projectile.update(delta, playScreen);
			}

			if (projectile.isDestroyed) {
				i --;
			}
		}
	}
	
	public void saveAndExit(String dir) {
		try {
			/*for (Projectile projectile: projectiles) {
				projectile.prepareForSaveAndExit();
			}*/
			
			KryoManager.write(this, "saves/" + dir + "/projectiles.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(String dir) {
		/*Array<btCollisionObject> colObjs = new Array<>();
		Array<btRigidBody> rigidBodies = new Array<>();

		for (Projectile projectile: projectiles) {
			if (projectile instanceof DynamicProjectile) {
				rigidBodies.add(((DynamicProjectile) projectile).prepareForSave());
			} else if (projectile instanceof StaticProjectile) {
				colObjs.add(((StaticProjectile) projectile).prepareForSave());
			}
		}*/

		try {
			KryoManager.write(this, "saves/" + dir + "/projectiles.txt");
			/*int dynamicCounter = 0;
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
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(String name, Kryo kryo) {
		try {
			Output output = new Output(new FileOutputStream(Gdx.files.getLocalStoragePath() + "/saves/" + name + "/projectiles.txt"));
			kryo.writeObject(output, this);
			output.close();
		} catch (FileNotFoundException e) {
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

	public void addProjectileNow(Projectile projectile, btDynamicsWorld dynamicsWorld) {
		projectile.generateId(this);
		projectile.loadPhysicsObject();
		projectile.addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
		projectiles.add(projectile);
	}

	public void addProjectileInFuture(Projectile projectile) {
		projectile.generateId(this);
		futureProjectiles.add(projectile);
	}

	/*public void addFirebolt(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new Firebolt(entity, pos, targetPos, lifetime));
	}
	
	public void addFireball(Entity entity, PlayScreen playScreen, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new Fireball(entity, pos, targetPos, lifetime));
	}

	public void addIncendiaryTrap(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new IncendiaryTrap(entity, pos, lifetime));
	}

	public void addIncendiaryTrapExplosion(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new IncendiaryTrapExplosion(entity, pos));
	}

	public void addFieryVortex(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new FieryVortex(entity, pos, lifetime));
	}

	public void addHeatwave(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new Heatwave(entity, pos));
	}

	public void addBurningBarrier(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new BurningBarrier(entity, pos, lifetime));
	}

	public void addFlamethrower(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float rotation) {
		projectiles.add(new Flamethrower(entity, pos, rotation));
	}

	public void addLavaSnare(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime, Entity targetEntity) {
		projectiles.add(new LavaSnare(entity, pos, lifetime, targetEntity));
	}

	public void addSupernovaExplosion(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new SupernovaExplosion(entity, pos));
	}

	public void addFrostbolt(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new Frostbolt(entity, pos, targetPos, lifetime));
	}

	public void addIceShard(Entity entity, PlayScreen playScreen, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new IceShard(entity, pos, targetPos, lifetime));
	}

	public void addBlizzard(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new Blizzard(entity, pos, lifetime));
	}

	public void addDefrostingArea(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos) {
		projectiles.add(new DefrostingArea(entity, pos));
	}

	public void addHailstorm(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new Hailstorm(entity, pos, lifetime));
	}

	public void addCryosleep(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new Cryosleep(entity, pos, lifetime));
	}

	public void addShatterExplosion(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, int stacks) {
		projectiles.add(new ShatterExplosion(entity, pos, stacks));
	}

	public void addGlacialWall(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new GlacialWall(entity, pos, lifetime));
	}

	public void addElectroball(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, Vector3 targetPos, float lifetime) {
		projectiles.add(new Electroball(entity, pos, targetPos, lifetime));
	}

	public void addThunderstrike(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime, Entity targetEntity) {
		projectiles.add(new Thunderstrike(entity, pos, lifetime, targetEntity));
	}

	public void addStormcaller(Entity entity, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		projectiles.add(new Stormcaller(entity, pos, lifetime));
	}*/
	
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
