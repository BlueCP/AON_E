package com.mygdx.game.skills.pyromancer;

import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.projectiles.pyromancer.SupernovaExplosion;
import com.mygdx.game.skills.PassiveSkill;

import javax.swing.text.PlainDocument;

public class SupernovaSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public SupernovaSkill() {
		super(null, false);
	}

	public SupernovaSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Supernova";
		desc = "Killing burning enemies causes them to explode, dealing damage to all nearby enemies.";
	}

	public void testfor(Entity entity, ProjectileManager projectileManager, btDynamicsWorld dynamicsWorld) {
		if (isLearned() && entity.burningEffect.powers.size > 0) {
			projectileManager.addProjectileNow(new SupernovaExplosion(this.entity, entity.pos), dynamicsWorld);
		}
	}

}
