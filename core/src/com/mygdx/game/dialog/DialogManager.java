package com.mygdx.game.dialog;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.dialog.dialogobjects.AssistanceDialog;
import com.mygdx.game.dialog.dialogobjects.GratitudeDialog;
import com.mygdx.game.stages.HudStage;
import javafx.util.Pair;

/**
 * The class that manages all dialogs.
 * There are two types of dialogs: generic and specific.
 * Generic dialogs have no functionality except for displaying dialog. They can be created using this class's createDialog method.
 * Specific dialogs have functionality, such as executing code at the end of a dialog. They have their own classes.
 */
public class DialogManager {

	private Array<DialogObject> allDialogs;

	/**
	 * The possible characters that can partake in dialog.
	 */
	public enum Character {

		DELTIS("Deltis"),
		TRAVELLER("Traveller");

		private String name;

		Character(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	/**
	 * Initialise all dialogs.
	 */
	public DialogManager() {

		allDialogs = new Array<>();

		allDialogs.add(new AssistanceDialog());
		allDialogs.add(new GratitudeDialog());

		createDialog("Journey",
				new Pair<>(Character.DELTIS, "Oh, hello again. I hope your journey is going well."));

	}

	/**
	 * Takes an indefinite number of character-speech pairs and puts them all into one dialog object.
	 * @param name the name of the dialog, used to identify it easily.
	 * @param dialogParts the actual content (characters and speech) of the dialog.
	 */
	private void createDialog(String name, Pair<Character, String>... dialogParts) {
		DialogObject dialogObject = new DialogObject(name);
		for (Pair<Character, String> dialogPart: dialogParts) {
			dialogObject.addPart(dialogPart.getKey(), dialogPart.getValue());
		}
		allDialogs.add(dialogObject);
	}

	/**
	 * Sends the chosen dialog to the hud to be displayed to the player.
	 */
	public void startDialog(String name, HudStage hudStage) {
		for (DialogObject dialogObject: allDialogs) {
			if (dialogObject.getName().equals(name)) {
				hudStage.startDialog(dialogObject);
				return;
			}
		}
	}

}
