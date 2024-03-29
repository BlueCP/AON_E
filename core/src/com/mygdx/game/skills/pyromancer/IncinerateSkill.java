package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class IncinerateSkill extends TargetedSkill {

	private static final int damage = 5;

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private IncinerateSkill() {
		super(null);
	}

	public IncinerateSkill(Entity entity) {
		super(entity);
		name = "Incinerate";
		desc = "Deal high damage to a single burning enemy.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		if (entity.getTargetEntity() != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			if (entity.actions.size == 0 && targetEntity.burningEffect.isActive()) {
				if (hasResource(entity, 20)) {
					useSkill();
//					Array<EntityAction> array = new Array<>();
//					array.add(new SkillAction(this, Entity.AnimationType.SHOOT_PROJECTILE, 0.7f));
//					array.add(new SkillAction(this, 0.7f));
					entity.actions.addFirst(new SkillAction(this, Entity.AnimationType.SHOOT_PROJECTILE, 0.7f));
					entity.setAnimationType(Entity.AnimationType.SHOOT_PROJECTILE);
					faceTarget(targetEntity);
				}
			}
		}
	}

	@Override
	public void finish(PlayScreen playScreen) {
		if (entity.getTargetEntity() != -1) {
			Entity targetEntity = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
			if (targetEntity.burningEffect.powers.size > 0) {
//				entity.dealDamage(targetEntity, damage);
				entity.dealAbilityDamage(targetEntity, damage, playScreen);
				entity.landAbility(targetEntity, playScreen);
//				targetEntity.dealDamageOLD(entity.id, playScreen.player, damage);
//				playScreen.player.flamingBarrage.testfor(entity.id);
//				playScreen.particleEngine.addFireUp(playScreen.physicsManager.getDynamicsWorld(), targetEntity.pos, 25, 6);
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
