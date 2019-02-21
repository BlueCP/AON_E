package com.mygdx.game.items;

public class Armour extends Item {
	
	/*
	public static String[] atrList = {"name","id","code","type","defense","rarity"};
	public static String[] viewAtrList = {"name","id","type","defense","rarity"};
	private HashMap<String, String> atr = new HashMap<String, String>();
	*/
	
	protected String type;
	protected int defense;
	protected boolean equipped;
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
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
