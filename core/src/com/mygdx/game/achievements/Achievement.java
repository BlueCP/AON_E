package com.mygdx.game.achievements;

import com.mygdx.game.screens.PlayScreen;

public abstract class Achievement {

	protected String name;
	protected String desc;
	boolean completed;
	private boolean displayed;
	
	Achievement() {
		completed = false;
		displayed = false;
	}

	public abstract void update(PlayScreen playScreen);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompleted() {
		return completed;
	}

	void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public boolean isDisplayed() {
		return displayed;
	}
	
	void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	
}
