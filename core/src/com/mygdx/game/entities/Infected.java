package com.mygdx.game.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.mygdx.game.screens.PlayScreen;

public class Infected extends Entity {
	
	public Infected() {
		super.setName("Infected");
		super.setLife(15);
//		super.basePhysDmg = 5;
		super.setBehaviour(Behaviour.FOLLOW);
		super.setNature(Nature.AGGRESSIVE);
	}

	@Override
	public void takeDamage(Entity entity, float damage) {

	}

	@Override
	public void individualUpdate(PlayScreen session) {
		if (isFollowingPlayer()) {
			//this.follow(session, session.player);
		} else {
			//this.wander(session.map);
		}
	}
	
	@Override
	public void interact(PlayScreen session) {
		session.newMessage("The infected looks at you ravenously.");
	}
	
	@Override
	public void attack(Entity target, int range, PlayScreen session) {
		if (basicAttack(target, 1, session) && ThreadLocalRandom.current().nextInt(1, 101) <= 20) {
//			target.changeEffect(EffectType.POISONED, 5, 1);
		}
	}
	
}