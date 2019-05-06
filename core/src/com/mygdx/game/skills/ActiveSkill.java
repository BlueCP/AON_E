package com.mygdx.game.skills;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.screens.PlayScreen;

/**
 * An active skill is one which requires input from the player to activate.
 */
public abstract class ActiveSkill extends Skill {

	private float cooldown = 0;
	public State state;

	public enum State {

		AVAILABLE, // The skill isn't being used, starting it will cause it proceed normally.
		BEING_CAST, // The skill logic is currently being executed. It has called start() but not end().
		WAITING_FOR_LOCATION, // The skill is waiting for location input.
		ON_STANDBY, // The skill is a 'toggle' spell and is just doing its thing. Invoking the skill will end it.
		ON_COOLDOWN // The skill cannot be used; it is on cooldown, with each frame decrementing the cooldown by delta.

	}

	ActiveSkill(Entity entity) {
		super(entity);
		state = State.AVAILABLE;
	}

	public void invoke(PlayScreen playScreen) {
		entity.currentSkill = this;
		if (state == State.AVAILABLE) { // If the skill is available
			start(playScreen); // Start executing its logic.
		} else if (state == State.ON_STANDBY || state == State.WAITING_FOR_LOCATION) { // If the skill is on standby or waiting for location input
			stop(playScreen); // Stop the skill; either stopping a 'toggle' type skill or a 'waiting for input' phase of a skill (i.e. waiting for location input).
		}
	}

	/**
	 * Update method. Does nothing by default; however, skills which wait for user input to do something may want to override
	 * to watch certain game state changes, e.g. the player targeting something.
	 */
	public void update(PlayScreen playScreen) {

	}

	/**
	 * Starts the skill action, caused by pressing the keyboard shortcut for that skill.
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
	 * Tells this class that the skill has now been put on standby; it will keep doing its own thing until it gets some input from the user.
	 */
	protected void putOnStandby() {
		state = State.ON_STANDBY;
	}

	/**
	 * Tells this class that the skill is waiting for location input; the next place the player clicks will be used as the target location.
	 */
	protected void waitForLocation() {
		state = State.WAITING_FOR_LOCATION;
	}

	/**
	 * Tells this class that this skill failed to resolve and should be made available again. Skips the cooldown.
	 */
	public void failResolve() {
		state = State.AVAILABLE;
	}

	/**
	 * The skill available to use again.
	 */
	protected void makeAvailable() {
		state = State.AVAILABLE;
	}

	/**
	 * Cancels the casting of ths skill, making it available again. Also removes the SkillAction from the entity.
	 * Useful for toggling/'waiting for input' type skills as stopping the skill is dependent on user input,
	 * not a cooldown which is always the same (which could be handled by the SkillAction).
	 */
	public void stop(PlayScreen playScreen) {
		state = State.AVAILABLE;
		removeAction();
	}

	/**
	 * Removes the action associated with this skill from the entity's array of actions.
	 */
	public void removeAction() {
		if (entity.actions.size > 0) {
			EntityAction entityAction = entity.actions.first();
			if (entityAction.getName().equals(name)) {
				entity.actions.removeValue(entityAction, false); // Removes the action from the entity's list of actions.
			}
		}
	}

	/**
	 * Put this skill on cooldown for the given number of seconds.
	 */
	public void putOnCooldown(float cooldown) {
		this.cooldown = cooldown;
		state = State.ON_COOLDOWN;
	}

	/**
	 * Reduce the cooldown by the given number of seconds.
	 */
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
		if (cooldown <= 0 && state != State.BEING_CAST && state != State.ON_STANDBY && state != State.WAITING_FOR_LOCATION) {
			state = State.AVAILABLE;
		}
	}

}
