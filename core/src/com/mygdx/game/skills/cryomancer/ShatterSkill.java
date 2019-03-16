package com.mygdx.game.skills.cryomancer;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.TargetedSkill;

public class ShatterSkill extends TargetedSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ShatterSkill() {
		super(null);
	}

	public ShatterSkill(Entity entity) {
		super(entity);
		name = "Shatter";
		desc = "Target chilled enemy loses all stacks of chill, then deals damage in an area for each chill stack lost.";
	}

	@Override
	public void start(PlayScreen playScreen) {
		Entity target = playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player);
		if (target.id != -1) {
			if (entity.actions.size == 0 && target.chilledEffect.isActive()) {
				if (hasResource(entity, 20)) {
					useSkill();
					targetEntity = entity.getTargetEntity();
					Array<EntityAction> array = new Array<>();
					array.add(new SkillAction(this, Entity.AnimationType.SHOOT_PROJECTILE, 0.7f));
					entity.actions.addFirst(array);
					entity.setAnimationType(Entity.AnimationType.SHOOT_PROJECTILE);
					faceTarget(target);
				}
			}
		}
	}

	@Override
	public void finish(PlayScreen playScreen) {
		Entity target = playScreen.entities.getEntity(targetEntity, playScreen.player);
		if (target.id != -1) {
			if (target.chilledEffect.isActive()) {
				playScreen.projectileManager.addShatterExplosion(entity, playScreen.physicsManager.getDynamicsWorld(),
						target.pos, target.chilledEffect.numStacks());
				target.chilledEffect.remove();
				putOnCooldown(2);
				return;
			}
		}
		failResolve();
	}

}
