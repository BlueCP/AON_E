package com.mygdx.game.items;

public class Equipped {
	
	private boolean mainEquipped = false;
	private boolean offEquipped = false;
	private boolean armourEquipped = false;
	private boolean equipment1Equipped = false;
	private boolean equipment2Equipped = false;
	private boolean equipment3Equipped = false;
	
	private Weapon selected; // Selected weapon
	private Weapon main;
	private Weapon off;
	private Armour armour;
	private Equipment equipment1;
	private Equipment equipment2;
	private Equipment equipment3;
	
	public enum SelectedWeapon {
		MAIN,
		OFF
	}
	
	public Equipped() {
		selected = new NullWeapon();
		main = new NullWeapon();
		off = new NullWeapon();
		armour = new NullArmour();
		equipment1 = new NullEquipment();
		equipment2 = new NullEquipment();
		equipment3 = new NullEquipment();
	}
	
	public void update(Inventory inventory) {
		boolean noMain = true;
		boolean noOff = true;
		boolean noArmour = true;
		boolean noEquipment1 = true;
		boolean noEquipment2 = true;
		boolean noEquipment3 = true;
		
		for (Weapon weapon: inventory.weapons) {
			if (weapon.id == main.id && noMain) {
				noMain = false;
			} else if (weapon.id == off.id && noOff) {
				noOff = false;
			}
		}
		
		for (Armour armour0: inventory.armour) {
			if (armour0.id == armour.id && noArmour) {
				noArmour = false;
			}
		}
		
		for (Equipment equipment: inventory.equipment) {
			if (equipment.id == equipment1.id && noEquipment1) {
				noEquipment1 = false;
			} else if (equipment.id == equipment2.id && noEquipment2) {
				noEquipment2 = false;
			} else if (equipment.id == equipment3.id && noEquipment3) {
				noEquipment3 = false;
			}
		}
		
		if (noMain) {
			resetMain();
		}
		if (noOff) {
			resetOff();
		}
		if (noArmour) {
			resetArmour();
		}
		if (noEquipment1) {
			resetEquipment1();
		}
		if (noEquipment2) {
			resetEquipment2();
		}
		if (noEquipment3) {
			resetEquipment3();
		}
	}

	public boolean isWeaponEquipped() {
		return mainEquipped || offEquipped;
	}

	public boolean isMainEquipped() {
		return mainEquipped;
	}

	public boolean isOffEquipped() {
		return offEquipped;
	}

	public boolean isArmourEquipped() {
		return armourEquipped;
	}

	public boolean isEquipment1Equipped() {
		return equipment1Equipped;
	}

	public boolean isEquipment2Equipped() {
		return equipment2Equipped;
	}

	public boolean isEquipment3Equipped() {
		return equipment3Equipped;
	}

	public Weapon getSelected() {
		return selected;
	}

	public void setSelected(SelectedWeapon selectedWeapon) {
		switch (selectedWeapon) {
			case MAIN:
				selected = main;
				break;
			case OFF:
				selected = off;
				break;
		}
	}
	
	public void resetSelected() {
		this.selected = new NullWeapon();
	}

	public Weapon getMain() {
		return main;
	}

	public void setMain(Weapon newMain) {
		main.equipped = false;
		main = newMain;
		main.equipped = true;
		mainEquipped = true;
	}
	
	public void resetMain() {
		main.equipped = false;
		main = new NullWeapon();
		mainEquipped = false;
	}

	public Weapon getOff() {
		return off;
	}

	public void setOff(Weapon newOff) {
		off.equipped = false;
		off = newOff;
		off.equipped = true;
		offEquipped = true;
	}
	
	public void resetOff() {
		off.equipped = false;
		off = new NullWeapon();
		offEquipped = false;
	}

	public Armour getArmour() {
		return armour;
	}

	public void setArmour(Armour newArmour) {
		armour.equipped = false;
		armour = newArmour;
		armour.equipped = true;
		armourEquipped = true;
	}
	
	public void resetArmour() {
		armour.equipped = false;
		armour = new NullArmour();
		armourEquipped = false;
	}

	public Equipment getEquipment1() {
		return equipment1;
	}

	public void setEquipment1(Equipment newEquipment1) {
		equipment1.equipped = false;
		equipment1 = newEquipment1;
		equipment1.equipped = true;
		equipment1Equipped = true;
	}
	
	public void resetEquipment1() {
		equipment1.equipped = false;
		equipment1 = new NullEquipment();
		equipment1Equipped = false;
	}

	public Equipment getEquipment2() {
		return equipment2;
	}

	public void setEquipment2(Equipment newEquipment2) {
		equipment2.equipped = false;
		equipment2 = newEquipment2;
		equipment2.equipped = true;
		equipment2Equipped = true;
	}
	
	public void resetEquipment2() {
		equipment2.equipped = false;
		equipment2 = new NullEquipment();
		equipment2Equipped = false;
	}

	public Equipment getEquipment3() {
		return equipment3;
	}

	public void setEquipment3(Equipment newEquipment3) {
		equipment3.equipped = false;
		equipment3 = newEquipment3;
		equipment3.equipped = true;
		equipment3Equipped = true;
	}
	
	public void resetEquipment3() {
		equipment3.equipped = false;
		this.equipment3 = new NullEquipment();
		equipment3Equipped = false;
	}
	
}
