package com.mygdx.game.skills.paladin;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.CracklingOrb;
import com.mygdx.game.projectiles.paladin.SeraphicFlare;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class SeraphicFlareSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SeraphicFlareSkill() {
		super(null);
	}

	public SeraphicFlareSkill(Entity entity) {
		super(entity);
		name = "Seraphic Flare";
		desc = "Damage enemies in a line.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new SeraphicFlare(entity, entity.pos,
				targetPos.add(0, entity.hitboxExtents.y/2, 0), 0.5f), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
