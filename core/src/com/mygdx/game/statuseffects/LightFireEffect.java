package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.screens.PlayScreen;

public class LightFireEffect extends ProcEffectDisc {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private LightFireEffect() { }

	public LightFireEffect(Entity entity) {
		super(entity);

		name = "Light Fire";
		desc = "Burning in righteous fire.";
	}

	public void apply(PlayScreen playScreen) {
		boolean particlesAdded = false;
		for (int power: powers) {
			entity.takeDamageNoEntity(2 * Gdx.graphics.getDeltaTime() * power);
			if (!particlesAdded) {
				playScreen.particleEngine.addFlyUpPillar(playScreen.physicsManager.getDynamicsWorld(), entity.pos, 1,
						4, 1f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
				particlesAdded = true;
			}
		}
	}

}
