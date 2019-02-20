package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.screens.PlayScreen;

public class ChilledEffect extends Effect {

	private static final int maxStacks = 5;

	private float duration;
	private Array<Boolean> isBitingCold; // For each stack, whether or not it has biting cold damage.
	private Array<Boolean> isEncaseInIce; // Whether of not these stacks have the encase in ice effect.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ChilledEffect() { }

	public ChilledEffect(Entity entity) {
		super(entity);
		name = "Chilled";
		desc = "A bit cold.";

		duration = 0;
		isBitingCold = new Array<>();
		isEncaseInIce = new Array<>();
	}

	@Override
	public void update() {
		if (duration > 0) {
			duration -= Gdx.graphics.getDeltaTime();
		} else {
			isBitingCold.clear();
			isEncaseInIce.clear();
		}
	}

	public boolean isActive() {
		return isBitingCold.size > 0;
	}

	public void add(int power, float duration, boolean isBitingCold, boolean isEncaseInIce) {
		this.duration += duration;
		for (int i = 0; i < power; i ++) {
			this.isBitingCold.add(isBitingCold);
			this.isEncaseInIce.add(isEncaseInIce);
		}
	}

	public void addOne(float duration, boolean isBitingCold, boolean isEncaseInIce) {
		this.duration += duration;
		this.isBitingCold.add(isBitingCold);
		this.isEncaseInIce.add(isEncaseInIce);
	}

	public void addStacks(int power, boolean isBitingCold, boolean isEncaseInIce) {
		for (int i = 0; i < power; i ++) {
			this.isBitingCold.add(isBitingCold);
			this.isEncaseInIce.add(isEncaseInIce);
		}
	}

	public int numStacks() {
		return isBitingCold.size;
	}

	public void remove() {
		isBitingCold.clear();
		isEncaseInIce.clear();
		duration = 0;
	}

	/**
	 * Calculates the % dampening on the affected entity's movement.
	 * @return the dampening.
	 */
	public float movementDampening() {
		return 0.05f * isBitingCold.size;
	}

	public void bitingColdDamage() {
		for (boolean bitingCold: isBitingCold) {
			if (bitingCold) {
				entity.takeDamageBase(2 * Gdx.graphics.getDeltaTime());
			}
		}
	}

	public void testEncaseInIce(PlayScreen playScreen) {
		if (isBitingCold.size >= maxStacks) {
			for (boolean encaseInIce: isEncaseInIce) {
				if (encaseInIce) {
					isBitingCold.setSize(maxStacks - 1);
					isEncaseInIce.setSize(maxStacks - 1);
					entity.frozenEffect.add(3);

					playScreen.particleEngine.addFlyUpPillar(playScreen.physicsManager.getDynamicsWorld(), entity.pos,
							20, 4, 1f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
					break;
				}
			}
		}
	}

}
