package com.mygdx.game.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btScaledBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.extras.btBulletWorldImporter;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.particles.ParticleEngine;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.physicsobjects.*;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.screens.PlayScreen;

import java.util.HashMap;

public class PhysicsWorldLoader {

	public static final int worldSize = 100; // Temporary, to avoid creating masses of unnecessary chunk folders.
	public static final int chunkSize = 100;
	public static final int numChunks = worldSize / chunkSize;

	btBulletWorldImporter importer;
	private AssetManager manager;

	private Array<Vector2> loadedChunks;

	Array<ImmobileTerrain> immobileTerrain;
	Array<ImmobileController> immobileControllers;
	Array<ImmobileControllable> immobileControllables;
	Array<ImmobileInteractive> immobileInteractives;

	Array<MobileTerrain> mobileTerrain;
	Array<MobileController> mobileControllers;
	Array<MobileControllable> mobileControllables;
	Array<MobileInteractive> mobileInteractives;

	private Array<String> immobileTerrainTypes;
	private Array<String> immobileControllerTypes;
	private Array<String> immobileControllableTypes;
	private Array<String> immobileInteractiveTypes;

	private Array<String> mobileTerrainTypes;
	private Array<String> mobileControllerTypes;
	private Array<String> mobileControllableTypes;
	private Array<String> mobileInteractiveTypes;

	private StringBuilder tempSubType;
	private StringBuilder tempIdBuilder;
	private String tempId;
	private Array<String> tempPorts;
	private Array<Tag> tempTags;
	
	PhysicsWorldLoader(AssetManager manager) {
		this.manager = manager;

		loadedChunks = new Array<>();

		immobileTerrain = new Array<>();
		immobileControllers = new Array<>();
		immobileControllables = new Array<>();
		immobileInteractives = new Array<>();

		mobileTerrain = new Array<>();
		mobileControllers = new Array<>();
		mobileControllables = new Array<>();
		mobileInteractives = new Array<>();


		immobileTerrainTypes = new Array<>();
		immobileControllerTypes = new Array<>();
		immobileControllableTypes = new Array<>();
		immobileInteractiveTypes = new Array<>();

		mobileTerrainTypes = new Array<>();
		mobileControllerTypes = new Array<>();
		mobileControllableTypes = new Array<>();
		mobileInteractiveTypes = new Array<>();

		tempSubType = new StringBuilder();
		tempIdBuilder = new StringBuilder();
		tempId = "";
		tempPorts = new Array<>();
		tempTags = new Array<>();
	}

	/*
	 * Turns an unordered arrow of regions into an array of frames (of an animation).
	 */
	private TextureRegion[] buildAnimationFrames(Array<AtlasRegion> regions, AtlasRegion region, btBulletWorldImporter importer, btCollisionObject collisionObject) {
		HashMap<Integer, AtlasRegion> regionMap = new HashMap<>();
		for (AtlasRegion region0: regions) {
			StringBuilder id0 = new StringBuilder();
			StringBuilder frame = new StringBuilder();
			boolean ontoFrameNum = false;
			for (char character: region.name.toCharArray()) {
    			if (character == '_') {
    				ontoFrameNum = true;
    				continue;
    			}
    			if (ontoFrameNum) {
    				frame.append(character);
    			} else {
    				id0.append(character);
    			}
    		}
			if (id0.toString().equals(importer.getNameForPointer(collisionObject.getCPointer()))) {
				regionMap.put(Integer.parseInt(frame.toString()), region0);
			}
		}
		TextureRegion[] TextureRegions = new TextureRegion[regionMap.size()];
		for (int i = 0; i < regionMap.size(); i ++) {
			TextureRegions[i] = new TextureRegion(regionMap.get(i));
		}
		return TextureRegions;
	}

