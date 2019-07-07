package com.mygdx.game.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.particles.Particle.Behaviour;
import com.mygdx.game.particles.Particle.Sprite;
import com.mygdx.game.serialisation.KryoManager;
import com.mygdx.game.settings.VideoSettings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ParticleEngine {

	static final float fireChangeRate = 1f;
	
	Array<Integer> idPool = new Array<>();

	private transient Pool<Particle> particlePool;
	
	public Array<Particle> particles;
	private Array<ParticleNode> particleNodes;
	
	public ParticleEngine() {
		particles = new Array<>();
		particleNodes = new Array<>();

		particlePool = Pools.get(Particle.class);
	}

	public void removeParticle(Particle particle, btDynamicsWorld dynamicsWorld) {
		idPool.add(particle.id);
		dynamicsWorld.removeRigidBody(particle.rigidBody);
		particle.shape.dispose();
		particle.rigidBody.dispose();
		particles.removeValue(particle, false);
	}
	
	public void update(float delta, btDynamicsWorld dynamicsWorld) {
		if (!VideoSettings.isParticlesEnabled()) {
			particles.clear();
			return;
		}

		for (int i = 0; i < particles.size; i ++) {
			Particle particle = particles.get(i);
			particle.setLifetime(particle.getLifetime() - delta);
			particle.setLifeSoFar(particle.getLifeSoFar() + delta);
			particle.setStateTime(particle.getStateTime() + delta);

			if (particles.get(i).getLifetime() <= -1) { // Allow the particle to exist for 1 tick if it was initialised with a lifetime of -1.
				particle.setLifetime(0);
				particle.setLifeSoFar(0);
				particle.setStateTime(particle.getStateTime() + delta);
			} else if (particles.get(i).getLifetime() <= 0) {
				removeParticle(particle, dynamicsWorld);
				i --;
				continue;
			}
			
			switch (particle.sprite) {
				case FIRE:
					if (particle.getStateTime() >= 2) { // Because there are three sprites for fire particles
						particle.setStateTime(0);
					}
					break;
			}
			
			particle.update();
		}
		
		for (ParticleNode node: particleNodes) {
			node.update(dynamicsWorld, this, delta);
		}
	}

	public void save(String dir) {
		/*Array<btRigidBody> tempBodies = new Array<>();

		for (Particle particle: particles) {
			tempBodies.add(particle.prepareForSave());
		}

		Pool<Particle> tempParticlePool = particlePool;
		particlePool = null;*/

		try {
			KryoManager.write(this, "saves/" + dir + "/particles.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*for (int i = 0; i < tempBodies.size; i ++) {
			particles.get(i).rigidBody = tempBodies.get(i);
			particles.get(i).shape = tempBodies.get(i).getCollisionShape();
		}

		particlePool = tempParticlePool;*/
	}

	public void save(String name, Kryo kryo) {
		try {
			Output output = new Output(new FileOutputStream(Gdx.files.getLocalStoragePath() + "/saves/" + name + "/particles.txt"));
			kryo.writeObject(output, this);
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAndExit(String dir) {
		try {
			/*for (Particle particle: particles) {
				particle.prepareForSaveAndExit();
			}

			particlePool = null;*/
			
			KryoManager.write(this, "saves/" + dir + "/particles.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static ParticleEngine load(String dir) {
		try {
			ParticleEngine particleEngine = KryoManager.read("saves/" + dir + "/particles.txt", ParticleEngine.class);
			
			for (Particle particle: particleEngine.particles) {
				particle.processAfterLoading();
			}

			particleEngine.particlePool = Pools.get(Particle.class);
			
			return particleEngine;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ParticleEngine load(String dir, Kryo kryo) {
		try {
			Input input = new Input(new FileInputStream(Gdx.files.getLocalStoragePath() + "saves/" + dir + "/particles.txt"));
			ParticleEngine particleEngine = kryo.readObject(input, ParticleEngine.class);
			input.close();

			for (Particle particle: particleEngine.particles) {
				particle.processAfterLoading();
			}

			particleEngine.particlePool = Pools.get(Particle.class);

			return particleEngine;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Particle getParticle(btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime, Sprite sprite, Behaviour behaviour) {
		Particle particle = particlePool.obtain();
		particle.init(this, dynamicsWorld, pos, lifetime, sprite, behaviour);
		return particle;
	}

	public void addParticle(btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime, Sprite sprite, Behaviour behaviour) {
		if (VideoSettings.isParticlesEnabled()) {
			particles.add(getParticle(dynamicsWorld, pos, lifetime, sprite, behaviour));
		}
	}

	public void addParticle(btDynamicsWorld dynamicsWorld, Vector3 pos, Vector3 velocity, float lifetime, Sprite sprite, Behaviour behaviour) {
		if (VideoSettings.isParticlesEnabled()) {
			Particle particle = getParticle(dynamicsWorld, pos, lifetime, sprite, behaviour);
			particle.rigidBody.setLinearVelocity(velocity);
			particles.add(particle);
		}
	}

	public void addBurst(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, float lifetime, Sprite sprite, Particle.Behaviour behaviour) {
		if (VideoSettings.isParticlesEnabled()) {
			Vector3 velocity = new Vector3();
			for (int i = 0; i < num; i++) {
				Particle particle = getParticle(dynamicsWorld, pos, lifetime, sprite, behaviour);
				particle.rigidBody.setLinearVelocity(velocity.setToRandomDirection().scl(speed));
				particles.add(particle);
			}
		}
	}

	/**
	 * Creates particles within a 1m radius sphere which fly up.
	 */
	public void addFlyUpPillar(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, float lifetime, Sprite sprite, Behaviour behaviour) {
		if (VideoSettings.isParticlesEnabled()) {
			for (int i = 0; i < num; i++) {
				Particle particle = getParticle(dynamicsWorld, pos, lifetime, sprite, behaviour);
				particle.rigidBody.setWorldTransform(particle.rigidBody.getWorldTransform().translate(new Vector3().setToRandomDirection().scl(0.5f)));
				Vector3 velocity = new Vector3();
				velocity.set(0, speed, 0).add(new Vector3().setToRandomDirection().scl(speed / 10));
				particle.rigidBody.setLinearVelocity(velocity);
				particles.add(particle);
			}
		}
	}

	/**
	 * Creates particles from a single point which fly up.
	 */
	public void addFlyUpPoint(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, float lifetime, Sprite sprite, Behaviour behaviour) {
		if (VideoSettings.isParticlesEnabled()) {
			for (int i = 0; i < num; i++) {
				Particle particle = getParticle(dynamicsWorld, pos, lifetime, sprite, behaviour);
				Vector3 velocity = new Vector3();
				velocity.set(0, speed, 0).add(new Vector3().setToRandomDirection().scl(speed / 10));
				particle.rigidBody.setLinearVelocity(velocity);
				particles.add(particle);
			}
		}
	}

	public void addWave(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, float lifetime, Sprite sprite, Behaviour behaviour) {
		if (VideoSettings.isParticlesEnabled()) {
			Vector2 velocity = new Vector2();
			for (int i = 0; i < num; i++) {
				Particle particle = getParticle(dynamicsWorld, pos, lifetime, sprite, behaviour);
				velocity.setToRandomDirection().scl(speed);
				particle.rigidBody.setLinearVelocity(new Vector3(velocity.x, 0, velocity.y));
				particles.add(particle);
			}
		}
	}

	public void addDirectionalWave(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, float lifetime, float rotation, float variation, Sprite sprite, Behaviour behaviour) {
		if (VideoSettings.isParticlesEnabled()) {
			Vector2 baseVelocity = new Vector2(0, -1).scl(speed).rotate(rotation);
			Vector2 newVelocity = new Vector2();
			for (int i = 0; i < num; i++) {
				Particle particle = getParticle(dynamicsWorld, pos, lifetime, sprite, behaviour);
				float directionChange = MathUtils.random(-variation / 2, variation / 2);
				newVelocity.set(baseVelocity).rotate(directionChange).scl(MathUtils.random(0.6f, 1));
				particle.rigidBody.setLinearVelocity(new Vector3(newVelocity.x, 0, newVelocity.y));
				particles.add(particle);
			}
		}
	}

	/**
	 * Add the given number of particles, each with a random velocity and zero gravity.
	 * They will look like they are floating around. With a low value of speed, they will appear to float in stasis.
	 */
	public void addFloatingParticles(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, float lifetime, Sprite sprite) {
		if (VideoSettings.isParticlesEnabled()) {
			Vector3 velocity = new Vector3();
			for (int i = 0; i < num; i ++) {
				Particle particle = getParticle(dynamicsWorld, pos, lifetime, sprite, Behaviour.GRAVITY);
				particle.rigidBody.setGravity(new Vector3());
				velocity.setToRandomDirection().scl(speed);
				particle.rigidBody.setLinearVelocity(velocity);
				particles.add(particle);
			}
		}
	}
	
	/*public void addFireParticle(btDynamicsWorld dynamicsWorld, Vector3 pos) {
		particles.add(new Particle(this, dynamicsWorld, pos, 2f, Sprite.FIRE, Particle.Behaviour.OSCILLATE_DOWN));
	}
	
	void addFireParticle(btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime) {
		particles.add(new Particle(this, dynamicsWorld, pos, lifetime, Particle.Sprite.FIRE, Behaviour.OSCILLATE_DOWN));
	}
	
	public void addFireBurst(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, Behaviour behaviour) {
		addBurst(dynamicsWorld, pos, num, speed, Sprite.FIRE, behaviour);
	}

	public void addFireUp(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed) {
		addFlyUp(dynamicsWorld, pos, num, speed, Sprite.FIRE, Particle.Behaviour.GRAVITY);
	}

	public void addFireWave(btDynamicsWorld dynamicsWorld, Vector3 pos, int num, float speed, Behaviour behaviour) {
		addWave(dynamicsWorld, pos, num, speed, Sprite.FIRE, behaviour);
	}*/
	
	/*public void addFireNode(Vector3 pos, Vector3 area) {
		particleNodes.add(new ParticleNode(pos, area, Particle.Sprite.FIRE, Behaviour.OSCILLATE_DOWN, 6f, 1f));
	}
	
	public void addFireNode(Vector3 pos, Vector3 area, float lifetime) {
		particleNodes.add(new ParticleNode(pos, area, Sprite.FIRE, Behaviour.OSCILLATE_DOWN, lifetime, 1f));
	}*/
	
	public void dispose() {
		for (Particle particle: particles) {
			particle.rigidBody.dispose();
			particle.shape.dispose();
		}
	}
	
}
