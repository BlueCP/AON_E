package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;

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

	public StackEffect(Entity entity) {
		super(entity);
		stacks = 0;
		duration = 0;
	}

	@Override
	public void update() {
		if (getDuration() > 0) { // And the effect is still active
			setDuration(getDuration() - Gdx.graphics.getDeltaTime()); // Decrease the duration
		} else {
			setStacks(0); // Otherwise (i.e. if the effect is no longer active) set the power to 0 (to reset the effect)
		}
	}

	/**
	 * Adds a given number of stacks and increases the duration.
	 * @param power the number of stacks to add.
	 * @param duration the amount to increase the duration by.
	 */
	public void add(int power, float duration) {
		stacks += power;
		this.duration += duration;
	}

	/**
	 * Adds 1 stack and increases the duration.
	 * @param duration the duration increase.
	 */
	public void addOne(float duration) {
		stacks ++;
		this.duration += duration;
	}

	/**
	 * Adds a given number of stacks without increasing the duration.
	 * @param power the number of stacks to add.
	 */
	public void addStacks(int power) {
		stacks ++;
	}

	public boolean isActive() {
		return stacks > 0;
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
