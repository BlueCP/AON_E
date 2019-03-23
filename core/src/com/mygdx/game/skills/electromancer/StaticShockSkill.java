package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.StaticShockArea;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class StaticShockSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public StaticShockSkill() {
		super(null);
	}

	public StaticShockSkill(Entity entity) {
		super(entity);
		name = "Static Shock";
		desc = "Passively stun and damage nearby enemies for a period of time.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new StaticShockArea(entity, entity.pos, 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
