package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.Stormcaller;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class StormcallerSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private StormcallerSkill() {
		super(null);
	}

	public StormcallerSkill(Entity entity) {
		super(entity);
		name = "Stormcaller";
		desc = "Summon random bolts of lightning in the targeted area.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new Stormcaller(entity, targetPos, 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
