package com.mygdx.game.skills.necromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.necromancer.UnearthlyMiasmaArea;
import com.mygdx.game.projectiles.pyromancer.Heatwave;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class UnearthlyMiasmaSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public UnearthlyMiasmaSkill() {
		super(null);
	}

	public UnearthlyMiasmaSkill(Entity entity) {
		super(entity);
		name = "Unearthly Miasma";
		desc = "Nearby enemies take damage and are slowed per soul you have.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new UnearthlyMiasmaArea(entity, entity.pos), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
