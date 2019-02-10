package com.mygdx.game.interactiveobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physicsobjects.InteractiveObject;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.stages.WorkbenchStage;

public class Workbench extends InteractiveObject {

	protected Workbench(btCollisionObject collisionObject, Sprite[] sprite, String id, Array<Tag> newTags, int spriteX,
			int spriteY) {
		super(collisionObject, sprite, id, newTags, spriteX, spriteY);
	}

	@Override
	public void interact(PlayScreen playScreen) {
		playScreen.ownStages.add(new WorkbenchStage(playScreen.game, playScreen.player.inventory()));
		Gdx.input.setInputProcessor(new InputMultiplexer(playScreen.ownStages.get(playScreen.ownStages.size - 1).stage, playScreen));
	}

}