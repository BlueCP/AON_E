package com.mygdx.game.items;

import com.mygdx.game.screens.PlayScreen;

public class NullConsumable extends Consumable {

	NullConsumable() {
		id = -1;
	}

	@Override
	protected boolean consume(PlayScreen playScreen) {
		return false;
	}

}
