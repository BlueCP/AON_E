package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;
import com.mygdx.game.entities.Entity.Facing;
import com.mygdx.game.screens.PlayScreen;

import java.util.HashMap;

public class PlayerBlueprint {
	
	private static HashMap<String, TextureRegion> standMap = new HashMap<>();
	private static HashMap<String, Animation<TextureRegion>> walkMap = new HashMap<>();
	private static HashMap<String, Animation<TextureRegion>> walkMap12 = new HashMap<>();
	private static HashMap<String, Animation<TextureRegion>> walkMap18 = new HashMap<>();
	private static HashMap<String, Animation<TextureRegion>> attackMap = new HashMap<>();
	
	//private static String currentState = "";
	//private static String previousState = "";
	
	//private static TextureRegion currentTexture;

	/**
	 * Translate the string name of the facing attributes into a string angle we can use.
	 * @param facing the facing enum of the entity.
	 * @return the string containing the angle the entity is facing.
	 */
	private static String getAngle(Facing facing) {
		return facing.toString().substring(1).replace('_', '.');
	}
	
	static TextureRegion getCurrentTexture(String currentState, Facing facing, float stateTime) {
		if (currentState.contains("STAND") || currentState.contains("NO_ANIMATION")) {
			return standMap.get(getAngle(facing));
		} else if (currentState.contains("WALK")) {
			if (PlayScreen.foo == 1) {
				return walkMap.get(getAngle(facing)).getKeyFrame(stateTime);
			} else if (PlayScreen.foo == 2) {
				return walkMap12.get(getAngle(facing)).getKeyFrame(stateTime);
			} else {
				return walkMap18.get(getAngle(facing)).getKeyFrame(stateTime);
			}
		} else if (currentState.contains("MIDAIR")) {
			return standMap.get(getAngle(facing));
		} else if (currentState.contains("SHOOT_PROJECTILE")) {
			return attackMap.get(getAngle(facing)).getKeyFrame(stateTime);
		} else {
			return null;
		}
	}

	private static void initWalkAndStandFrames(AON_E game) {
		Array<AtlasRegion> walkFramesAll = game.playerWalkingAtlas.getRegions();

		TextureRegion[] standFrames = new TextureRegion[Player.directions];
		TextureRegion[][] walkFrames = new TextureRegion[Player.directions][24];

		int index = 0;
		int counter = 0;

		for (AtlasRegion frame: walkFramesAll) {
			if (counter >= Player.directions) {
				counter = 0;
				index ++;
			}

			int indexOfSlash = frame.name.indexOf('/');
			if (index == 7) {
				standFrames[(int)((Float.parseFloat(frame.name.substring(indexOfSlash + 1)) / 360.0f) * Player.directions)] = new TextureRegion(frame);
			}
			walkFrames[(int)((Float.parseFloat(frame.name.substring(indexOfSlash + 1)) / 360.0f) * Player.directions)][index] = new TextureRegion(frame);
			counter ++;
		}

		for (int i = 0; i < Player.directions; i ++) {
			standMap.put(String.valueOf((i / (float) (Player.directions)) * 360), standFrames[i]);

			Array<TextureRegion> regionsById = new Array<>();
			Array<TextureRegion> regionsById0 = new Array<>();
			Array<TextureRegion> regionsById1 = new Array<>();
			boolean bar = true;
			int a = 1;
			for (TextureRegion region : walkFrames[i]) {
				regionsById.add(region);
				if (bar) {
					regionsById0.add(region);
					bar = false;
				} else {
					bar = true;
				}
				if (a > 1) {
					regionsById1.add(region);
				}
				if (a == 4) {
					a = 0;
				}
				a++;
			}
			Animation<TextureRegion> animation = new Animation<>(1/24f, regionsById, PlayMode.LOOP);
			walkMap.put(String.valueOf((i / (float) (Player.directions)) * 360), animation);

			Animation<TextureRegion> animation0 = new Animation<>(1/24f * 2, regionsById0, PlayMode.LOOP);
			walkMap12.put(String.valueOf((i / (float) (Player.directions)) * 360), animation0);

			Animation<TextureRegion> animation1 = new Animation<>(1/24f * (4f / 3f), regionsById1, PlayMode.LOOP);
			walkMap18.put(String.valueOf((i / (float) (Player.directions)) * 360), animation1);
		}
	}

	private static void initAttackFrames(AON_E game) {
		Array<AtlasRegion> attackFramesAll = game.playerAttackAtlas.getRegions();

		TextureRegion[][] attackFrames = new TextureRegion[Player.directions][12];
		int index = 0;
		int counter = 0;

		for (AtlasRegion frame: attackFramesAll) {
			if (counter >= Player.directions) {
				counter = 0;
				index ++;
			}

			int indexOfSlash = frame.name.indexOf('/');
			attackFrames[(int)((Float.parseFloat(frame.name.substring(indexOfSlash + 1)) / 360.0f) * Player.directions)][index] = new TextureRegion(frame);
			counter ++;
		}

		for (int i = 0; i < Player.directions; i ++) {
			Array<TextureRegion> regionsById = new Array<>();

			for (TextureRegion region : attackFrames[i]) {
				regionsById.add(region);
			}

			Animation<TextureRegion> animation = new Animation<>(2/24f, regionsById, PlayMode.LOOP);
			attackMap.put(String.valueOf((i / (float) (Player.directions)) * 360), animation);
		}
	}
	
	public static void initialise(AON_E game) {
		initWalkAndStandFrames(game);
		initAttackFrames(game);
	}
	
}
