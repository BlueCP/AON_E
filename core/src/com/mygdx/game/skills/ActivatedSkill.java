package com.mygdx.game.skills;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;

public abstract class ActivatedSkill extends ActiveSkill {

	protected ActivatedSkill(Entity entity) {
		super(entity);
	}

	protected void defaultStart(int cost, float time, Entity.AnimationType animationType) {
		if (entity.actions.size == 0 && state == State.AVAILABLE) {
			if (hasResource(entity, cost)) {
				useSkill();
				Array<EntityAction> array = new Array<>();
				array.add(new SkillAction(this, animationType, time));
				entity.actions.addFirst(array);
				entity.setAnimationType(animationType);
			}
		}
	}

}
