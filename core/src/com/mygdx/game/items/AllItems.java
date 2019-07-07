package com.mygdx.game.items;

import com.badlogic.gdx.utils.Array;

public class AllItems {

	static Array<String> weaponNames;
	static Array<String> armourNames;
	static Array<String> equipmentNames;
	static Array<String> otherItemNames;
	static Array<String> consumableItemNames;

//	private static Array<Item> allWeapons = new Array<>();
//	private static Array<Item> allArmour = new Array<>();
//	private static Array<Item> allEquipment = new Array<>();
//	private static Array<Item> allOtherItems = new Array<>();
	
	public static void init() {
//		AllItems allItems = new AllItems();

//		allItems.initItemNames();
		AllItems.initItemNames();

		/*allItems.loadAllWeapons();
		allItems.loadAllArmour();
		allItems.loadAllEquipment();
		allItems.loadAllOtherItems();*/
//		return allItems;
	}

	private static void initItemNames() {
		weaponNames = new Array<>();
		weaponNames.add("Iron shortsword");

		armourNames = new Array<>();

		equipmentNames = new Array<>();

		otherItemNames = new Array<>();
		otherItemNames.add("Iron ore");
		otherItemNames.add("Iron ingot");
		otherItemNames.add("Silverheart");
		otherItemNames.add("Continuum Fragment");

		consumableItemNames = new Array<>();
	}
	
	/*private void loadAllWeapons() {
		// Acts on items
		String weaponsFile = Gdx.files.internal("data/allWeapons.txt").readString();
		Scanner fileWeapons = new Scanner(weaponsFile);
		while (fileWeapons.hasNextLine()) {
			Weapon weapon = new Weapon();
			weapon.setName(fileWeapons.nextLine());
			weapon.setOrigName(weapon.getName());
			weapon.setDesc(fileWeapons.nextLine());
			String rawRarity = fileWeapons.nextLine();
			for (Rarity rarity: Rarity.values()) {
				if (rarity.type().equalsIgnoreCase(rawRarity)) {
					weapon.setRarity(rarity);
				}
			}
			weapon.setType(fileWeapons.nextLine());
			weapon.setPhysDamage(Integer.parseInt(fileWeapons.nextLine()));
			weapon.setMagDamage(Integer.parseInt(fileWeapons.nextLine()));
			weapon.setRange(Integer.parseInt(fileWeapons.nextLine()));
			weapon.setRangeType(fileWeapons.nextLine());
			weapon.setHands(Integer.parseInt(fileWeapons.nextLine()));
			allWeapons.add(weapon);
			fileWeapons.nextLine(); // To skip past the semicolon
			if (fileWeapons.hasNextLine()) {
				weapon = new Weapon();
			} else {
				break;
			}
		}
		fileWeapons.close();
	}
	
	private void loadAllArmour() {
		// Acts on items
		String armourFile = Gdx.files.internal("data/allArmour.txt").readString();
		Scanner fileArmour = new Scanner(armourFile);
		Armour armour = new Armour();
		while (fileArmour.hasNextLine()) {
			armour.setName(fileArmour.nextLine());
			armour.setOrigName(armour.getName());
			armour.setDesc(fileArmour.nextLine());
			String rawRarity = fileArmour.nextLine();
			for (Rarity rarity: Rarity.values()) {
				if (rarity.type().equalsIgnoreCase(rawRarity)) {
					armour.setRarity(rarity);
				}
			}
			armour.setType(fileArmour.nextLine());
			armour.setDefense(Integer.parseInt(fileArmour.nextLine()));
			allArmour.add(armour);
			fileArmour.nextLine(); // To skip past the semicolon
			if (fileArmour.hasNextLine()) {
				armour = new Armour();
			} else {
				break;
			}
		}
		fileArmour.close();
	}
	
	private void loadAllEquipment() {
		// Acts on items
		String equipmentFile = Gdx.files.internal("data/allEquipment.txt").readString();
		Scanner fileEquipment = new Scanner(equipmentFile);
		Equipment equipment = new Equipment();
		while (fileEquipment.hasNextLine()) {
			equipment.setName(fileEquipment.nextLine());
			equipment.setOrigName(equipment.getName());
			equipment.setDesc(fileEquipment.nextLine());
			String rawRarity = fileEquipment.nextLine();
			for (Rarity rarity: Rarity.values()) {
				if (rarity.type().equalsIgnoreCase(rawRarity)) {
					equipment.setRarity(rarity);
				}
			}
			allEquipment.add(equipment);
			fileEquipment.nextLine(); // To skip past the semicolon
			if (fileEquipment.hasNextLine()) {
				equipment = new Equipment();
			} else {
				break;
			}
		}
		fileEquipment.close();
	}
	
	private void loadAllOtherItems() {
		// Acts on items
		String otherItemsFile = Gdx.files.internal("data/allOtherItems.txt").readString();
		Scanner fileOtherItems = new Scanner(otherItemsFile);
		OtherItem otherItem = new OtherItem();
		while (fileOtherItems.hasNextLine()) {
			otherItem.setName(fileOtherItems.nextLine());
			otherItem.setOrigName(otherItem.getName());
			otherItem.setDesc(fileOtherItems.nextLine());
			String rawRarity = fileOtherItems.nextLine();
			for (Rarity rarity: Rarity.values()) {
				if (rarity.type().equalsIgnoreCase(rawRarity)) {
					otherItem.setRarity(rarity);
				}
			}
			otherItem.setConsumable(fileOtherItems.nextLine().equals("true")); // If it is "true", set it to true, otherwise set it to false
			allOtherItems.add(otherItem);
			fileOtherItems.nextLine(); // To skip past the semicolon
			if (fileOtherItems.hasNextLine()) {
				otherItem = new OtherItem();
			} else {
				break;
			}
		}
		fileOtherItems.close();
	}*/
	
	/*static Array<Item> getAllWeapons() {
		return allWeapons;
	}

	static Array<Item> getAllArmour() {
		return allArmour;
	}

	static Array<Item> getAllEquipment() {
		return allEquipment;
	}

	static Array<Item> getAllOtherItems() {
		return allOtherItems;
	}*/
	
}
