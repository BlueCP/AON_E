package com.mygdx.game.quests;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.screens.PlayScreen;

public abstract class Quest {

	protected String name;
	protected String desc;
	protected QuestType questType;
	protected boolean visible;
	boolean inProgress;
	private boolean completed;
	private boolean displayed;

	public Array<Objective> objectives;
	private int objectiveIndex; // The index of the array 'objectives', which points to the current objective.

	protected Quest() {
		inProgress = false;
		completed = false;
		displayed = false;

		objectives = new Array<>();
		objectiveIndex = 0;
	}
	
	void updateObjectives(PlayScreen playScreen) {
		objectives.get(objectiveIndex).update(playScreen);
	}

	public abstract void updateVisibility(PlayScreen playScreen);

	void startQuest() {
		inProgress = true;
		objectives.first().setInProgress(true);
	}

	public void ontoNextObjective() {
		objectives.get(objectiveIndex).setInProgress(false);
		objectives.get(objectiveIndex).setCompleted(true);
		objectiveIndex ++;
		objectives.get(objectiveIndex).setInProgress(true);
	}

	public void finishQuest(PlayScreen playScreen) {
		objectives.get(objectiveIndex).setInProgress(false);
		objectives.get(objectiveIndex).setCompleted(true);

		inProgress = false;
		completed = true;

		onCompletion(playScreen);
	}

	public abstract void onCompletion(PlayScreen playScreen);

	void abandonQuest() {
		objectiveIndex = 0;
		for (Objective objective: objectives) {
			objective.setInProgress(false);
			objective.setCompleted(false);
		}

		inProgress = false;
	}
	
	protected enum QuestType {
		MAIN_QUEST,
		SIDE_QUEST,
		NO_QUEST
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public QuestType getQuestType() {
		return questType;
	}

	public void setQuestType(QuestType questType) {
		this.questType = questType;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
}