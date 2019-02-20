package com.mygdx.game.physicsobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.AON_E;
import com.mygdx.game.physics.PhysicsManager.Tag;
import com.mygdx.game.physics.WorldObject;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.IsometricRenderer.Visibility;

/**
 * Represents objects in the world; terrain, controllers, controllables, and interactives.
 * They are constant because unlike all other objects (entities, projectiles, particles, etc.), they exist indefinitely.
 */
public abstract class ConstantObject extends WorldObject implements Disposable {

	protected Vector3 pos;
	private TextureRegion[] textures;
	private boolean isAnimation;
	private short animationFrame = 0; // If not an animation, stay at 0
	private int spriteX;
	private int spriteY;
	public btCollisionObject collisionObject;
	private Array<Tag> tags;
	
	ConstantObject(btCollisionObject collisionObject, TextureRegion[] textureRegions, String id, Array<Tag> newTags,
				   int spriteX, int spriteY) {
		this.collisionObject = collisionObject;
		this.textures = textureRegions;
		this.id = Integer.parseInt(id);
		this.tags = newTags;
		if (textureRegions.length > 1) {
			isAnimation = true;
		}
		this.spriteX = spriteX;
		this.spriteY = spriteY;
		pos = new Vector3();
		collisionObject.getWorldTransform().getTranslation(pos);
	}
	
	protected void update() {
		collisionObject.getWorldTransform().getTranslation(pos);
	}
	
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

	public short getAnimationFrame() {
		return animationFrame;
	}

	public void setAnimationFrame(short animationFrame) {
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