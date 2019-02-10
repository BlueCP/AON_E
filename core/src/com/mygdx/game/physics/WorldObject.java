package com.mygdx.game.physics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.IsometricRenderer.Visibility;

public abstract class WorldObject {

	public int id;
	public int physicsId;
	//public TextureRegion texture;
	public Vector2 renderPos;
	public Visibility visibility;
	
	/*
	public static enum Visibility {
		VISIBLE,
		TRANSLUCENT,
		INVISIBLE;
	}
	*/
	
	public abstract void updateWorldObject(IsometricRenderer renderer);
	
	public abstract TextureRegion getTexture();
	
}
