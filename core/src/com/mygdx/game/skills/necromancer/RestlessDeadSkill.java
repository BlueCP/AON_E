package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.necromancer.RestlessDeadArea;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class RestlessDeadSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private RestlessDeadSkill() {
		super(null);
	}

	public RestlessDeadSkill(Entity entity) {
		super(entity);
		name = "Restless Dead";
		desc = "Curse the targeted area, damaging enemies there for each soul you have in your possession.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new RestlessDeadArea(entity, targetPos, 2), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
