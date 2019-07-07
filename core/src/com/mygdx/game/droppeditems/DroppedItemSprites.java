package com.mygdx.game.droppeditems;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;

public class DroppedItemSprites {

	private static TextureRegion ironShortswordTexture;

	private static TextureRegion ironOreTexture;
	private static TextureRegion ironIngotTexture;
	private static TextureRegion silverheartTexture;

	static TextureRegion getCurrentTexture(DroppedItem droppedItem) {
		switch (droppedItem.sprite) {
			case IRON_SHORTSWORD:
				return ironShortswordTexture;
			case IRON_ORE:
				return ironOreTexture;
			case IRON_INGOT:
				return ironIngotTexture;
			case SILVERHEART:
				return silverheartTexture;
			default:
				return null;
		}
	}

	public static void initialise(AON_E game) {
		Array<TextureAtlas.AtlasRegion> allFrames = game.droppedItemsAtlas.getRegions();

		for (TextureAtlas.AtlasRegion region: allFrames) {
			switch (region.name) {
				case "iron shortsword":
					ironShortswordTexture = region;

					ironOreTexture = region;
					ironIngotTexture = region;
					silverheartTexture = region;

					break;
			}
		}
	}

}
