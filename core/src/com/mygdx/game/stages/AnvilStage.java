package com.mygdx.game.stages;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.items.CRecipes;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.items.CRecipes.Station;
import com.mygdx.game.screens.PlayScreen;

public class AnvilStage extends OwnStage {
	
	public AnvilStage(AON_E game, Inventory inventory) {
		name = "anvil";
		stage = new Stage(game.viewport);
		Table recipeTable = new Table();
		
		ArrayList<CRecipes> anvilRecipes = new ArrayList<>();
		for (CRecipes recipe: CRecipes.values()) {
			if (recipe.station() == Station.ANVIL) {
				anvilRecipes.add(recipe);
			}
		}
		for (CRecipes recipe: anvilRecipes) {
			TextButton button = new TextButton(recipe.id(), AON_E.SKIN);
			button.addListener(new ClickListener() {

				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					Table infoTable = new Table();
					infoTable.setName("infoTable");
					infoTable.align(Align.center | Align.right);
					infoTable.setWidth(stage.getWidth());
					infoTable.setHeight(stage.getHeight());
					for (String component: recipe.stringComponents()) {
						infoTable.add(new Label(component, AON_E.SKIN)).padRight(50).row();
					}
					stage.addActor(infoTable);
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					for (Actor actor: stage.getActors()) {
						if ("infoTable".equals(actor.getName())) {
							actor.remove();
							break;
						}
					}
				}

				@Override
				public void clicked(InputEvent event, float x, float y) {
					recipe.craft(inventory);
				}
				
			});
			recipeTable.add(button).padBottom(10).row();
		}
		ScrollPane anvilPane = new ScrollPane(recipeTable, AON_E.SKIN);
		
		Table table = new Table();
		table.align(Align.center | Align.top);
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.add(anvilPane).padTop(50);
		stage.addActor(table);
	}

	@Override
	public void update(PlayScreen playScreen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(PlayScreen playScreen) {
		update(playScreen);
		stage.act();
		stage.draw();
	}
	
}
