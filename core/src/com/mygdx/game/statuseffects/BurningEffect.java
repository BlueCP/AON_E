package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.screens.PlayScreen;

public class BurningEffect extends ProcEffect {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public BurningEffect() { }

	public BurningEffect(Entity entity) {
		super(entity);

		name = "Burning";
		desc = "Just burning.";
	}

	public void apply(PlayScreen playScreen) {
		boolean burningParticlesAdded = false;
		for (int power: powers) {
			entity.changeLife(-2 * Gdx.graphics.getDeltaTime() * power);
			if (!burningParticlesAdded) {
				playScreen.particleEngine.addFlyUpPillar(playScreen.physicsManager.getDynamicsWorld(), entity.pos, 1,
						4, 1f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
				burningParticlesAdded = true;
			}
		}
	}

}