	private void findNameData(String name) {
		// -1: not reading anything
		// 0: reading subtype
		// 1: reading port
		// 2: reading id
		// 3: reading tags
		short reading = -1;
		StringBuilder tempTag = new StringBuilder();
		StringBuilder tempPort = new StringBuilder();
		if (name.contains("_")) {
			reading = 0;
			for (char character: name.toCharArray()) {
				if (character == '{' && reading != 1) {
					reading = 1;
					continue;
				} else if (character == '{' && reading == 1) {
					tempPorts.add(tempPort.toString());
					tempPort.setLength(0);
				} else if (character == '_') {
					reading = 2;
					continue;
				} else if (character == '<' && reading != 3) {
					reading = 3;
					continue;
				} else if (character == '<' && reading == 3) {
					for (Tag tag: Tag.values()) {
						if (tag.id().equals(tempTag.toString())) {
							tempTags.add(tag);
						}
					}
					tempTag.setLength(0);
					continue;
				}

				if (reading == 0) {
					tempSubType.append(character);
				} else if (reading == 1) {
					tempPort.append(character);
				} else if (reading == 2) {
					tempIdBuilder.append(character);
				} else if (reading == 3) {
					tempTag.append(character);
				}
			}
			for (Tag tag: Tag.values()) {
				if (tag.id().equals(tempTag.toString())) {
					tempTags.add(tag);
				}
			}
		} else { // If the object is generic terrain (without a subtype)
			reading = 2;
			for (char character: name.toCharArray()) {
//				System.out.println(character);
				if (character == '<' && reading != 3) {
					reading = 3;
					continue;
				} else if (character == '<' && reading == 3) {
					for (Tag tag: Tag.values()) {
						if (tag.id().equals(tempTag.toString())) {
							tempTags.add(tag);
						}
					}
					tempTag.setLength(0);
					continue;
				}

				if (reading == 2) {
					tempIdBuilder.append(character);
				} else if (reading == 3) {
					tempTag.append(character);
				}
			}

			tempTag.setLength(0);

			for (Tag tag: Tag.values()) {
				if (tag.id().equals(tempTag.toString())) {
					tempTags.add(tag);
				}
			}
		}

		tempTags.add(Tag.WALKABLE); // TODO: remove this temp. code eventually, when Will starts using the new naming syntax with tags.

		tempId = String.valueOf(Integer.parseInt(tempIdBuilder.toString())); // To get rid of 0s at the start of the string.
	}

	/*private String findSubType(String name) {
		String type = "";
		for (char character: name.toCharArray()) {
			if (character != '_' && character != '<') {
				type += character;
			} else {
				break;
			}
		}
		return type;
	}*/
	
	/*
	 * Finds the id of the objects within its name.
	 */
	/*private String findId(String name) {
		if (!(name.contains("<")) && !(name.contains("_"))) {
			return String.valueOf(Integer.parseInt(name));
		} else if (name.contains("_")) {
			String id = "";
			boolean isId = false;
			for (char character: name.toCharArray()) {
				if (character == '_') {
					isId = true;
				} else if (isId && character != '<') {
					id += character;
				}
			}
			return String.valueOf(Integer.parseInt(id));
		} else {
			String id = "";
			for (char character: name.toCharArray()) {
				if (character != '<') {
					id += character;
				} else {
					break;
				}
			}
			return String.valueOf(Integer.parseInt(id)); // To get rid of 0s at start of number
		}
	}*/
	
	/*
	 * Returns an array of tags belonging to this static object.
	 */
	/*private Array<Tag> findTags(String name) {
		Array<Tag> tags = new Array<>();
		tags.add(Tag.WALKABLE);
		boolean isTagNext = false;
		for (char character: name.toCharArray()) {
			if (character == '<') {
				isTagNext = true;
				continue;
			}
			if (isTagNext) {
				for (Tag tag: Tag.values()) {
					if (tag.id().equals(String.valueOf(character))) {
						tags.add(tag);
					}
				}
			}
		}
		return tags;
	}*/
	
	/*
	 * Method used by loadTextureRegionCoords and loadHighestLowest to provide a common method of interpreting the same coord syntax in .txt files
	 */
	private HashMap<String, int[]> loadCoordValues(String path) {
		FileHandle file = Gdx.files.internal(path);
		String fileString = file.readString();
		StringBuilder strippedString = new StringBuilder();
		for (int i = 0; i < fileString.length(); i ++) {
			if (fileString.charAt(i) != ' ') {
				strippedString.append(fileString.charAt(i));
			}
		}
		HashMap<String, int[]> map = new HashMap<>();
		boolean skipLine = false;
		boolean readingId = true;
		boolean readingX = false;
		boolean readingY = false;
		String currentId = "";
		String currentX = "";
		String currentY = "";
		char currentChar; // Placeholder
		for (int i = 0; i < strippedString.length() + 1; i ++) {
			currentChar = i < strippedString.length() ? strippedString.charAt(i) : ' ';
			if (currentChar == '\n' || i == strippedString.length()) { // \ is the escape character so \\ is needed to represent a single \
				readingY = false;
				currentId = String.valueOf(Integer.parseInt(currentId));
				int currentXInt = Integer.parseInt(currentX);
				int currentYInt = Integer.parseInt(currentY);
				int[] ints = {currentXInt, currentYInt};
				map.put(currentId, ints);
			} else if (skipLine) {
				skipLine = false;
			} else if (readingId && currentChar != ':') {
				currentId += currentChar;
			} else if (readingId && currentChar == ':') {
				readingId = false;
				readingX = true;
			} else if (readingX && currentChar != ',') {
				currentX += currentChar;
			} else if (readingX && currentChar == ',') {
				readingX = false;
				readingY = true;
			} else if (readingY) {
				currentY += currentChar;
			}
		}
		return map;
	}
	
