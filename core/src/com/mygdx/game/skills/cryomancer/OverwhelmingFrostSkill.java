package com.mygdx.game.skills.cryomancer;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class OverwhelmingFrostSkill extends TargetedSkill {

	private static final float freezeDuration = 3;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public OverwhelmingFrostSkill() {
		super(null);
	}

	public OverwhelmingFrostSkill(Entity entity) {
		super(entity);
		name = "Overwhelming Frost";
		desc = "Freeze a target chilled enemy in place.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		if (entity.getTargetEntity() != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity());
			if (entity.actions.size == 0 && targetEntity.chilledEffect.isActive()) {
				if (hasResource(entity, 20)) {
					useSkill();
					Array<EntityAction> array = new Array<>();
					array.add(new SkillAction(this, Entity.AnimationType.SHOOT_PROJECTILE, 0.7f));
//					array.add(new SkillAction(this, 0.7f));
					entity.actions.addFirst(array);
					entity.setAnimationType(Entity.AnimationType.SHOOT_PROJECTILE);
					faceTarget(targetEntity);
				}
			}
		}
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (entity.getTargetEntity() != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity());
			if (targetEntity.chilledEffect.isActive()) {
				targetEntity.frozenEffect.add(freezeDuration);
				entity.landAbility(targetEntity, playScreen);
				playScreen.particleEngine.addFlyUpPillar(playScreen.physicsManager.getDynamicsWorld(), targetEntity.pos, 25, 6,
						1.5f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
				playScreen.isoRenderer.camera.screenShake(0.4f, 0.4f);
				putOnCooldown(3);
				return;
			}
		}
		failResolve();
	}

}
