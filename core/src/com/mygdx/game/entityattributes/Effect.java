package com.mygdx.game.entityattributes;

import java.awt.Color;

public abstract class Effect {
	
	EffectType effectType;
//	private float duration = 0;
//	private int power = 0;
//	private boolean initial = true;
	
	public enum EffectType {
		
		BURNING("Burning", "Just burning.", new Color(255, 128, 38)),
		BLEEDING("Bleeding", "Bleeding out.", new Color(194, 20, 0)),
		POISONED("Poisoned", "Poisoned.", new Color(153, 204, 0)),
		STUNNED("Stunned", "Seeing stars.", Color.yellow),
		INVISIBLE("Invisible", "Just invisible.", new Color(168, 222, 255)),
		SPEED("Speed", "Faster than usual.", new Color(0, 255, 194)),
		SLOWED("Slowed", "A bit slow.", new Color(0, 0, 0)),
		ROOTED("Rooted", "Can't move.", new Color(0, 0, 0));
		
		private String type;
		public String type() { return this.type; }
		
		private String desc;
		public String desc() { return this.desc; }
		
		private Color colour;
		public Color colour() { return this.colour; }
		
		EffectType(String type, String desc, Color colour) {
			this.type = type;
			this.desc = desc;
			this.colour = colour;
		}
		
	}
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	public Effect() { }
	
	/*public Effect(EffectType effectType) {
		this.effectType = effectType;
	}*/
	
	public Effect(EffectType effectType) {
		this.effectType = effectType;
//		this.duration = duration;
//		this.power = power;
	}

	public abstract void add(int power, float duration);

	public abstract void addOne(float duration);

	public abstract void addThisTick(int power);

	public EffectType getStatusEffect() {
		return effectType;
	}
	
	/*public void setStatusEffect(EffectType effectType) {
		this.effectType = effectType;
	}*/

//	public float getDuration() {
//		return duration;
//	}

//	public void setDuration(float duration) {
//		this.duration = duration;
//	}

//	public int getPower() {
//		return power;
//	}

//	public void setPower(int power) {
//		this.power = power;
//	}

}
