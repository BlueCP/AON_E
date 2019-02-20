package com.mygdx.game.items;

public class OtherItem extends Item {

	private boolean consumable;
	
	OtherItem(String name, String origName, int id, String desc, Rarity rarity, boolean consumable) {
		this.setName(name);
		this.setOrigName(origName);
		this.setId(id);
		this.setDesc(desc);
		this.setRarity(rarity);
		this.setConsumable(consumable);
	}
	
	OtherItem() { }
	
	OtherItem(boolean var) {
		this.setName("[no item]");
		this.setOrigName("[no item]");
		this.setId(-1);
		this.setDesc("[no item]");
		this.setRarity(Rarity.COMMON);
		this.setConsumable(false);
	}

	public boolean isConsumable() {
		return consumable;
	}

	void setConsumable(boolean consumable) {
		this.consumable = consumable;
	}
	
}
