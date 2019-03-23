package com.mygdx.game.skills;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.utils.Util;

/**
 * A directional skill is one which uses the player's facing direction to influence properties of the spell,
 * such as which direction a projectile should face.
 */
public abstract class DirectionSkill extends ActiveSkill {

	protected DirectionSkill(Entity entity) {
		super(entity);
	}

	protected void defaultStart(int cost, float time, Entity.AnimationType animationType) {
		if (entity.actions.size == 0 && state == State.AVAILABLE) {
			if (hasResource(entity, cost)) {
				useSkill();
//				Array<EntityAction> array = new Array<>();
//				array.add(new SkillAction(this, animationType, time));
				entity.actions.addLast(new SkillAction(this, animationType, time));
				entity.setAnimationType(animationType);
			}
		}
	}

	protected float getFacingAngle() {
		float angle = Util.toAngle(entity.getFacing().toString());
		angle += 45;
		if (angle >= 360) {
			angle -= 360;
		}
		return angle;
	}

}
