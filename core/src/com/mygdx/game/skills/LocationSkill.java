package com.mygdx.game.skills;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.entityactions.SkillAction;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.Util;

/**
 * A locational skill uses the entity's target location to influence the skill, such as where to summon
 * a field of magic.
 */
public abstract class LocationSkill extends ActiveSkill {

	protected Vector3 targetPos;

	private Entity.AnimationType animationType;
	private float time;
	private int cost;
	private float range;

	protected LocationSkill(Entity entity) {
		super(entity);
	}

	protected void defaultStart(int cost, float time, float range, Entity.AnimationType animationType, PlayScreen playScreen) {
//		this.targetPos = targetPos;
		if (entity.actions.size == 0 && state == State.AVAILABLE) {
			this.animationType = animationType;
			this.time = time;
			this.cost = cost;
			this.range = range;
			putOnStandby();
		}
	}

	@Override
	public void update(PlayScreen playScreen) {
		if (state == State.ON_STANDBY && playScreen.player.hasTargetedLocationThisTick() && entity.pos.dst(entity.getTargetLocation()) <= range) { // If the player has just targeted a location
			if (hasResource(entity, cost)) {
				useSkill();
//				Array<EntityAction> array = new Array<>();
//				array.add(new SkillAction(this, animationType, time));
				entity.actions.addLast(new SkillAction(this, animationType, time));
				entity.setAnimationType(animationType);
				targetPos = entity.getTargetLocation();
				faceTarget(targetPos);
			}
		}
	}

	protected float getAngle(Vector3 startPos, Vector3 endPos) {
		Vector3 diff = endPos.cpy().sub(startPos);
		float rotation = (float) Math.toDegrees(MathUtils.atan2(diff.z, diff.x));

		rotation += 90;
		if (rotation < 0) {
			rotation += 360;
		}
		return rotation;
	}

	protected void faceTarget(Vector3 targetPos) {
		Vector3 diff = targetPos.cpy().sub(entity.pos);
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
