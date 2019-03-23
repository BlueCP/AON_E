package com.mygdx.game.audio;

import com.mygdx.game.AON_E;

/**
 * Contains all of the Sound objects in the game. This manager is a convenient place to give each Sound object a type.
 * This type determines which setting for volume it uses (e.g. UI, soundFX, etc.).
 */
public class SoundManager {

	// Environmental

	// Sound FX
	public WorldSound fireballExplosion;
	public WorldSound fireballTravel;

	// Creatures

	// UI
	public TrueSound click;

	public SoundManager(AON_E game) {
		// Environmental

		// Sound FX
		fireballTravel = new WorldSound(game.manager.get("sound/soundFX/fireballTravel.wav"), TrueSound.Type.SOUNDFX);
		fireballExplosion = new WorldSound(game.manager.get("sound/soundFX/fireballExplosion.wav"), TrueSound.Type.SOUNDFX);

		// Creatures

		// UI
		click = new TrueSound(game.manager.get("sound/UI/click.wav"), TrueSound.Type.UI);
	}

	public void pauseAll() {
		// Environmental

		// Sound FX
		fireballTravel.pause();
		fireballExplosion.pause();

		// Creatures

		// UI
		click.pause();
	}

	public void resumeAll() {
		// Environmental

		// Sound FX
		fireballTravel.resume();
		fireballExplosion.resume();

		// Creatures

		// UI
		click.resume();
	}

	public void stopAll() {
		// Environmental

		// Sound FX
		fireballTravel.stop();
		fireballExplosion.stop();

		// Creatures

		// UI
		click.stop();
	}

}
