package com.mygdx.game.items;

public class Weapon extends Item {
	
	protected String type;
	protected int physDamage;
	protected int magDamage;
	protected int range;
	protected String rangeType;
	protected int hands;
	boolean equipped = false;
	
	/*Weapon(String name, String origName, int id, String desc, String type, int physDamage, int magDamage, Rarity rarity, int range, String rangeType, int hands) {
		// String name, String id, int code, String type, int physDamage, int magDamage, String rarity, boolean ranged, int hands
		
		this.setName(name);
		this.setOrigName(origName);
		this.setId(id);
		this.setDesc(desc);
		this.setRarity(rarity);
		this.setType(type);
		this.setPhysDamage(physDamage);
		this.setMagDamage(magDamage);
		this.setRange(range);
		this.setRangeType(rangeType);
		this.setHands(hands);
		this.setEquipped(false);
	}*/
	
	public Weapon() { }
	
	/*Weapon(boolean var) {
		*//*
		this.setName("[no item]");
		this.setOrigName("[no item]");
		this.setId(-1);
		this.setDesc("[no item]");
		this.setRarity(Rarity.COMMON);
		this.setType("[no item]");
		this.setPhysDamage(0);
		this.setMagDamage(0);
		this.setRange(0);
		this.setRangeType("[no item]");
		this.setHands(0);
		this.setEquipped(false);
		*//*
		id = -1;
	}*/

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPhysDamage() {
		return physDamage;
	}

	void setPhysDamage(int physDamage) {
		this.physDamage = physDamage;
	}

	public int getMagDamage() {
		return magDamage;
	}

	void setMagDamage(int magDamage) {
		this.magDamage = magDamage;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getHands() {
		return hands;
	}

	void setHands(int hands) {
		this.hands = hands;
	}

	public String getRangeType() {
		return rangeType;
	}

	void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}

	public boolean isEquipped() {
		return equipped;
	}

	private void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}
	
}
