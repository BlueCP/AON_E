package com.mygdx.game.serialisation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.droppeditems.DroppedItemManager;
import com.mygdx.game.entities.characters.Deltis;
import com.mygdx.game.entities.characters.SilverStatue;
import com.mygdx.game.particles.ParticleEngine;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.quests.Quests;
import com.mygdx.game.utils.NewGameData;
import com.mygdx.game.world.Time;

public class SaveNewGameRunner implements Runnable {

	private NewGameData newGameData;
	
	public SaveNewGameRunner(NewGameData newGameData) {
		this.newGameData = newGameData;
	}
	
	@Override
	public void run() {
		calculateStats();
		getStartingItems();
		initialiseEntities();
		saveEverything();
	}

	private void initialiseEntities() {
		newGameData.entities.addEntity(new Deltis(newGameData.entities, new Vector3(0, 1, -4)));
		newGameData.entities.addEntity(new SilverStatue(newGameData.entities, new Vector3(4, 1, 0)));
	}
	
	private void getStartingItems() {
		/*
		switch (newGameData.player.getMobClass().name) {
		case "warrior":
			newGameData.player.inventory().addWeapon("Iron longsword");
			break;
		case "ranger":
			newGameData.player.inventory().addWeapon("Basic wood shortbow");
			break;
		case "rogue":
			newGameData.player.inventory().addWeapon("Iron dagger");
			break;
		case "mage":
			newGameData.player.inventory().addWeapon("Basic wood magestaff");
			break;
		default:
			newGameData.player.inventory().addWeapon("Iron longsword");
		}
		*/
	}
	
	private void calculateStats() {
		//newGameData.player.eStats().setMaxLife(45 + newGameData.player.eStats().getRes());
		//newGameData.player.eStats().setMaxEnergy(45 + newGameData.player.eStats().getAwa());
		//newGameData.player.eStats().setMaxMana(45 + newGameData.player.eStats().getInt());

		// player.stats.put("eva",1 + player.stats.get("dex"));
		
		/*
		newGameData.player.stats.put("str",tempScores.get("strength"));
		newGameData.player.stats.put("dex",tempScores.get("dexterity"));
		newGameData.player.stats.put("res",tempScores.get("resistance"));
		newGameData.player.stats.put("awa",tempScores.get("awareness"));
		newGameData.player.stats.put("int",tempScores.get("intelligence"));
		*/
		
		//newGameData.player.eStats().setPhysDamage(5);
		//newGameData.player.eStats().setMagDamage(5);
		
		//newGameData.player.eStats().setCritChance(5); // 5% starting crit chance
	}
	
	private void saveEverything() {
		String name = newGameData.player.getPlayerName();
		
		Gdx.files.local("saves/" + name).mkdirs();
		
		// Saving stuff to player.txt
//		newGameData.player.saveInitial(name);
		newGameData.player.saveInitial();

		// Saving stuff to entities.txt
		newGameData.entities.saveEntitiesAndExit(name);
		
		// Saving stuff to particles.txt
		new ParticleEngine().saveAndExit(name);
		
		// Saving stuff to projectiles.txt
		new ProjectileManager().saveAndExit(name);

		// Saving stuff to droppedItems.txt
		new DroppedItemManager().saveAndExit(name);
		
		// Saving stuff to time.txt
		Time time = new Time();
		time.setHour(12);
		time.saveTime(name);
		
		// Saving stuff to playerAchievements.txt
		newGameData.achievements.savePlayerAchievements(name);

		// Saving stuff to quests.txt
		new Quests().save(name);

		PhysicsManager.createInitialFiles(name);
		
		/*// Saving stuff to skills.txt
		newGameData.skillCollection.saveSkills(name);
		
		// Saving stuff to playerMoves.txt
		newGameData.moveCollection.savePlayerMoves(name);
		
		// Saving stuff to playerSpells.txt
		newGameData.spellCollection.savePlayerSpells(name);*/
	}

}
