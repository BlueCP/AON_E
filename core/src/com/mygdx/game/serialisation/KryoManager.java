package com.mygdx.game.serialisation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.cyphercove.gdx.gdxtokryo.GdxToKryo;
import com.cyphercove.gdx.gdxtokryo.gdxserializers.utils.ObjectMapSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class KryoManager {

	private static Kryo kryo;
	
	public static void initialise() {
		kryo = new Kryo();
//		kryo.setRegistrationRequired(true); // Although this is 'recommended', it makes it a nightmare to create new classes since they all require a custom serialiser.
		GdxToKryo.registerAll(kryo);
		kryo.register(Queue.class, new MyQueueSerialiser());
//		kryo.register(Array.class, new ArraySerialiser());
//		kryo.register(ObjectMap.class, new ObjectMapSerializer());
	}
	
	/*
	 * Serialises an object to the local directory using a string path.
	 */
	public static void write(Object object, String fileName) {
		// Initialize kryo
        //Kryo kryo = new Kryo();
        // Create file in dedicated local storage
        File file = new File(Gdx.files.getLocalStoragePath() + "/" + fileName);
        try {
            Output output = new Output(new FileOutputStream(file));
            // Serialize object to file
            kryo.writeObject(output, object);
            output.close();
        } catch (Exception e){
            e.printStackTrace();
        }
	}
	
	/*
	 * Deserialises an object from the local directory using a string path.
	 */
	public static <T> T read(String fileName, Class<T> aClass) {
		// Initialize kryo
        //Kryo kryo = new Kryo(); 
        // Use file in dedicated local storage
        File file = new File(Gdx.files.getLocalStoragePath() + "/" + fileName);
        try {
            Input input = new Input(new FileInputStream(file));
            // Deserialize object from file, in this case LinkedList
            //Object object = kryo.readObject(input, aClass.getClass());
            T object = kryo.readObject(input, aClass);
            input.close();
            return object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/*
	 * Serialises an object to the local directory using a FileHandle.
	 */
	public static void write(Object object, FileHandle fileHandle) {
		// Initialize kryo
        //Kryo kryo = new Kryo();
        // Create file in dedicated local storage
        File file = new File(Gdx.files.getLocalStoragePath() + "/" + fileHandle.path());
        try {
            Output output = new Output(new FileOutputStream(file));
            // Serialize object to file
            kryo.writeObject(output, object);
            output.close();
        } catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static <T> T read(FileHandle fileHandle, Class<T> aClass) {
		// Initialize kryo
        //Kryo kryo = new Kryo(); 
        // Use file in dedicated local storage
        File file = new File(Gdx.files.getLocalStoragePath() + "/" + fileHandle.path());
        try {
            Input input = new Input(new FileInputStream(file));
            // Deserialize object from file, in this case LinkedList
            T object = kryo.readObject(input, aClass);
            input.close();
            return object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
	}
	
}
