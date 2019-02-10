package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.AON_E;

abstract class MyScreen implements Screen {

	public AON_E game;

	Vector2 virtualCoords;

	MyScreen(AON_E game) {
		this.game = game;
	}

	abstract void update();

	/**
	 * Updates the position of the pointer used as the cursor.
	 */
	void updatePointer() {
		game.pointer.setCenterX(virtualCoords.x);
		game.pointer.setCenterY(virtualCoords.y); // Unproject gives a vector with origin at bottom left, so height - y is not needed.
		game.camera.update();
	}

	Vector2 screenToVirtual(int x, int y) {
		return game.viewport.unproject(new Vector2(x, y));
	}

	void updateVirtualCoords() {
		virtualCoords = screenToVirtual(Gdx.input.getX(), Gdx.input.getY());
	}

	void updateViewport(int width, int height) {
		game.viewport.update(width, height, true);
	}

}
