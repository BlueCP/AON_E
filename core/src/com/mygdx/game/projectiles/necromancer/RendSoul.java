package com.mygdx.game.projectiles.necromancer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.RenderMath;

public class RendSoul extends StaticProjectile {

	private static final float baseDamage = 2;

	private Vector3 startPoint;
	private Vector3 endPoint;

	private int targetEntity;
	private boolean alreadyHit = false;
	private float realDamage;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public RendSoul() { }

	public RendSoul(Entity entity, Vector3 pos, float lifetime, Entity targetEntity) {
		super(entity, ProjectileSprite.LIGHTNING_BOLT, pos, lifetime);

		name = "Rend Soul";
		this.targetEntity = targetEntity.id;

		realDamage = baseDamage + entity.getRealDamage() + entity.soulsEffect.numStacks();

		startPoint = entity.pos.cpy();
		endPoint = targetEntity.pos.cpy();
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

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btSphereShape(1f));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(this.pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		Entity target = playScreen.entities.getEntity(targetEntity, playScreen.player);

		if (target.id == -1) {
			destroy(playScreen);
		} else if (!alreadyHit) {
			float finalDamage = offender.dealDamage(target, realDamage);
			offender.landAbility(target, playScreen);
			offender.landAbilityDamage(target, finalDamage, playScreen);
			alreadyHit = true;
			playScreen.particleEngine.addFlyUpPoint(playScreen.physicsManager.getDynamicsWorld(), endPoint, 10, 7, 1.5f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
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

}
