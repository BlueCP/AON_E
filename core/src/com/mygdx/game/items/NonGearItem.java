package com.mygdx.game.items;

public class NonGearItem extends Item {

	protected int cost; // The monetary value of this item (buying price).

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
