package com.mygdx.game.entityattributes;
import java.util.LinkedHashMap;
import java.util.Scanner;

import com.mygdx.game.playerattributes.SkillCollection;
import com.mygdx.game.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;

public class EntityStats {
	
	// STATS such as maxHealth, strength, etc
	private LinkedHashMap<String,Integer> stats = new LinkedHashMap<String, Integer>();
	// "race","class","maxLife","maxEnergy","maxMana","eva","str","dex","res","vit","int","wis","cha","worldSeedO","critChance","xpLevel","xpProg","mapSize","skillPoints","mapSize0","physDamage","lifesteal","magDamage","energyRegen","controlPower","cooldownSpeed","burstSpeed"
	
	// Copy of stats, used to apply % effects to so it can be refreshed every tick
	private LinkedHashMap<String, Integer> statsReal = new LinkedHashMap<String, Integer>();
	
	// "xpLevel","xpProg","skillPoints"
	// LVLS such as xp progress
	private LinkedHashMap<String,Integer> lvls = new LinkedHashMap<String,Integer>();
	
	public EntityStats() {
		stats.put("maxLife",0);
		stats.put("maxEnergy",0);
		stats.put("maxMana",0);
		stats.put("str",0);
		stats.put("dex",0);
		stats.put("res",0);
		stats.put("awa",0);
		stats.put("int",0);
		stats.put("critChance",0);
		stats.put("physDamage",0);
		stats.put("magDamage", 0);
		stats.put("lifesteal",0);
		stats.put("energyRegen",0);
		stats.put("controlPower",0);
		stats.put("cooldownSpeed",0);
		stats.put("burstSpeed",0);
		
		lvls.put("xpLevel",0); lvls.put("xpProg",0); lvls.put("skillPoints",0);
	}
	
	/*
	public void updateRealStats() {
		for (String key: this.stats.keySet()) {
			this.statsReal.put(key, this.stats.get("key"));
		}
	}
	*/
	
	public int getMaxLife() { return stats.get("maxLife"); }
	public void setMaxLife(int maxLife) { this.stats.put("maxLife", maxLife); }
	
	public int getMaxEnergy() { return stats.get("maxEnergy"); }
	public void setMaxEnergy(int maxEnergy) { this.stats.put("maxEnergy", maxEnergy); }
	
	public int getMaxMana() { return stats.get("maxMana"); }
	public void setMaxMana(int maxMana) { this.stats.put("maxMana", maxMana); }
	
	public int getStr() { return stats.get("str"); }
	public void setStr(int str) { this.stats.put("str", str); }
	
	public int getDex() { return stats.get("dex"); }
	public void setDex(int dex) { this.stats.put("dex", dex); }
	
	public int getRes() { return stats.get("res"); }
	public void setRes(int res) { this.stats.put("res", res); }
	
	public int getAwa() { return stats.get("awa"); }
	public void setAwa(int awa) { this.stats.put("awa", awa); }
	
	public int getInt() { return stats.get("int"); }
	public void setInt(int intel) { this.stats.put("int", intel); }
	
	public int getCritChance() { return stats.get("critChance"); }
	public void setCritChance(int critChance) { this.stats.put("critChance", critChance); }
	
	public int getPhysDamage() { return stats.get("physDamage"); }
	public void setPhysDamage(int physDamage) { this.stats.put("physDamage", physDamage); }
	
	public int getMagDamage() { return stats.get("magDamage"); }
	public void setMagDamage(int magDamage) { this.stats.put("magDamage", magDamage); }
	
	public int getLifesteal() { return stats.get("lifesteal"); }
	public void setLifesteal(int lifesteal) { this.stats.put("lifesteal", lifesteal); }
	
	public int getEnergyRegen() { return stats.get("energyRegen"); }
	public void setEnergyRegen(int energyRegen) { this.stats.put("energyRegen", energyRegen); }
	
	public int getControlPower() { return stats.get("controlPower"); }
	public void setControlPower(int controlPower) { this.stats.put("controlPower", controlPower); }
	
	public int getCooldownSpeed() { return stats.get("cooldownSpeed"); }
	public void setCooldownSpeed(int cooldownSpeed) { this.stats.put("cooldownSpeed", cooldownSpeed); }
	
	public int getBurstSpeed() { return stats.get("burstSpeed"); }
	public void setBurstSpeed(int burstSpeed) { this.stats.put("burstSpeed", burstSpeed); }
	
	
	public int getRealMaxLife() { return statsReal.get("maxLife"); }
	public void setRealMaxLife(int maxLife) { this.statsReal.put("maxLife", maxLife); }
	
	public int getRealMaxEnergy() { return statsReal.get("maxEnergy"); }
	public void setRealMaxEnergy(int maxEnergy) { this.statsReal.put("maxEnergy", maxEnergy); }
	
	public int getRealMaxMana() { return statsReal.get("maxMana"); }
	public void setRealMaxMana(int maxMana) { this.statsReal.put("maxMana", maxMana); }
	
	public int getRealStr() { return statsReal.get("str"); }
	public void setRealStr(int str) { this.statsReal.put("str", str); }
	
	public int getRealDex() { return statsReal.get("dex"); }
	public void setRealDex(int dex) { this.statsReal.put("dex", dex); }
	
	public int getRealRes() { return statsReal.get("res"); }
	public void setRealRes(int res) { this.statsReal.put("res", res); }
	
	public int getRealAwa() { return statsReal.get("awa"); }
	public void setRealAwa(int awa) { this.statsReal.put("awa", awa); }
	
	public int getRealInt() { return statsReal.get("int"); }
	public void setRealInt(int intel) { this.statsReal.put("int", intel); }
	
