package com.mygdx.game.physicsobjects.immobilecontrollables;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.physicsobjects.ImmobileControllable;
import com.mygdx.game.screens.PlayScreen;

public class LightControllable extends ImmobileControllable {

	public LightControllable(btCollisionObject collisionObject, TextureRegion[] texture, String id, Array<PhysicsManager.Tag> tags, Array<String> ports) {
		super(collisionObject, texture, id, tags, ports);
	}

	@Override
	public void update(PlayScreen playScreen) {
		int portValue = playScreen.physicsManager.controllableFlags.get(ports.first());
		setAnimationFrame(portValue);
	}

	@Override
	public void save(Kryo kryo, Output output) {

	}

	@Override
	public void load(Kryo kryo, Input input) {

	}

}
