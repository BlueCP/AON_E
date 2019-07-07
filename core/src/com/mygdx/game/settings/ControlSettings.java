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
		takenKeys.add(respawnKey());
		takenKeys.add(optionsScreenKey());
		takenKeys.add(cancelMovementKey());
		takenKeys.add(questScreenKey());
		takenKeys.add(achivementsScreenKey());
		takenKeys.add(interactKey());
	}

	public static void resetKeyBindings() {
		preferences.putInteger("basicAttackKey", Keys.W);

		for (int i = 0; i < 8; i ++) {
			preferences.putInteger("ability" + (i + 1) + "Key", Keys.valueOf(String.valueOf(i + 1)));
		}

		preferences.putInteger("openInventoryKey", Keys.I);
		preferences.putInteger("respawnKey", Keys.R);
		preferences.putInteger("optionsScreenKey", Keys.M);
		preferences.putInteger("cancelMovementKey", Keys.ESCAPE);
		preferences.putInteger("questScreenKey", Keys.Q);
		preferences.putInteger("achievementsScreenKey", Keys.A);
		preferences.putInteger("interactKey", Keys.E);

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

	/**
	 * Sets the key for respawning.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setRespawnKey(int key) {
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(respawnKey(), key);
			preferences.putInteger("respawnKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int respawnKey() {
		return preferences.getInteger("respawnKey", Keys.R);
	}

	/**
	 * Sets the key for opening the options screen.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setOptionsScreenKey(int key) {
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(optionsScreenKey(), key);
			preferences.putInteger("optionsScreenKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int optionsScreenKey() {
		return preferences.getInteger("optionsScreenKey", Keys.M);
	}

	/**
	 * Sets the key for cancelling movement (stop walking to the targeted location).
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setCancelMovementKey(int key) {
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(cancelMovementKey(), key);
			preferences.putInteger("cancelMovementKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int cancelMovementKey() {
		return preferences.getInteger("cancelMovementKey", Keys.ESCAPE);
	}

	/**
	 * Sets the key for opening the quest screen.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setQuestScreenKey(int key) {
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(questScreenKey(), key);
			preferences.putInteger("questScreenKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int questScreenKey() {
		return preferences.getInteger("questScreenKey", Keys.Q);
	}

	/**
	 * Sets the key for opening the achievements screen.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setAchivementsScreenKey(int key) {
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(achivementsScreenKey(), key);
			preferences.putInteger("achievementsScreenKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int achivementsScreenKey() {
		return preferences.getInteger("achievementsScreenKey", Keys.A);
	}

	/**
	 * Sets the key for interacting with an entity.
	 * @return true if the key was updated, false if not.
	 */
	public static boolean setInteractKey(int key) {
		if (!takenKeys.contains(key, true)) {
			updateTakenKeys(interactKey(), key);
			preferences.putInteger("interactKey", key);
			preferences.flush();
			return true;
		} else {
			return false;
		}
	}

	public static int interactKey() {
		return preferences.getInteger("interactKey", Keys.E);
	}

}