	public int getRealCritChance() { return statsReal.get("critChance"); }
	public void setRealCritChance(int critChance) { this.statsReal.put("critChance", critChance); }
	
	public int getRealPhysDamage() { return statsReal.get("physDamage"); }
	public void setRealPhysDamage(int physDamage) { this.statsReal.put("physDamage", physDamage); }
	
	public int getRealMagDamage() { return statsReal.get("magDamage"); }
	public void setRealMagDamage(int magDamage) { this.statsReal.put("magDamage", magDamage); }
	
	public int getRealLifesteal() { return statsReal.get("lifesteal"); }
	public void setRealLifesteal(int lifesteal) { this.statsReal.put("lifesteal", lifesteal); }
	
	public int getRealEnergyRegen() { return statsReal.get("energyRegen"); }
	public void setRealEnergyRegen(int energyRegen) { this.statsReal.put("energyRegen", energyRegen); }
	
	public int getRealControlPower() { return statsReal.get("controlPower"); }
	public void setRealControlPower(int controlPower) { this.statsReal.put("controlPower", controlPower); }
	
	public int getRealCooldownSpeed() { return statsReal.get("cooldownSpeed"); }
	public void setRealCooldownSpeed(int cooldownSpeed) { this.statsReal.put("cooldownSpeed", cooldownSpeed); }
	
	public int getRealBurstSpeed() { return statsReal.get("burstSpeed"); }
	public void setRealBurstSpeed(int burstSpeed) { this.statsReal.put("burstSpeed", burstSpeed); }
	
	
	/*
	public String getName() { return ids.get("name"); }
	public void setName(String name) { this.ids.put("name", name); }
	
	public String getRace() { return ids.get("race"); }
	public void setRace(String race) { this.ids.put("race", race); }
	
	public String getEntityClass() { return ids.get("class"); }
	public void setEntityClass(String classN) { this.ids.put("class", classN); }
	*/
	
	public int getXpLevel() { return lvls.get("xpLevel"); }
	public void setXpLevel(int xpLevel) { this.lvls.put("xpLevel", xpLevel); }
	
	public int getXpProg() { return lvls.get("xpProg"); }
	public void setXpProg(int xpProg) { this.lvls.put("xpProg", xpProg); }
	
	public int getSkillPoints() { return lvls.get("skillPoints"); }
	public void setSkillPoints(int skillPoints) { this.lvls.put("skillPoints", skillPoints); }
	
	public void saveStats(String dir) throws FileNotFoundException {
		Formatter dirN = new Formatter(dir + "stats.txt");
		String statValue;
		for (String statName: this.stats.keySet()) {
			statValue = String.valueOf(this.stats.get(statName));
			dirN.format(statValue + "\r\n");
		}
		dirN.close();
	}
	
	/*
	public void saveIds(String dir) throws FileNotFoundException {
		Formatter dirN = new Formatter(dir + "ids.txt");
		String idValue;
		for (String idName:this.ids.keySet()) {
			idValue = this.ids.get(idName);
			dirN.format(idValue + "\r\n");
		}
		dirN.close();
	}
	*/
	
	public void saveLvls(String dir) throws FileNotFoundException {
		Formatter dirN = new Formatter(dir + "lvls.txt");
		String lvlValue;
		for (String lvlName:this.lvls.keySet()) {
			lvlValue = String.valueOf(this.lvls.get(lvlName));
			dirN.format(lvlValue + "\r\n");
		}
		dirN.close();
	}
	
	public void loadStats(String dir) {
		try {
			File dirN = new File(dir + "stats.txt");
			Scanner fileStats = new Scanner(dirN);
			for (String statName: this.stats.keySet()) {
				this.stats.put(statName,fileStats.nextInt());
			}
			fileStats.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	public void loadIds(String dir) {
		try {
			File dirN = new File(dir + "ids.txt");
			Scanner fileIds = new Scanner(dirN);
			for (String idName: this.ids.keySet()) {
				this.ids.put(idName,fileIds.next());
			}
			fileIds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	public void loadLvls(String dir) {
		try {
			File dirN = new File(dir + "lvls.txt");
			Scanner fileLvls = new Scanner(dirN);
			for (String lvlName: this.lvls.keySet()) {
				this.lvls.put(lvlName,fileLvls.nextInt());
			}
			fileLvls.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeStat(String atr, int amount) {
		if (this.stats.keySet().contains(atr)) {
			this.stats.put(atr, this.stats.get(atr) + amount);
		} else {
			this.lvls.put(atr, this.lvls.get(atr) + amount);
		}
		// return this;
	}
	
	public void updateStatsCopy(SkillCollection skillCollection) {
		int count = 0;
		for (String statName: this.stats.keySet()) { // Changing all statsCopy values back to default
			this.statsReal.put(statName, this.stats.get(statName));
			if (this.stats.get(statName) != 0) {
				String[] valuesRaw = {"physDamage", "critChance", "lifesteal", "magDamage", "maxMana", "maxLife"};
				ArrayList<String> values = Util.generateArrayList(valuesRaw);
				if (values.contains(statName)) {
					this.changeStat(statName, skillCollection.getSkillList()[count].getLevel() * 5);
					count++;
				} else if ("energyRegen".equals(statName) || "burstSpeed".equals(statName)) {
					this.changeStat(statName, skillCollection.getSkillList()[count].getLevel() / 2);
					count++;
				} else {
					this.changeStat(statName, skillCollection.getSkillList()[count].getLevel());
					count++;
				}
			}
		}
		
	}
	
	public void updateStatsCopy() {
		for (String statName: this.stats.keySet()) { // Changing all statsCopy values back to default
			this.statsReal.put(statName, this.stats.get(statName));
		}
		
	}
	
}
