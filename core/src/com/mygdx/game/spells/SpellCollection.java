package com.mygdx.game.spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.serialisation.KryoManager;

import java.util.Scanner;

public class SpellCollection {

	private Array<Spell> allSpells = new Array<>();
	// private LinkedList<Spell> knownSpells = new LinkedList<Spell>();
	
	public SpellCollection() {
		/*
		this.addSpell("FireballSkill","Shoot a ball of fire at a single enemy.");
		this.addSpell("Immolate","Burn the area 3 tiles around you.");
		this.addSpell("Blink","Blink to a target tile within 3 tiles.");
		this.addSpell("Lightning bolt","Release a bolt of lightning at an enemy, stunning them.");
		this.addSpell("Shroud","Surround yourself with invisibility magic. It will break when you perform a combat action or cast a spell.");
		*/
	}
	
	public void savePlayerSpells(String name) {
		/*
		try {
			Formatter dirN = new Formatter(Gdx.files.getLocalStoragePath() + "/saves/" + dir + "/playerSpells.txt");
			for (Spell spell: this.getAllSpells()) {
				dirN.format(String.valueOf(spell.isLearned()) + "\r\n");
			}
			dirN.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		KryoManager.write(this, "saves/" + name + "/playerSpells.txt");
	}
	
	public static SpellCollection loadAll(String dir) {
		SpellCollection spellCollection = loadPlayerSpells(dir);
		spellCollection.loadAllSpells();
		return spellCollection;
	}
	
	private static SpellCollection loadPlayerSpells(String name) {
		/*
		try {
			File dirN = new File(Gdx.files.getLocalStoragePath() + "/saves/" + dir + "/playerSpells.txt");
			Scanner filePlayerSpells = new Scanner(dirN);
			for (int i = 0; i < this.getAllSpells().size(); i++) {
				if ("true".equals(filePlayerSpells.next())) {
					this.getAllSpells().get(i).setLearned(true);
					this.getAllSpells().get(i).setDisplayed(true);
				} else {
					this.getAllSpells().get(i).setLearned(false);
					this.getAllSpells().get(i).setDisplayed(false);
				}
			}
			filePlayerSpells.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		return KryoManager.read("saves/" + name + "/playerSpells.txt", SpellCollection.class);
	}
	
	public void loadAllSpells() {
		String allSpellsFile = Gdx.files.internal("data/allSpells.txt").readString();
		Scanner fileAllSpells = new Scanner(allSpellsFile);
		String dataBit;
		Spell spell = new Spell();
		while (fileAllSpells.hasNextLine()) {
			dataBit = fileAllSpells.nextLine();
			if (";".equals(dataBit)) {
				this.getAllSpells().add(spell); // Add the completed weapon to items
				if (fileAllSpells.hasNextLine()) {
					spell = new Spell();
					continue;
				} else {
					break;
				}
			}
			spell.setName(dataBit);
			spell.setDesc(fileAllSpells.nextLine());
		}
		fileAllSpells.close();
	}
	
	/*
	public void updateKnownSpells() {
		this.getKnownSpells().clear();
		for (Spell spell: this.getAllSpells()) {
			if (spell.isLearned() == true) {
				this.getKnownSpells().add(spell);
			}
		}
	}
	*/
	
	public Spell getSpell(String name) {
		for (Spell spell: this.getAllSpells()) {
			if (spell.getName().equals(name)) {
				return spell;
			}
		}
		return null;
	}

	public Array<Spell> getAllSpells() {
		return allSpells;
	}

	public void setAllSpells(Array<Spell> allSpells) {
		this.allSpells = allSpells;
	}
	
}
