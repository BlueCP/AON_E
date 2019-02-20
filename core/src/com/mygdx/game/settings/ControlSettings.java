package com.mygdx.game.settings;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;

/**
 * Responsible mostly for custom keybindings. In future may also be used to miscellaneous control options,
 * such as auto-climb when colliding with a climbable wall (?).
 */
public class ControlSettings extends Settings {

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

		preferences.flush();

		init(); // To update the taken keys.
	}

	private static void updateTakenKeys(int oldKey, int newKey) {
		takenKeys.removeValue(oldKey, true);
		takenKeys.add(newKey);
	}

	/**
	 * Sets the basic attack key.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setBasicAttackKey(int key) {
//		basicAttackKey = key;
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(basicAttackKey(), key);
			preferences.putInteger("basicAttackKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int basicAttackKey() {
		return preferences.getInteger("basicAttackKey", Keys.W);
	}

	/**
	 * Sets the chosen ability's key.
	 * @param abilityNumber the index of the ability.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setAbilityKey(int abilityNumber, int key) {
//		abilityKeys[abilityNumber - 1] = key;
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(abilityKey(abilityNumber), key);
			preferences.putInteger("ability" + abilityNumber + "Key", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int abilityKey(int abilityNumber) {
		return preferences.getInteger("ability" + abilityNumber + "Key", Keys.valueOf(String.valueOf(abilityNumber)));
	}

	/**
	 * Sets the key for opening inventory.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setOpenInventoryKey(int key) {
//		openInventoryKey = key;
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(openInventoryKey(), key);
			preferences.putInteger("openInventoryKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int openInventoryKey() {
		return preferences.getInteger("openInventoryKey", Keys.I);
	}

}
