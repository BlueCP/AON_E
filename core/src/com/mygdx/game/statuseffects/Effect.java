package com.mygdx.game.statuseffects;

import com.mygdx.game.entities.Entity;

public abstract class Effect {

	String name;
	String desc;

	Entity entity; // The entity that this effect is acting on.

	/*public enum EffectType {
		
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
		
	}*/
	
	/*
	 * No-arg constructor for serialisation purposes.
	 */
	Effect() { }
	
	Effect(Entity entity) {
		this.entity = entity;
	}

	public abstract void update();

}
