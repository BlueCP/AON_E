package com.mygdx.game.playerattributes;

public class Skill {

	private String name;
	private String visibleAtr;
	private String internalAtr;
	private int level;
	
	public Skill() {
		
	}
	
	Skill(String name, String visibleAtr, String internalAtr, int level) {
		this.setName(name);
		this.setVisibleAtr(visibleAtr);
		this.setInternalAtr(internalAtr);
		this.setLevel(level);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	void setLevel(int level) {
		this.level = level;
	}

	public String getVisibleAtr() {
		return visibleAtr;
	}

	private void setVisibleAtr(String visibleAtr) {
		this.visibleAtr = visibleAtr;
	}

	public String getInternalAtr() {
		return internalAtr;
	}

	private void setInternalAtr(String internalAtr) {
		this.internalAtr = internalAtr;
	}
	
}
