package com.mygdx.game.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.screens.PlayScreen;

public class Dummy extends Entity {

	static final float frameInterval = 0.0417f;
	static final int fps = 24;
	public static final int directions = 16;
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	private Dummy() { }

	public Dummy(Entities entities, Vector3 pos) {
		generateId(entities);
		name = "dummy";
		life = 10; maxLife = 10;
		spirit = 10; maxSpirit = 10;
		baseWalkSpeed = 1.4f;

		interactive = true;
		speechBubbleText = "Hey! Come over here!";
		
		Matrix4 matrix = new Matrix4();
		matrix.setTranslation(pos);
		rigidBodyMatrix = matrix;
		
		loadRigidBody(); // We need to load rigid body here because unlike the player, a new Dummy may be added mid-game.
		rigidBody.setWorldTransform(rigidBody.getWorldTransform().setTranslation(new Vector3(4, 1, 4)));
	}
	
	@Override
	public void individualUpdate(PlayScreen session) {
//		stunnedEffect.add(1); // To stop the dummy from constantly walking to (0, 0, 0).
		showSpeechBubble = pos.dst(session.player.pos) <= 5;
	}

	@Override
	public void interact(PlayScreen session) {
		session.dialogManager.startDialog("Welcome", session.hudStage);
	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}

}
