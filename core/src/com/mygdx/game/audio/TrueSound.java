package com.mygdx.game.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.settings.AudioSettings;

public class TrueSound {

	private Sound sound;
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

	private float calcVolume(float volume) {
		return calcVolume() * volume;
	}

	/**
	 * Calculates the pan of the sound, using the angle of the line between the camera and the source of the sound.
	 * @param cameraPos the position of the camera (in most cases also the player).
	 * @param soundPos the position of the sound in the world.
	 * @return the calculated pan, between -1 and 1.
	 */
	private float calcPan(Vector3 cameraPos, Vector3 soundPos) {
		Vector3 diff = soundPos.cpy().sub(cameraPos);
		float rotation = (float) Math.toDegrees(MathUtils.atan2(diff.z, diff.x));

		if (rotation < 0) {
			rotation += 360;
		}

		rotation += 45;
		if (rotation >= 360) {
			rotation -= 360;
		}

		return MathUtils.sinDeg(rotation);
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

	public long play(Vector3 cameraPos, Vector3 soundPos) {
//		System.out.println(calcPan(cameraPos, soundPos));
		return sound.play(calcVolume(), 1, calcPan(cameraPos, soundPos));
	}

	public long play(float volume, Vector3 cameraPos, Vector3 soundPos) {
		return sound.play(calcVolume(volume), 1, calcPan(cameraPos, soundPos));
	}

	public long play(float volume, float pitch, Vector3 cameraPos, Vector3 soundPos) {
		return sound.play(calcVolume(volume), pitch, calcPan(cameraPos, soundPos));
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
