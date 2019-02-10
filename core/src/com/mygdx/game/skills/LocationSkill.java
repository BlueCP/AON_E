package com.mygdx.game.skills;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;

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
//				this.targetPos = targetPos;
				Array<EntityAction> array = new Array<>();
				array.add(new SkillAction(this, animationType, time));
//				System.out.println(entity.actions.size);
//				System.out.println(array.size);
				entity.actions.addLast(array);
				entity.setAnimationType(animationType);
			}
		}
	}

}
