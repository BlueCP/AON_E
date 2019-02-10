package com.mygdx.game.particles;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;

public class ParticleSprites {

	private static HashMap<String, TextureRegion[]> fireMap;
	
	static TextureRegion getCurrentTexture(Particle particle) {
		if (particle.sprite == Particle.Sprite.FIRE) {
			return fireMap.get(particle.getType())[(int)particle.getStateTime()];
		}
		return null;
	}
	
	public static void initialise(AON_E game) {
		initialiseFire(game);
	}
	
	private static void initialiseFire(AON_E game) {
		fireMap = new HashMap<>();
		
		Array<AtlasRegion> allFrames = game.fireParticlesAtlas.getRegions();
		TextureRegion[][] orderedFrames = new TextureRegion[5][2];
		int index = 0;
		int counter = 0;
		for (AtlasRegion frame: allFrames) {
			if (counter >= 5) {
				counter = 0;
				index ++;
			}
			orderedFrames[Integer.parseInt(frame.name) - 1][index] = new TextureRegion(frame);
			counter ++;
		}
		for (int i = 0; i < 5; i ++) {
			fireMap.put(String.valueOf(i + 1), orderedFrames[i]);
		}
	}
	
}
