package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;

/**
 * Refers to effects which have individual 'procs'. This means there is no common pool of 'stacks'.
 * Each instance of the effect is treated individually.
 * The 'cont' at the end is short for continuous; values for power can take any float value. This is useful when more
 * precision is needed for the power of an effect, for example increases in regen speed.
 */
public class ProcEffectCont extends Effect {

	private Array<Integer> idPool;
	// I chose not to add a pending ids system like that which exists in the Entities class for pragmatic reasons.
	// Basically, if there is ever a time where the lifetime of an effect is dependent upon something else (such as
	// a projectile), then the duration can be set to Integer.MAX_VALUE and the effect can be ended when it needs to
	// be (for example, in the destroy() method of Projectile).

	public Array<Float> powers;
	private Array<Float> durations;
	private Array<Integer> ids;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	ProcEffectCont() { }

	ProcEffectCont(Entity entity) {
		super(entity);
		powers = new Array<>();
		durations = new Array<>();
		ids = new Array<>();

		idPool = new Array<>();
	}

	private int generateId() {
		int id;
		if (idPool.size > 0) {
			id = Integer.MAX_VALUE; // Set it to the max value so that we can't accidentally be lower than the lowest int in the pool
			for (Integer int0: idPool) {
				if (int0 < id) {
					id = int0;
				}
			}
			idPool.removeValue(id, true);
		} else {
			id = 0;
			for (int i = 0; i < powers.size - 1; i ++) {
				// Power.size - 1 as that's the number of procs with actual ids; this proc has been added to powers and durations but not to ids.
				if (ids.get(i) > id) {
					id = ids.get(i);
				}
			}
			id ++; // Since highest is already the id for an actual entity.
		}
		ids.add(id);
		return id;
	}

	@Override
	public void update() {
		for (int i = 0; i < powers.size; i ++) {
			if (durations.get(i) > 0) {
				durations.set(i, durations.get(i) - Gdx.graphics.getDeltaTime());
			} else {
				powers.removeIndex(i);
				durations.removeIndex(i);
				ids.removeIndex(i);
			}
		}
	}

	/**
	 * Adds a new instance of this effect with the given power and duration.
	 */
	public int add(float power, float duration) {
		powers.add(power);
		durations.add(duration);
		return generateId();
	}

	/**
	 * Adds a new instance of this effect with the given power, which will only operate for one tick.
	 */
	public int addThisTick(float power) {
		powers.add(power);
		durations.add(0f);
		return generateId();
	}

	public void remove(int id) {
		if (ids.contains(id, true)) {
			int index = ids.indexOf(id, true);
			powers.removeIndex(index);
			durations.removeIndex(index);
			ids.removeIndex(index);
		}
	}

	public void removeAll() {
		powers.clear();
		durations.clear();
		ids.clear();
	}

	public boolean isActive() {
		return powers.size > 0;
	}

}
