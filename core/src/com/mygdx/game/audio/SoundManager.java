package com.mygdx.game.audio;

import com.mygdx.game.AON_E;

/**
 * Contains all of the Sound objects in the game. This manager is a convenient place to give each Sound object a type.
 * This type determines which setting for volume it uses (e.g. UI, soundFX, etc.).
 */
public class SoundManager {

	public TrueSound fireball;
	public TrueSound click;

	public SoundManager(AON_E game) {
		fireball = new TrueSound(game.manager.get("sound/soundFX/fireball.wav"), TrueSound.Type.SOUNDFX);
		click = new TrueSound(game.manager.get("sound/UI/click.wav"), TrueSound.Type.UI);
	}

}
