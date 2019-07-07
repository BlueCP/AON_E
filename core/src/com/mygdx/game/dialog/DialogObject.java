package com.mygdx.game.dialog;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.screens.PlayScreen;
import javafx.util.Pair;

/**
 * An entire, enclosed dialog between any number of characters.
 */
public class DialogObject {

	private String name;
	private Array<DialogPart> dialogParts;

	protected DialogObject() {
		dialogParts = new Array<>();
	}

	DialogObject(String name) {
		this.name = name;
		dialogParts = new Array<>();
	}

	/**
	 * By default, does nothing.
	 */
	public void onCompletion(PlayScreen playScreen) {

	}

	/**
	 * An altered version of the same method from DialogManager.
	 * Takes an indefinite number of character-speech pairs and puts them all into one dialog object.
	 * @param name the name of the dialog, used to identify it easily.
	 * @param dialogParts the actual content (characters and speech) of the dialog.
	 */
	protected void createDialog(String name, Pair<DialogManager.Character, String>... dialogParts) {
		this.name = name;
		for (Pair<DialogManager.Character, String> dialogPart: dialogParts) {
			addPart(dialogPart.getKey(), dialogPart.getValue());
		}
	}

	void addPart(DialogManager.Character character, String speech) {
		dialogParts.add(new DialogPart(character, speech));
	}

	public DialogPart getPart(int index) {
		return dialogParts.get(index);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Array<DialogPart> getDialogParts() {
		return dialogParts;
	}

}
