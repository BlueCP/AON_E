package com.mygdx.game.serialisation;

import com.badlogic.gdx.utils.Queue;
import com.cyphercove.gdx.gdxtokryo.gdxserializers.utils.QueueSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class MyQueueSerialiser extends QueueSerializer {

	@Override
	public Queue read(Kryo kryo, Input input, Class<Queue> type) {
		Queue queue = super.read(kryo, input, type);
		queue.ensureCapacity(1); // For some reason the values field of queue is messed up unless you manually resize it.
		return queue;
	}

}
