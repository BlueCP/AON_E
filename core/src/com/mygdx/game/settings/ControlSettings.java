package com.mygdx.game.settings;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;

/**
 * Responsible mostly for custom keybindings. In future may also be used to miscellaneous control options,
 * such as auto-climb when colliding with a climbable wall (?).
 */
public class ControlSettings extends Settings {

//	public static int basicAttackKey = Keys.W;

	/*static int ability1Key = Keys.NUM_1;
	static int ability2Key = Keys.NUM_2;
	static int ability3Key = Keys.NUM_3;
	static int ability4Key = Keys.NUM_4;
	static int ability5Key = Keys.NUM_5;
	static int ability6Key = Keys.NUM_6;
	static int ability7Key = Keys.NUM_7;
	static int ability8Key = Keys.NUM_8;*/

//	public static int openInventoryKey = Keys.I;

	private static Array<Integer> takenKeys = new Array<>();

	public static void init() {
		takenKeys.add(basicAttackKey());

		for (int i = 0; i < 8; i ++) {
			takenKeys.add(abilityKey(i + 1));
		}

		takenKeys.add(openInventoryKey());
	}

	public static void resetKeyBindings() {
		preferences.putInteger("basicAttackKey", Keys.W);

		for (int i = 0; i < 8; i ++) {
			preferences.putInteger("ability" + (i + 1) + "Key", Keys.valueOf(String.valueOf(i + 1)));
		}

		preferences.putInteger("openInventoryKey", Keys.I);
	}

	private static void updateTakenKeys(int oldKey, int newKey) {
		takenKeys.removeValue(oldKey, true);
		takenKeys.add(newKey);
	}

	public static void setBasicAttackKey(int key) {
//		basicAttackKey = key;
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(basicAttackKey(), key);
			preferences.putInteger("basicAttackKey", key);
			preferences.flush();
		}
	}

	public static int basicAttackKey() {
		return preferences.getInteger("basicAttackKey", Keys.W);
	}

	public static void setAbilityKey(int abilityNumber, int key) {
//		abilityKeys[abilityNumber - 1] = key;
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(abilityKey(abilityNumber), key);
			preferences.putInteger("ability" + abilityNumber + "Key", key);
			preferences.flush();
		}
	}

	public static int abilityKey(int abilityNumber) {
		return preferences.getInteger("ability" + abilityNumber + "Key", Keys.valueOf(String.valueOf(abilityNumber)));
	}

	public static void setOpenInventoryKey(int key) {
//		openInventoryKey = key;
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(openInventoryKey(), key);
			preferences.putInteger("openInventoryKey", key);
			preferences.flush();
		}
	}

	public static int openInventoryKey() {
		return preferences.getInteger("openInventoryKey", Keys.I);
	}

}
