package com.mygdx.game.quests;

import com.mygdx.game.screens.PlayScreen;

public abstract class Quest {

	protected String name;
	protected String desc;
	protected QuestType questType;
	protected boolean visible;
	protected boolean inProgress;
	protected boolean completed;
	protected boolean rewardReceived;
	
	public abstract void update(PlayScreen playScreen);
	
	protected enum QuestType {
		MAIN_QUEST,
		SIDE_QUEST
	}
	
}