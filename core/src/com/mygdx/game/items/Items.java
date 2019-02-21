package com.mygdx.game.items;

class Items {
	
	/*static Weapon getWeapon(Array<Item> list, String name) {
		for (Item item: list) {
			if (name.equals(item.getOrigName())) {
				return (Weapon)item;
			}
		}
		return null;
	}
	
	static Armour getArmour(Array<Item> list, String name) {
		for (Item item: list) {
			if (name.equals(item.getOrigName())) {
				return (Armour)item;
			}
		}
		return null;
	}
	
	static Equipment getEquipment(Array<Item> list, String name) {
		for (Item item: list) {
			if (name.equals(item.getOrigName())) {
				return (Equipment)item;
			}
		}
		return null;
	}
	
	static OtherItem getOtherItem(Array<Item> list, String name) {
		for (Item item: list) {
			if (name.equals(item.getOrigName())) {
				return (OtherItem)item;
			}
		}
		return null;
	}*/
	
	/*static Item getItem(Items items, String name) {
		Array<Item> weapons;
		Array<Item> armour;
		Array<Item> equipment;
		Array<Item> otherItems;
		if (items instanceof Inventory) { // If it's an inventory
			weapons = ((Inventory) items).getWeapons();
			armour = ((Inventory) items).getArmour();
			equipment = ((Inventory) items).getEquipment();
			otherItems = ((Inventory) items).getOtherItems();
		} else { // If it's all items
			weapons = AllItems.getAllWeapons();
			armour = AllItems.getAllArmour();
			equipment = AllItems.getAllEquipment();
			otherItems = AllItems.getAllOtherItems();
		}
		if (getWeapon(weapons, name) != null) { // If it is a weapon
			return getWeapon(weapons, name); // Return it
		} else if (getArmour(armour, name) != null) { // etc...
			return getArmour(armour, name);
		} else if (getEquipment(equipment, name) != null) {
			return getEquipment(equipment, name);
		} else if (getOtherItem(otherItems, name) != null) {
			return getOtherItem(otherItems, name);
		} else {
			return null;
		}
	}*/
	
}
