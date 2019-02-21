package com.mygdx.game.items.weapons;

import com.mygdx.game.items.Weapon;

public class IronShortswordWeapon extends Weapon {

	public IronShortswordWeapon() {
		origName = "Iron shortsword";
		name = origName;
		desc = "The trusty companion of many an adventurer.";
		rarity = Rarity.COMMON;

		physDamage = 1;
		magDamage = 1;
	}

}
