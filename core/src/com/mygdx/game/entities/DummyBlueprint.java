package com.mygdx.game.entities;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;
import com.mygdx.game.entities.Entity.Facing;

public class DummyBlueprint {

	private static HashMap<String, TextureRegion> standMap = new HashMap<>();
	private static HashMap<String, Animation<TextureRegion>> walkMap = new HashMap<>();

	/**
	 * Translate the string name of the facing attributes into a string angle we can use.
	 * @param facing the facing enum of the entity.
	 * @return the string containing the angle the entity is facing.
	 */
	private static String getAngle(Facing facing) {
		return facing.toString().substring(1).replace('_', '.');
	}
	
	static TextureRegion getCurrentTexture(String currentState, Facing facing, float stateTime) {
		if (currentState.contains("STAND")) {
			return standMap.get(getAngle(facing));
		} else if (currentState.contains("WALK")) {
			return walkMap.get(getAngle(facing)).getKeyFrame(stateTime);
		} else if (currentState.contains("PRONE")) {
			return standMap.get(getAngle(facing));
		} else if (currentState.contains("MIDAIR")) {
			return standMap.get(getAngle(facing));
		} else {
			return null;
		}
	}
	
	public static void initialise(AON_E game) {
		Array<AtlasRegion> walkFramesAll = game.dummyWalkingAtlas.getRegions();
		TextureRegion[] standFrames = new TextureRegion[Dummy.directions];
		TextureRegion[][] walkFrames = new TextureRegion[Dummy.directions][Dummy.fps];
		int index = 0;
		int counter = 0;
		for (AtlasRegion frame: walkFramesAll) {
			if (counter >= Dummy.directions) {
				counter = 0;
				index ++;
			}
			if (index == 7) {
				standFrames[(int)((Float.parseFloat(frame.name) / 360.0f) * Dummy.directions)] = new TextureRegion(frame);
			}
			walkFrames[(int)((Float.parseFloat(frame.name) / 360.0f) * Dummy.directions)][index] = new TextureRegion(frame);
			counter ++;
		}
		for (int i = 0; i < Dummy.directions; i ++) {
			standMap.put(String.valueOf((i / (float)(Dummy.directions)) * 360), standFrames[i]);
			
			Array<TextureRegion> regionsById = new Array<>();
			for (TextureRegion region: walkFrames[i]) {
				regionsById.add(region);
			}
			Animation<TextureRegion> animation = new Animation<>(Dummy.frameInterval, regionsById, PlayMode.LOOP);
			walkMap.put(String.valueOf((i / (float)(Dummy.directions)) * 360), animation);
		}
	}
	
}
