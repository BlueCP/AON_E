package com.mygdx.game.entityactions;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.BasicAttack;
import com.mygdx.game.skills.Skill;

public class BasicAttackAction extends EntityAction {

	protected BasicAttack basicAttack;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BasicAttackAction() {
		super("", null);
	}

	public BasicAttackAction(BasicAttack basicAttack, Entity.AnimationType animationType, float lifetime) {
		super(basicAttack.getName(), animationType);

		this.basicAttack = basicAttack;

		name = basicAttack.getName();
		totalLifetime = lifetime;
	}

	public Skill getBasicAttack() {
		return basicAttack;
	}

	@Override
	public void update(Entity entity, PlayScreen playScreen) {
		if (wantsDeletion()) {
			basicAttack.finish(playScreen);
		} else if (entity.hasTargetedLocationThisTick()) {
			basicAttack.stop(playScreen); // If the entity has targeted another location this frame, stop auto attacking.
		}
	}

}
