package com.mygdx.game.serialisation;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.mygdx.game.entities.Player;

public class PlayerSerialiser extends Serializer<Player> {

	@Override
	public void write(Kryo kryo, Output output, Player object) {
		kryo.writeObject(output, object);
	}

	@Override
	public Player read(Kryo kryo, Input input, Class<Player> type) {
		Player player = new Player();
		kryo.reference(player);
		player = kryo.readObject(input, Player.class, new FieldSerializer(kryo, Player.class));
		return player;
	}

}
