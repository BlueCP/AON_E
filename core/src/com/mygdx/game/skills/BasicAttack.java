package com.mygdx.game.skills;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.BasicAttackAction;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Util;

/**
 * Each class has one basic attack, which is effectively just a special case of the Active Skill.
 */
public abstract class BasicAttack extends ActiveSkill {

	protected int targetEntity;

	protected BasicAttack(Entity entity) {
		super(entity);
	}

	protected void defaultStart(float time, Entity.AnimationType animationType, PlayScreen playScreen) {
		if (entity.actions.size == 0 && playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player).id != -1 && state == State.AVAILABLE) {
			if (entity.pos.dst(playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player).pos) <= entity.equipped().getWeapon().getRange()) {
				useSkill();
				targetEntity = entity.getTargetEntity();
//				Array<EntityAction> array = new Array<>();
//				array.add(new BasicAttackAction(this, animationType, time));
				entity.actions.addFirst(new BasicAttackAction(this, animationType, time));
				entity.setAnimationType(animationType);
				entity.resetAnimation(); // Cut the animation back to the beginning; necessary for automatic basic attacks.
				faceTarget(playScreen.entities.getEntity(entity.getTargetEntity(), playScreen.player));
			}
		}
	}

	protected void nextBasicAttack(float time, Entity.AnimationType animationType, PlayScreen playScreen) {
		if (entity.actions.size > 0) {
			if (entity.actions.first().getName().equals(name)) { // If the current action is this basic attack
				entity.actions.clear(); // Remove the finished basic attack.
				makeAvailable(); // Make the basic attack skill available again.
				defaultStart(time, animationType, playScreen); // Start the next basic attack.
			}
		}
	}

	private void faceTarget(Entity target) {
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
