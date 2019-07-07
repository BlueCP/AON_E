package com.mygdx.game.entities.characters;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.quests.Quest;
import com.mygdx.game.screens.PlayScreen;

public class Deltis extends Entity {

	static final float frameInterval = 0.0417f;
	static final int fps = 24;
	static final int directions = 16;

	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public Deltis() { }

	public Deltis(Entities entities, Vector3 pos) {
		generateId(entities);
		name = "Deltis";
		life = 10; maxLife = 10;
		spirit = 10; maxSpirit = 10;
		baseWalkSpeed = 1.4f;

		interactive = true;
		speechBubbleText = "Traveller of time, I've been wanting to speak with you.";

		Matrix4 matrix = new Matrix4();
		matrix.setTranslation(pos);
		rigidBodyMatrix = matrix;

		loadRigidBody();
		rigidBody.setWorldTransform(rigidBody.getWorldTransform().setTranslation(new Vector3(4, 1, 4)));
	}

	@Override
	public void individualUpdate(PlayScreen session) {
		stunnedEffect.addThisTick();
		float distance = pos.dst(session.player.pos);
		showSpeechBubble = distance <= 5 && distance >= 2;
	}

	@Override
	public void interact(PlayScreen session) {
		Quest quest = session.quests.getQuest("Search for the Silverheart");
		if (!quest.isCompleted() && !quest.isInProgress()) {
			session.dialogManager.startDialog("Assistance", session.hudStage);
		} else if (session.player.inventory.contains("Silverheart")) {
			session.dialogManager.startDialog("Gratitude", session.hudStage);
		} else if (quest.isInProgress()) {
			session.dialogManager.startDialog("Journey", session.hudStage);
		}
	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}

}
