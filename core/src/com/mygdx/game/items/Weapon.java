package com.mygdx.game.items;

public abstract class Weapon extends GearItem {
	
	protected Type type;
	protected int damage;
//	protected int magDamage;
	protected int range;
//	protected String rangeType;
//	private int hands;
//	boolean equipped = false;

	public enum Type {
		SWORD("Sword");

		private String type;
		public String type() { return type; }

		Type(String type) {
			this.type = type;
		}
	}
	
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	/*public int getHands() {
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
	}*/
	
}
