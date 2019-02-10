package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.DirectionSkill;

public class FlamethrowerSkill extends DirectionSkill {

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
		defaultStart(5, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addFlamethrower(entity, playScreen.physicsManager.getDynamicsWorld(), entity.pos.cpy(), getFacingAngle());
		putOnCooldown(3);
	}

}
