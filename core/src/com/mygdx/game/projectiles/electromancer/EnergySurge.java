package com.mygdx.game.projectiles.electromancer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.RenderMath;

public class EnergySurge extends StaticProjectile {

	private static final float radius = 0.2f;
	private static final float dps = 2f;

	private int targetEntity;
	private Vector3 startPoint;
	private Vector3 endPoint;
	private boolean firstTick = true;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public EnergySurge() { }

	public EnergySurge(Entity entity, Entity targetEntity) {
		super(entity, ProjectileSprite.LIGHTNING_BOLT, entity.pos, Float.MAX_VALUE);

		name = "Energy Surge";

		this.targetEntity = targetEntity.id;
		this.startPoint = entity.pos;
		this.endPoint = targetEntity.pos;
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
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		Entity target = playScreen.entities.getEntity(targetEntity, playScreen.player);

		if (firstTick) {
			offender.landAbility(target, playScreen);
		}

		float finalDamage = offender.dealDamage(target, dps * Gdx.graphics.getDeltaTime() + offender.getRealDamage());
		offender.landAbilityDamage(target, finalDamage, playScreen);
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
