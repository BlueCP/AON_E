package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;

public class SoulsEffect extends StackEffect {

	private static final int maxStacks = 5;
	private static final float stackLifetime = 5f; // The (fixed) lifetime of a single stack.

	private Array<Float> durations; // The duration of each stack (as they decay individually).

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private SoulsEffect() { }

	public SoulsEffect(Entity entity) {
		super(entity);
		name = "Souls";
		desc = "The spirits of the dead linger around you.";

		durations = new Array<>();
	}

	@Override
	public void update() {
		if (durations.size > 0) {
			float newFloat = durations.get(durations.size - 1) - Gdx.graphics.getDeltaTime(); // Calculate the new duration
			durations.removeIndex(durations.size - 1); // Remove the old duration.
			if (newFloat > 0) { // If this stack is still alive
				durations.add(newFloat); // Replace the old duration with the new one (appended to the end).
			}
		}
	}

	public boolean isActive() {
		return durations.size > 0;
	}

	public void add(int power) {
		for (int i = 0; i < power; i ++) {
			durations.add(stackLifetime);
			if (durations.size >= maxStacks) {
				break;
			}
		}
	}

	public void addOne() {
		if (durations.size < maxStacks) {
			durations.add(stackLifetime);
		}
	}

	public int numStacks() {
		return durations.size;
	}

	public void remove() {
		durations.clear();
	}

}
