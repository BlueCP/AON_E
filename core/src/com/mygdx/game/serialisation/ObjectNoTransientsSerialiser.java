package com.mygdx.game.serialisation;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

public class ObjectNoTransientsSerialiser extends FieldSerializer<Object> {

	public ObjectNoTransientsSerialiser(Kryo kryo, Class type) {
		super(kryo, type);
		setCopyTransient(false);
	}

}
