package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;

/**
 * Refers to effects which are either 'on' or 'off', such as stuns and roots.
 */
public class BinaryEffect extends Effect {

	float duration;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BinaryEffect() { }

	public BinaryEffect(Entity entity) {
		super(entity);
	}

	@Override
	public void update() {
		if (duration > 0) {
			duration -= Gdx.graphics.getDeltaTime();
		}
		if (duration < 0) {
			duration = 0;
		}
	}

	public void add(float duration) {
		this.duration += duration;
	}

	public boolean isActive() {
		return duration > 0;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

}
