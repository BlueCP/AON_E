package com.mygdx.game.items;

import com.mygdx.game.items.otheritems.IronIngotOtherItem;
import com.mygdx.game.items.otheritems.IronOreOtherItem;
import com.mygdx.game.items.weapons.IronShortswordWeapon;

public class ItemFactory {

	public static Weapon createWeapon(String name) {
		switch (name) {
			case "Iron shortsword":
				return new IronShortswordWeapon();
			default:
				return new NullWeapon();
		}
	}

	public static Armour createArmour(String name) {
		switch (name) {
			default:
				return new NullArmour();
		}
	}

	public static Equipment createEquipment(String name) {
		switch (name) {
			default:
				return new NullEquipment();
		}
	}

	public static OtherItem createOtherItem(String name) {
		switch (name) {
			case "Iron ore":
				return new IronOreOtherItem();
			case "Iron ingot":
				return new IronIngotOtherItem();
			default:
				return new NullOtherItem();
		}
	}

	public static Consumable createConsumableItem(String name) {
		switch (name) {
			default:
				return new NullConsumable();
		}
	}

}
