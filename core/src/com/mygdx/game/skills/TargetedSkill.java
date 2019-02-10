package com.mygdx.game.skills;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.utils.Util;

public abstract class TargetedSkill extends ActiveSkill {

	protected int targetEntity;

	protected TargetedSkill(Entity entity) {
		super(entity);
	}

	protected void defaultStart(int cost, float time, Entity.AnimationType animationType, Entities entities) {
		if (entity.actions.size == 0 && entities.getEntity(entity.getTargetEntity()) != null && state == State.AVAILABLE) {
			if (hasResource(entity, cost)) {
				useSkill();
				targetEntity = entity.getTargetEntity();
				Array<EntityAction> array = new Array<>();
				array.add(new SkillAction(this, animationType, time));
				entity.actions.addFirst(array);
				entity.setAnimationType(animationType);
				faceTarget(entities.getEntity(entity.getTargetEntity()));
			}
		}
	}

	protected void faceTarget(Entity target) {
		Vector3 diff = target.pos.cpy().sub(entity.pos);
		float angle = (float) Math.toDegrees(MathUtils.atan2(diff.z, diff.x));
//		System.out.println("---");
//		System.out.println(angle);

		if (angle < 0) {
			angle += 360;
		}
//		System.out.println(angle);
		angle = (float) (Math.round(angle/22.5) * 22.5); // Round to nearest 22.5 degrees (which is 360/number of directions)
//		System.out.println(angle);
		angle += 45;
		if (angle >= 360) {
			angle -= 360;
		}
		String facing = Util.toFacing(angle); // Convert to the format for 'facing'
		for (Entity.Facing facing0: Entity.Facing.values()) {
			if (facing.equals(facing0.toString())) {
				entity.setFacing(facing0);
			}
		}
	}

}
