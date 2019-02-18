package com.mygdx.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.physics.PhysicsManager.TestMode;
import com.mygdx.game.physics.WorldObject;
import com.mygdx.game.physicsobjects.ConstantObject;
import com.mygdx.game.physicsobjects.ImmobileObject;
import com.mygdx.game.physicsobjects.MobileObject;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.world.Time;

public class IsometricRenderer {

	//private final int sunsDistFromEarth = 1000;
	
	//private AON_E game;
	//private World world;
	private Time time;
	private Texture fullEntityLifeBar;
	//private TextureRegion entityLifeBar;
	
	//private PolygonSprite polySprite;
	//private PolygonSpriteBatch polyBatch; // To assign at the beginning
	//private Texture tileShadowPolygon;
	
	//private FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	//TextureRegion fboRegion;
	
	//private Vector3 sunPos;
	//private float sunAngleDegrees;
	//private float sunAngleRadians;
	
//	private final int tileWidth = 128;
	private final int tileHeight = 64;
//	private final int pixelWorldWidth = 256000;
//	private final int pixelWorldHeight = 128000;
//	private final int physicsWorldSize = 2000;
	//private final int constA = (pixelWorldHeight - 1) - (int)(pixelWorldHeight / 2);
	//private final int constA = 0;
	
	private Vector2 isoPlayer;
	//private Vector3 playerCoords;
	
	//private Point3 selectedCartesian = new Point3();
	//private Point2 selectedScreen = new Point2();
	
	//private Vector2 pixelCoords;
	
	//private Vector3 emptyVector = new Vector3();
	
	/*
	// 4 vertices of polygon shadow (in world coords):
	Point3 vertex1;
	Point3 vertex2;
	Point3 vertex3;
	Point3 vertex4;
	Point2 point1;
	Point2 point2;
	Point2 point3;
	Point2 point4;
	*/
	
	private ShapeRenderer shapeRenderer;
	
	//Array<TextureRegion> orderedTextures;
	//Array<Vector2> orderedPositions;
	//Array<Integer> orderedIds;
	//Array<Visibility> orderedVisibilities;
	
	private Array<WorldObject> orderedObjects;
	
	public Camera camera;
	
	private TextureRegion entityLifeBar;
	private Texture vignette;
	private Texture dayNightOverlay;
	
	private float effectiveZoom = 1; // A combination of the camera zoom and the zoom considering the screen size.
	
	public IsometricRenderer(AON_E game, Time time) {
		//this.game = game;
		//this.world = world;
		this.time = time;
		fullEntityLifeBar = game.manager.get("sprites/fullEntityLifeBar.png");
		//entityLifeBar = new TextureRegion(fullEntityLifeBar);
		vignette = game.manager.get("textures/vignette.png");
		
		//orderedTextures = new Array<TextureRegion>();
		//orderedPositions = new Array<Vector2>();
		//orderedIds = new Array<Integer>();
		//orderedVisibilities = new Array<Visibility>();
		
		orderedObjects = new Array<>();
		
		camera = new Camera();
		
		entityLifeBar = new TextureRegion(fullEntityLifeBar);
		
		//camera.addHardPanNow(5, new Vector3(2, 0, 2));
		//camera.addHardZoomToQueue(3, 2f);
		camera.addSoftZoomToQueue(15, 2f);
		//camera.addSoftZoomToQueue(5, 2f);
		//camera.setZoom(0.5f);
		//camera.addCutToQueue(2, Vector3.Zero);
		//camera.addHardPanToQueue(3, new Vector3(3, 0, 3));
		//camera.addCutToQueue(2, Vector3.Zero);
//		camera.addSoftPanToQueue(3, new Vector3(-3, 0, -3));
//		camera.addCutToQueue(5, new Vector3(-2, 0, 2));
		//camera.addCutToQueue(2, new Vector3(2, 0, 2));
		//camera.addCutToQueue(2, new Vector3(2, 0, -2));
		//camera.addCutToQueue(2, new Vector3(-2, 0, -2));
		
		// Creating the color filling (but textures would work the same way)
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(0x00000080);
		pix.fill();
		//tileShadowPolygon = new Texture(pix);
		//polyBatch = new PolygonSpriteBatch();
		
		Pixmap pixmap = new Pixmap((int)AON_E.WORLD_WIDTH, (int)AON_E.WORLD_HEIGHT, Pixmap.Format.RGBA8888);
		pixmap.setColor(0, 0, 0.1f, 1);
		pixmap.fill();
		dayNightOverlay = new Texture(pixmap);
		
		//sunPos = new Vector3();
		
		/*
		vertex1 = new Point3();
		vertex2 = new Point3();
		vertex3 = new Point3();
		vertex4 = new Point3();
		point1 = new Point2();
		point2 = new Point2();
		point3 = new Point2();
		point4 = new Point2();
		*/
		
		//pixelCoords = new Vector2();
		
		shapeRenderer = new ShapeRenderer();
	}
	
	/*
	private static enum RenderingStage {
		ENTITIES,
		PLAYER,
		PARTICLES,
		PROJECTILES;
	}
	*/
	
	public enum Visibility {
		VISIBLE,
		TRANSLUCENT,
		INVISIBLE
	}
	
