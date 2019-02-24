package com.mygdx.game.achievements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.screens.PlayScreen;

public class WalkerAchievement extends Achievement {

	private float secondsWalked;

	WalkerAchievement() {
		name = "Walker";
		desc = "Walk for a total of 3 seconds.";
	}

	@Override
	public void update(PlayScreen playScreen) {
		if (playScreen.player.getAnimationType() == Entity.AnimationType.WALK) {
			secondsWalked += Gdx.graphics.getDeltaTime();
		}
		if (secondsWalked >= 3 && !completed) {
			completed = true;
		}
	}

}