	/*
	 * Loads the coords of all sprites of all static objects.
	 */
//	private HashMap<String, int[]> loadTextureRegionCoords(String type) {
//		return loadCoordValues(type, "coords");
//	}
	
	/*
	 * Loads the highest and lowest points of all static objects.
	 */
//	private HashMap<String, int[]> loadHighestLowest(String type) {
//		return loadCoordValues(type, "highestLowest");
//	}
	
	/*
	protected void loadHitboxes(btDynamicsWorld dynamicsWorld) {
		importer = new btBulletWorldImporter(); // Import physics world
        loadEntityHitbox("player");
        PlayerBlueprint.setRigidBody(importer.getRigidBodyByName("player"));
        dynamicsWorld.addRigidBody(importer.getRigidBodyByName("player"));
	}
	*/

	Array<Integer> loadImmobileRenderingOrder() {
		Array<Integer> indexes = new Array<>();
		FileHandle file = Gdx.files.internal("world/immobileRenderingOrder.txt");
		String fileString = file.readString();
		for (char character: fileString.toCharArray()) {
			if (character != '\n' && character != '\r') {
				indexes.add(Integer.parseInt(String.valueOf(character)));
			}
		}

		return indexes;
	}

	/*private Array<Integer> loadRenderingOrder(String fileName) {
		Array<Integer> indexes = new Array<>();
		FileHandle file = Gdx.files.internal("world/" + fileName + ".txt");
		String fileString = file.readString();
		for (char character: fileString.toCharArray()) {
			if (character != '\n') {
				indexes.add(Integer.parseInt(String.valueOf(character)));
			}
		}

		return indexes;
	}*/

	/*Array<Integer> loadTerrainRenderingOrder() {
		return loadRenderingOrder("terrainRenderingOrder");
	}

	Array<Integer> loadControllerRenderingOrder() {
		return loadRenderingOrder("controllersRenderingOrder");
	}

	Array<Integer> loadControllableRenderingOrder() {
		return loadRenderingOrder("controllablesRenderingOrder");
	}

	Array<Integer> loadInteractiveRenderingOrder() {
		return loadRenderingOrder("interactivesRenderingOrder");
	}*/

	private void resetTempVars() {
		tempSubType.setLength(0);
		tempIdBuilder.setLength(0);
		tempId = "";
		tempPorts = new Array<>();
		tempTags = new Array<>(); // Create a new array, don't use clear(). This is because if clear is used, the arrays of tags in all objects will also be cleared (same reference).
	}

	/**
	 * Not sure if this method is needed or not, perhaps remove in future.
	 * @param pos
	 */
	public void dynamicallyImportChunks(Vector3 pos) {
		Vector2 chunkCoords = new Vector2(Math.floorDiv((int) pos.x, chunkSize), Math.floorDiv((int) pos.z, chunkSize));
		for (int x = -1; x <= 1; x ++) {
			for (int z = -1; z <= 1; z ++) {
				Vector2 currentChunk = chunkCoords.cpy().add(x, z);
				currentChunk = new Vector2(); // Temp code; see importNearbyChunks
				if (loadedChunks.contains(currentChunk, false)) {
					return;
				}
			}
		}
	}

