package com.mygdx.game.audio;

import com.badlogic.gdx.audio.Music;
import com.mygdx.game.AON_E;

/**
 * Contains all of the Music objects in the game. Some music will be played manually in the Screen classes.
 * However, most 'gameplay' music will rotate periodically, and will depend on things such as which culture the player
 * is in. This class will manage when these tracks will play automatically.
 * TODO: Add support for rotating tracks based on culture when the world map has been drawn up in more detail.
 */
public class MusicManager {

	public Music titleMusic;
	public Music travelMusic;

	public MusicManager(AON_E game) {
		titleMusic = game.manager.get("music/titletheme.mp3", Music.class);
		travelMusic = game.manager.get("music/travelmusic.mp3");
	}

}
