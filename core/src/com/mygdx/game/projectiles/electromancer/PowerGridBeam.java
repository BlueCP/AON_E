package com.mygdx.game.projectiles.electromancer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.RenderMath;
import com.mygdx.game.utils.Util;

public class PowerGridBeam extends StaticProjectile {

	private static final float radius = 0.2f;

	private Array<Integer> hitEntities;
	private Vector3 startPoint;
	private Vector3 endPoint;
	private Projectile projectile1; // One of the power grid conductors that this beam comes from.
	private Projectile projectile2; // The other conductor that this beam comes from.

	private float damage;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private PowerGridBeam() { }

	public PowerGridBeam(Entity entity, Vector3 startPoint, Vector3 endPoint, Projectile projectile1, Projectile projectile2, float lifetime, float damage) {
		super(entity, ProjectileSprite.LIGHTNING_BOLT, startPoint.cpy().lerp(endPoint, 0.5f), lifetime);

		name = "Power Grid Beam";
		hitEntities = new Array<>();

		this.projectile1 = projectile1;
		this.projectile2 = projectile2;

		this.startPoint = startPoint.cpy();
		this.endPoint = endPoint.cpy();

		this.damage = damage;
	}

	@Override
	public void render(SpriteBatch spriteBatch, IsometricRenderer isometricRenderer) {
		Vector2 screenPosStart = RenderMath.cartToScreen(isometricRenderer.camera, startPoint.x, startPoint.y, startPoint.z);
		Vector2 screenPosEnd = RenderMath.cartToScreen(isometricRenderer.camera, endPoint.x, endPoint.y, endPoint.z);

		int distance = (int) screenPosStart.dst(screenPosEnd);
		TextureRegion region = new TextureRegion(getTexture(), 0, 0, distance, getTexture().getRegionHeight());

		Vector2 diff = screenPosEnd.cpy().sub(screenPosStart);
		float rotation = (float) Math.toDegrees(MathUtils.atan2(diff.y, diff.x));
		if (rotation < 0) {
			rotation += 360;
		}

		spriteBatch.draw(region, screenPosStart.x, screenPosStart.y - region.getRegionHeight()/2f, 0,
				region.getRegionHeight()/2f, region.getRegionWidth(), region.getRegionHeight(),
				1, 1, rotation);
	}

	protected void loadPhysicsObject() {
		btCollisionShape shape = new btSphereShape(radius);
		defaultLoadCollisionObject(shape);

		collisionObject.setWorldTransform(collisionObject.getWorldTransform());

		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		if (projectile1.isDestroyed() || projectile2.isDestroyed()) {
			destroy(playScreen);
			return;
		}
		Array<Integer> hitIds = playScreen.physicsManager.convexSweepTestAll(shape, startPoint, endPoint); // Cast the shape from the start to the end.
		for (Integer id: hitIds) {
			if (PhysicsManager.isEntityOrPlayer(id)) { // If the cast hit an entity, hit that entity with the lightning bolt effect.
				Entity entity = playScreen.entities.getEntity(Util.getId(id), playScreen.player);
				Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
				if (!hitEntities.contains(entity.id, true) && entity.id != owner) {
//					float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
					offender.landAbility(entity, playScreen);
					offender.dealAbilityDamage(entity, damage + offender.getRealDamage(), playScreen);
					hitEntities.add(entity.id);
				}
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

	@Override
	public void addToDynamicsWorld(btDynamicsWorld dynamicsWorld, int group, int mask) {
		super.addToDynamicsWorld(dynamicsWorld, group, mask);
	}

}
