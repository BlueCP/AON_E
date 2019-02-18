package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;

/**
 * Refers to effects which have individual 'procs'. This means there is no common pool of 'stacks'.
 * Each instance of the effect is treated individually.
 */
public class ProcEffect extends Effect {

	public Array<Integer> powers;
	public Array<Float> durations;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ProcEffect() { }

	public ProcEffect(Entity entity) {
		super(entity);
		powers = new Array<>();
		durations = new Array<>();
	}

	@Override
	public void update() {
		for (int a = 0; a < powers.size; a ++) {
			if (durations.get(a) > 0) {
				durations.set(a, durations.get(a) - Gdx.graphics.getDeltaTime());
			} else {
				powers.removeIndex(a);
				durations.removeIndex(a);
			}
		}
	}

	/**
	 * Adds a new instance of this effect with the given power and duration.
	 */
	public void add(int power, float duration) {
		powers.add(power);
		durations.add(duration);
	}

	/**
	 * Adds a new instance of this effect with a power of 1 and the given duration.
	 * @param duration
	 *//*
	public void addOne(float duration) {
		powers.add(1);
		durations.add(duration);
	}*/

	/**
	 * Adds a new instance of this effect with the given power, which will only operate for one tick.
	 */
	public void addThisTick(int power) {
		powers.add(power);
		durations.add(0f);
	}

	public void remove() {
		powers.clear();
		durations.clear();
	}

	public boolean isActive() {
		return powers.size > 0;
	}

}
