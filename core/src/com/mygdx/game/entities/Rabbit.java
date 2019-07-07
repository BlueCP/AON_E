package com.mygdx.game.entities;

import com.mygdx.game.screens.PlayScreen;

public class Rabbit extends Entity {
	
	public Rabbit() {
		super.name = "Rabbit";
		super.setLife(5);
//		super.basePhysDmg = 0;
		super.setBehaviour(Behaviour.FLEE);
		super.setNature(Nature.PASSIVE);
	}

	@Override
	public void individualUpdate(PlayScreen session) {

	}

	@Override
	public void interact(PlayScreen session) {
		session.newMessage("The rabbit looks at you in fear.");
	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}
	
}
