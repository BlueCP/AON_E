package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.physicsobjects.ConstantObject;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.IsometricRenderer.Visibility;

public abstract class WorldObject {

	public int id;
	public int physicsId;

	public Vector3 pos;

	//public TextureRegion texture;
	public Vector2 renderPos;
	public Visibility visibility;
	
	public abstract void updateWorldObject(IsometricRenderer renderer);
	
	public abstract TextureRegion getTexture();

	/**
	 * The default way that world objects are rendered. Can be overridden for custom rendering, such as for lightning bolts.
	 * @param spriteBatch the sprite batch to be rendered to.
	 */
	public void render(SpriteBatch spriteBatch, IsometricRenderer isometricRenderer) {
		spriteBatch.draw(getTexture(), renderPos.x, renderPos.y);
	}

	/**
	 * Returns the chunk that this world object is currently in.
	 * TODO: currently just returns 'start' chunk. When Will sends a dummy map, make this method actually find the chunk you're in.
	 * @return the chunk this object is in.
	 */
	public ConstantObject.Chunk getChunk() {
		return ConstantObject.Chunk.START;
	}

	/**
	 * Returns an array of chunks which are adjacent to this object, including the current chunk.
	 */
	public Array<ConstantObject.Chunk> getAdjacentChunksInclusive() {
		Array<ConstantObject.Chunk> chunks = new Array<>();
		chunks.add(getChunk());
		return chunks;
	}

	/**
	 * Returns an array of chunks which are adjacent to this object, not including the current chunk.
	 * @return
	 */
	public Array<ConstantObject.Chunk> getAdjacentChunksExclusive() {
		return new Array<>();
	}

	public int getId() {
		return id;
	}

}
