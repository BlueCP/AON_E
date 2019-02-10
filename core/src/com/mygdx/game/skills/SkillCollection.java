package com.mygdx.game.skills;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.skills.pyromancer.*;

public class SkillCollection {

	Array<Skill> pyromancerSkills;

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
	}

}
