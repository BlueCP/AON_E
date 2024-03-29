package com.mygdx.game.projectiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.physics.WorldObject;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.IsometricRenderer.Visibility;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.RenderMath;
import com.mygdx.game.utils.Util;

public abstract class Projectile extends WorldObject {

	public String name;
	protected int owner; // The id of the entity that created this projectile
	
	private float stateTime;
	protected float lifetime;
	protected int ticksPast;
	boolean isDestroyed;
	
	Matrix4 worldTransform;

	protected transient btCollisionShape shape;
	
//	public Vector3 pos;
	
	ProjectileSprite sprite;
	
	public enum ProjectileSprite {
		FIREBOLT,
		LIGHTNING_BOLT,
		RAZE_ZONE,
		IMMOLATE_ZONE,
		NO_SPRITE
	}
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	Projectile() { }
	
	Projectile(Entity entity, ProjectileSprite sprite, Vector3 pos, float lifetime) {
//		generateId(projectileManager);
		this.owner = entity.getId();
		
		worldTransform = new Matrix4();

		this.sprite = sprite;
		this.pos = pos.cpy();
		this.stateTime = 0;
		this.ticksPast = 0;
		this.lifetime = lifetime;

		isDestroyed = false;
	}
	
	void universalUpdate(float delta) {
		lifetime -= delta;
		stateTime += delta;
		ticksPast ++;

		updatePos();
	}

	abstract void updatePos();
	
	public void updateWorldObject(IsometricRenderer renderer) {
		Vector2 coords = RenderMath.cartToScreen(renderer.camera, pos.x, pos.y, pos.z);
		coords.sub(getTexture().getRegionWidth()/2f, getTexture().getRegionHeight()/2f);
		/*coords.x -= getTexture().getRegionWidth()/2f;
		coords.y -= getTexture().getRegionHeight()/2f;*/
		renderPos = coords;
		visibility = Visibility.VISIBLE;
	}
	
	public abstract void update(float delta, PlayScreen playScreen);
	
	/*
	 * True if the projectile was destroyed, false if it wasn't.
	 */
	public abstract boolean onHitEntity(Entity entity, PlayScreen playScreen);

	public abstract boolean onHitProjectile(Projectile projectile, PlayScreen playScreen);
	
	public abstract void destroy(PlayScreen playScreen);
	
	public abstract btCollisionObject getCollisionObject();
	
	/*
	 * Collision object here refers to whatever the specific projectile type uses, e.g. rigid body, ghost object, or actually a collision object.
	 */
	public abstract void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask);

//	public abstract void prepareForSaveAndExit();
	
	void processAfterLoading() {
		loadPhysicsObject();
	}

	protected abstract void loadPhysicsObject();

	void generateId(ProjectileManager projectileManager) {
		if (projectileManager.idPool.size > 0) {
			int lowest = Integer.MAX_VALUE; // Set it to the max value so that we can't accidentally be lower than the lowest int in the pool
			for (Integer int0: projectileManager.idPool) {
				if (int0 < lowest) {
					lowest = int0;
				}
			}
			id = lowest;
			projectileManager.idPool.removeValue(lowest, false);
		} else {
			int highest = 0;
			for (int i = 0; i < projectileManager.projectiles.size; i ++) {
				if (projectileManager.projectiles.get(i).id > highest) {
					highest = projectileManager.projectiles.get(i).id;
				}
			}
			id = highest + 1; // Since highest is already the id for an actual projectile
		}
		physicsId = Util.getProjectileId(id);
	}
	
	@Override
	public TextureRegion getTexture() {
		return ProjectileSprites.getCurrentTexture(this);
	}
	
	public boolean hasTexture() {
		return ProjectileSprites.hasTexture(sprite);
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

	public float getLifetime() {
		return lifetime;
	}

	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}

	public int getTicksPast() {
		return ticksPast;
	}

	public void setTicksPast(int ticksPast) {
		this.ticksPast = ticksPast;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

}
