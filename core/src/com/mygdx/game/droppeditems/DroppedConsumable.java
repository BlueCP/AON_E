package com.mygdx.game.droppeditems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.items.Consumable;

public class DroppedConsumable extends DroppedItem {

	private Consumable consumable; // The consumable that this dropped item represents.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private DroppedConsumable() { }

	DroppedConsumable(Consumable consumable, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		super(consumable.getId(), pos, dynamicsWorld);

		this.consumable = consumable;
		findSprite();
	}

	@Override
	void findSprite() {

	}

	@Override
	public void pickedUpBy(Entity entity, btDynamicsWorld dynamicsWorld, DroppedItemManager droppedItemManager) {
		entity.inventory.addConsumableItem(consumable);
		destroy(dynamicsWorld, droppedItemManager);
	}

}