	private int findLowestIndex(Array<Integer> ids) {
		int index = Integer.MAX_VALUE;
		for (Integer id: ids) {
			//int id = id0; // In order to make this work with the operators, we need to convert it to a primitive int
			for (int i = 0; i < orderedObjects.size; i ++) {
				if (orderedObjects.get(i).physicsId == id && i < index) {
					index = i;
					break;
				}
			}
			/*
			if (worldObjects.indexOf(id, false) < index && orderedIds.indexOf(id, false) != -1) { // If this object is a static object (which all begin with 1100, 2100, 3100, etc)
				index = orderedIds.indexOf(id, false);
				//
				switch (renderingStage) {
					case ENTITIES:
						if (String.valueOf(id).charAt(1) == '1') {
							lowestRenderPos = Util.getId(id);
						}
						break;
					case PLAYER:
						if (String.valueOf(id).charAt(1) == '1' || String.valueOf(id).startsWith("1000")) {
							lowestRenderPos = Util.getId(id);
						}
						break;
					case PARTICLES:
						if (String.valueOf(id).charAt(1) == '1' || String.valueOf(id).startsWith("1000")) {
							lowestRenderPos = Util.getId(id);
						}
						break;
					case PROJECTILES:
						if (String.valueOf(id).charAt(1) == '1' || String.valueOf(id).startsWith("1000") || String.valueOf(id).startsWith("3000")) {
							lowestRenderPos = Util.getId(id);
						}
						break;
				}
				//
			}
			*/
		}
		//int index = orderedIds.indexOf(lowestRenderPos, false);
		/*
		if (index - 1 < 0) {
			index = 0;
		}
		*/
		if (index == Integer.MAX_VALUE) {
			index = -1;
		}
		return index;
	}
	
	/*
	private int findIndex(int id, Array<WorldObject> worldObjects) {
		int index = -1;
		for (int i = 0; i < worldObjects.size; i ++) {
			if (worldObjects.get(i).physicsId == id) {
				index = i;
				break;
			}
		}
		return index;
	}
	*/

	private void calculateImmobileObjectRenderingOrder(PhysicsManager physicsManager) {
		boolean repeat;
		do {
			if (physicsManager.renderableImmobileObjects.size == 0) {
				break;
			}
			repeat = false;
			for (int i = 0; i < physicsManager.renderableImmobileObjects.size - 1; i ++ ) {
				ImmobileObject current = physicsManager.renderableImmobileObjects.get(i);
				ImmobileObject next = physicsManager.renderableImmobileObjects.get(i + 1);
				if (current.getRenderingIndex() > next.getRenderingIndex()) {
					physicsManager.renderableImmobileObjects.swap(i, i + 1);
					repeat = true;
				}
			}
		} while (repeat);

		for (ImmobileObject obj: physicsManager.renderableImmobileObjects) {
			obj.updateWorldObject(this);
			orderedObjects.add(obj);
		}
	}

