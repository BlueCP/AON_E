package com.mygdx.game.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.mygdx.game.particles.ParticleEngine;

/**
 * Controls settings to do with graphics, such as VSync and fullscreen.
 * Also deals with some performance-boosting options, such as disabling particles.
 */
public class VideoSettings extends Settings {

	/*public VideoSettings() {
		preferences = Gdx.app.getPreferences("Settings");

		*//*preferences.putBoolean("VSync", false);
		preferences.putInteger("Framerate", 60);
		preferences.putBoolean("Particles", true);
		preferences.putBoolean("Screen shake", true);
		preferences.putBoolean("Fullscreen", true);*//*
	}*/

	public static boolean isFullscreen() {
		return preferences.getBoolean("Fullscreen", true);
	}

	public static void enableFullScreen() {
		DisplayMode mode = null;
		for (DisplayMode currentMode : Gdx.graphics.getDisplayModes()) {
			if (mode == null) {
				mode = currentMode;
			} else if (mode.width < currentMode.width) {
				mode = currentMode;
			}
		}
		Gdx.graphics.setFullscreenMode(mode);

		preferences.putBoolean("Fullscreen", true);
		preferences.flush();
	}

	public static void disableFullScreen() {
		Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		preferences.putBoolean("Fullscreen", false);
		preferences.flush();
	}

	public static boolean isVSyncEnabled() {
		return preferences.getBoolean("VSync", false);
	}

	public static void enableVSync() {
		Gdx.graphics.setVSync(true);
		preferences.putBoolean("VSync", true);
		preferences.flush();
	}

	public static void disableVSync() {
		Gdx.graphics.setVSync(false);
		preferences.putBoolean("VSync", false);
		preferences.flush();
	}

	public static int getFramerate() {
		return preferences.getInteger("Framerate", 60);
	}

	// TODO: Consider if it's worth having an upper limit for framerate at all.
	public static void setFramerate(int val) {
		preferences.putInteger("Framerate", val);
		preferences.flush();
	}

	public static boolean isParticlesEnabled() {
		return preferences.getBoolean("Particles", true);
	}

	public static void enableParticles() {
		preferences.putBoolean("Particles", true);
		preferences.flush();
	}

	public static void disableParticles() {
		preferences.putBoolean("Particles", false);
		preferences.flush();
	}

	/*public static void disableParticles(ParticleEngine particleEngine) {
		particleEngine.particles.clear();

		preferences.putBoolean("Particles", false);
		preferences.flush();
	}*/

	public static boolean isScreenShakeEnabled() {
		return preferences.getBoolean("Screen shake", true);
	}

	public static void enableScreenShake() {
		preferences.putBoolean("Screen shake", true);
		preferences.flush();
	}

	public static void disableScreenShake() {
		preferences.putBoolean("Screen shake", false);
		preferences.flush();
	}

}
