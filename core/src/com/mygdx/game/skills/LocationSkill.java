package com.mygdx.game.skills;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;

/**
 * A locational skill uses the entity's target location to influence the skill, such as where to summon
 * a field of magic.
 */
public abstract class LocationSkill extends ActiveSkill {

	protected Vector3 targetPos;

	protected LocationSkill(Entity entity) {
		super(entity);
	}

	protected void defaultStart(int cost, float time, float range, Entity.AnimationType animationType, Vector3 targetPos) {
		this.targetPos = targetPos;
		if (entity.actions.size == 0 && entity.pos.dst(targetPos) <= range && state == State.AVAILABLE) {
			if (hasResource(entity, cost)) {
				useSkill();
				Array<EntityAction> array = new Array<>();
				array.add(new SkillAction(this, animationType, time));
				entity.actions.addLast(array);
				entity.setAnimationType(animationType);
			}
		}
	}

}
