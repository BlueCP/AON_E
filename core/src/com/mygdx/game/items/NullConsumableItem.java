package com.mygdx.game.items;

import com.mygdx.game.screens.PlayScreen;

public class NullConsumableItem extends ConsumableItem {

	public NullConsumableItem() {
		id = -1;
	}

	@Override
	protected boolean consume(PlayScreen playScreen) {
		return false;
	}

}
