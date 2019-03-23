package com.mygdx.game.physicsobjects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class NullConstantObject extends ConstantObject {

	public NullConstantObject() {
		super();
	}

	@Override
	public void save(Kryo kryo, Output output) {

	}

	@Override
	public void load(Kryo kryo, Input input) {

	}

}
