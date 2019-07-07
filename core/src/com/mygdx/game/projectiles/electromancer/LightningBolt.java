package com.mygdx.game.projectiles.electromancer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.StaticProjectile;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.RenderMath;

/**
 * Represents a lightning bolt coming from the sky down; however, for programming purposes we take the start point as the ground.
 */
public class LightningBolt extends StaticProjectile {

	private static final int damage = 3;
	private static final Vector3 halfExtents = new Vector3(0.5f, 5f, 0.5f);

	Array<Integer> hitEntities;
	private Vector3 startPoint;
//	private Vector3 endPoint;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private LightningBolt() { }

	public LightningBolt(Entity entity, Vector3 pos, float lifetime) {
		super(entity, ProjectileSprite.LIGHTNING_BOLT, pos, lifetime);

		name = "Lightning Bolt";
		hitEntities = new Array<>();

		startPoint = pos.cpy();
		this.pos.add(0, halfExtents.y, 0);
//		endPoint = pos.cpy().add(0, halfExtents.y, 0);

//		loadPhysicsObject();
//		addToDynamicsWorld(dynamicsWorld, PhysicsManager.PROJECTILE_FLAG, PhysicsManager.ALL_FLAG);
	}

	@Override
	public void render(SpriteBatch spriteBatch, IsometricRenderer isometricRenderer) {
		Vector2 screenPosStart = RenderMath.cartToScreen(isometricRenderer.camera, startPoint.x, startPoint.y, startPoint.z);

		// Draw a vertical lightning bolt.
		spriteBatch.draw(getTexture(), screenPosStart.x, screenPosStart.y - getTexture().getRegionHeight()/2f, 0,
				getTexture().getRegionHeight()/2f, getTexture().getRegionWidth(), getTexture().getRegionHeight(),
				1, 1, 90);
	}

	@Override
	protected void loadPhysicsObject() {
		defaultLoadCollisionObject(new btCylinderShape(halfExtents));
		collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(pos));
	}

	@Override
	public void update(float delta, PlayScreen playScreen) {

	}

	@Override
	public boolean onHitEntity(Entity entity, PlayScreen playScreen) {
		Entity offender = playScreen.entities.getEntity(owner, playScreen.player);
		if (entity.id != owner && !hitEntities.contains(entity.id, true)) {
//			float finalDamage = offender.dealDamage(entity, damage + offender.getRealDamage());
			offender.landAbility(entity, playScreen);
			offender.dealAbilityDamage(entity, damage + offender.getRealDamage(), playScreen);
			hitEntities.add(entity.id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onHitProjectile(Projectile projectile, PlayScreen playScreen) {
		return false;
	}

}
