package com.mygdx.game.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Contains the single object that is used by all settings subclasses to access settings.
 */
public abstract class Settings {

	static Preferences preferences;

	public static void init() {
		preferences = Gdx.app.getPreferences("Settings");
	}

}
