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
