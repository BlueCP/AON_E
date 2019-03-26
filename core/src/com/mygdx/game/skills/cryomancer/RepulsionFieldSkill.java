package com.mygdx.game.skills.cryomancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.RepulsionField;
import com.mygdx.game.projectiles.pyromancer.Heatwave;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.SimpleSkill;

public class RepulsionFieldSkill extends SimpleSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public RepulsionFieldSkill() {
		super(null);
	}

	public RepulsionFieldSkill(Entity entity) {
		super(entity);
		name = "Repulsion Field";
		desc = "Knock back and stun nearby enemies.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		defaultStart(10, 0.5f, Entity.AnimationType.SHOOT_PROJECTILE);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		playScreen.projectileManager.addProjectileNow(new RepulsionField(entity, entity.pos), playScreen.physicsManager.getDynamicsWorld());
		putOnCooldown(3);
	}

}
