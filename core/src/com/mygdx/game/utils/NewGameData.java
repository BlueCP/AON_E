package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.achievements.AchievementCollection;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Player;
import com.mygdx.game.items.AllItems;
import com.mygdx.game.playerattributes.MoveCollection;
import com.mygdx.game.spells.SpellCollection;

public class NewGameData {
	
	public Player player; // New player object
	public Entities entities = new Entities();
//	public AllItems items; //  New items object
	// public Equipped equipped = new Equipped(); // New equipped object
	public AchievementCollection achievements = new AchievementCollection(); // New achievements object
//	public SkillCollection skillCollection = new SkillCollection(true); // New skills object
	private MoveCollection moveCollection = new MoveCollection(); // New actions object
	private SpellCollection spellCollection = new SpellCollection(); // New spells object
	//public String worldSeed = "";
	//public int worldSize;
	
	public NewGameData() {
		player = new Player();
		try {
//			items = AllItems.init();
			AllItems.init();
			moveCollection.loadAllMoves();
			spellCollection.loadAllSpells();
		} catch (Exception e) {
			Gdx.app.exit();
		}
	}
}
