package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.paladin.HolyFireArea;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class HolyFireSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public HolyFireSkill() {
		super(null);
	}

	public HolyFireSkill(Entity entity) {
		super(entity);
		name = "Holy Fire";
		desc = "Spread holy fire onto nearby enemies, damaging them over time.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new HolyFireArea(entity, entity.pos), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
