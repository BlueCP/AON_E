package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.paladin.EnforcementZone;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class EnforcementZoneSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private EnforcementZoneSkill() {
		super(null);
	}

	public EnforcementZoneSkill(Entity entity) {
		super(entity);
		name = "Enforcement Zone";
		desc = "Create a ring on the ground which will heal you and grant you more damage when you are inside it.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new EnforcementZone(entity, entity.pos, 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
