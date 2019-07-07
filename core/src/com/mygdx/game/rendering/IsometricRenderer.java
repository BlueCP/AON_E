package com.mygdx.game.rendering;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;
import com.mygdx.game.droppeditems.DroppedItem;
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
import com.mygdx.game.statuseffects.StatusEffectSprites;
import com.mygdx.game.utils.RenderMath;
import com.mygdx.game.world.Time;
import javafx.util.Pair;

public class IsometricRenderer {

	//private final int sunsDistFromEarth = 1000;
	
	private Time time;
	private Texture fullEntityLifeBar;

	//private PolygonSprite polySprite;
	//private PolygonSpriteBatch polyBatch; // To assign at the beginning
	//private Texture tileShadowPolygon;
	
	//private FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	//TextureRegion fboRegion;
	
	//private Vector3 sunPos;
	//private float sunAngleDegrees;
	//private float sunAngleRadians;
	
//	private final int tileWidth = 128;
	public static final int tileHeight = 64;
//	private final int pixelWorldWidth = 256000;
//	private final int pixelWorldHeight = 128000;
//	private final int physicsWorldSize = 2000;
	//private final int constA = (pixelWorldHeight - 1) - (int)(pixelWorldHeight / 2);
	//private final int constA = 0;

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
	
	private Array<WorldObject> orderedObjects;
	
	public Camera camera;
	
	private TextureRegion entityLifeBar;
	private Texture vignette;
	private Texture dayNightOverlay;
	
//	private float effectiveZoom = 1; // A combination of the camera zoom and the zoom considering the screen size.
	
