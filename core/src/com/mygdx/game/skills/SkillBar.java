package com.mygdx.game.skills;

public class SkillBar {

	private Skill[] skills;

	public SkillBar() {
		skills = new Skill[10];
		for (int i = 0; i < skills.length; i ++) {
			skills[i] = new NullSkill(null);
		}
	}

	public Skill skillAt(int index) {
		return skills[index];
	}

	public void setSkill(Skill skill, int index) {
		for (int i = 0; i < skills.length; i ++) {
			if (skills[i].sameAs(skill)) {
				skills[i] = new NullSkill(null);
			}
		}
		skills[index] = skill;
	}

}
