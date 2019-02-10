package com.mygdx.game.entityattributes;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

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

	public ProcEffect(EffectType effectType) {
		super(effectType);
		powers = new Array<>();
		durations = new Array<>();
	}

	/**
	 * Adds a new instance of this effect with the given power and duration.
	 * @param power
	 * @param duration
	 */
	@Override
	public void add(int power, float duration) {
		powers.add(power);
		durations.add(duration);
	}

	/**
	 * Adds a new instance of this effect with a power of 1 and the given duration.
	 * @param duration
	 */
	@Override
	public void addOne(float duration) {
		powers.add(1);
		durations.add(duration);
	}

	/**
	 * Adds a new instance of this effect with the given power, which will only operate for one tick.
	 * @param power
	 */
	@Override
	public void addThisTick(int power) {
		powers.add(power);
		durations.add(0f);
	}

}
