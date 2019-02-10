package com.mygdx.game.items;

import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;

public class Inventory extends Items {
	
	private Array<Item> weapons = new Array<>();
	private Array<Item> armour = new Array<>();
	private Array<Item> equipment = new Array<>();
	private Array<Item> otherItems = new Array<>();
	
	/*
	private int generateCode(LinkedList<Item> list) {
		int highestCode = 0;
		if (list.size() > 0) {
			for (Item item: list) {
				if (item.getId() > highestCode) {
					highestCode = item.getId();
				}
			}
		}
		return highestCode;
	}
	*/
	
	public void addWeapon(Weapon weaponToAdd) {
		//weaponToAdd.setId(generateCode(weapons));
		weapons.add(weaponToAdd);
	}
	
	public void addWeapon(String name) {
		Weapon weaponToAdd = Items.getWeapon(AllItems.getAllWeapons(), name);
		addWeapon(weaponToAdd);
	}
	
	public void addArmour(Armour armourToAdd) {
		//armourToAdd.setId(generateCode(armour));
		armour.add(armourToAdd);
	}
	
	public void addArmour(String name) {
		Armour armourToAdd = Items.getArmour(AllItems.getAllArmour(), name);
		addArmour(armourToAdd);
	}
	
	public void addEquipment(Equipment equipmentToAdd) {
		//equipmentToAdd.setId(generateCode(equipment));
		equipment.add(equipmentToAdd);
	}
	
	public void addEquipment(String name) {
		Equipment equipmentToAdd = Items.getEquipment(AllItems.getAllEquipment(), name);
		addEquipment(equipmentToAdd);
	}
	
	public void addOtherItem(OtherItem otherItemToAdd) {
		//otherItemToAdd.setId(generateCode(otherItems));
		otherItems.add(otherItemToAdd);
	}
	
	public void addOtherItem(String name) {
		OtherItem otherItemToAdd = Items.getOtherItem(AllItems.getAllOtherItems(), name);
		addOtherItem(otherItemToAdd);
	}
	
	public void addItem(Item itemToAdd) {
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
	}
	
	public void addItem(String name) {
		Item item = Items.getItem(new AllItems(), name);
		addItem(item);
	}
	
	public Array<Item> getWeapons() {
		return weapons;
	}
	
	public Array<Item> getArmour() { return armour; }
	
	public Array<Item> getEquipment() {
		return equipment;
	}
	
	public Array<Item> getOtherItems() {
		return otherItems;
	}
	
}
