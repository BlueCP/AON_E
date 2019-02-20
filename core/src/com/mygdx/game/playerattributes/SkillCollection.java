package com.mygdx.game.playerattributes;

import com.mygdx.game.serialisation.KryoManager;

public class SkillCollection {

	private Skill[] skillList = new Skill[10];
	
	public SkillCollection() {
		
	}
	
	public SkillCollection(boolean bool) {
		this.getSkillList()[0] = new Skill("Mightmist", "physical damage", "physDamage", 0);
		this.getSkillList()[1] = new Skill("Strobeheart", "critical strike chance", "critChance", 0);
		this.getSkillList()[2] = new Skill("Soul snare", "lifesteal", "lifesteal", 0);
		this.getSkillList()[3] = new Skill("Amplifier", "magic damage", "magDamage", 0);
		this.getSkillList()[4] = new Skill("Aether cell", "max mana", "maxMana", 0);
		this.getSkillList()[5] = new Skill("Generator", "energy regen", "energyRegen", 0);
		this.getSkillList()[6] = new Skill("Spirit battery", "max life", "maxLife", 0);
		this.getSkillList()[7] = new Skill("Jolt scope", "control power", "controlPower", 0);
		this.getSkillList()[8] = new Skill("Overclock", "cooldown speed", "cooldownSpeed", 0);
		this.getSkillList()[9] = new Skill("Cloak of ghosts", "burst speed", "burstSpeed", 0);
		
		/*
		// Mightmist
		LinkedHashMap<String,String> mightmist = new LinkedHashMap<String,String>();
		mightmist.put("name","Mightmist"); mightmist.put("attribute","physical damage"); mightmist.put("level","0");
		this.skillList.add(mightmist);
		
		// Strobeheart
		LinkedHashMap<String,String> strobeheart = new LinkedHashMap<String,String>();
		strobeheart.put("name","Strobeheart"); strobeheart.put("attribute","crit chance"); strobeheart.put("level","0");
		this.skillList.add(strobeheart);
		
		// Soul snare
		LinkedHashMap<String,String> soulSnare = new LinkedHashMap<String,String>();
		soulSnare.put("name","Soul snare"); soulSnare.put("attribute","lifesteal"); soulSnare.put("level","0");
		this.skillList.add(soulSnare);
		
		// Amplifier
		LinkedHashMap<String,String> amplifier = new LinkedHashMap<String,String>();
		amplifier.put("name","Amplifier"); amplifier.put("attribute","magic damage"); amplifier.put("level","0");
		this.skillList.add(amplifier);
		
		// Aether cell
		LinkedHashMap<String,String> aetherCell = new LinkedHashMap<String,String>();
		aetherCell.put("name","Aether cell"); aetherCell.put("attribute","max energy"); aetherCell.put("level","0");
		this.skillList.add(aetherCell);
		
		// Generator
		LinkedHashMap<String,String> generator = new LinkedHashMap<String,String>();
		generator.put("name","Generator"); generator.put("attribute","energy regen"); generator.put("level","0");
		this.skillList.add(generator);
		
		// Spirit battery
		LinkedHashMap<String,String> spiritBattery = new LinkedHashMap<String,String>();
		spiritBattery.put("name","Spirit battery"); spiritBattery.put("attribute","maxLife"); spiritBattery.put("level","0");
		this.skillList.add(spiritBattery);
		
		// Jolt scope
		LinkedHashMap<String,String> joltScope = new LinkedHashMap<String,String>();
		joltScope.put("name","Jolt scope"); joltScope.put("attribute","control power"); joltScope.put("level","0");
		this.skillList.add(joltScope);
		
		// Overclock
		LinkedHashMap<String,String> overclock = new LinkedHashMap<String,String>();
		overclock.put("name","Overclock"); overclock.put("attribute","cooldown speed"); overclock.put("level","0");
		this.skillList.add(overclock);
		
		// Cloak of ghosts
		LinkedHashMap<String,String> cloakOfGhosts = new LinkedHashMap<String,String>();
		cloakOfGhosts.put("name","Cloak of ghosts"); cloakOfGhosts.put("attribute","burst speed"); cloakOfGhosts.put("level","0");
		this.skillList.add(cloakOfGhosts);
		*/
	}
	
	public void levelUpSkill(int skillNum) {
		this.skillList[skillNum].setLevel(this.skillList[skillNum].getLevel() + 1);
		// Adds one to the level of the chosen skill
	}
	
	public void saveSkills(String name) {
		KryoManager.write(this, "saves/" + name + "/playerSkills.txt");
	}
	
	public static SkillCollection loadSkills(String name) {
		return KryoManager.read("saves/" + name + "/playerSkills.txt", SkillCollection.class);
	}

	public Skill[] getSkillList() {
		return skillList;
	}

	public void setSkillList(Skill[] skillList) {
		this.skillList = skillList;
	}
	
}
