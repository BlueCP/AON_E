package com.mygdx.game.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.screens.PlayScreen;

public class StoneDemidemon extends Entity {
	
	public StoneDemidemon() {
		super.setName("Stone Demidemon");
		super.setLife(15);
//		super.basePhysDmg = 5;
		super.setBehaviour(Behaviour.IDLE);
		super.setNature(Nature.NEUTRAL);
	}

	@Override
	public void individualUpdate(PlayScreen session) {
		if (nature == Nature.AGGRESSIVE && isFollowingPlayer()) {
			//this.follow(session, session.player);
			attack(session.player, 1, session);
		} else if (nature == Nature.FRIENDLY) {
			if (session.player.getClosestOffensiveEnemy() == null) {
				//this.follow(session, session.player);
			} else {
				//this.follow(session, session.player.getClosestOffensiveEnemy());
				attack(session.player.getClosestOffensiveEnemy(), 1, session);
			}
		}
		// Does not wander
	}
	
	@Override
	public void onInteract(PlayScreen session) {
		if (nature == Nature.AGGRESSIVE) {
			session.newMessage("The Stone Demidemon looks at you with hatred.");
		} else if (nature == Nature.FRIENDLY) {
			session.newMessage("The Stone Demidemon looks upon you with kindness.");
		} else if (ThreadLocalRandom.current().nextInt(1, 101) <= 50) {
			super.setNature(Nature.FRIENDLY);
			super.setBehaviour(Behaviour.FOLLOW);
			session.newMessage("You have befriended the Stone Demidemon!", Color.GREEN);
		} else {
			super.setNature(Nature.AGGRESSIVE);
			super.setBehaviour(Behaviour.FOLLOW);
			session.newMessage("The Stone Demidemon has turned on you!", Color.RED);
		}
		// 50/50 chance of becoming the player's ally or enemy
	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {
		basicAttack(target, range, session);
	}
	
}
