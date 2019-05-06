package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.AON_E;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.physics.WorldObject;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.IsometricRenderer.Visibility;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.utils.RenderMath;

/**
 * Represents objects in the world; terrain, controllers, controllables, and interactives.
 * They are constant because unlike all other objects (entities, projectiles, particles, etc.), they exist indefinitely.
 */
public abstract class ConstantObject extends WorldObject implements Disposable {

//	protected Vector3 pos;
	private TextureRegion[] textures;
	private boolean isAnimation;
	private int animationFrame = 0; // If not an animation, stay at 0
	private int spriteX;
	private int spriteY;
	public btCollisionObject collisionObject;
	private Array<Tag> tags;
	private Chunk chunk;

	/**
	 * The way to represent which chunk this object belongs to.
	 * Because chunks are irregular, this is necessary to identify which objects are in a chunk.
	 * For example, when unloading a single chunk, it's possible to only unload objects which belong to that chunk.
	 */
	public enum Chunk {

		START("start"); // Just the default value for now, as there's only one chunk.

		private String type;
		public String type() { return type; }

		Chunk(String type) {
			this.type = type;
		}

	}

	/**
	 * The normal constructor that most Constant Objects will use.
	 * @param collisionObject the collision object of this object.
	 * @param textureRegions the array of texture regions which will be used as the sprite/animation.
	 * @param id the id of this constant object.
	 * @param newTags the tags which this object has.
	 */
	ConstantObject(btCollisionObject collisionObject, TextureRegion[] textureRegions, String id, Array<Tag> newTags) {
		this.collisionObject = collisionObject;
		this.textures = textureRegions;
		this.id = Integer.parseInt(id);
		this.tags = newTags;
		if (textureRegions.length > 1) {
			isAnimation = true;
		}
		/*this.spriteX = spriteX;
		this.spriteY = spriteY;*/
		pos = new Vector3();
//		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(new Vector3(0, 0, 0)));
//		collisionObject.setWorldTransform(collisionObject.getWorldTransform().setTranslation(new Vector3(3, 3, 0)));
		collisionObject.getWorldTransform().getTranslation(pos);
		Vector2 renderCoords = RenderMath.cartToIso(pos.x, pos.y, pos.z);
		this.spriteX = (int) (renderCoords.x - textureRegions[0].getRegionWidth()/2f); // Use the first element of texture regions as we assume they're all the same size.
		this.spriteY = (int) (renderCoords.y - textureRegions[0].getRegionHeight()/2f);
	}

	/**
	 * This constructor is used exclusively by NullConstantObject.
	 */
	ConstantObject() {
		this.collisionObject = new btCollisionObject();
		this.textures = new TextureRegion[1];
		this.id = -1;
		this.tags = new Array<>();
		isAnimation = false;
		pos = new Vector3();
		spriteX = 0;
		spriteY = 0;
	}

	/**
	 * This method overrides getChunk in WorldObject. Unlike other world objects such as entities and projectiles,
	 * the chunk of constant objects never changes, so it makes sense to put it in a field.
	 * @return the chunk this constant object is in.
	 */
	public Chunk getChunk() {
		return chunk;
	}

	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	public abstract void save(Kryo kryo, Output output);

	public abstract void load(Kryo kryo, Input input);

	/**
	 * Called when the save is loaded for the first time. Empty body by default; custom implementations in subclasses.
	 * Especially useful so that controllers/controllables can set up their ports in controllableFlags.
	 */
	public void initialise(PhysicsManager physicsManager) {

	}

	public void basicUpdate() {
		collisionObject.getWorldTransform().getTranslation(pos);
	}

	/**
	 * By default, does nothing. Custom implementations possible in subclasses.
	 */
	public void update(PlayScreen playScreen) { }

	/**
	 * By default, does nothing. Custom implementations possible in subclasses.
	 */
	public void clicked(PlayScreen playScreen) { }
	
	@Override
	public void updateWorldObject(IsometricRenderer renderer) {
		renderPos = new Vector2(spriteX - renderer.camera.isoPos.x + AON_E.WORLD_WIDTH/2, spriteY - renderer.camera.isoPos.y + AON_E.WORLD_HEIGHT/2);
		visibility = Visibility.VISIBLE;
	}
	
	@Override
	public TextureRegion getTexture() {
		return textures[animationFrame];
	}

	@Override
	public void dispose() {
		for (TextureRegion textureRegion: textures) {
			textureRegion.getTexture().dispose();
		}
		collisionObject.dispose();
	}

	public btCollisionObject getCollisionObject() {
		return collisionObject;
	}

	public void setCollisionObject(btCollisionObject collisionObject) {
		this.collisionObject = collisionObject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TextureRegion[] getTextureArray() {
		return textures;
	}

	public void setTextureArray(TextureRegion[] TextureRegion) {
		this.textures = TextureRegion;
	}

	public int getSpriteX() {
		return spriteX;
	}

	public void setSpriteX(int spriteX) {
		this.spriteX = spriteX;
	}

	public int getSpriteY() {
		return spriteY;
	}

	public void setSpriteY(int spriteY) {
		this.spriteY = spriteY;
	}

	public Vector3 getPos() {
		return pos;
	}

	public void setPos(Vector3 pos) {
		this.pos = pos;
	}

	public int getAnimationFrame() {
		return animationFrame;
	}

	public void setAnimationFrame(int animationFrame) {
		this.animationFrame = animationFrame;
	}

	public TextureRegion[] getTextures() {
		return textures;
	}

	public void setTextures(TextureRegion[] textures) {
		this.textures = textures;
	}

	public boolean isAnimation() {
		return isAnimation;
	}

	public void setAnimation(boolean isAnimation) {
		this.isAnimation = isAnimation;
	}

	public Array<Tag> getTags() {
		return tags;
	}

	public void setTags(Array<Tag> tags) {
		this.tags = tags;
	}

}