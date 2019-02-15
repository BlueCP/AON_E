package com.mygdx.game.settings;

/**
 * Controls everything to do with sound and music. At the moment pretty much just controls volumes.
 */
public class AudioSettings extends Settings {

	public static float masterVolume() {
		return preferences.getFloat("masterVolume", 1);
	}

	public static void setMasterVolume(float volume) {
		preferences.putFloat("masterVolume", volume);
	}

	public static float musicVolume() {
		return preferences.getFloat("musicVolume", 1);
	}

	public static void setMusicVolume(float volume) {
		preferences.putFloat("musicVolume", volume);
	}

	public static float environmentalVolume() {
		return preferences.getFloat("environmentalVolume", 1);
	}

	public static void setEnvironmentalVolume(float volume) {
		preferences.putFloat("environmental", volume);
	}

	public static float soundFXVolume() {
		return preferences.getFloat("soundFXVolume", 1);
	}

	public static void setSoundFXVolume(float volume) {
		preferences.putFloat("soundFXVolume", volume);
	}

	public static float creatureVolume() {
		return preferences.getFloat("creatureVolume", 1);
	}

	public static void setCreatureVolume(float volume) {
		preferences.putFloat("creatureVolume", volume);
	}

	public static float UIVolume() {
		return preferences.getFloat("UIVolume", 1);
	}

	public static void setUIVolume(float volume) {
		preferences.putFloat("UIVolume", volume);
	}

}
