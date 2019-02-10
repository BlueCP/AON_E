package com.mygdx.game.entities;

import com.mygdx.game.screens.PlayScreen;

public class Bandit extends Entity {
	
	public Bandit() {
		super.setName("Bandit");
		super.setLife(6);
		super.basePhysDmg = 3;
		super.setBehaviour(Behaviour.FOLLOW);
		super.setNature(Nature.AGGRESSIVE);
	}

	@Override
	public void dealDamageNoCheck(Entity entity, float damage) {

	}

	@Override
	public void takeDamage(Entity entity, float damage) {

	}

	@Override
	public void onUpdate(PlayScreen session) {
		if (this.isFollowingPlayer()) {
			//follow(session, session.player);
			/*
			if (chargeTime < 1) {
				attack(session.player, 5, session);
			}
			*/
		} else {
			//wander(session.map);
		}
	}

	@Override
	public void onInteract(PlayScreen session) {
		session.newMessage("The bandit looks at you slyly.");
	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {
		basicAttack(target, range, session);
		//setChargeTime(2); // One more than needed
	}
	
}
