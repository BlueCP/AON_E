package com.mygdx.game.items;

public abstract class GearItem extends Item {

	protected boolean equipped = false;

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

}
