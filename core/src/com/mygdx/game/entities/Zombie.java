package com.mygdx.game.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.mygdx.game.screens.PlayScreen;

public class Zombie extends Entity {
	
	public Zombie() {
		super.setName("Zombie");
		super.setLife(10);
		super.basePhysDmg = 5;
		super.setBehaviour(Behaviour.FOLLOW);
		super.setNature(Nature.AGGRESSIVE);
	}

	@Override
	public void onUpdate(PlayScreen session) {
		if (isFollowingPlayer()) {
			int chance = ThreadLocalRandom.current().nextInt(1, 101);
			if (chance <= 80) {
				//this.follow(session, session.player);
				attack(session.player, 1, session);
			} else {
				//
			}
		} else {
			//this.wander(session.map);
		}
	}

	@Override
	public void onInteract(PlayScreen session) {
		session.newMessage("The zombie growls and stumbles towards you.");
	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {
		basicAttack(target, range, session);
	}
	
}
