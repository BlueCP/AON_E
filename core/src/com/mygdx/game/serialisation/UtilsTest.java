package com.mygdx.game.serialisation;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Queue;
import com.cyphercove.gdx.gdxtokryo.GdxToKryo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class UtilsTest {

	static Kryo kryo;

	public static void test() {
		UtilsTest test = new UtilsTest();
		try {
			kryo = new Kryo();
			kryo.setRegistrationRequired(true);
			kryo.register(Object.class);
			kryo.register(Object[].class);
			GdxToKryo.registerAll(kryo, 50);
		} catch (Exception e) {
			e.printStackTrace();
		}
		test.testEmptyQueue();
	}

	public void testEmptyQueue (){
		Queue<GridPoint2> queue = new Queue<GridPoint2>();

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		Output output = new Output(outStream);
		kryo.writeClassAndObject(output, queue);
		output.close();
		byte[] outBytes = outStream.toByteArray();

		ByteArrayInputStream inStream = new ByteArrayInputStream(outBytes);
		Input input = new Input(inStream);
		Queue<GridPoint2> object1 = (Queue)kryo.readClassAndObject(input);
		input.close();

		object1.addFirst(new GridPoint2());
	}

}