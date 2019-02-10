package com.mygdx.game.spells;

public class SpellBar {
	
	private Spell[] spells;
	
	public SpellBar() {
		this.spells = new Spell[10];
		for (int i = 0; i < 10; i ++) {
			this.spells[i] = null;
		}
	}

	public Spell getSpell(int i) {
		return spells[i - 1];
	}

	public void setSpell(Spell spell, int i) {
		if (this.contains(spell)) {
			this.spells[this.positionOf(spell) - 1] = null;
		}
		this.spells[i - 1] = spell;
	}
	
	public Spell[] getSpells() {
		return spells;
	}
	
	public boolean contains(Spell spell) {
		for (int i = 0; i < 10; i ++) {
			if (spells[i] != null)
				if (spell.getName().equals(spells[i].getName()))
					return true;
		}
		return false;
	}
	
	public int positionOf(Spell spell) {
		for (int i = 0; i < 10; i ++) {
			if (spells[i] != null)
				if (spell.getName().equals(spells[i].getName()))
					return i + 1;
		}
		return -1; // To show that it is not there
	}
	
}
