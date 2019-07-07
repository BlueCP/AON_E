package com.mygdx.game.items;

import com.badlogic.gdx.utils.Array;

public class Inventory {
	
	public Array<Weapon> weapons = new Array<>();
	public Array<Armour> armour = new Array<>();
	public Array<Equipment> equipment = new Array<>();
	public Array<OtherItem> otherItems = new Array<>();
	public Array<Consumable> consumables = new Array<>();

	public boolean contains(String name) {
		if (AllItems.weaponNames.contains(name, false)) {
			return searchFor(name, weapons.toArray(Weapon.class));
		} else if (AllItems.armourNames.contains(name, false)) {
			return searchFor(name, armour.toArray(Armour.class));
		} else if (AllItems.equipmentNames.contains(name, false)) {
			return searchFor(name, equipment.toArray(Equipment.class));
		} else if (AllItems.otherItemNames.contains(name, false)) {
			return searchFor(name, otherItems.toArray(OtherItem.class));
		} else if (AllItems.consumableItemNames.contains(name, false)) {
			return searchFor(name, consumables.toArray(Consumable.class));
		} else {
			return false;
		}
	}

	private boolean searchFor(String name, Item[] collection) {
		for (Item item: collection) {
			if (item.origName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void addItem(String name) {
		if (AllItems.weaponNames.contains(name, false)) {
			addWeapon(name);
		} else if (AllItems.armourNames.contains(name, false)) {
			addArmour(name);
		} else if (AllItems.equipmentNames.contains(name, false)) {
			addEquipment(name);
		} else if (AllItems.otherItemNames.contains(name, false)) {
			addOtherItem(name);
		} else if (AllItems.consumableItemNames.contains(name, false)) {
			addConsumableItem(name);
		}
	}
	
	public void addWeapon(Weapon weaponToAdd) {
		//weaponToAdd.setId(generateCode(weapons));
		weapons.add(weaponToAdd);
	}

	public void addWeapon(String name) {
		weapons.add(ItemFactory.createWeapon(name));
	}
	
	public void addArmour(Armour armourToAdd) {
		//armourToAdd.setId(generateCode(armour));
		armour.add(armourToAdd);
	}

	public void addArmour(String name) {
		armour.add(ItemFactory.createArmour(name));
	}
	
	public void addEquipment(Equipment equipmentToAdd) {
		//equipmentToAdd.setId(generateCode(equipment));
		equipment.add(equipmentToAdd);
	}

	public void addEquipment(String name) {
		equipment.add(ItemFactory.createEquipment(name));
	}
	
	public void addOtherItem(OtherItem otherItemToAdd) {
		//otherItemToAdd.setId(generateCode(otherItems));
		otherItems.add(otherItemToAdd);
	}

	public void addOtherItem(String name) {
		otherItems.add(ItemFactory.createOtherItem(name));
	}

	public void addConsumableItem(Consumable consumableItem) {
		consumables.add(consumableItem);
	}

	public void addConsumableItem(String name) {
		consumables.add(ItemFactory.createConsumableItem(name));
	}

	public boolean removeItem(String name) {
		if (AllItems.weaponNames.contains(name, false)) {
			return removeWeapon(name);
		} else if (AllItems.armourNames.contains(name, false)) {
			return removeArmour(name);
		} else if (AllItems.equipmentNames.contains(name, false)) {
			return removeEquipment(name);
		} else if (AllItems.otherItemNames.contains(name, false)) {
			return removeOtherItem(name);
		} else if (AllItems.consumableItemNames.contains(name, false)) {
			return removeConsumableItem(name);
		} else {
			return false;
		}
	}

	/**
	 * Removes the first item with the same name as that given.
	 * @param name the name of the item to be removed.
	 * @return true if an item was removed. false if not (happens if the item is not there).
	 */
	public boolean removeWeapon(String name) {
		for (Weapon weapon: weapons) {
			if (weapon.origName.equals(name)) {
				weapons.removeValue(weapon, false);
				return true;
			}
		}
		return false;
	}

	public boolean removeArmour(String name) {
		for (Armour armour0: armour) {
			if (armour0.origName.equals(name)) {
				armour.removeValue(armour0, false);
				return true;
			}
		}
		return false;
	}

	public boolean removeEquipment(String name) {
		for (Equipment equipment0: equipment) {
			if (equipment0.origName.equals(name)) {
				equipment.removeValue(equipment0, false);
				return true;
			}
		}
		return false;
	}

	public boolean removeOtherItem(String name) {
		for (OtherItem otherItem: otherItems) {
			if (otherItem.origName.equals(name)) {
				otherItems.removeValue(otherItem, false);
				return true;
			}
		}
		return false;
	}

	public boolean removeConsumableItem(String name) {
		for (Consumable consumable: consumables) {
			if (consumable.origName.equals(name)) {
				consumables.removeValue(consumable, false);
				return true;
			}
		}
		return false;
	}
	
	/*public void addItem(Item itemToAdd) {
		if (itemToAdd instanceof Weapon) {
			//itemToAdd.setId(generateCode(weapons));
			weapons.add(itemToAdd);
		} else if (itemToAdd instanceof Armour) {
			//itemToAdd.setId(generateCode(armour));
			armour.add(itemToAdd);
		} else if (itemToAdd instanceof Equipment) {
			//itemToAdd.setId(generateCode(equipment));
			equipment.add(itemToAdd);
		} else if (itemToAdd instanceof OtherItem) {
			//itemToAdd.setId(generateCode(otherItems));
			otherItems.add(itemToAdd);
		}
	}*/
	
}
