package com.mygdx.game.items;

public abstract class Armour extends Item {
	
	/*
	public static String[] atrList = {"name","id","code","type","defense","rarity"};
	public static String[] viewAtrList = {"name","id","type","defense","rarity"};
	private HashMap<String, String> atr = new HashMap<String, String>();
	*/
	
	protected Type type;
	protected int defense;
	protected boolean equipped;

	public enum Type {
		MELEE("Melee"), // For warriors, summoners and paladins.
		MAGE("Mage"), // For pyromancers, cryomancers, electromancers and necromancers.
		SCOUT("Scout"); // For rogues and archers.

		private String type;
		public String type() { return type; }

		Type(String type) {
			this.type = type;
		}
	}
	
	/*Armour(String name, String origName, int id, String desc, String type, int defense, Rarity rarity) {
		this.setName(name);
		this.setOrigName(origName);
		this.setId(id);
		this.setDesc(desc);
		this.setRarity(rarity);
		this.setType(type);
		this.setDefense(defense);
		this.setEquipped(false);
	}*/
	
	public Armour() {

	}
	
	/*public Armour(boolean var) {
		this.setName("[no item]");
		this.setOrigName("[no item]");
		this.setId(-1);
		this.setDesc("[no item]");
		this.setRarity(Rarity.COMMON);
		this.setType("[no item]");
		this.setDefense(0);
		this.setEquipped(false);
	}*/

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}
	
}
