package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.pyromancer.Flamethrower;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.LocationSkill;

public class FlamethrowerSkill extends LocationSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public FlamethrowerSkill() {
		super(null);
	}

	public FlamethrowerSkill(Entity entity) {
		super(entity);
		name = "Flamethrower";
		desc = "Spew fire in the direction you're facing.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(5, 0.5f, 10, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new Flamethrower(entity, entity.pos.cpy(), getAngle(entity.pos, entity.getTargetLocation())), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
