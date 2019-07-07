package com.mygdx.game.settings;

public class GameplaySettings extends Settings {

	public static void reset() {
		preferences.putInteger("textSpeed", 1);

		preferences.flush();
	}

	public static float textSpeed() {
		return preferences.getFloat("textSpeed", 1);
	}

	public static void setTextSpeed(float textSpeed) {
		preferences.putFloat("textSpeed", textSpeed);
		preferences.flush();
	}

}
