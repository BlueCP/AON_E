package com.mygdx.game.entityattributes;

/**
 * Refers to an effect which increases in power based on the number of 'stacks'.
 */
public class StackEffect extends Effect {

	int stacks;
	float duration;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public StackEffect() { }

	public StackEffect(EffectType effectType) {
		super(effectType);
		stacks = 0;
		duration = 0;
	}

	/**
	 * Adds a given number of stacks and increases the duration.
	 * @param power the number of stacks to add.
	 * @param duration the amount to increase the duration by.
	 */
	@Override
	public void add(int power, float duration) {
		stacks += power;
		this.duration += duration;
	}

	/**
	 * Adds 1 stack and increases the duration.
	 * @param duration the duration increase.
	 */
	@Override
	public void addOne(float duration) {
		stacks ++;
		this.duration += duration;
	}

	/**
	 * Adds a given number of stacks without increasing the duration.
	 * @param power the number of stacks to add.
	 */
	@Override
	public void addThisTick(int power) {
		stacks ++;
	}

	public int getStacks() {
		return stacks;
	}

	public void setStacks(int stacks) {
		this.stacks = stacks;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

}
