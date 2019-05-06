package com.mygdx.game.skills.electromancer;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.electromancer.EnergySurge;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class EnergySurgeSkill extends TargetedSkill {

	private Projectile projectile;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public EnergySurgeSkill() {
		super(null);
	}

	public EnergySurgeSkill(Entity entity) {
		super(entity);
		name = "Energy Surge";
		desc = "Fire a high energy beam of electricity at a single enemy, draining your spirit over time.";
	}

	@Override
	public void update(PlayScreen playScreen) {
		if (state == State.ON_STANDBY) { // If the beam is active
			if (playScreen.entities.getEntity(targetEntity, playScreen.player).id == -1) {
				stop(playScreen);
			} else {
				entity.changeSpirit(-5 * Gdx.graphics.getDeltaTime());
			}
		}
	}

	@Override
	public void start(PlayScreen playScreen) {
		/*if (entity.actions.size == 0 && playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player).id != -1 && state == State.AVAILABLE) {
			if (hasResource(entity, 5)) {
				useSkill();
				targetEntity = entity.getTargetEntity();
				entity.actions.addFirst(new SkillAction(this, Entity.AnimationType.SHOOT_PROJECTILE, 0.7f));
				entity.setAnimationType(Entity.AnimationType.SHOOT_PROJECTILE);
				faceTarget(playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player));
			}
		}*/
		defaultStart(5, 0.7f, Entity.AnimationType.SHOOT_PROJECTILE, playScreen);
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (playScreen.entities.getEntity(targetEntity, playScreen.player).id != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			projectile = new EnergySurge(entity, targetEntity);
			playScreen.projectileManager.addProjectileNow(projectile, playScreen.physicsManager.getDynamicsWorld());

			removeAction(); // Remove the old action, then add the new action (next line).
			entity.actions.addFirst(new SkillAction(this, Entity.AnimationType.SHOOT_PROJECTILE, Integer.MAX_VALUE));
			entity.setAnimationType(Entity.AnimationType.STAND);
			faceTarget(playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player));

			putOnStandby();
		} else {
			failResolve();
		}
	}

	@Override
	public void stop(PlayScreen playScreen) {
		putOnCooldown(3);
		projectile.destroy(playScreen);
		removeAction();
	}

}
