package com.mygdx.game.statuseffects;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.screens.PlayScreen;

/**
 * The damage over time caused by some of the necromancer's attacks.
 */
public class WitherEffect extends ProcEffectDisc {

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	private WitherEffect() { }

	public WitherEffect(Entity entity) {
		super(entity);

		name = "Wither";
		desc = "Bleeding the very essence of life.";
	}

	public void apply(PlayScreen playScreen) {
		boolean particlesAdded = false;
		for (int power: powers) {
			entity.takeDamageNoEntity(power * 2 * Gdx.graphics.getDeltaTime());
			if (!particlesAdded) {
				playScreen.particleEngine.addFlyUpPillar(playScreen.physicsManager.getDynamicsWorld(), entity.pos, 1,
						4, 1f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
				particlesAdded = true;
			}
		}
	}

}
