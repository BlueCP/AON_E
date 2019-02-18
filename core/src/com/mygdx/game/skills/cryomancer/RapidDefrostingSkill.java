package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.ActivatedSkill;

public class RapidDefrostingSkill extends ActivatedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public RapidDefrostingSkill() {
		super(null);
	}

	public RapidDefrostingSkill(Entity entity) {
		super(entity);
		name = "Rapid Defrosting";
		desc = "Defrost enemies around you, dealing damage and removing the effects of chill.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addDefrostingArea(entity, playScreen.physicsManager.getDynamicsWorld(), entity.pos);
		putOnCooldown(3);
	}

}
