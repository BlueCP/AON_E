package com.mygdx.game.audio;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.settings.AudioSettings;

public class TrueSound {

	Sound sound;
	private Type type;

	public enum Type {
		ENVIRONMENTAL,
		SOUNDFX,
		CREATURE,
		UI
	}

	TrueSound(Sound sound, Type type) {
		this.sound = sound;
		this.type = type;
	}

	private float calcVolume() {
		switch (type) {
			case ENVIRONMENTAL:
				return AudioSettings.masterVolume() * AudioSettings.environmentalVolume();
			case SOUNDFX:
				return AudioSettings.masterVolume() * AudioSettings.soundFXVolume();
			case CREATURE:
				return AudioSettings.masterVolume() * AudioSettings.creatureVolume();
			case UI:
				return AudioSettings.masterVolume() * AudioSettings.UIVolume();
			default:
				return 0;
		}
	}

	float calcVolume(float volume) {
		return calcVolume() * volume;
	}

	public long play() {
		return sound.play(calcVolume());
	}

	public long play(float volume) {
		return sound.play(calcVolume(volume));
	}

	public long play(float volume, float pitch, float pan) {
		return sound.play(calcVolume(volume), pitch, pan);
	}

	public long loop() {
		return sound.loop();
	}

	public long loop(float volume) {
		return sound.loop(volume);
	}

	public long loop(float volume, float pitch, float pan) {
		return sound.loop(volume, pitch, pan);
	}

	public void stop() {
		sound.stop();
	}

	public void pause() {
		sound.pause();
	}

	public void resume() {
		sound.resume();
	}

	public void dispose() {
		sound.dispose();
	}

	public void stop(long soundId) {
		sound.stop(soundId);
	}

	public void pause(long soundId) {
		sound.pause(soundId);
	}

	public void resume(long soundId) {
		sound.resume(soundId);
	}

	public void setLooping(long soundId, boolean looping) {
		sound.setLooping(soundId, looping);
	}

	public void setPitch(long soundId, float pitch) {
		sound.setPitch(soundId, pitch);
	}

	public void setVolume(long soundId, float volume) {
		sound.setVolume(soundId, volume);
	}

	public void setPan(long soundId, float pan, float volume) {
		sound.setPan(soundId, pan, volume);
	}

}
