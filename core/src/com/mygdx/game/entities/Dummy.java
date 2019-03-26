package com.mygdx.game.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.screens.PlayScreen;

public class Dummy extends Entity {

	static final float frameInterval = 0.0417f;
	static final int fps = 24;
	static final int directions = 16;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public Dummy() { }

	public Dummy(Entities entities, Vector3 pos) {
		generateId(entities);
		name = "dummy";
		life = 10; maxLife = 10;
		spirit = 10; maxSpirit = 10;
		baseWalkSpeed = 1.4f;
		
		Matrix4 matrix = new Matrix4();
		matrix.setTranslation(pos);
		rigidBodyMatrix = matrix;
		
		loadRigidBody(); // We need to load rigid body here because unlike the player, a new Dummy may be added mid-game.
	}
	
	@Override
	public void onUpdate(PlayScreen session) {
		stunnedEffect.add(1); // To stop the dummy from constantly walking to (0, 0, 0).
	}

	@Override
	public void onInteract(PlayScreen session) {

	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}

}
