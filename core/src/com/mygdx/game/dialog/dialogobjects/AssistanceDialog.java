package com.mygdx.game.dialog.dialogobjects;

import com.mygdx.game.dialog.DialogManager;
import com.mygdx.game.dialog.DialogObject;
import com.mygdx.game.screens.PlayScreen;
import javafx.util.Pair;

public class AssistanceDialog extends DialogObject {

	public AssistanceDialog() {
		createDialog("Assistance",
				new Pair<>(DialogManager.Character.DELTIS, "Why, hello there."),
				new Pair<>(DialogManager.Character.DELTIS, "It's rare to see someone like me here. We're quite rare, you know."),
				new Pair<>(DialogManager.Character.DELTIS, "But, Traveller, I am troubled. You see, there is a rare item that I need to escape this place."),
				new Pair<>(DialogManager.Character.DELTIS, "To obtain it, you must destroy the silver statue over there."),
				new Pair<>(DialogManager.Character.DELTIS, "I would do it myself, but my magic is completely ineffective here... I don't know why. That's a mystery to be solved for certain."),
				new Pair<>(DialogManager.Character.DELTIS, "Will you help me, Traveller?"),
				new Pair<>(DialogManager.Character.TRAVELLER, "Anything for a fellow Continuum Breaker, Deltis Silverspear."));
	}

	@Override
	public void onCompletion(PlayScreen playScreen) {
		playScreen.quests.chooseQuest("Search for the Silverheart");
	}

}
