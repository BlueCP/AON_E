package com.mygdx.game.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;
import com.mygdx.game.projectiles.Projectile.ProjectileSprite;

public class ProjectileSprites {

	private static Animation<AtlasRegion> fireballAnimation;
	private static TextureRegion lightningBolt;
	
	static TextureRegion getCurrentTexture(Projectile projectile) {
		switch (projectile.sprite) {
			case FIREBOLT:
				return fireballAnimation.getKeyFrame(projectile.getStateTime());
			case LIGHTNING_BOLT:
				return lightningBolt;
			default:
				return null;
		}
	}
	
	static boolean hasTexture(ProjectileSprite sprite) {
		/*switch (sprite) {
			case FIREBOLT:
				return true;
			case RAZE_ZONE:
				return false;
			case IMMOLATE_ZONE:
				return false;
			case NO_SPRITE:
				return false;
			default:
				return false;
		}*/
		return sprite != ProjectileSprite.NO_SPRITE;
	}
	
	public static void init(AON_E game) {
		Array<AtlasRegion> allFrames = game.projectilesAtlas.getRegions();

		Array<AtlasRegion> fireballFrames = new Array<>();

		for (AtlasRegion region: allFrames) {
			if (region.name.equals("fireball")) {
				fireballFrames.add(region);
			} else if (region.name.equals("lightning bolt")) {
				lightningBolt = region;
			}
		}

		fireballAnimation = new Animation<>(0.2f, fireballFrames, PlayMode.LOOP);
//		initialiseFireball(allFrames);
	}
	
}
