package com.mygdx.game.spells;

public class Spell {
	
	private String name;
	private String desc;
	private boolean learned;
	private boolean displayed;
	private MagicElement magicElement;
	
	public enum MagicElement {
		FIRE
	}
	
	public Spell() { }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLearned() {
		return learned;
	}
	public void setLearned(boolean learned) {
		this.learned = learned;
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
	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	public MagicElement getMagicElement() {
		return magicElement;
	}
	public void setMagicElement(MagicElement magicElement) {
		this.magicElement = magicElement;
	}
	
}
