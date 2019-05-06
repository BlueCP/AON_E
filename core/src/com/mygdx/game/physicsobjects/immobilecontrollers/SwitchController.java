package com.mygdx.game.physicsobjects.immobilecontrollers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.physicsobjects.ImmobileController;
import com.mygdx.game.screens.PlayScreen;

public class SwitchController extends ImmobileController {

	public SwitchController(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags, Array<String> ports, int startingState) {
		super(collisionObject, texture, id, tags, ports, startingState);
	}

	@Override
	public void update(PlayScreen playScreen) {
		int portValue = playScreen.physicsManager.controllableFlags.get(ports.first());
		setAnimationFrame(portValue);
	}

	@Override
	public void clicked(PlayScreen playScreen) {
		if (playScreen.physicsManager.controllableFlags.get(ports.first()) == 0) {
			playScreen.physicsManager.controllableFlags.put(ports.first(), 1);
		} else {
			playScreen.physicsManager.controllableFlags.put(ports.first(), 0);
		}
	}

	@Override
	public void save(Kryo kryo, Output output) {

	}

	@Override
	public void load(Kryo kryo, Input input) {

	}

	@Override
	public void initialise(PhysicsManager physicsManager) {
		for (int port: ports) {
			physicsManager.controllableFlags.put(port, startingState);
		}
	}

}
