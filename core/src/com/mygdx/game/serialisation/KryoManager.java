package com.mygdx.game.serialisation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Queue;
import com.cyphercove.gdx.gdxtokryo.GdxToKryo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.achievements.AchievementCollection;
import com.mygdx.game.entities.Dummy;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Player;
import com.mygdx.game.particles.ParticleEngine;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.quests.Quests;
import org.objenesis.strategy.StdInstantiatorStrategy;

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

		kryo.register(Player.class, new ObjectNoTransientsSerialiser(kryo, Player.class));
		kryo.register(Dummy.class, new ObjectNoTransientsSerialiser(kryo, Dummy.class));
		kryo.register(AchievementCollection.class, new ObjectNoTransientsSerialiser(kryo, AchievementCollection.class));
		kryo.register(Quests.class, new ObjectNoTransientsSerialiser(kryo, Quests.class));
		kryo.register(Entities.class, new ObjectNoTransientsSerialiser(kryo, Entities.class));
		kryo.register(ParticleEngine.class, new ObjectNoTransientsSerialiser(kryo, ParticleEngine.class));
		kryo.register(ProjectileManager.class, new ObjectNoTransientsSerialiser(kryo, ProjectileManager.class));
		kryo.register(PhysicsManager.class, new ObjectNoTransientsSerialiser(kryo, PhysicsManager.class));

//		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
	}
	
	/*
	 * Serialises an object to the local directory using a string path.
	 */
	public static void write(Object object, String fileName) {
		// Initialize kryo
        //Kryo kryo = new Kryo();
        // Create file in dedicated local storage
//        File file = new File(Gdx.files.getLocalStoragePath() + "/" + fileName);
        try {
//            Output output = new Output(new FileOutputStream(file));
            Output output = new Output(new FileOutputStream(Gdx.files.getLocalStoragePath() + "/" + fileName));
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
//        File file = new File(Gdx.files.getLocalStoragePath() + "/" + fileName);
        try {
//            Input input = new Input(new FileInputStream(file));
            Input input = new Input(new FileInputStream(Gdx.files.getLocalStoragePath() + "/" + fileName));
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
	 * Serialises the next object to the local directory using a string path, using the same output stream.
	 */
	public static void writeNext(Object object, Output output) {
		try {
			// Serialize object to file
			kryo.writeObject(output, object);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * Deserialises the next object from the local directory using a string path, using the same input stream.
	 */
	public static <T> T readNext(Input input, Class<T> aClass) {
		T object = kryo.readObject(input, aClass);
		return object;
	}
	
	/*
	 * Serialises an object to the local directory using a FileHandle.
	 */
	/*public static void write(Object object, FileHandle fileHandle) {
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
	}*/

	/*public static <T> T read(FileHandle fileHandle, Class<T> aClass) {
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
	}*/

	public static Kryo getKryo() {
		return kryo;
	}

}
