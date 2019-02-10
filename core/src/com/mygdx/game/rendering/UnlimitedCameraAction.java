package com.mygdx.game.rendering;

import com.mygdx.game.screens.PlayScreen;

public abstract class UnlimitedCameraAction {

	boolean toBeDestroyed;
	
	public abstract void update(PlayScreen playScreen);

	public boolean isToBeDestroyed() {
		return toBeDestroyed;
	}

	public void setToBeDestroyed(boolean toBeDestroyed) {
		this.toBeDestroyed = toBeDestroyed;
	}
	
}
