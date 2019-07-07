package com.mygdx.game.physicsobjects.immobileinteractives;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.physicsobjects.ImmobileInteractive;
import com.mygdx.game.screens.PlayScreen;

/**
 * This is an interactive object that saves the game when the player clicks on it.
 * It also functions as a respawn point for the player, since the player respawns at the last saved position.
 */
public class SaveInteractive extends ImmobileInteractive {

	public SaveInteractive(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags) {
		super(collisionObject, texture, id, tags);
	}

	@Override
	public void clicked(PlayScreen playScreen) {
		if (!playScreen.gameSavingThread.isAlive()) {
			playScreen.gameSavingThread.start();
		}
	}

	@Override
	public void save(Kryo kryo, Output output) {

	}

	@Override
	public void load(Kryo kryo, Input input) {

	}

}
