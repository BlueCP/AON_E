package com.mygdx.game.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.mygdx.game.screens.PlayScreen;

public class Cat extends Entity {
	
	public Cat(Entities entities) {
		generateId(entities);
		super.setName("Cat");
		super.setLife(20);
//		super.basePhysDmg = 0;
		super.setBehaviour(Behaviour.IDLE);
		super.setNature(Nature.PASSIVE);
	}

	@Override
	public void individualUpdate(PlayScreen session) {
		if (this.isFollowingPlayer()) {
			//this.follow(session, session.player);
		} else {
			//this.wander(session.map);
		}
	}

	@Override
	public void interact(PlayScreen session) {
		if (ThreadLocalRandom.current().nextInt(1, 101) <= 20) { // 20% chance of taming
			super.setBehaviour(Behaviour.FOLLOW);
			session.newMessage("You have tamed the cat!");
		} else {
			session.newMessage("The cat looks at you curiously.");
		}
	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}

	@Override
	public void takeDamage(Entity entity, float damage) {

	}

}
