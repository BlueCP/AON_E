package com.mygdx.game.entities.characters;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;

public class SilverStatue extends Entity {

	static final float frameInterval = 0.0417f;
	static final int fps = 24;
	static final int directions = 16;

	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public SilverStatue() { }

	public SilverStatue(Entities entities, Vector3 pos) {
		generateId(entities);
		name = "Silver statue";
		life = 10; maxLife = 10;
		spirit = 10; maxSpirit = 10;
		baseWalkSpeed = 1.4f;

		interactive = false;

		Matrix4 matrix = new Matrix4();
		matrix.setTranslation(pos);
		rigidBodyMatrix = matrix;

		loadRigidBody();
		rigidBody.setWorldTransform(rigidBody.getWorldTransform().setTranslation(new Vector3(4, 1, 4)));
	}

	@Override
	public void individualUpdate(PlayScreen session) {
		stunnedEffect.addThisTick();
	}

	@Override
	public void procDeathEffects(PlayScreen playScreen) {
		procDeathEffectsBase(playScreen);
		playScreen.droppedItemManager.addDroppedOtherItemFlyUp("Silverheart", pos, 3, playScreen.physicsManager.getDynamicsWorld());
	}

	@Override
	public void interact(PlayScreen session) {

	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}

}
