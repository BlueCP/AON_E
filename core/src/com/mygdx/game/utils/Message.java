package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Color;

public class Message {

	private String text;
	private Color colour;
	private float duration;
	// private int yMod;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Color getColour() {
		return colour;
	}
	public void setColour(Color colour) {
		this.colour = colour;
	}
	/*
	public int getYMod() {
		return yMod;
	}
	public void setYMod(int yMod) {
		this.yMod = yMod;
	}
	*/
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
}
