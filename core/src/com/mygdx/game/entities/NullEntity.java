package com.mygdx.game.entities;

import com.mygdx.game.screens.PlayScreen;

public class NullEntity extends Entity {

	NullEntity() {
		id = -1;
	}

	@Override
	public void individualUpdate(PlayScreen session) {

	}

	@Override
	public void interact(PlayScreen session) {

	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}

}
