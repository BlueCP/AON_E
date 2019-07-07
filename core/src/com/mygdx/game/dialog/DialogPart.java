package com.mygdx.game.dialog;

/**
 * A single part of a dialog, spoken by a single character.
 */
public class DialogPart {

	private DialogManager.Character character;
	private String speech;

	DialogPart(DialogManager.Character character, String speech) {
		this.character = character;
		this.speech = speech;
	}

	public DialogManager.Character getCharacter() {
		return character;
	}


	public String getSpeech() {
		return speech;
	}

}
