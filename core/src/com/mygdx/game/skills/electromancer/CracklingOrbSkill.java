package com.mygdx.game.skills.electromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.CracklingOrb;
import com.mygdx.game.projectiles.electromancer.Stormcaller;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class CracklingOrbSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public CracklingOrbSkill() {
		super(null);
	}

	public CracklingOrbSkill(Entity entity) {
		super(entity);
		name = "Crackling Orb";
		desc = "Summon a ball of electricity which passes through enemies in a line, damaging them.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(15, 1f, 10, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new CracklingOrb(entity, entity.pos,
				targetPos.add(0, entity.hitboxExtents.y/2, 0), 5), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