	public IsometricRenderer(AON_E game, Time time) {
		this.time = time;
		fullEntityLifeBar = game.manager.get("sprites/fullEntityLifeBar.png");
		vignette = game.manager.get("textures/vignette.png");
		
		orderedObjects = new Array<>();
		
		camera = new Camera();

		entityLifeBar = new TextureRegion(fullEntityLifeBar);
		
//		camera.addHardPanNow(5, new Vector3(2, 0, 2));
//		camera.addHardZoomToQueue(3, 2f);
//		camera.addSoftZoomToQueue(15, 2f);
//		camera.addSoftZoomToQueue(5, 2f);
//		camera.setZoom(0.5f);
//		camera.addCutToQueue(2, Vector3.Zero);
//		camera.addHardPanToQueue(3, new Vector3(3, 0, 3));
//		camera.addCutToQueue(2, Vector3.Zero);
//		camera.addSoftPanToQueue(3, new Vector3(-3, 0, -3));
//		camera.addCutToQueue(5, new Vector3(-2, 0, 2));
//		camera.addCutToQueue(2, new Vector3(2, 0, 2));
//		camera.addCutToQueue(2, new Vector3(2, 0, -2));
//		camera.addCutToQueue(2, new Vector3(-2, 0, -2));
		
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
		}
		if (index == Integer.MAX_VALUE) {
			index = -1;
		}
		return index;
	}

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
	
	private void calculateEntitiesRenderingOrder(PhysicsManager physicsManager) {
		for (int i = 0; i < physicsManager.renderableEntities.size; i ++) {
			Entity entity = physicsManager.renderableEntities.get(i);
			Array<Integer> hitIds = physicsManager.convexSweepTestAll(entity.rigidBody, entity.pos);
			int index = findLowestIndex(hitIds);
			entity.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the entity
				orderedObjects.add(entity);
			} else {
				orderedObjects.insert(index, entity);
			}
		}
	}
	
	private void calculatePlayerRenderingOrder(PhysicsManager physicsManager, Player player) {
		Array<Integer> hitIds = physicsManager.convexSweepTestAll(player.rigidBody, player.pos);
		int index = findLowestIndex(hitIds);
		player.updateWorldObject(this);
		if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the player
			orderedObjects.add(player);
		} else {
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
			Array<Pair<Integer, Vector3>> hits = physicsManager.rayTestAll(particle.pos, TestMode.UPWARDS);
			Array<Integer> hitIds = new Array<>();
			for (Pair<Integer, Vector3> pair: hits) {
				hitIds.add(pair.getKey());
			}
			int index = findLowestIndex(hitIds);
			particle.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the particle
				orderedObjects.add(particle);
			} else {
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
			Array<Integer> hitIds = physicsManager.convexSweepTestAll(projectile.getCollisionObject(), projectile.pos);
//			System.out.println(hitIds);
			int index = findLowestIndex(hitIds);
			projectile.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the projectile
				orderedObjects.add(projectile);
			} else {
				orderedObjects.insert(index, projectile);
			}
		}
	}

	private void calculateDroppedItemsRenderingOrder(PhysicsManager physicsManager) {
		for (int i = 0; i < physicsManager.renderableDroppedItems.size; i ++) {
			DroppedItem droppedItem = physicsManager.renderableDroppedItems.get(i);
			Array<Integer> hitIds = physicsManager.convexSweepTestAll(droppedItem.rigidBody, droppedItem.pos);
			int index = findLowestIndex(hitIds);
			droppedItem.updateWorldObject(this);
			if (hitIds.size == 0 || index == -1) { // If there are no objects blocking the projectile
				orderedObjects.add(droppedItem);
			} else {
				orderedObjects.insert(index, droppedItem);
			}
		}
	}
	
	private void calculateRenderingOrder(PhysicsManager physicsManager, Player player) {
		orderedObjects.clear();
		calculateImmobileObjectRenderingOrder(physicsManager);
		calculateMobileObjectRenderingOrder(physicsManager);
		calculatePlayerRenderingOrder(physicsManager, player); // For some reason, player -> entities works but entities -> player doesn't
		calculateEntitiesRenderingOrder(physicsManager);
		calculateParticlesRenderingOrder(physicsManager);
		calculateProjectilesRenderingOrder(physicsManager);
		calculateDroppedItemsRenderingOrder(physicsManager);
	}
	
	private void renderObjects(SpriteBatch spriteBatch, PlayScreen playScreen) {
//		effectiveZoom = camera.getZoom();
		for (int i = 0; i < orderedObjects.size; i ++) {
			/*switch (orderedObjects.get(i).visibility) {
				case VISIBLE:
					break;
				case TRANSLUCENT:
					spriteBatch.setColor(1, 1, 1, 0.5f);
					break;
				case INVISIBLE:
					// Invisibility is handled in an if statement
					break;
			}*/
			if (orderedObjects.get(i).visibility == Visibility.INVISIBLE) {
				continue;
			} else if (orderedObjects.get(i).visibility == Visibility.TRANSLUCENT) {
				spriteBatch.setColor(1, 1, 1, 0.5f);
				renderSingleObject(orderedObjects.get(i), spriteBatch, playScreen);
			} else {
				renderSingleObject(orderedObjects.get(i), spriteBatch, playScreen);
			}

			spriteBatch.setColor(1, 1, 1, 1);
		}
	}

	private void renderSingleObject(WorldObject worldObject, SpriteBatch spriteBatch, PlayScreen playScreen) {
//		spriteBatch.draw(worldObject.getTexture(), worldObject.renderPos.x, worldObject.renderPos.y);
		worldObject.render(spriteBatch, this);

		if (PhysicsManager.isEntityOrPlayer(worldObject.physicsId)) {
			Entity entity = playScreen.entities.getEntity(worldObject.id, playScreen.player);
			drawStackEffects(entity, spriteBatch);
		}
		if (PhysicsManager.isNonPlayerEntity(worldObject.physicsId)) {
			Entity entity = playScreen.entities.getEntity(worldObject.id);
			drawEntityLifeBar(entity, spriteBatch);
			drawInteractiveIndicator(entity, spriteBatch, playScreen.game);
			drawSpeechBubble(entity, spriteBatch);
		}
	}

	private void drawEntityLifeBar(Entity entity, SpriteBatch spriteBatch) {
		float percLife = entity.getLife() / entity.getMaxLife();
		int barLength = (int)(fullEntityLifeBar.getTextureData().getWidth() * percLife);
		//TextureRegion entityLifeBar = new TextureRegion(fullEntityLifeBar);
		entityLifeBar.setRegionWidth(barLength); ///
		spriteBatch.draw(entityLifeBar, entity.renderPos.x + entity.getTexture().getRegionWidth()/2f - fullEntityLifeBar.getWidth()/2f, entity.renderPos.y + entity.getTexture().getRegionHeight() + 20);
	}

	/**
	 * Draws the indicator showing the player that this entity can be interacted with.
	 */
	private void drawInteractiveIndicator(Entity entity, SpriteBatch spriteBatch, AON_E game) {
		if (entity.isWaitingToBeInteracted()) {
			spriteBatch.draw(game.exclaimationMark, entity.renderPos.x + entity.getTexture().getRegionWidth() * 3f/4f, entity.renderPos.y + entity.getTexture().getRegionHeight() * 3f/4f, 50, 50);
		}
	}

	/**
	 * Draws a speech bubble for this entity, if it has text to show.
	 */
	private void drawSpeechBubble(Entity entity, SpriteBatch spriteBatch) {
		if (entity.isShowSpeechBubble()) {
			AON_E.DEFAULT_FONT.draw(spriteBatch, entity.getSpeechBubbleText(), entity.renderPos.x + entity.getTexture().getRegionWidth()/2f - 500, entity.renderPos.y + entity.getTexture().getRegionHeight() + 80, 1000, Align.center, true);
		}
	}

	/**
	 * Most stack-type status effects are indicated by a number of images above the entity, to clearly convey the number of stacks.
	 */
	private void drawStackEffects(Entity entity, SpriteBatch spriteBatch) {
		float yCoord = entity.renderPos.y + entity.getTexture().getRegionHeight() + 15; // The number of pixels below the origin of the life bar the images should be drawn at.

		// Chill
		for (int i = 0; i < entity.chilledEffect.numStacks(); i ++) {
			spriteBatch.draw(StatusEffectSprites.soul,
					entity.renderPos.x + entity.getTexture().getRegionWidth()/2f - fullEntityLifeBar.getWidth()/2f + 5 * i,
					yCoord);
		}
		if (entity.chilledEffect.isActive()) {
			yCoord -= 5;
		}

		// Souls
		for (int i = 0; i < entity.soulsEffect.numStacks(); i ++) {
			spriteBatch.draw(StatusEffectSprites.chill,
					entity.renderPos.x + entity.getTexture().getRegionWidth()/2f - fullEntityLifeBar.getWidth()/2f + 5 * i,
					yCoord);
		}
		if (entity.soulsEffect.isActive()) {
			yCoord -= 5;
		}

		// More effects will go here in future.
	}
	
	public void render(PlayScreen playScreen) {
		SpriteBatch spriteBatch = playScreen.game.batch;
		Player player = playScreen.player;
//		Entities entities = playScreen.entities;
		PhysicsManager physicsManager = playScreen.physicsManager;

		Vector2 isoPlayer = RenderMath.cartToIso(player.pos.x, player.pos.z);
		isoPlayer.y += player.pos.y*tileHeight;

		camera.update(player, playScreen);
		camera.isoPos = RenderMath.cartToIso(camera.pos.x, camera.pos.z);
		camera.isoPos.y += camera.pos.y*tileHeight;
		
		calculateRenderingOrder(physicsManager, player);

//		camera.viewport.apply();
		spriteBatch.setProjectionMatrix(camera.orthographicCamera.combined);
		spriteBatch.begin();
		renderObjects(spriteBatch, playScreen);
		spriteBatch.end();

//		playScreen.game.viewport.apply();
		spriteBatch.setProjectionMatrix(playScreen.game.camera.combined);
		spriteBatch.begin();

		spriteBatch.draw(vignette, 0, 0, AON_E.WORLD_WIDTH, AON_E.WORLD_HEIGHT);
		
		spriteBatch.setColor(1, 1, 1, (1 - time.getSunlightLevel()) / 1.3f);
		spriteBatch.draw(dayNightOverlay, 0, 0);
		spriteBatch.setColor(1, 1, 1, 1);
		
		spriteBatch.end();
	}
	
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
	
	public void dispose() {
		shapeRenderer.dispose();
	}

//	public float getEffectiveZoom() {
//		return effectiveZoom;
//	}

//	public void setEffectiveZoom(float effectiveZoom) {
//		this.effectiveZoom = effectiveZoom;
//	}
	
}
