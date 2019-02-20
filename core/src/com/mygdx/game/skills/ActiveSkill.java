package com.mygdx.game.skills;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;

/**
 * An active skill is one which requires input from the player to activate.
 */
public abstract class ActiveSkill extends Skill {

	private float cooldown = 0;
	public State state;

	public enum State {

		AVAILABLE,
		BEING_CAST,
		ON_COOLDOWN

	}

	ActiveSkill(Entity entity) {
		super(entity);
		state = State.AVAILABLE;
	}

	/**
	 * Invokes the skill action, causing the countdown to start.
	 */
	public abstract void start(PlayScreen playScreen);

	/**
	 * Executes the gameplay logic when the countdown has finished and the skill is actually 'used'.
	 */
	public abstract void finish(PlayScreen playScreen);

	/**
	 * Tells this class that the state of the skill should be changed to 'being cast'.
	 */
	protected void useSkill() {
		state = State.BEING_CAST;
	}

	/**
	 * Tells this class that this skill failed to resolve and should be made available again.
	 */
	protected void failResolve() {
		state = State.AVAILABLE;
	}

	/**
	 * Put this skill on cooldown.
	 */
	public void putOnCooldown(float cooldown) {
		this.cooldown = cooldown;
		state = State.ON_COOLDOWN;
	}

	public void reduceCooldown(float reduction) {
		cooldown -= reduction;
		if (cooldown < 0) {
			cooldown = 0;
		}
	}

	/**
	 * Decrement the cooldown, set it to 0 if it's below.
	 */
	public void updateCooldown() {
		if (cooldown > 0) {
			cooldown -= Gdx.graphics.getDeltaTime();
		}
		if (cooldown < 0) {
			cooldown = 0;
		}
		if (cooldown <= 0 && state != State.BEING_CAST) {
			state = State.AVAILABLE;
		}
	}

}
