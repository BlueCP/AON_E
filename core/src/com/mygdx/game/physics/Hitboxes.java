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
	private static btCollisionShape getColShape(String name) {
		for (int i = 0; i < importer.getNumCollisionShapes(); i ++) {
			if (name.equals(importer.getNameForPointer(importer.getRigidBodyByIndex(i).getCPointer()))) {
				return importer.getCollisionShapeByIndex(i);
			}
		}
		return null;
	}
	*/
	
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

		importer.dispose();
	}
	
}
