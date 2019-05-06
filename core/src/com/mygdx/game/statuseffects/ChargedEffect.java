package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.GenericLightning;
import com.mygdx.game.screens.PlayScreen;

public class ChargedEffect extends Effect {

	private static final int maxStacks = 5;
	private static final float stackLifetime = 2f; // The (fixed) lifetime of a single stack.
	private static final float maxRange = 3; // The max range a discharge of lightning will go.
	private static final float damage = 2; // The damage that a lightning discharge does.

	private Array<Float> durations; // The duration of each stack (as they decay individually).
//	private Array<Boolean> isDischarge; // For each stack, whether or not it will discharge to a nearby enemy when it decays.

	boolean dischargeNow = false; // Whether or not a random discharge should be attempted this tick.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ChargedEffect() { }

	public ChargedEffect(Entity entity) {
		super(entity);
		name = "Charged";
		desc = "Prickly.";

		durations = new Array<>();
//		isDischarge = new Array<>();
	}

	@Override
	public void update() {
		dischargeNow = false;
		if (durations.size > 0) {
			float newFloat = durations.get(durations.size - 1) - Gdx.graphics.getDeltaTime(); // Calculate the new duration
			durations.removeIndex(durations.size - 1); // Remove the old duration.
			if (newFloat > 0) { // If this stack is still alive
				durations.add(newFloat); // Replace the old duration with the new one (appended to the end).
			} else {
				dischargeNow = true; // Tell this effect that a random discharge should be attempted, because a stack has decayed.
//				isDischarge.removeIndex(isDischarge.size - 1);
			}/* else {
				isDischarge.removeIndex(isDischarge.size - 1);
			}*/
		}
	}

	public void randomDischarge(PlayScreen playScreen) {
		if (dischargeNow) {
			Array<Entity> nearestEntities = playScreen.entities.getNearestEntities(entity, 1, maxRange);
			if (nearestEntities.size > 0) {
				playScreen.projectileManager.addProjectileNow(new GenericLightning(entity, entity.pos, nearestEntities.first().pos,
						0.2f, damage), playScreen.physicsManager.getDynamicsWorld());
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
//		this.isDischarge.add(isDischarge);
	}

	public int numStacks() {
		return durations.size;
	}

	public void remove() {
		durations.clear();
//		isDischarge.clear();
	}

}
