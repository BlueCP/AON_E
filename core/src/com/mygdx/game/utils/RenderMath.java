package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.AON_E;
import com.mygdx.game.rendering.Camera;
import com.mygdx.game.rendering.IsometricRenderer;

/**
 * Contains helpful conversions between different vectors; cartesian, isometric, and screen coordinates.
 */
public class RenderMath {

	public static Vector2 cartToIso(float x, float y) {
		Vector2 point = new Vector2();
		point.x = (x + y) * 64;
		point.y = ((y - x)/2) * 64;
		return point;
	}

	/**
	 * Converts cartesian to iso, taking into account the height of the coordinates (the y coord).
	 */
	public static Vector2 cartToIso(float x, float y, float z) {
		Vector2 point = cartToIso(x, z);
		point.y += y*IsometricRenderer.tileHeight;
		return point;
	}

	/*
	 * Whereas normally the cart world is turned clockwise (to form the diamond shape), here it is turned anti-clockwise.
	 * In addition, the diamond, once it has been rotated from a square, is not squashed.
	 * Finally, this method does not consider pixels, but instead, arbitrary units.
	 */
	public static Vector2 cartToInvertedIso(float x, float y) {
		Vector2 point = new Vector2();
		point.x = x - y;
		point.y = x + y;
		return point;
	}

	public static Vector2 isoToCart(float x, float y) {
		Vector2 point = new Vector2();
		point.x = (x - 2*y) / 128;
		point.y = (x + 2*y) / 128;
		return point;
	}

	public static Vector2 isoToScreen(Camera camera, float x, float y) {
		Vector2 point = new Vector2();
		point.x = (int)(x - camera.isoPos.x + AON_E.WORLD_WIDTH/2);
		point.y = (int)(y - camera.isoPos.y + AON_E.WORLD_HEIGHT/2);
		return point;
	}

	public static Vector2 cartToScreen(Camera camera, float x, float y, float z) {
		Vector2 point = RenderMath.cartToIso(x, z);
		point.y += y * IsometricRenderer.tileHeight;
		return RenderMath.isoToScreen(camera, point.x, point.y);
	}

	public static Vector2 screenToIso(Camera camera, float x, float y) {
		Vector2 point = new Vector2();
		float newX = (x - AON_E.WORLD_WIDTH/2) * (1 / camera.getZoom()) + AON_E.WORLD_WIDTH/2;
		float newY = (y - AON_E.WORLD_HEIGHT/2) * (1 / camera.getZoom()) + AON_E.WORLD_HEIGHT/2;
		//float newX = x;
		//float newY = y;
		point.x = newX - AON_E.WORLD_WIDTH/2 + camera.isoPos.x;
		point.y = newY - AON_E.WORLD_HEIGHT/2 + camera.isoPos.y - camera.pos.y*IsometricRenderer.tileHeight;
		return point;
	}

	/*
	 * Returns coords relative to the player's y plane
	 */
	public static Vector2 screenToRelativeCart(Camera camera, float x, float y) {
		Vector2 point = screenToIso(camera, x, y);
		return RenderMath.isoToCart(point.x, point.y);
	}

}