	private void removeImmobileTerrain(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.immobileTerrain.size; i ++) {
			ImmobileTerrain obj = physicsManager.immobileTerrain.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeImmobileTerrain(obj);
				i --;
			}
		}
	}

	private void removeImmobileControllers(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.immobileControllers.size; i ++) {
			ImmobileController obj = physicsManager.immobileControllers.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeImmobileController(obj);
				i --;
			}
		}
	}

	private void removeImmobileControllables(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.immobileControllables.size; i ++) {
			ImmobileControllable obj = physicsManager.immobileControllables.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeImmobileControllable(obj);
				i --;
			}
		}
	}

	private void removeImmobileInteractives(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.immobileInteractives.size; i ++) {
			ImmobileInteractive obj = physicsManager.immobileInteractives.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeImmobileInteractive(obj);
				i --;
			}
		}
	}

	private void removeMobileTerrain(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.mobileTerrain.size; i ++) {
			MobileTerrain obj = physicsManager.mobileTerrain.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeMobileTerrain(obj);
				i --;
			}
		}
	}

	private void removeMobileControllers(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.mobileControllers.size; i ++) {
			MobileController obj = physicsManager.mobileControllers.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeMobileController(obj);
				i --;
			}
		}
	}

	private void removeMobileControllables(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.mobileControllables.size; i ++) {
			MobileControllable obj = physicsManager.mobileControllables.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeMobileControllable(obj);
				i --;
			}
		}
	}

	private void removeMobileInteractives(PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < physicsManager.mobileInteractives.size; i ++) {
			MobileInteractive obj = physicsManager.mobileInteractives.get(i);
			if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
					obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
				physicsManager.removeMobileInteractive(obj);
				i --;
			}
		}
	}

	private void removeEntities(Entities entities, PlayScreen playScreen, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < entities.allEntities.size; i ++) {
			Entity entity = entities.allEntities.get(i);
			if (entity.pos.x < lower.x || entity.pos.z < lower.y || entity.pos.x > higher.x || entity.pos.z > higher.y) {
				entities.removeEntity(entity, playScreen);
				i --;
			}
		}
	}

	private void removeParticles(ParticleEngine particleEngine, PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < particleEngine.particles.size; i ++) {
			Particle particle = particleEngine.particles.get(i);
			if (particle.pos.x < lower.x || particle.pos.z < lower.y || particle.pos.x > higher.x || particle.pos.z > higher.y) {
				particleEngine.removeParticle(particle, physicsManager.getDynamicsWorld());
				i --;
			}
		}
	}

	private void removeProjectiles(ProjectileManager projectileManager, PhysicsManager physicsManager, Vector3 vector, Vector2 chunkCoords, Vector2 lower, Vector2 higher) {
		for (int i = 0; i < projectileManager.projectiles.size; i ++) {
			Projectile projectile = projectileManager.projectiles.get(i);
			if (projectile.pos.x < lower.x || projectile.pos.z < lower.y || projectile.pos.x > higher.x || projectile.pos.z > higher.y) {
				projectile.destroy(physicsManager.getDynamicsWorld(), projectileManager);
				i --;
			}
		}
	}

	void unloadFarAwayChunks(PlayScreen playScreen, Vector3 pos) {
		// If more chunks are loaded than those immediately surrounding the player.
		PhysicsManager physicsManager = playScreen.physicsManager;
		if (loadedChunks.size > 9) {
			playScreen.save(); // So that entities/projectiles/whatever in chunks that are being unloaded aren't lost.
			Vector3 vector = new Vector3();
			Vector2 chunkCoords = new Vector2(Math.floorDiv((int) pos.x, chunkSize), Math.floorDiv((int) pos.z, chunkSize));
			Vector2 lower = chunkCoords.cpy().scl(chunkSize);
			Vector2 higher = chunkCoords.cpy().add(1, 1).scl(chunkSize);
			/*for (int i = 0; i < physicsManager..size; i ++) {
				ConstantObject obj = physicsManager.getAllConstantObjects().get(i);
				if (obj.collisionObject.getWorldTransform().getTranslation(vector).x < lower.x ||
						obj.collisionObject.getWorldTransform().getTranslation(vector).z < lower.y ||
						obj.collisionObject.getWorldTransform().getTranslation(vector).x > higher.x ||
						obj.collisionObject.getWorldTransform().getTranslation(vector).z > higher.y) {
					if (PhysicsManager.isImmobileTerrain(obj.physicsId)) {
						physicsManager.removeImmobileTerrain(obj);
					}
				}
			}*/
			removeImmobileTerrain(physicsManager, vector, chunkCoords, lower, higher);
			removeImmobileControllers(physicsManager, vector, chunkCoords, lower, higher);
			removeImmobileControllables(physicsManager, vector, chunkCoords, lower, higher);
			removeImmobileInteractives(physicsManager, vector, chunkCoords, lower, higher);
			removeMobileTerrain(physicsManager, vector, chunkCoords, lower, higher);
			removeMobileControllers(physicsManager, vector, chunkCoords, lower, higher);
			removeMobileControllables(physicsManager, vector, chunkCoords, lower, higher);
			removeMobileInteractives(physicsManager, vector, chunkCoords, lower, higher);

			removeEntities(playScreen.entities, playScreen, vector, chunkCoords, lower, higher);
			removeParticles(playScreen.particleEngine, physicsManager, vector, chunkCoords, lower, higher);
			removeProjectiles(playScreen.projectileManager, physicsManager, vector, chunkCoords, lower, higher);
		}
	}

	public void importNearbyChunks(Vector3 pos) {
		Array<btBulletWorldImporter> importers = new Array<>();
		Vector2 chunkCoords = new Vector2(Math.floorDiv((int) pos.x, chunkSize), Math.floorDiv((int) pos.z, chunkSize));
		for (int x = -1; x <= 1; x ++) {
			for (int z = -1; z <= 1; z ++) {
				Vector2 currentChunk = chunkCoords.cpy().add(x, z);
				currentChunk = new Vector2(); // TODO: this is temp. code because there's no point in creating eight empty chunk folders. When more content is added to the world, remove this continue statement.
				if (!loadedChunks.contains(currentChunk, false)) {
					String path = "world/chunks/" + (int) currentChunk.x + "_" + (int) currentChunk.y + "/";
					importChunk(path);
					loadedChunks.add(currentChunk.cpy());
				}
				break; // Temp. code (see above).
			}
			break; // Temp. code (see above).
		}
	}

	private void importChunk(String path) {
		importer = new btBulletWorldImporter(); // Import physics world
		importer.loadFile(Gdx.files.internal(path + "constObjects.bullet")); // Import terrain objects

		manager.load(path + "constObjects.atlas", TextureAtlas.class); // Load textures
		manager.finishLoading();
		TextureAtlas atlas = manager.get(path + "constObjects.atlas"); // Create temp. atlas
		Array<AtlasRegion> regions = atlas.getRegions(); // Create temp. array of regions
		//ConstantObject[] instances = new ConstantObject[importer.getNumRigidBodies()];
//		Array<StaticObject> objs = new Array<>();
		HashMap<String, int[]> textureRegionCoords = loadCoordValues(path + "coords.txt");

		System.out.println(importer.getNumRigidBodies());
		System.out.println(importer.getNumCollisionShapes());
		for (int a = 0; a < importer.getNumCollisionShapes(); a ++) {
			System.out.println(importer.getCollisionShapeByIndex(a));
		}

		int numInvalidShapes = 0; // A counter of how many invalid collision shapes have been skipped so far.
		for (int i = 0; i <importer.getNumRigidBodies(); i ++) {
			btCollisionObject collisionObject = importer.getRigidBodyByIndex(i);

			// If this shape is invalid.
			// A pattern was found that triangle meshes had two shapes, a triangle mesh and a scaled triangle mesh.
			// The scaled triangle mesh was found to be the correct one.
			if (importer.getCollisionShapeByIndex(i + numInvalidShapes) instanceof btBvhTriangleMeshShape &&
					importer.getCollisionShapeByIndex(i + numInvalidShapes + 1) instanceof btScaledBvhTriangleMeshShape) {
				numInvalidShapes ++;
			}

			collisionObject.setCollisionShape(importer.getCollisionShapeByIndex(i + numInvalidShapes));

			findNameData(importer.getNameForPointer(collisionObject.getCPointer()));

//			String physicsId = findId(importer.getNameForPointer(collisionObject.getCPointer()));
//			String subType = findSubType(importer.getNameForPointer(collisionObject.getCPointer()));

			if (immobileTerrainTypes.contains(tempSubType.toString(), false)) {
				buildImmobileTerrain(collisionObject, regions, textureRegionCoords);
			} else if (immobileControllerTypes.contains(tempSubType.toString(), false)) {

			} else if (immobileControllableTypes.contains(tempSubType.toString(), false)) {

			} else if (immobileInteractiveTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileTerrainTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileControllerTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileControllableTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileInteractiveTypes.contains(tempSubType.toString(), false)) {

			} else {
				buildImmobileTerrain(collisionObject, regions, textureRegionCoords);
			}

			resetTempVars();
		}

		importer.dispose();
	}

	/*void importAll() {
		importer = new btBulletWorldImporter(); // Import physics world
		importer.loadFile(Gdx.files.internal("world/constObjects/constObjects.bullet")); // Import terrain objects
		manager.load("world/constObjects/constObjects.atlas", TextureAtlas.class); // Load textures
		TextureAtlas atlas = manager.get("world/constObjects/constObjects.atlas"); // Create temp. atlas
		Array<AtlasRegion> regions = atlas.getRegions(); // Create temp. array of regions
		//ConstantObject[] instances = new ConstantObject[importer.getNumRigidBodies()];
//		Array<StaticObject> objs = new Array<>();
		HashMap<String, int[]> textureRegionCoords = loadTextureRegionCoords("constObjects");

		System.out.println(importer.getNumRigidBodies());
		System.out.println(importer.getNumCollisionShapes());
		for (int a = 0; a < importer.getNumCollisionShapes(); a ++) {
			System.out.println(importer.getCollisionShapeByIndex(a));
		}

		int numInvalidShapes = 0; // A counter of how many invalid collision shapes have been skipped so far.
		for (int i = 0; i <importer.getNumRigidBodies(); i ++) {
			btCollisionObject collisionObject = importer.getRigidBodyByIndex(i);

			// If this shape is invalid.
			// A pattern was found that triangle meshes had two shapes, a triangle mesh and a scaled triangle mesh.
			// The scaled triangle mesh was found to be the correct one.
			if (importer.getCollisionShapeByIndex(i + numInvalidShapes) instanceof btBvhTriangleMeshShape &&
				importer.getCollisionShapeByIndex(i + numInvalidShapes + 1) instanceof btScaledBvhTriangleMeshShape) {
				numInvalidShapes ++;
			}

			collisionObject.setCollisionShape(importer.getCollisionShapeByIndex(i + numInvalidShapes));

			findNameData(importer.getNameForPointer(collisionObject.getCPointer()));

//			String physicsId = findId(importer.getNameForPointer(collisionObject.getCPointer()));
//			String subType = findSubType(importer.getNameForPointer(collisionObject.getCPointer()));

			if (immobileTerrainTypes.contains(tempSubType.toString(), false)) {
				buildImmobileTerrain(collisionObject, regions, textureRegionCoords);
			} else if (immobileControllerTypes.contains(tempSubType.toString(), false)) {

			} else if (immobileControllableTypes.contains(tempSubType.toString(), false)) {

			} else if (immobileInteractiveTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileTerrainTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileControllerTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileControllableTypes.contains(tempSubType.toString(), false)) {

			} else if (mobileInteractiveTypes.contains(tempSubType.toString(), false)) {

			} else {
				buildImmobileTerrain(collisionObject, regions, textureRegionCoords);
			}

			resetTempVars();
		}

		importer.dispose();
	}*/

	private void buildImmobileTerrain(btCollisionObject collisionObject, Array<AtlasRegion> regions, HashMap<String, int[]> textureRegionCoords) {
		boolean isAnimation = false;
//		String physicsId = findId(importer.getNameForPointer(collisionObject.getCPointer()));
		for (AtlasRegion region: regions) {
			StringBuilder textureRegionId = new StringBuilder();
			for (char character: region.name.toCharArray()) {
				textureRegionId.append(character);
				if (character == '_') {
					isAnimation = true;
				}
			}
			TextureRegion[] textureRegions;
			if (textureRegionId.toString().equals(tempId) && !isAnimation) {
				textureRegions = new TextureRegion[1];
				textureRegions[0] = new TextureRegion(region);
//				Array<Tag> tags = findTags(tempIdBuilder);
				int[] coords = textureRegionCoords.get(tempId);
				immobileTerrain.add(new ImmobileTerrain(collisionObject, textureRegions, tempId, tempTags, coords[0], coords[1]));
				break;
			} else if (textureRegionId.toString().equals(tempId) && isAnimation) {
				textureRegions = buildAnimationFrames(regions, region, importer, collisionObject);
//				Array<Tag> tags = findTags(tempIdBuilder);
				int[] coords = textureRegionCoords.get(tempId);
				immobileTerrain.add(new ImmobileTerrain(collisionObject, textureRegions, tempId, tempTags, coords[0], coords[1]));
				break;
			}

		}
	}

	/*
	 * Returns an array of all terrain-type static objects imported in.
	 */
	/*Array<StaticObject> loadImmobileTerrain(btDynamicsWorld dynamicsWorld) {
		importer = new btBulletWorldImporter(); // Import physics world
        importer.loadFile(Gdx.files.internal("world/statics/statics.bullet")); // Import terrain objects
        manager.load("world/statics/statics.atlas", TextureAtlas.class); // Load textures
        TextureAtlas atlas = manager.get("world/statics/statics.atlas"); // Create temp. atlas
        Array<AtlasRegion> regions = atlas.getRegions(); // Create temp. array of regions
		//ConstantObject[] instances = new ConstantObject[importer.getNumRigidBodies()];
        Array<StaticObject> objs = new Array<>();
		HashMap<String, int[]> TextureRegionCoords = loadTextureRegionCoords("statics");
//		HashMap<String, int[]> highestLowest = loadHighestLowest("statics");

        for (int i = 0; i < importer.getNumRigidBodies(); i ++) {
        	btCollisionObject collisionObject = importer.getRigidBodyByIndex(i);

        	*//*collisionObject.setCollisionShape(importer.getCollisionShapeByIndex(i));

        	// Rigid bodies
        	System.out.println(importer.getNumRigidBodies());
        	for (int a = 0; a < importer.getNumRigidBodies(); a ++) {
				System.out.println(importer.getRigidBodyByIndex(a));
				System.out.println(importer.getRigidBodyByIndex(a).getCollisionShape());
				System.out.println(importer.getNameForPointer(importer.getRigidBodyByIndex(a).getCPointer()));
			}

        	// Collision shapes
        	System.out.println(importer.getNumCollisionShapes());
        	for (int a = 0; a < importer.getNumCollisionShapes(); a ++) {
        		System.out.println(importer.getCollisionShapeByIndex(a));
        		System.out.println(importer.getNameForPointer(importer.getCollisionShapeByIndex(a).getCPointer()));
			}*//*

        	boolean isAnimation = false;
        	String physicsId = findId(importer.getNameForPointer(collisionObject.getCPointer()));
        	for (AtlasRegion region: regions) {
        		String TextureRegionId = "";
        		for (char character: region.name.toCharArray()) {
        			TextureRegionId += character;
        			if (character == '_') {
        				isAnimation = true;
        			}
        		}
        		if (TextureRegionId.equals(physicsId) && !isAnimation) {
        			TextureRegion[] TextureRegion = {new TextureRegion(region)};
        			Array<Tag> tags = findTags(physicsId);
        			int[] coords = TextureRegionCoords.get(physicsId);
//        			int[] highLow = highestLowest.get(physicsId);
        			objs.add(new StaticObject(collisionObject, TextureRegion, physicsId, tags, coords[0], coords[1]));
        			break;
        		} else if (TextureRegionId.equals(physicsId) && isAnimation) {
        			TextureRegion[] TextureRegions = buildAnimationFrames(regions, region, importer, collisionObject);
        			Array<Tag> tags = findTags(physicsId);
        			int[] coords = TextureRegionCoords.get(physicsId);
//        			int[] highLow = highestLowest.get(physicsId);
        			objs.add(new StaticObject(collisionObject, TextureRegions, physicsId, tags, coords[0], coords[1]));
        			break;
        		}
        	}
            //dynamicsWorld.addCollisionObject(collisionObject);
        }
        importer.dispose();
		return objs;
	}*/
	
	/*
	 * Returns an array of all controller-type static objects imported in.
	 */
	/*protected Array<ControllerObject> loadControllers(btDynamicsWorld dynamicsWorld) {
		btBulletWorldImporter importer = new btBulletWorldImporter(); // Import physics world
        importer.loadFile(Gdx.files.internal("controllers.bullet")); // Import terrain objects
        manager.load("world/controllers/controllers.atlas", TextureAtlas.class); // Load textures
        TextureAtlas atlas = manager.get("world/controllers/controllers.atlas"); // Create temp. atlas
        Array<AtlasRegion> regions = atlas.getRegions(); // Create temp. array of regions
		//ControllerObject[] instances = new ControllerObject[importer.getNumCollisionShapes()];
        Array<ControllerObject> objs = new Array<>();
		HashMap<String, int[]> TextureRegionCoords = loadTextureRegionCoords("controllers");
//		HashMap<String, int[]> highestLowest = loadHighestLowest("controllers");
		
        for (int i = 0; i < importer.getNumCollisionShapes(); i ++) {
        	btCollisionObject collisionObject = importer.getRigidBodyByIndex(i);

        	collisionObject.setCollisionShape(importer.getCollisionShapeByIndex(i));
        	
        	boolean isAnimation = false;
        	for (AtlasRegion region: regions) {
        		String id = "";
        		for (char character: region.name.toCharArray()) {
        			id += character;
        			if (character == '_') {
        				isAnimation = true;
        			}
        		}
        		String physicsId = findId(importer.getNameForPointer(collisionObject.getCPointer()));
        		if (id.equals(physicsId) && !isAnimation) {
        			TextureRegion[] TextureRegion = {new TextureRegion(region)};
        			Array<Tag> tags = findTags(importer.getNameForPointer(collisionObject.getCPointer()));
        			int[] coords = TextureRegionCoords.get(physicsId);
//        			int[] highLow = highestLowest.get(physicsId);
        			objs.add(new ControllerObject(collisionObject, TextureRegion, physicsId, tags, coords[0], coords[1]));
        			break;
        		} else if (id.equals(physicsId) && isAnimation) {
        			TextureRegion[] TextureRegions = buildAnimationFrames(regions, region, importer, collisionObject);
        			Array<Tag> tags = findTags(importer.getNameForPointer(collisionObject.getCPointer()));
        			int[] coords = TextureRegionCoords.get(physicsId);
//        			int[] highLow = highestLowest.get(physicsId);
        			objs.add(new ControllerObject(collisionObject, TextureRegions, physicsId, tags, coords[0], coords[1]));
        			break;
        		}
        	}
        	
            //dynamicsWorld.addCollisionObject(collisionObject);
        }
        
        importer.dispose();
		return objs;
	}*/
	
	/*
	 * Returns an array of all controllable-type static objects imported in.
	 */
	/*protected Array<ControllableObject> loadControllables(btDynamicsWorld dynamicsWorld) {
		btBulletWorldImporter importer = new btBulletWorldImporter(); // Import physics world
        importer.loadFile(Gdx.files.internal("controllables.bullet")); // Import terrain objects
        manager.load("world/controllables/controllables.atlas", TextureAtlas.class); // Load textures
        TextureAtlas atlas = manager.get("world/controllables/controllables.atlas"); // Create temp. atlas
        Array<AtlasRegion> regions = atlas.getRegions(); // Create temp. array of regions
		//ControllableObject[] instances = new ControllableObject[importer.getNumCollisionShapes()];
        Array<ControllableObject> objs = new Array<>();
		HashMap<String, int[]> TextureRegionCoords = loadTextureRegionCoords("controllables");
//		HashMap<String, int[]> highestLowest = loadHighestLowest("controllables");
		
        for (int i = 0; i < importer.getNumCollisionShapes(); i ++) {
        	btCollisionObject collisionObject = importer.getRigidBodyByIndex(i);

        	collisionObject.setCollisionShape(importer.getCollisionShapeByIndex(i));
        	
        	boolean isAnimation = false;
        	for (AtlasRegion region: regions) {
        		String id = "";
        		for (char character: region.name.toCharArray()) {
        			id += character;
        			if (character == '_') {
        				isAnimation = true;
        			}
        		}
        		String physicsId = findId(importer.getNameForPointer(collisionObject.getCPointer()));
        		if (id.equals(physicsId) && !isAnimation) {
        			TextureRegion[] TextureRegion = {new TextureRegion(region)};
        			Array<Tag> tags = findTags(importer.getNameForPointer(collisionObject.getCPointer()));
        			int[] coords = TextureRegionCoords.get(physicsId);
//        			int[] highLow = highestLowest.get(physicsId);
        			objs.add(new ControllableObject(collisionObject, TextureRegion, physicsId, tags, coords[0], coords[1]));
        			break;
        		} else if (id.equals(physicsId) && isAnimation) {
        			TextureRegion[] TextureRegions = buildAnimationFrames(regions, region, importer, collisionObject);
        			Array<Tag> tags = findTags(importer.getNameForPointer(collisionObject.getCPointer()));
        			int[] coords = TextureRegionCoords.get(physicsId);
//        			int[] highLow = highestLowest.get(physicsId);
        			objs.add(new ControllableObject(collisionObject, TextureRegions, physicsId, tags, coords[0], coords[1]));
        			break;
        		}
        	}
        	
            //dynamicsWorld.addCollisionObject(collisionObject);
        }
        
        importer.dispose();
		return objs;
	}*/
	
}
