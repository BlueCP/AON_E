package com.mygdx.game.statuseffects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.AON_E;

public class StatusEffectSprites {

	public static TextureRegion chill;
	public static TextureRegion soul;

	public static void init(AON_E game) {
		chill = game.fireParticlesAtlas.getRegions().get(0); // Just get any fire particle sprite.
		soul = game.fireParticlesAtlas.getRegions().get(0); // Just a placeholder sprite for now.
	}

}
