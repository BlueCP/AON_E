package com.mygdx.game.droppeditems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.items.OtherItem;

public class DroppedOtherItem extends DroppedItem {

	private OtherItem otherItem; // The other item that this dropped item represents.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private DroppedOtherItem() { }

	DroppedOtherItem(OtherItem otherItem, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		super(otherItem.getId(), pos, dynamicsWorld);

		this.otherItem = otherItem;
		findSprite();
	}

	@Override
	void findSprite() {
		switch (otherItem.getOrigName()) {
			case "Iron ore":
				sprite = Sprite.IRON_ORE;
				break;
			case "Iron ingot":
				sprite = Sprite.IRON_INGOT;
				break;
			case "Silverheart":
				sprite = Sprite.SILVERHEART;
				break;
		}
	}

	@Override
	public void pickedUpBy(Entity entity, btDynamicsWorld dynamicsWorld, DroppedItemManager droppedItemManager) {
		entity.inventory.addOtherItem(otherItem);
		destroy(dynamicsWorld, droppedItemManager);
	}

}
