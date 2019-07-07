package com.mygdx.game.serialisation;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

class ObjectNoTransientsSerialiser extends FieldSerializer<Object> {

	ObjectNoTransientsSerialiser(Kryo kryo, Class type) {
		super(kryo, type);
		setCopyTransient(false);
	}

}
