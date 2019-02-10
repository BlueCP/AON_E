package com.mygdx.game.playerattributes;

public class Move {
	
	private String name;
	private String desc;
	private String req;
	private boolean learned;
	private boolean displayed;
	private int condition1;
	private int condition2;
	
	Move() { }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLearned() {
		return learned;
	}
	void setLearned(boolean learned) {
		this.learned = learned;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getReq() {
		return req;
	}
	void setReq(String req) {
		this.req = req;
	}
	public boolean isDisplayed() {
		return displayed;
	}
	void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	int getCondition1() {
		return condition1;
	}
	void setCondition1(int condition1) {
		this.condition1 = condition1;
	}
	int getCondition2() {
		return condition2;
	}
	void setCondition2(int condition2) {
		this.condition2 = condition2;
	}
	
}
