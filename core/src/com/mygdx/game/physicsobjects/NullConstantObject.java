package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class NullConstantObject extends ConstantObject {

	public NullConstantObject() {
		super(new btCollisionObject(), new TextureRegion[1], "0", null, 0, 0);
	}

	@Override
	public void save(Kryo kryo, Output output) {

	}

	@Override
	public void load(Kryo kryo, Input input) {

	}

}
