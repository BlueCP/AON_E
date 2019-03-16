package com.mygdx.game.droppeditems;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.physics.WorldObject;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.utils.Util;

public abstract class DroppedItem extends WorldObject {

	Vector3 linearVelocity;
	Matrix4 worldTransform;

	transient btCollisionShape collisionShape;
	public transient btRigidBody rigidBody;

	Sprite sprite;

	public enum Sprite {
		IRON_SHORTSWORD,
		IRON_ORE,
		IRON_INGOT
	}

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	DroppedItem() { }

	public DroppedItem(int id, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		this.id = id;
		physicsId = Util.getDroppedItemId(id);
		this.pos = pos.cpy();

		linearVelocity = new Vector3();
		worldTransform = new Matrix4();
		worldTransform.setTranslation(pos);

		loadRigidBody();
		dynamicsWorld.addRigidBody(rigidBody, PhysicsManager.DROPPED_ITEM_FLAG, PhysicsManager.ALL_FLAG ^ PhysicsManager.DROPPED_ITEM_FLAG);
	}

	abstract void findSprite();

	void universalUpdate() {
		rigidBody.getWorldTransform().getTranslation(pos);
		rigidBody.getWorldTransform(worldTransform);
		linearVelocity.set(rigidBody.getLinearVelocity());
	}

	private void loadRigidBody() {
		collisionShape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));
		Vector3 localInertia = new Vector3();
		collisionShape.calculateLocalInertia(1, localInertia);
		rigidBody = new btRigidBody(1, null, collisionShape, localInertia);

		rigidBody.setWorldTransform(worldTransform);
		rigidBody.setLinearVelocity(linearVelocity);

		rigidBody.setAngularFactor(new Vector3());
		rigidBody.setUserValue(physicsId);
		rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
		rigidBody.setFriction(0.7f);
	}

	public void processAfterLoading() {
		loadRigidBody();
	}

	/**
	 * Called when an entity picks up this item. Abstract so that subclasses (dropped consumable, dropped other item, etc)
	 * must override.
	 * @param entity the entity that has picked up this item.
	 */
	public abstract void pickedUpBy(Entity entity, btDynamicsWorld dynamicsWorld, DroppedItemManager droppedItemManager);

	public void destroy(btDynamicsWorld dynamicsWorld, DroppedItemManager droppedItemManager) {
		dynamicsWorld.removeRigidBody(rigidBody);
		droppedItemManager.droppedItems.removeValue(this, false);
		collisionShape.dispose();
		rigidBody.dispose();
	}

	@Override
	public void updateWorldObject(IsometricRenderer renderer) {
		Vector2 coords = renderer.cartesianToScreen(pos.x, pos.y, pos.z);
		coords.x -= getTexture().getRegionWidth()/2f;
		coords.y -= getTexture().getRegionHeight()/2f;
		renderPos = coords;
		visibility = IsometricRenderer.Visibility.VISIBLE;
	}

	@Override
	public TextureRegion getTexture() {
		return DroppedItemSprites.getCurrentTexture(this);
	}

}
