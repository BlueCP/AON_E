package com.mygdx.game.physics;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.extras.btBulletWorldImporter;

public class Hitboxes {

	private static btBulletWorldImporter importer;
	public static HashMap<String, btCollisionShape> hitboxes;
	private static short counter = 0;
	
	/*
	 * Load the hitbox for a given entity.
	 */
	private static void initEntity(String name) {
		importer.loadFile(Gdx.files.internal("hitboxes/" + name + ".bullet")); // Import terrain objects
		hitboxes.put(name, importer.getCollisionShapeByIndex(counter));
		counter ++;
	}
	
	public static void initialise() {
		importer = new btBulletWorldImporter(); // Import physics world
		hitboxes = new HashMap<>();

		initEntity("player");
		initEntity("dummy");
		initEntity("Deltis");
		initEntity("Silver statue");

		importer.dispose();
	}
	
}
