package com.mygdx.game.dialog.dialogobjects;

import com.mygdx.game.dialog.DialogManager;
import com.mygdx.game.dialog.DialogObject;
import com.mygdx.game.screens.PlayScreen;
import javafx.util.Pair;

public class GratitudeDialog extends DialogObject {

	public GratitudeDialog() {
		createDialog("Gratitude",
				new Pair<>(DialogManager.Character.DELTIS, "Traveller, I am very grateful for your help."),
				new Pair<>(DialogManager.Character.DELTIS, "Please accept this gift from me as thanks."),
				new Pair<>(DialogManager.Character.TRAVELLER, "Thank you. I hope we meet again, Deltis."),
				new Pair<>(DialogManager.Character.DELTIS, "Likewise. But for now, I will take my leave. Adventures will not have themselves."));
	}

	@Override
	public void onCompletion(PlayScreen playScreen) {
		playScreen.quests.currentQuest.finishQuest(playScreen);
	}

}
