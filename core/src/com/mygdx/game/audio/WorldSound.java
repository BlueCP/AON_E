package com.mygdx.game.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Represents a sound with a position, and therefore influenced by distance from the camera and angle from the camera.
 * Distance; influences volume.
 * Angle; influences pan.
 */
public class WorldSound extends TrueSound {

	WorldSound(Sound sound, Type type) {
		super(sound, type);
	}

	public void updateVolumeAndPan(long id, Vector3 cameraPos, Vector3 soundPos) {
		setPan(id, calcPan(cameraPos, soundPos), calcVolume(cameraPos, soundPos));
	}

	private float calcVolume(Vector3 cameraPos, Vector3 soundPos) {
		float dist = cameraPos.dst(soundPos);

		return calcVolume((float) Math.pow(1.072, -dist));
	}

	private float calcVolume(float volume, Vector3 cameraPos, Vector3 soundPos) {
		float dist = cameraPos.dst(soundPos);

		return calcVolume((float) Math.pow(1.072, -dist) * volume);
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

	public long play(Vector3 cameraPos, Vector3 soundPos) {
		return sound.play(calcVolume(cameraPos, soundPos), 1, calcPan(cameraPos, soundPos));
	}

	public long play(float volume, Vector3 cameraPos, Vector3 soundPos) {
		return sound.play(calcVolume(volume, cameraPos, soundPos), 1, calcPan(cameraPos, soundPos));
	}

	public long play(float volume, float pitch, Vector3 cameraPos, Vector3 soundPos) {
		return sound.play(calcVolume(volume, cameraPos, soundPos), pitch, calcPan(cameraPos, soundPos));
	}

}
