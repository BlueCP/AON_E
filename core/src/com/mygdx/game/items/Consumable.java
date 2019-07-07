package com.mygdx.game.items;

import com.mygdx.game.screens.PlayScreen;

public abstract class Consumable extends NonGearItem {

	Consumable() {

	}

	/**
	 * Consume this item, doing something and destroying it in the process.
	 * @return true if the item was consumed successfully, false if it was not.
	 */
	protected abstract boolean consume(PlayScreen playScreen);

}
