package com.mygdx.game.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.screens.PlayScreen;

public abstract class OwnStage {

	protected String name;
	public Stage stage;
	
	public abstract void render(PlayScreen playScreen);
	
	public abstract void update(PlayScreen playScreen);
	
	public String getName() {
		return name;
	}
	
}
