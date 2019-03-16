package com.mygdx.game.items.otheritems;

import com.mygdx.game.items.OtherItem;

public class IronIngotOtherItem extends OtherItem {

	public IronIngotOtherItem() {
		origName = "Iron ingot";
		name = origName;
		desc = "A refined bar of iron.";
		rarity = Rarity.UNCOMMON;

		cost = 10;
	}

}
