package com.mygdx.game.items;

public class Equipment extends Item {
	
	protected boolean equipped;

	Equipment(String name, String origName, int id, String desc, Rarity rarity) {
		this.setName(name);
		this.setOrigName(origName);
		this.setId(id);
		this.setDesc(desc);
		this.setRarity(rarity);
		this.setEquipped(false);
	}
	
	Equipment() { }
	
	public Equipment(boolean var) {
		this.setName("[no item]");
		this.setOrigName("[no item]");
		this.setId(-1);
		this.setDesc("[no item]");
		this.setRarity(Rarity.COMMON);
		this.setEquipped(false);
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}
	
}
