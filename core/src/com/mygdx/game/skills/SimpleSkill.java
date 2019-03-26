package com.mygdx.game.skills;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.SkillAction;

/**
 * A 'simple' skill is one where all that is required to do to activate it is press a button.
 * There is no targeting of any sort involved.
 */
public abstract class SimpleSkill extends ActiveSkill {

	protected SimpleSkill(Entity entity) {
		super(entity);
	}

	protected void defaultStart(int cost, float time, Entity.AnimationType animationType) {
		if (entity.actions.size == 0 && state == State.AVAILABLE) {
			if (hasResource(entity, cost)) {
				useSkill();
//				Array<EntityAction> array = new Array<>();
//				array.add(new SkillAction(this, animationType, time));
				entity.actions.addFirst(new SkillAction(this, animationType, time));
				entity.setAnimationType(animationType);
			}
		}
	}

}
