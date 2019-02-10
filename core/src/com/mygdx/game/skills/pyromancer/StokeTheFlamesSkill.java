package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entityattributes.Effect;
import com.mygdx.game.skills.PassiveSkill;

public class StokeTheFlamesSkill extends PassiveSkill {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public StokeTheFlamesSkill() {
		super(null, false);
	}

	public StokeTheFlamesSkill(Entity entity, boolean learned) {
		super(entity, learned);
	}

	public void burn(Entity entity, int power, float duration) {
		if (isLearned()) {
			entity.findProcEffect(Effect.EffectType.BURNING).add(power, duration * 2);
		} else {
			entity.findProcEffect(Effect.EffectType.BURNING).add(power, duration);
		}
	}

	public void damage(Entity entity, float damage) {
		if (entity.findProcEffect(Effect.EffectType.BURNING).powers.size > 0){
			entity.takeDamage(this.entity, damage * 2);
		} else {
			entity.takeDamage(this.entity, damage);
		}
	}

}
