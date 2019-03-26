package com.mygdx.game.skills;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Util;

/**
 * A skill which targets one specific entity, usually the target entity of the caster.
 */
public abstract class TargetedSkill extends ActiveSkill {

	protected int targetEntity;

	protected TargetedSkill(Entity entity) {
		super(entity);
	}

	protected void defaultStart(int cost, float time, Entity.AnimationType animationType, PlayScreen playScreen) {
		if (entity.actions.size == 0 && playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player).id != -1 && state == State.AVAILABLE) {
			if (hasResource(entity, cost)) {
				useSkill();
				targetEntity = entity.getTargetEntity();
//				Array<EntityAction> array = new Array<>();
//				array.add(new SkillAction(this, animationType, time));
				entity.actions.addFirst(new SkillAction(this, animationType, time));
				entity.setAnimationType(animationType);
				faceTarget(playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player));
			}
		}
	}

	protected void faceTarget(Entity target) {
		Vector3 diff = target.pos.cpy().sub(entity.pos);
		float angle = (float) Math.toDegrees(MathUtils.atan2(diff.z, diff.x));

		if (angle < 0) {
			angle += 360;
		}
		angle = (float) (Math.round(angle/22.5) * 22.5); // Round to nearest 22.5 degrees (which is 360/number of directions)
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
