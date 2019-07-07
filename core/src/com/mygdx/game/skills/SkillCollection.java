package com.mygdx.game.skills;

import com.mygdx.game.entities.Player;

public class SkillCollection {

	private Player player;
	private int counter = 0;

	/**
	 * No-arg constructor for serialisaton purposes.
	 */
	private SkillCollection() { }

	public SkillCollection(Player player) {
		this.player = player;
	}

	/**
	 * Adds the next skill of the player's class.
	 */
	public void addSkill() {
		switch (player.gameClass) {
			case NONE:
				break;
			case PYROMANCER:
				addPyromancerSkill();
				break;
			case CRYOMANCER:
				addCryomancerSkill();
				break;
			case ELECTROMANCER:
				addElectromancerSkill();
				break;
			case NECROMANCER:
				addNecromancerSkill();
				break;
			case WARRIOR:
				addWarriorSkill();
				break;
			case SUMMONER:
				addSummonerSkill();
				break;
			case PALADIN:
				addPaladinSkill();
				break;
			case ROGUE:
				addRogueSkill();
				break;
			case ARCHER:
				addArcherSkill();
				break;
		}
		counter ++;
	}

	private void addPyromancerSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addCryomancerSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addElectromancerSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addNecromancerSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addWarriorSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addSummonerSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addPaladinSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addRogueSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	private void addArcherSkill() {
		switch (counter) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
		}
	}

	/*Array<Skill> pyromancerSkills;

	private void initPyromancer() {
		pyromancerSkills = new Array<>();

		pyromancerSkills.add(new FireballSkill(null));
		pyromancerSkills.add(new IncendiaryTrapSkill(null));
		pyromancerSkills.add(new FieryVortexSkill(null));
		pyromancerSkills.add(new HeatwaveSkill(null));
		pyromancerSkills.add(new IncinerateSkill(null));
		pyromancerSkills.add(new BurningBarrierSkill(null));
		pyromancerSkills.add(new FlamethrowerSkill(null));
		pyromancerSkills.add(new LavaSnareSkill(null));

		pyromancerSkills.add(new VikingFuneralSkill(null, false));
		pyromancerSkills.add(new StokeTheFlamesSkill(null, false));
		pyromancerSkills.add(new FlamingBarrageSkill(null, false));
		pyromancerSkills.add(new SupernovaSkill(null, false));
	}

	SkillCollection() {
		initPyromancer();
	}*/

}
