package com.mygdx.game.items;

import java.awt.Color;

public abstract class Item {
	
	private static long highestId = 0;
	
	protected String name;
	protected String origName;
	protected int id;
	protected String desc;
	protected Rarity rarity;
	
	public static enum Rarity {
		
		COMMON("Common", Color.lightGray),
		UNCOMMON("Uncommon", Color.white),
		RARE("Rare", Color.green),
		EXAULTED("Exaulted", Color.orange),
		EPIC("Epic", Color.red),
		LEGENDARY("Legendary", Color.magenta),
		MYTHICAL("Mythical", Color.cyan),
		ARTEFACT("Artefact", Color.yellow);
		
		private String type;
		public String type() { return type; }
		
		private Color colour;
		public Color colour() { return colour; }
		
		Rarity(String type, Color colour) {
			this.type = type;
			this.colour = colour;
		}
	}
	
	public Item() {
		id = (int)highestId++;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrigName() {
		return origName;
	}
	public void setOrigName(String origName) {
		this.origName = origName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Rarity getRarity() {
		return rarity;
	}
	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
