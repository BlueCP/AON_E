package com.mygdx.game.skills.pyromancer;

import com.mygdx.game.entities.Entity;
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
		name = "Stoke the Flames";
		desc = "Enemies burn for longer, and you deal more damage to burning enemies.";
	}

	public void burn(Entity entity, int power, float duration) {
		if (isLearned()) {
			entity.burningEffect.add(power, duration * 2);
		} else {
			entity.burningEffect.add(power, duration);
		}
	}

	public float damage(Entity entity, float damage) {
		/*if (entity.burningEffect.powers.size > 0){
			entity.takeDamage(this.entity, damage * 2);
		} else {
			entity.takeDamage(this.entity, damage);
		}*/
		if (isLearned() && entity.burningEffect.isActive()) {
			return damage;
		} else {
			return 0;
		}
	}

}
