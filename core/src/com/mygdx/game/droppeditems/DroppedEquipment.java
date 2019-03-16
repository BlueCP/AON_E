package com.mygdx.game.droppeditems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.items.Equipment;

public class DroppedEquipment extends DroppedItem {

	Equipment equipment; // The equipment that this dropped item represents.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private DroppedEquipment() { }

	public DroppedEquipment(Equipment equipment, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		super(equipment.getId(), pos, dynamicsWorld);

		this.equipment = equipment;
		findSprite();
	}

	@Override
	void findSprite() {

	}

	@Override
	public void pickedUpBy(Entity entity, btDynamicsWorld dynamicsWorld, DroppedItemManager droppedItemManager) {
		entity.inventory.addEquipment(equipment);
		destroy(dynamicsWorld, droppedItemManager);
	}

}
