package com.mygdx.game.droppeditems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.items.Armour;

public class DroppedArmour extends DroppedItem {

	Armour armour; // The armour that this dropped item represents.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private DroppedArmour() { }

	public DroppedArmour(Armour armour, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		super(armour.getId(), pos, dynamicsWorld);

		this.armour = armour;
		findSprite();
	}

	@Override
	void findSprite() {

	}

	@Override
	public void pickedUpBy(Entity entity, btDynamicsWorld dynamicsWorld, DroppedItemManager droppedItemManager) {
		entity.inventory.addArmour(armour);
		destroy(dynamicsWorld, droppedItemManager);
	}

}
