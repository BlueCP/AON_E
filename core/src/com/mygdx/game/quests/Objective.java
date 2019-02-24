package com.mygdx.game.quests;

import com.mygdx.game.screens.PlayScreen;

public abstract class Objective {

	protected String desc;
	private boolean inProgress;
	private boolean completed;

	protected Quest quest; // The quest that this objective is a part of.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public Objective() { }

	protected Objective(Quest quest) {
		inProgress = false;
		completed = false;

		this.quest = quest;
	}

	public abstract void update(PlayScreen playScreen);

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
