package com.mygdx.game.particles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.physics.WorldObject;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.IsometricRenderer.Visibility;
import com.mygdx.game.utils.Util;

public class Particle extends WorldObject implements Pool.Poolable {

	private Vector3 emptyVector = new Vector3();
	
	private String type;
	
	private float stateTime;
	private float lifetime; // A non-changing value, the total amount of time this particle will exist for.
	private float lifeSoFar; // A count-up from the moment this particle is created.
	private float changeRate;
	
	private Matrix4 rigidBodyMatrix;
	private Vector3 linearVelocity;
	
	transient btCollisionShape shape;
	public transient btRigidBody rigidBody;
//	public Vector3 pos;
	
	Sprite sprite;
	public Behaviour behaviour;
	
	/*
	 * Sprite is responsible for the sprite that is rendered.
	 */
	public enum Sprite {
		FIRE
	}
	
	/*
	 * Behaviour is responsible for physics interactions.
	 */
	public enum Behaviour {
		OSCILLATE_DOWN,
		POOF,
		GRAVITY
	}
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public Particle() {}

	void init(ParticleEngine particleEngine, btDynamicsWorld dynamicsWorld, Vector3 pos, float lifetime, Sprite sprite, Behaviour behaviour) {
		generateId(particleEngine);

		this.sprite = sprite;
		this.behaviour = behaviour;
		this.pos = pos.cpy();
		this.stateTime = 0;
		this.lifetime = lifetime;
		this.lifeSoFar = 0;
		type = String.valueOf(MathUtils.random(1, 5));

		switch (sprite) {
			case FIRE:
				changeRate = ParticleEngine.fireChangeRate;
				break;
		}

		Matrix4 matrix = new Matrix4();
		matrix.setTranslation(pos);
		rigidBodyMatrix = matrix;
		linearVelocity = new Vector3();

		loadRigidBody();

		dynamicsWorld.addRigidBody(rigidBody, PhysicsManager.PARTICLE_FLAG, PhysicsManager.TERRAIN_FLAG);
	}

	/**
	 * Called when a particle is to be added to the pool.
	 */
	@Override
	public void reset() {
		emptyVector.setZero();
		type = "";
		stateTime = 0;
		lifetime = 0;
		lifeSoFar = 0;
		changeRate = 0;
		linearVelocity.setZero();
		pos.setZero();
	}

	private void loadRigidBody() {
		shape = new btSphereShape(0.1f);
		Vector3 localInertia = new Vector3();
		shape.calculateLocalInertia(1, localInertia);
		rigidBody = new btRigidBody(1, null, shape, localInertia);
		
		rigidBody.setWorldTransform(rigidBodyMatrix);
		rigidBody.setLinearVelocity(linearVelocity);
		
		rigidBody.setAngularFactor(new Vector3());
		rigidBody.setUserValue(physicsId);
		rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
		rigidBody.setFriction(0.7f);
	}
	
	private void generateId(ParticleEngine particleEngine) {
		if (particleEngine.idPool.size > 0) {
			int lowest = Integer.MAX_VALUE; // Set it to the max value so that we can't accidentally be lower than the lowest int in the pool
			for (Integer int0: particleEngine.idPool) {
				if (int0 < lowest) {
					lowest = int0;
				}
			}
			id = lowest;
			particleEngine.idPool.removeValue(lowest, false);
		} else {
			int highest = 0;
			for (int i = 0; i < particleEngine.particles.size; i ++) {
				if (particleEngine.particles.get(i).id > highest) {
					highest = particleEngine.particles.get(i).id;
				}
			}
			id = highest + 1; // Since highest is already the id for an actual projectile
		}
		physicsId = Util.getParticleId(id);
	}
	
	/*void prepareForSaveAndExit() {
		rigidBody.getWorldTransform(rigidBodyMatrix);
		linearVelocity.set(rigidBody.getLinearVelocity());
	}*/

	/*btRigidBody prepareForSave() {
		btRigidBody body = rigidBody;
		prepareForSaveAndExit();
		return body;
	}*/

	void processAfterLoading() {
		loadRigidBody();
	}
	
	/*
	 * Handles the physics interactions of the particle.
	 */
	void update() {
		switch (behaviour) {
			case OSCILLATE_DOWN:
				float xChange = MathUtils.sinDeg(lifeSoFar * 180) / 10; // The change in the x coord of the particle
				rigidBody.setLinearVelocity(emptyVector.set(xChange, -0.2f, xChange));
				break;
			case POOF:
				if (lifeSoFar <= 0.25f) {
					rigidBody.setGravity(emptyVector);
				} else {
					rigidBody.setDamping(0.98f, 0); // To decelerate the particle considerably, letting it fall to gravity
					rigidBody.setGravity(PhysicsManager.gravity);
				}
			case GRAVITY:
				// Just allow the particle to fall by gravity
				break;
		}

		rigidBody.getWorldTransform().getTranslation(pos);
		rigidBody.getWorldTransform(rigidBodyMatrix);
		linearVelocity.set(rigidBody.getLinearVelocity());
	}
	
	@Override
	public void updateWorldObject(IsometricRenderer renderer) {
		//physicsId = rigidBody.getUserValue();
		//texture = getTexture();
		Vector2 coords = renderer.cartesianToScreen(pos.x, pos.y, pos.z);
		coords.sub(getTexture().getRegionWidth()/2f, getTexture().getRegionHeight()/2f);
//		coords.x -= getTexture().getRegionWidth()/2f;
//		coords.y -= getTexture().getRegionHeight()/2f;
		renderPos = coords;
		visibility = Visibility.VISIBLE;
	}
	
	@Override
	public TextureRegion getTexture() {
		return ParticleSprites.getCurrentTexture(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	float getStateTime() {
		return stateTime;
	}

	void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public float getLifetime() {
		return lifetime;
	}

	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	float getLifeSoFar() {
		return lifeSoFar;
	}

	void setLifeSoFar(float lifeSoFar) {
		this.lifeSoFar = lifeSoFar;
	}

	public float getChangeRate() {
		return changeRate;
	}

	public void setChangeRate(float changeRate) {
		this.changeRate = changeRate;
	}
	
}
