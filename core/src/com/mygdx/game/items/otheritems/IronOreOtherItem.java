package com.mygdx.game.items.otheritems;

import com.mygdx.game.items.OtherItem;

public class IronOreOtherItem extends OtherItem {

	public IronOreOtherItem() {
		origName = "Iron ore";
		name = origName;
		desc = "An unrefined chunk of iron in rock.";
		rarity = Rarity.UNCOMMON;

		cost = 2;
	}

}
