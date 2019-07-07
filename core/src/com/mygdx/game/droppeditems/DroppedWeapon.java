package com.mygdx.game.droppeditems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.items.Weapon;

public class DroppedWeapon extends DroppedItem {

	private Weapon weapon; // The weapon that this dropped item represents.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private DroppedWeapon() { }

	public DroppedWeapon(Weapon weapon, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		super(weapon.getId(), pos, dynamicsWorld);

		this.weapon = weapon;
		findSprite();
	}

	void findSprite() {
		switch (weapon.getOrigName()) {
			case "Iron shortsword":
				sprite = Sprite.IRON_SHORTSWORD;
				break;
		}
	}

	@Override
	public void pickedUpBy(Entity entity, btDynamicsWorld dynamicsWorld, DroppedItemManager droppedItemManager) {
		entity.inventory.addWeapon(weapon);
		destroy(dynamicsWorld, droppedItemManager);
	}

}