	private void calculateMobileObjectRenderingOrder(PhysicsManager physicsManager) {
		for (int i = 0; i < physicsManager.renderableMobileObjects.size; i ++) {
			MobileObject obj = physicsManager.renderableMobileObjects.get(i);
			Array<Integer> hitIds = physicsManager.convexSweepTestAll(obj.collisionObject, obj.getPos());
			int index = findLowestIndex(hitIds);
			obj.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) {
				orderedObjects.add(obj);
			} else {
				orderedObjects.insert(index, obj);
			}
		}
	}
	
	/*private void calculateStaticObjectRenderingOrder(PhysicsManager physicsManager) {
		//renderList = physicsManager.renderable;
		boolean repeat;
		do {
			if (physicsManager.renderableMobileObjects.size == 1) {
				break;
			}
			repeat = false;
			for (int i = 0; i < physicsManager.renderableMobileObjects.size - 1; i ++) {
				ConstantObject object = physicsManager.renderableMobileObjects.get(i);
				ConstantObject nextObject = physicsManager.renderableMobileObjects.get(i + 1);
				*//*if (object.getLowestPoint() >= nextObject.getHighestPoint() ||
						object.getHighestPoint() <= nextObject.getLowestPoint()) {
					if (object.getPos().z > nextObject.getPos().z) {
						physicsManager.renderableMobileObjects.swap(i, i + 1);
						repeat = true;
					}
				} else {
					if (object.getIsoY() < nextObject.getIsoY()) {
						physicsManager.renderableMobileObjects.swap(i, i + 1);
						repeat = true;
					}
				}*//*
			}
		} while (repeat);
		
		for (ConstantObject object: physicsManager.renderableMobileObjects) {
			object.updateWorldObject(this);
			orderedObjects.add(object);
			*//*
			orderedTextures.add(object.getTexture());
			orderedPositions.add(new Vector2(object.getSpriteX() - camera.isoPos.x + Gdx.graphics.getWidth()/2, object.getSpriteY() - camera.isoPos.y + Gdx.graphics.getHeight()/2));
			orderedIds.add(object.getCollisionObject().getUserValue());
			orderedVisibilities.add(Visibility.VISIBLE); // This is the default, it may be changed in calculatePlayerRenderingOrder
			*//*
		}
	}*/
	
	private void calculateEntitiesRenderingOrder(PhysicsManager physicsManager) {
		//renderList = physicsManager.renderable;
		for (int i = 0; i < physicsManager.renderableEntities.size; i ++) {
			Entity entity = physicsManager.renderableEntities.get(i);
			Array<Integer> hitIds = physicsManager.convexSweepTestAll(entity.rigidBody, entity.pos);
			int index = findLowestIndex(hitIds);
			//int index = findIndex(physicsManager.convexSweepTestFirst(entity.rigidBody, entity.pos), orderedObjects);
			//Vector2 coords = cartesianToScreen(entity.pos.x, entity.pos.y, entity.pos.z);
			//entity.renderPos = coords;
			entity.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the entity
				orderedObjects.add(entity);
				/*
				orderedTextures.add(entity.getTexture());  // Put the entity at the front of the rendering order
				orderedPositions.add(new Vector2(coords.x - entity.getTexture().getRegionWidth()/2, coords.y - entity.getTexture().getRegionHeight()/2));
				orderedIds.add(entity.rigidBody.getUserValue());
				orderedVisibilities.add(Visibility.VISIBLE);
				*/
			} else {
				/*
				orderedTextures.insert(index, entity.getTexture());
				orderedPositions.insert(index, new Vector2(coords.x - entity.getTexture().getRegionWidth()/2, coords.y - entity.getTexture().getRegionHeight()/2));
				orderedIds.insert(index, entity.rigidBody.getUserValue());
				orderedVisibilities.add(Visibility.VISIBLE);
				*/
				orderedObjects.insert(index, entity);
				
				/*
				float percLife = entity.getLife() / entity.getMaxLife();
				int barLength = (int)(fullEntityLifeBar.getTextureData().getWidth() * percLife);
				TextureRegion entityLifeBar = new TextureRegion(fullEntityLifeBar);
				entityLifeBar.setRegionWidth(barLength);
				orderedTextures.insert(index, entityLifeBar);
				orderedPositions.insert(index, new Vector2(coords.x - fullEntityLifeBar.getWidth()/2, coords.y + entity.getTexture().getRegionHeight()/2 + 20));
				orderedIds.insert(index, entity.rigidBody.getUserValue());
				orderedVisibilities.add(Visibility.VISIBLE);
				*/
			}
		}
	}
	
	private void calculatePlayerRenderingOrder(PhysicsManager physicsManager, Player player) {
		Array<Integer> hitIds = physicsManager.convexSweepTestAll(player.rigidBody, player.pos);
		int index = findLowestIndex(hitIds);
		//int index = findIndex(physicsManager.convexSweepTestFirst(player.rigidBody, player.pos), orderedObjects);
		//Vector2 coords = cartesianToScreen(player.pos.x, player.pos.y, player.pos.z);
		//player.renderPos = coords.cpy();
		player.updateWorldObject(this);
		if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the player
			/*
			orderedTextures.add(player.getTexture());  // Put the entity at the front of the rendering order
			orderedPositions.add(new Vector2(coords.x - player.getTexture().getRegionWidth()/2, coords.y - player.getTexture().getRegionHeight()/2));
			orderedIds.add(player.rigidBody.getUserValue());
			orderedVisibilities.add(Visibility.VISIBLE);
			*/
			orderedObjects.add(player);
		} else {
			/*
			orderedTextures.insert(index, player.getTexture());
			orderedPositions.insert(index, new Vector2(coords.x - player.getTexture().getRegionWidth()/2, coords.y - player.getTexture().getRegionHeight()/2));
			orderedIds.insert(index, player.rigidBody.getUserValue());
			orderedVisibilities.add(Visibility.VISIBLE);
			*/
			orderedObjects.insert(index, player);
			for (int i = orderedObjects.size - 1; i > index; i --) {
				if (PhysicsManager.isConstObject(orderedObjects.get(i).physicsId)) {
					for (ConstantObject object: physicsManager.renderableImmobileObjects) {
						updateConstObjectVisibility(object, i);
					}
					for (ConstantObject object: physicsManager.renderableMobileObjects) {
						updateConstObjectVisibility(object, i);
					}
				}
			}
		}
	}

	private void updateConstObjectVisibility(ConstantObject object, int index) {
		if (object.id == orderedObjects.get(index).id) {
			if (object.getTags().contains(Tag.INVISIBLE, true)) {
				orderedObjects.get(index).visibility = Visibility.INVISIBLE;
			} else {
				orderedObjects.get(index).visibility = Visibility.TRANSLUCENT;
			}
		}
	}
	
	private void calculateParticlesRenderingOrder(PhysicsManager physicsManager) {
		for (int i = 0; i < physicsManager.renderableParticles.size; i ++) {
			Particle particle = physicsManager.renderableParticles.get(i);
			Array<Integer> hitIds = physicsManager.rayTestAll(particle.pos, TestMode.UPWARDS);
			//Vector2 coords = cartesianToScreen(particle.pos.x, particle.pos.y, particle.pos.z);
			//coords.x -= particle.getTexture().getRegionWidth()/2;
			//coords.y -= particle.getTexture().getRegionHeight()/2;
			int index = findLowestIndex(hitIds);
			particle.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the particle
				/*
				orderedTextures.add(particle.getTexture()); // Put the particle at the front of the rendering order
				orderedPositions.add(coords);
				orderedIds.add(particle.rigidBody.getUserValue());
				orderedVisibilities.add(Visibility.VISIBLE);
				*/
				orderedObjects.add(particle);
			} else {
				/*
				orderedTextures.insert(index, particle.getTexture()); // Put the particle at the front of the rendering order
				orderedPositions.insert(index, coords);
				orderedIds.insert(index, particle.rigidBody.getUserValue());
				orderedVisibilities.add(Visibility.VISIBLE);
				*/
				orderedObjects.insert(index, particle);
			}
		}
	}
	
	private void calculateProjectilesRenderingOrder(PhysicsManager physicsManager) {
		for (int i = 0; i < physicsManager.renderableProjectiles.size; i ++) {
			Projectile projectile = physicsManager.renderableProjectiles.get(i);
			if (!projectile.hasTexture()) {
				continue;
			}
//			System.out.println(projectile.getCollisionObject());
//			System.out.println(projectile.getCollisionObject().getCollisionShape());
//			System.out.println(projectile.pos);
			Array<Integer> hitIds = physicsManager.convexSweepTestAll(projectile.getCollisionObject(), projectile.pos);
			int index = findLowestIndex(hitIds);
			//int id = physicsManager.convexSweepTestFirst(projectile.getCollisionObject(), projectile.pos);
			//Vector2 coords = cartesianToScreen(projectile.pos.x, projectile.pos.y, projectile.pos.z);
			//coords.x -= projectile.getTexture().getRegionWidth()/2;
			//coords.y -= projectile.getTexture().getRegionHeight()/2;
			//int index = findIndex(id, orderedObjects);
			projectile.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the projectile
				/*
				orderedTextures.add(projectile.getTexture()); // Put the projectile at the front of the rendering order
				orderedPositions.add(coords);
				orderedIds.add(projectile.getCollisionObject().getUserValue());
				orderedVisibilities.add(Visibility.VISIBLE);
				*/
				orderedObjects.add(projectile);
			} else {
				/*
				orderedTextures.insert(index, projectile.getTexture()); // Put the projectile at the front of the rendering order
				orderedPositions.insert(index, coords);
				orderedIds.insert(index, projectile.getCollisionObject().getUserValue());
				orderedVisibilities.add(Visibility.VISIBLE);
				*/
				orderedObjects.insert(index, projectile);
			}
		}
	}
	
	/*
	private void calculateEntityHealthBarsRenderingOrder(Entities entities, Array<TextureRegion> orderedTextures, Array<Vector2> orderedPositions) {
		for (Entity entity: entities.allEntities) {
			if (entity instanceof Dummy) {
				Vector2 coords = cartesianToScreen(entity.rigidBody.getWorldTransform().getTranslation(emptyVector).x, entity.rigidBody.getWorldTransform().getTranslation(emptyVector).y, entity.rigidBody.getWorldTransform().getTranslation(emptyVector).z);
				float percLife = entity.getLife() / entity.getMaxLife();
				int barLength = (int)(fullEntityLifeBar.getTextureData().getWidth() * percLife);
				entityLifeBar.setRegionWidth(barLength);
				orderedTextures.add(entityLifeBar);
				orderedPositions.add(new Vector2(coords.x - fullEntityLifeBar.getWidth()/2, coords.y + entity.getTexture().getRegionHeight()/2 + 20));
			}
		}
	}
	*/
	
	private void calculateRenderingOrder(PhysicsManager physicsManager, Player player) {
		//orderedTextures.clear();
		//orderedPositions.clear();
		//orderedIds.clear();
		//orderedVisibilities.clear();
		
		orderedObjects.clear();
//		calculateStaticObjectRenderingOrder(man);
		calculateImmobileObjectRenderingOrder(physicsManager);
		calculateMobileObjectRenderingOrder(physicsManager);
		calculatePlayerRenderingOrder(physicsManager, player); // For some reason, player -> entities works but entities -> player doesn't
		calculateEntitiesRenderingOrder(physicsManager);
		calculateParticlesRenderingOrder(physicsManager);
		calculateProjectilesRenderingOrder(physicsManager);
	}
	
	private void renderObjects(SpriteBatch spriteBatch, Entities entities) {
		/*
		for (int i = 0; i < orderedTextures.size; i ++) {
			switch (orderedVisibilities.get(i)) {
				case VISIBLE:
					break;
				case TRANSLUCENT:
					spriteBatch.setColor(1, 1, 1, 0.5f);
					//System.out.println(123);
					break;
				case INVISIBLE:
					spriteBatch.setColor(1, 1, 1, 0);
					break;
			}
			if (camera.getZoom() == 1) {
				spriteBatch.draw(orderedTextures.get(i), orderedPositions.get(i).x, orderedPositions.get(i).y);
			} else {
				//float factor = Math.abs(orderedPositions.get(i).x - Gdx.graphics.getWidth()/2) / Gdx.graphics.getWidth()/2;
				float xDisp = (orderedPositions.get(i).x - Gdx.graphics.getWidth()/2) * camera.getZoom();
				float yDisp = (orderedPositions.get(i).y - Gdx.graphics.getHeight()/2) * camera.getZoom();
				orderedPositions.get(i).set(Gdx.graphics.getWidth()/2 + xDisp, Gdx.graphics.getHeight()/2 + yDisp);
				spriteBatch.draw(orderedTextures.get(i), orderedPositions.get(i).x, orderedPositions.get(i).y, 0, 0,
								 orderedTextures.get(i).getRegionWidth(), orderedTextures.get(i).getRegionHeight(),
								 camera.getZoom(), camera.getZoom(), 0);
			}
			spriteBatch.setColor(1, 1, 1, 1);
		}
		*/
		
		effectiveZoom = camera.getZoom();
		for (int i = 0; i < orderedObjects.size; i ++) {
			switch (orderedObjects.get(i).visibility) {
				case VISIBLE:
					break;
				case TRANSLUCENT:
					spriteBatch.setColor(1, 1, 1, 0.5f);
					break;
				case INVISIBLE:
					// Invisibility is handled in an if statement
					break;
			}
			
			if (orderedObjects.get(i).visibility == Visibility.INVISIBLE) {
				continue;
			} else if (effectiveZoom == 1) {
				renderUnzoomed(orderedObjects.get(i), spriteBatch, entities);
			} else {
				renderZoomed(orderedObjects.get(i), spriteBatch, entities);
			}
			spriteBatch.setColor(1, 1, 1, 1);
		}
	}

	private void renderUnzoomed(WorldObject worldObject, SpriteBatch spriteBatch, Entities entities) {
		spriteBatch.draw(worldObject.getTexture(), worldObject.renderPos.x, worldObject.renderPos.y);

		if (PhysicsManager.isNonPlayerEntity(worldObject.physicsId)) {
			Entity entity = entities.getEntity(worldObject.id);
			float percLife = entity.getLife() / entity.getMaxLife();
			int barLength = (int)(fullEntityLifeBar.getTextureData().getWidth() * percLife);
			//TextureRegion entityLifeBar = new TextureRegion(fullEntityLifeBar);
			entityLifeBar.setRegionWidth(barLength); ///
			spriteBatch.draw(entityLifeBar, entity.renderPos.x + entity.getTexture().getRegionWidth()/2f - fullEntityLifeBar.getWidth()/2f, entity.renderPos.y + entity.getTexture().getRegionHeight() + 20);
		}
	}

	private void renderZoomed(WorldObject worldObject, SpriteBatch spriteBatch, Entities entities) {
		//float factor = Math.abs(orderedPositions.get(i).x - Gdx.graphics.getWidth()/2) / Gdx.graphics.getWidth()/2;
		float xDisp = (worldObject.renderPos.x - AON_E.WORLD_WIDTH/2) * effectiveZoom;
		float yDisp = (worldObject.renderPos.y - AON_E.WORLD_HEIGHT/2) * effectiveZoom;
		worldObject.renderPos.set(AON_E.WORLD_WIDTH/2 + xDisp, AON_E.WORLD_HEIGHT/2 + yDisp);
		spriteBatch.draw(worldObject.getTexture(), worldObject.renderPos.x, worldObject.renderPos.y, 0, 0,
				worldObject.getTexture().getRegionWidth(), worldObject.getTexture().getRegionHeight(),
				effectiveZoom, effectiveZoom, 0);

		if (PhysicsManager.isNonPlayerEntity(worldObject.physicsId)) {
			drawEntityLifeBar(entities.getEntity(worldObject.id), spriteBatch);
		}
	}

	private void drawEntityLifeBar(Entity entity, SpriteBatch spriteBatch) {
		float percLife = entity.getLife() / entity.getMaxLife();
		int barLength = (int)(fullEntityLifeBar.getTextureData().getWidth() * percLife);
		//TextureRegion entityLifeBar = new TextureRegion(fullEntityLifeBar);
		entityLifeBar.setRegionWidth(barLength); ///
		Vector2 coords = new Vector2(entity.renderPos.x - ((fullEntityLifeBar.getWidth()/2f - entity.getTexture().getRegionWidth()/2f) * effectiveZoom), entity.renderPos.y + ((entity.getTexture().getRegionHeight() + 20) * effectiveZoom));
		//xDisp = (coords.x - Gdx.graphics.getWidth()/2) * camera.getZoom();
		//yDisp = (coords.y - Gdx.graphics.getHeight()/2) * camera.getZoom();
		//coords.set(Gdx.graphics.getWidth()/2 + xDisp, Gdx.graphics.getHeight()/2 + yDisp);
		spriteBatch.draw(entityLifeBar, coords.x, coords.y, 0, 0, entityLifeBar.getRegionWidth(), entityLifeBar.getRegionHeight(),
				effectiveZoom, effectiveZoom, 0);
	}
	
	public void render(PlayScreen playScreen) {
		SpriteBatch spriteBatch = playScreen.game.batch;
		Player player = playScreen.player;
		Entities entities = playScreen.entities;
		PhysicsManager physicsManager = playScreen.physicsManager;
//		ParticleEngine particleEngine = playScreen.particleEngine;
//		ProjectileManager projectileManager = playScreen.projectileManager;
		
		//calcSunPos();
		
		//playerCoords = player.pos.cpy();
		
		isoPlayer = cartesianToIso(player.pos.x, player.pos.z);
		isoPlayer.y += player.pos.y*tileHeight;
		//int x = (int)player.getStatus().getXPos();
		//int y = (int)player.getStatus().getYPos();
		
		camera.update(player, playScreen);
		camera.isoPos = cartesianToIso(camera.pos.x, camera.pos.z);
		camera.isoPos.y += camera.pos.y*tileHeight;
		
		calculateRenderingOrder(physicsManager, player);
		
		spriteBatch.begin();
		
		renderObjects(spriteBatch, entities);
		
//		spriteBatch.draw(vignette, AON_E.leftLimit, AON_E.lowerLimit, AON_E.effectiveScreenWidth, AON_E.effectiveScreenHeight);
		spriteBatch.draw(vignette, 0, 0, AON_E.WORLD_WIDTH, AON_E.WORLD_HEIGHT);
		
		/*
		for (StaticObject object: physicsManager.renderable) {
			pixelCoords.x = object.getSpriteX() - isoPlayer.x + Gdx.graphics.getWidth()/2;
			pixelCoords.y = object.getSpriteY() - isoPlayer.y + Gdx.graphics.getHeight()/2;
			//System.out.println(1);
			// Sprite x = 0, sprite y = 0
			//System.out.println(2);
			//System.out.println(pixelCoords.x);
			//System.out.println(pixelCoords.y);
			spriteBatch.draw(object.getTexture(), pixelCoords.x, pixelCoords.y);
		}
		for (Entity entity: entities.allEntities) {
			if (entity instanceof Dummy) {
				Vector2 coords = cartesianToScreen(entity.rigidBody.getWorldTransform().getTranslation(emptyVector).x, entity.rigidBody.getWorldTransform().getTranslation(emptyVector).y, entity.rigidBody.getWorldTransform().getTranslation(emptyVector).z);
				spriteBatch.draw(entity.getTexture(), coords.x - entity.getTexture().getRegionWidth()/2, coords.y - entity.getTexture().getRegionHeight()/2);
			}
		}
		
		spriteBatch.draw(player.getTexture(), Gdx.graphics.getWidth()/2 - player.getTexture().getRegionWidth()/2, Gdx.graphics.getHeight()/2 - player.getTexture().getRegionHeight()/2);
		
		for (Entity entity: entities.allEntities) {
			if (entity instanceof Dummy) {
				Vector2 coords = cartesianToScreen(entity.rigidBody.getWorldTransform().getTranslation(emptyVector).x, entity.rigidBody.getWorldTransform().getTranslation(emptyVector).y, entity.rigidBody.getWorldTransform().getTranslation(emptyVector).z);
				float percLife = entity.getLife() / entity.getMaxLife();
				int barLength = (int)(fullEntityLifeBar.getTextureData().getWidth() * percLife);
				entityLifeBar.setRegionWidth(barLength);
				game.batch.draw(entityLifeBar, coords.x - fullEntityLifeBar.getWidth()/2, coords.y + entity.getTexture().getRegionHeight()/2 + 20);
			}
		}
		
		renderParticles(particleEngine, spriteBatch, player);
		renderProjectiles(projectileManager, spriteBatch, player);
		*/
		
		spriteBatch.setColor(1, 1, 1, (1 - time.getSunlightLevel()) / 1.3f);
		spriteBatch.draw(dayNightOverlay, 0, 0);
		spriteBatch.setColor(1, 1, 1, 1);
		
		//displayCriticalStats(spriteBatch, player);
		
		spriteBatch.end();
		
		/*
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0.1f, (1 - time.getSunlightLevel()) / 1.3f);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();
		*/
	}
	
	/*
	private void renderParticles(ParticleEngine particleEngine, SpriteBatch spriteBatch, Player player) {
		//System.out.println(particleEngine.particles.size);
		for (Particle particle: particleEngine.particles) {
			Vector2 point = cartesianToScreen(particle.pos.x, particle.pos.y, particle.pos.z);
			spriteBatch.draw(ParticleSprites.getCurrentTexture(particle), point.x, point.y);
		}
		//System.out.println(i);
	}
	
	private void renderProjectiles(ProjectileManager projectileManager, SpriteBatch spriteBatch, Player player) {
		for (Projectile projectile: projectileManager.projectiles) {
			if (ProjectileSprites.hasTexture(projectile.sprite)) {
				Vector2 point = cartesianToScreen(projectile.pos.x, projectile.pos.y, projectile.pos.z);
				spriteBatch.draw(ProjectileSprites.getCurrentTexture(projectile), point.x, point.y);
			}
		}
	}
	*/
	
	/*
	private void calcSunPos() {
		//renderShadows = true;
		float a;
		if (time.getDecHour() < 8) {
			a = 16 + time.getDecHour();
		} else {
			a = time.getDecHour() - 8;
		}
		sunAngleDegrees = (a / 24) * 360;
		if (sunAngleDegrees > 180) {
			//renderShadows = false;
			return;
		}
		sunAngleRadians = (float)Math.toRadians(sunAngleDegrees);
		float x0 = (float)(sunsDistFromEarth * Math.cos(sunAngleRadians)); // abs value of x coord of sun relative to centre of world map
		if (sunAngleDegrees > 90)
			x0 = -x0; // If the sun is nearer to west, then its pos relative to the centre of the world will be negative
		float z0 = (float)(sunsDistFromEarth * Math.sin(sunAngleRadians)); // z coord of sun relative to centre of world map
		sunPos.x = physicsWorldSize / 2 + x0;
		sunPos.y = physicsWorldSize / 2;
		sunPos.z = z0; // Actually 0 + z0 (where 0 is general ground level)
		//System.out.println(sunPos.x + ":" + sunPos.y + ":" + sunPos.z);
	}
	*/
	
	/*
	 * Generates the shadow of a tile.
	 * tileZ is the z coord of the tile on which the shadow is being drawn.
	 */
	/*
	private void drawShadow(int x, int y, int z, int tileZ, int screenX, int screenY) {
		float x0, z0;
		x0 = Math.abs(x - sunPos.x); // Diff in x
		//y0 = y - sunPos.y;
		// The difference in the y coords doesn't matter because in real life all shadows are parallel to each other.
		z0 = Math.abs(z - sunPos.z); // Diff in z
		int heightDiff = z - tileZ;
		if (sunAngleDegrees <= 90 && sunAngleDegrees > 1) {
			//System.out.println(1);
			// Vertex 1:
			{
				float gradient = (z0 - 1) / x0;
				vertex1.setCoords(x - heightDiff / gradient, y + 1, tileZ + 1);
			}
			// Vertex 2:
			{
				float gradient = (z0 - 1) / x0;
				vertex2.setCoords(x - heightDiff / gradient, y, tileZ + 1);
			}
			// Vertex 3:
			{
				float gradient = z0 / (x0 - 1);
				vertex3.setCoords((x + 1) - (heightDiff - 1) / gradient, y + 1, tileZ + 1);
			}
			// Vertex 4:
			{
				float gradient = z0 / (x0 - 1);
				vertex4.setCoords((x + 1) - (heightDiff - 1) / gradient, y, tileZ + 1);
			}
			checkPoints(vertex1, vertex2, vertex3, vertex4); // Make sure the shadows don't go into the void
			// Now converting world coords to screen coords
			//point1 = cartesianToScreen(vertex1);
			//point2 = cartesianToScreen(vertex2);
			//point3 = cartesianToScreen(vertex3);
			//point4 = cartesianToScreen(vertex4);
			//if (x == 45 && y == 45 || x == 55 & y == 55 || x == 45 & y == 55 || x == 55 & y == 45) 
			//System.out.println(x + " " + y + " : " + point1.x + " " + point1.y);
		} else if (sunAngleDegrees < 178 && sunAngleDegrees > 90) {
			//System.out.println(3);
			// Vertex 1:
			{
				float gradient = (z0 - 1) / (x0 + 1);
				vertex1.setCoords((x + 1) + heightDiff / gradient, y, tileZ + 1);
			}
			// Vertex 2:
			{
				float gradient = (z0 - 1) / (x0 + 1);
				vertex2.setCoords((x + 1) + heightDiff / gradient, y + 1, tileZ + 1);
			}
			// Vertex 3:
			{
				float gradient = z0 / x0;
				vertex3.setCoords(x + (heightDiff - 1) / gradient, y, tileZ + 1);
			}
			// Vertex 4:
			{
				float gradient = z0 / x0;
				vertex4.setCoords(x + (heightDiff - 1) / gradient, y + 1, tileZ + 1);
			}
			checkPoints(vertex1, vertex2, vertex3, vertex4); // Make sure the shadows don't go into the void
			// Now converting world coords to screen coords
			//point1 = cartesianToScreen(vertex1);
			//point2 = cartesianToScreen(vertex2);
			//point3 = cartesianToScreen(vertex3);
			//point4 = cartesianToScreen(vertex4);
		} else {
			return;
		}
		
		// This is the quadrilateral:
		// 1---2
		// |   |
		// 3---4
		// To join the points together to form a rectangle, we must go 1,2,4,3 rather than 1,2,3,4.
		// This is because going 1,2,3,4 would form an hourglass shape.
		PolygonRegion polyReg = new PolygonRegion(new TextureRegion(tileShadowPolygon),
				  new float[] {      // Four vertices
				    point1.x, point1.y,        // Vertex 0         3--2
				    point2.x, point2.y,        // Vertex 1         | /|
				    point4.x, point4.y,        // Vertex 2         |/ |
				    point3.x, point3.y         // Vertex 3         0--1
				}, new short[] {
				    0, 1, 2,         // Two triangles using vertex indices.
				    0, 2, 3          // Take care of the counter-clockwise direction. 
				});
		//TextureRegion region = polyReg.getRegion();
		polySprite = new PolygonSprite(polyReg);
		polySprite.setOrigin(0, 0);
		polySprite.draw(polyBatch);
	}
	*/
	
	/*
	private void checkPoints(Point3 vertex1, Point3 vertex2, Point3 vertex3, Point3 vertex4) {
		Point3[] vertexArray = {vertex1, vertex2, vertex3, vertex4};
		for (Point3 vertex: vertexArray) {
			if (vertex.x < World.chunkSize) {
				vertex.x = World.chunkSize;
			} else if (vertex.x > world.worldSize() - World.chunkSize) {
				vertex.x = world.worldSize() - World.chunkSize;
			} else if (vertex.y < World.chunkSize) {
				vertex.y = World.chunkSize;
			} else if (vertex.y > world.worldSize() - World.chunkSize) {
				vertex.y = world.worldSize() - World.chunkSize;
			}
		}
	}
	*/
	
	/*
	 * Whereas normally the cart world is turned clockwise (to form the diamond shape), here it is turned anti-clockwise.
	 * In addition, the diamond, once it has been rotated from a square, is not squashed.
	 * Finally, this method does not consider pixels, but instead, arbitrary units.
	 */
	public Vector2 cartToInvertedIso(float x, float y) {
		Vector2 point = new Vector2();
		point.x = x - y;
		point.y = x + y;
		return point;
	}
	
	private Vector2 cartesianToIso(float x, float y) {
		Vector2 point = new Vector2();
		point.x = (x + y) * 64;
		point.y = ((y - x)/2) * 64;
		return point;
	}
	
	private Vector2 isoToCartesian(float x, float y) {
		Vector2 point = new Vector2();
		point.x = (x - 2*y) / 128;
		point.y = (x + 2*y) / 128;
		return point;
	}
	
	private Vector2 isoToScreen(float x, float y) {
		Vector2 point = new Vector2();
		point.x = (int)(x - camera.isoPos.x + AON_E.WORLD_WIDTH/2);
		point.y = (int)(y - camera.isoPos.y + AON_E.WORLD_HEIGHT/2);
		return point;
	}
	
	public Vector2 cartesianToScreen(float x, float y, float z) {
		Vector2 point = cartesianToIso(x, z);
		point.y += y*tileHeight;
		return isoToScreen(point.x, point.y);
	}
	
	/*
	 * Returns coords relative to the player's y plane
	 */
	public  Vector2 screenToRelativeCartesian(float x, float y) {
		Vector2 point = screenToIso(x, y);
		return isoToCartesian(point.x, point.y);
	}
	
	public Vector2 screenToIso(float x, float y) {
		Vector2 point = new Vector2();
		float newX = (x - AON_E.WORLD_WIDTH/2) * (1 / effectiveZoom) + AON_E.WORLD_WIDTH/2;
		float newY = (y - AON_E.WORLD_HEIGHT/2) * (1 / effectiveZoom) + AON_E.WORLD_HEIGHT/2;
		//float newX = x;
		//float newY = y;
		point.x = newX - AON_E.WORLD_WIDTH/2 + camera.isoPos.x;
		point.y = newY - AON_E.WORLD_HEIGHT/2 + camera.isoPos.y - camera.pos.y*tileHeight;
		return point;
	}
	
	/*
	public Point3 screenToCartesian(float x, float y) {
		Point3 point = new Point3();
		Point2 isoPoint = screenToIso(x, y);
		Point2 cartPoint = isoToCartesian(isoPoint.x, isoPoint.y);
		for (int z = World.depth - 1; z >= 0; z --) {
			if (!(world.type(cartPoint.x, cartPoint.y, z) instanceof Air)) {
				point.x = cartPoint.x;
				point.y = cartPoint.y;
				point.z = z;
				return point;
			}
			cartPoint.x --;
			cartPoint.y --;
		}
		return null;
		//x1 += distanceToTop;
		//
		Point3D point = new Point3D();
		Point2D cartPoint = isoToCartesian(x / 64, y / 64);
		int x1 = (int)(player.getStatus().getXPos() + World.depth - 1 - player.getStatus().getZPos()) + (int)cartPoint.x;
		x1 += x - player.getStatus().getXPos();
		int y1 = (int)(player.getStatus().getYPos() + World.depth - 1 - player.getStatus().getZPos()) + (int)cartPoint.y;
		y1 += y - player.getStatus().getYPos();
		for (int z1 = World.depth - 1; z1 >= 0; z1 --) {
			if (!(world.type(x1, y1, z1) instanceof Air)) {
				point.x = x1;
				point.y = y1;
				point.z = z1;
				return point;
			}
			x1 --;
			y1 --;
		}
		return null;
		//
	}
	*/
	
	/*
	public Point3 screenToCartesian(Point2 screenPoint) {
		Point3 point = new Point3();
		Point2 isoPoint = screenToIso(screenPoint.x, screenPoint.y);
		Point2 cartPoint = isoToCartesian(isoPoint.x, isoPoint.y);
		for (int z = World.depth - 1; z >= 0; z --) {
			if (!(world.type(cartPoint.x, cartPoint.y, z) instanceof Air)) {
				point.x = cartPoint.x;
				point.y = cartPoint.y;
				point.z = z;
				return point;
			}
			cartPoint.x --;
			cartPoint.y --;
		}
		return null;
	}
	*/
	
	public void dispose() {
		shapeRenderer.dispose();
	}

	public float getEffectiveZoom() {
		return effectiveZoom;
	}

	public void setEffectiveZoom(float effectiveZoom) {
		this.effectiveZoom = effectiveZoom;
	}
	
}
