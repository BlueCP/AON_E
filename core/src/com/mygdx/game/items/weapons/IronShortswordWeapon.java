package com.mygdx.game.items.weapons;

public class IronShortswordWeapon extends SwordWeapon {

	public IronShortswordWeapon() {
		origName = "Iron shortsword";
		name = origName;
		desc = "The trusty companion of many an adventurer.";
		rarity = Rarity.COMMON;

		physDamage = 1;
		magDamage = 1;
	}

}
