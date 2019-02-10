package com.mygdx.game.particles;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.particles.Particle.Behaviour;
import com.mygdx.game.particles.Particle.Sprite;

/*
 * A ParticleNode is a point in the world with limited lifetime, from which particles spawn.
 */
public class ParticleNode {
	
	private Vector3 pos;
	private Vector3 area; // Dimensions of the cuboid within which particles should spawn
	private Vector3 halfExtents; // Half-extents of area
	
	private Particle.Sprite sprite;
	private Particle.Behaviour behaviour;
	private float lifetime;
	
	private float frequency; // How many particles/second
	private float timeSinceLastParticle;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public ParticleNode() { }
	
	ParticleNode(Vector3 pos, Vector3 area, Sprite sprite, Behaviour behaviour, float lifetime, float frequency) {
		this.pos = pos;
		this.area = area;
		halfExtents = new Vector3();
		halfExtents.x = area.x/2; halfExtents.y = area.y/2; halfExtents.z = area.z/2;
		this.sprite = sprite;
		this.behaviour = behaviour;
		this.lifetime = lifetime;
		this.frequency = frequency;
		timeSinceLastParticle = 0;
	}
	
	public void update(btDynamicsWorld dynamicsWorld, ParticleEngine engine, float delta) {
		timeSinceLastParticle += delta;
		if (timeSinceLastParticle >= frequency) {
			float x = MathUtils.random(-halfExtents.x, halfExtents.x);
			float y = MathUtils.random(-halfExtents.y, halfExtents.y);
			float z = MathUtils.random(-halfExtents.z, halfExtents.z);
//			engine.addFireParticle(dynamicsWorld, new Vector3(pos.x + x, pos.y + y, pos.z + z), lifetime);
			timeSinceLastParticle = 0;
		}
	}
	
	public Vector3 getPos() {
		return pos;
	}

	public void setPos(Vector3 pos) {
		this.pos = pos;
	}

	public Vector3 getArea() {
		return area;
	}

	public void setArea(Vector3 area) {
		this.area = area;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public Particle.Behaviour getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(Particle.Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	public float getLifetime() {
		return lifetime;
	}

	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	
}
