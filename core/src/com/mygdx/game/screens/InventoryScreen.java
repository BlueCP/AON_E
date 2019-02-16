package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.AON_E;
import com.mygdx.game.items.*;

public class InventoryScreen extends MyScreen {

//	private AON_E game;
	private PlayScreen playScreen;
	
	private Stage stage;
	private Table table;
	
	private ButtonGroup<TextButton> itemTypeGroup;
	private TextButton weaponButton;
	private TextButton armourButton;
	private TextButton equipmentButton;
	private TextButton otherItemButton;
	
	private ScrollPane weaponsPane;
	private ScrollPane armourPane;
	private ScrollPane equipmentPane;
	private ScrollPane otherItemsPane;
	
	InventoryScreen(AON_E game, PlayScreen playScreen) {
		super(game);

//		this.game = game;
		this.playScreen = playScreen;
		
		stage = new Stage(game.viewport);
		
		itemTypeGroup = new ButtonGroup<>();
		
		weaponButton = new TextButton("Weapons", AON_E.SKIN, "toggle");
		weaponButton.setChecked(true);
		weaponButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponsPane.setVisible(true);
				armourPane.setVisible(false);
				equipmentPane.setVisible(false);
				otherItemsPane.setVisible(false);
			}
		});
		itemTypeGroup.add(weaponButton);
		
		armourButton = new TextButton("Armour", AON_E.SKIN, "toggle");
		armourButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponsPane.setVisible(false);
				armourPane.setVisible(true);
				equipmentPane.setVisible(false);
				otherItemsPane.setVisible(false);
			}
		});
		itemTypeGroup.add(armourButton);
		
		equipmentButton = new TextButton("Equipment", AON_E.SKIN, "toggle");
		equipmentButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponsPane.setVisible(false);
				armourPane.setVisible(false);
				equipmentPane.setVisible(true);
				otherItemsPane.setVisible(false);
			}
		});
		itemTypeGroup.add(equipmentButton);
		
		otherItemButton = new TextButton("Other items", AON_E.SKIN, "toggle");
		otherItemButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponsPane.setVisible(false);
				armourPane.setVisible(false);
				equipmentPane.setVisible(false);
				otherItemsPane.setVisible(true);
			}
		});
		itemTypeGroup.add(otherItemButton);
		
		Table weaponsTable = new Table();
		weaponsTable.setName("weaponsTable");
		weaponsTable.align(Align.center | Align.top);
		for (Item item: playScreen.player.inventory().getWeapons()) {
			TextButton infoButton = new TextButton(item.getName(), AON_E.SKIN);
			infoButton.addListener(new ClickListener() {

				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					Table statsTable = new Table();
					statsTable.setName("statsTable");
					statsTable.align(Align.right | Align.center);
					statsTable.setWidth(stage.getWidth());
					statsTable.setHeight(stage.getHeight());
					Weapon weapon = (Weapon)item;
					
					// Below we are reusing label by reassigning it new objects which makes the code easier to write as well as more efficient
					statsTable.add(new Label("Name:", AON_E.SKIN)).row();
					Label label = new Label(weapon.getName(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Description:", AON_E.SKIN)).row();
					label = new Label(weapon.getDesc(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Rarity:", AON_E.SKIN)).row();
					statsTable.add(new Label(weapon.getRarity().type(), AON_E.SKIN)).row();
					
					// Below are labels specifically to do with weapons
					statsTable.add(new Label("Type", AON_E.SKIN)).row();
					statsTable.add(new Label(weapon.getType(), AON_E.SKIN)).row();
					
					statsTable.add(new Label("Physical damage:", AON_E.SKIN)).row();
					statsTable.add(new Label(String.valueOf(weapon.getPhysDamage()), AON_E.SKIN)).row();
					
					statsTable.add(new Label("Magical damage:", AON_E.SKIN)).row();
					statsTable.add(new Label(String.valueOf(weapon.getMagDamage()), AON_E.SKIN)).row();
					
					statsTable.add(new Label("Range:", AON_E.SKIN)).row();
					statsTable.add(new Label(String.valueOf(weapon.getRange()), AON_E.SKIN)).row();
					
					statsTable.add(new Label("Range type:", AON_E.SKIN)).row();
					statsTable.add(new Label(String.valueOf(weapon.getRangeType()), AON_E.SKIN)).row();
					
					statsTable.add(new Label("Handedness:", AON_E.SKIN)).row();
					statsTable.add(new Label(String.valueOf(weapon.getHands()), AON_E.SKIN));
					
					stage.addActor(statsTable);
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					for (Actor actor: stage.getActors()) {
						if ("statsTable".equals(actor.getName())) {
							actor.remove();
							return;
						}
					}
				}
				
			});
			
			TextButton equipButton = new TextButton("[equip]", AON_E.SKIN, "toggle");
			equipButton.setName(String.valueOf(item.getId()));
			equipButton.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					Weapon weapon = (Weapon)item;
					Equipped equipped = playScreen.player.equipped();
					if (weapon.isEquipped() && weapon.getId() == equipped.getMain().getId()) {
						equipped.resetMain();
					} else if (weapon.isEquipped() && weapon.getId() == equipped.getOff().getId()) {
						equipped.resetOff();
					} else if (equipped.getMain().getId() == -1) { // If there is nothing session.player.equipped in the main hand
						equipped.setMain(weapon);
					} else if (equipped.getOff().getId() == -1 && weapon.getHands() == 1 && equipped.getMain().getHands() == 1) { // If there is nothing session.player.equipped in the off hand and both weapons are 1-handed
						equipped.setOff(weapon);
					} else {
						// If 2 weapons are equipped
					}
				}
				
			});
			
			weaponsTable.add(infoButton).padBottom(10);
			weaponsTable.add(equipButton).padBottom(10).row();
			
		}
		weaponsPane = new ScrollPane(weaponsTable, AON_E.SKIN);
		weaponsPane.setVisible(true);
		
		Table armourTable = new Table();
		armourTable.setName("armourTable");
		armourTable.align(Align.center | Align.top);
		for (Item item: playScreen.player.inventory().getArmour()) {
			TextButton infoButton = new TextButton(item.getName(), AON_E.SKIN);
			infoButton.addListener(new ClickListener() {
				
				@Override
				public void clicked(InputEvent event, float x, float y) {
					for (Actor actor: stage.getActors()) {
						if ("statsTable".equals(actor.getName())) {
							actor.remove();
							return;
						}
					}
					Table statsTable = new Table();
					statsTable.setName("statsTable");
					statsTable.align(Align.center | Align.right);
					Armour armour = (Armour)item;
					
					statsTable.add(new Label("Name:", AON_E.SKIN)).row();
					Label label = new Label(armour.getName(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Description:", AON_E.SKIN)).row();
					label = new Label(armour.getDesc(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Rarity:", AON_E.SKIN)).row();
					statsTable.add(new Label(armour.getRarity().type(), AON_E.SKIN)).row();
					
					// Below are labels specifically to do with armour
					statsTable.add(new Label("Type:", AON_E.SKIN)).row();
					statsTable.add(new Label(armour.getType(), AON_E.SKIN)).row();
					
					statsTable.add(new Label("Type:", AON_E.SKIN)).row();
					statsTable.add(new Label(String.valueOf(armour.getDefense()), AON_E.SKIN));
					
					stage.addActor(statsTable);
				}
				
			});
			
			TextButton equipButton = new TextButton("[equip]", AON_E.SKIN, "toggle");
			equipButton.setName(String.valueOf(item.getId()));
			equipButton.addListener(new ClickListener() {
				
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Armour armour = (Armour)item;
					Equipped equipped = playScreen.player.equipped();
					if (armour.isEquipped()) {
						equipped.resetArmour();
					} else {
						equipped.setArmour(armour);
					}
				}
				
			});
			
			armourTable.add(infoButton).padBottom(10);
			armourTable.add(equipButton).padBottom(10).row();
		}
		armourPane = new ScrollPane(armourTable, AON_E.SKIN);
		armourPane.setVisible(false);
		
		Table equipmentTable = new Table();
		equipmentTable.setName("equipmentTable");
		equipmentTable.align(Align.center | Align.top);
		for (Item item: playScreen.player.inventory().getEquipment()) {
			TextButton infoButton = new TextButton(item.getName(), AON_E.SKIN);
			infoButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					for (Actor actor: stage.getActors()) {
						if ("statsTable".equals(actor.getName())) {
							actor.remove();
							return;
						}
					}
					Table statsTable = new Table();
					statsTable.setName("statsTable");
					statsTable.align(Align.center | Align.right);
					Equipment equipment = (Equipment)item;
					
					statsTable.add(new Label("Name:", AON_E.SKIN)).row();
					Label label = new Label(equipment.getName(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Description:", AON_E.SKIN)).row();
					label = new Label(equipment.getDesc(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Rarity:", AON_E.SKIN)).row();
					statsTable.add(new Label(equipment.getRarity().type(), AON_E.SKIN)).row();
					
					stage.addActor(statsTable);
				}
			});
			
			TextButton equipButton = new TextButton("[equip]", AON_E.SKIN, "toggle");
			equipButton.setName(String.valueOf(item.getId()));
			equipButton.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					Equipment equipment = (Equipment)item;
					Equipped equipped = playScreen.player.equipped();
					
					if (equipped.getEquipment1().getId() == -1) {
						equipped.setEquipment1(equipment);
						equipButton.setChecked(true);
					} else if (equipped.getEquipment2().getId() == -1) {
						equipped.setEquipment2(equipment);
						equipButton.setChecked(true);
					} else { // If all slots are filled, default to the third slot (people usually put their most important equipment in their first slot)
						equipped.setEquipment3(equipment);
						equipButton.setChecked(true);
					}
				}
				
			});
			
			equipmentTable.add(infoButton).padBottom(10);
			equipmentTable.add(equipButton).padBottom(10).row();
			
		}
		equipmentPane = new ScrollPane(equipmentTable, AON_E.SKIN);
		equipmentPane.setVisible(false);
		
		Table otherItemsTable = new Table();
		otherItemsTable.setName("otherItemsTable");
		otherItemsTable.align(Align.center | Align.top);
		for (Item item: playScreen.player.inventory().getOtherItems()) {
			TextButton button = new TextButton(item.getName(), AON_E.SKIN);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					for (Actor actor: stage.getActors()) {
						if ("statsTable".equals(actor.getName())) {
							actor.remove();
							return;
						}
					}
					Table statsTable = new Table();
					statsTable.setName("statsTable");
					statsTable.align(Align.center | Align.right);
					OtherItem otherItem = (OtherItem)item;
					
					statsTable.add(new Label("Name:", AON_E.SKIN)).row();
					Label label = new Label(otherItem.getName(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Description:", AON_E.SKIN)).row();
					label = new Label(otherItem.getDesc(), AON_E.SKIN); label.setWrap(true);
					statsTable.add(label).width(500).row();
					
					statsTable.add(new Label("Rarity:", AON_E.SKIN)).row();
					statsTable.add(new Label(otherItem.getRarity().type(), AON_E.SKIN)).row();
					
					stage.addActor(statsTable);
				}
			});
			otherItemsTable.add(button);
			otherItemsTable.row();
		}
		otherItemsPane = new ScrollPane(otherItemsTable, AON_E.SKIN);
		otherItemsPane.setVisible(false);
		
		Stack paneGroup = new Stack();
		paneGroup.addActor(weaponsPane);
		paneGroup.addActor(armourPane);
		paneGroup.addActor(equipmentPane);
		paneGroup.addActor(otherItemsPane);
		
		table = new Table();
		table.align(Align.center | Align.top);
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		table.padTop(50);
		table.add(weaponButton, armourButton, equipmentButton, otherItemButton);
		table.row().padTop(30);
		table.add(paneGroup).fillX().colspan(4);
		stage.addActor(table);
		//stage.setDebugAll(true);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.ESCAPE:
				game.setScreen(playScreen);
				break;
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		//  Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		//  Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		game.pointer.setRegion(game.pointerDown);
//		game.click.play();
		game.soundManager.click.play();
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		game.pointer.setRegion(game.pointerUp);
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		game.pointer.setRegion(game.pointerUp);
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		//  Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		//  Auto-generated method stub
		return false;
	}
	
	@Override
	public void show() {
		//  Auto-generated method stub
		
	}
	
	void update() {
		universalUpdate();
		
		Table weaponsTable = table.findActor("weaponsTable");
		for (int i = 1; i < weaponsTable.getRows() * 2; i += 2) {
			// This for loop skips over the even-numbered actors because those are the infoButtons; we want the equipButtons
			if (Integer.parseInt(weaponsTable.getChildren().get(i).getName()) == (playScreen.player.equipped().getMain().getId()) ||
				Integer.parseInt(weaponsTable.getChildren().get(i).getName()) == (playScreen.player.equipped().getOff().getId())) {
				// If the code of the weapon (stored in the name of the equipButton) is equal to one of the equipped weapons
				((TextButton)weaponsTable.getChildren().get(i)).setChecked(true); // Then check the button
				// The reason I have used such a system for checking the buttons is because equipped items is a complicated matter and is difficult to implement with simple button grouping
			} else {
				((TextButton)weaponsTable.getChildren().get(i)).setChecked(false);
			}
		}
		
		Table armourTable = table.findActor("armourTable");
		for (int i = 1; i < armourTable.getRows() * 2; i += 2) {
			if (Integer.parseInt(armourTable.getChildren().get(i).getName()) == playScreen.player.equipped().getMain().getId() ||
				Integer.parseInt(armourTable.getChildren().get(i).getName()) == playScreen.player.equipped().getOff().getId()) {
				((TextButton)armourTable.getChildren().get(i)).setChecked(true);
			} else {
				((TextButton)armourTable.getChildren().get(i)).setChecked(false);
			}
		}
		
		Table equipmentTable = table.findActor("equipmentTable");
		for (int i = 1; i < equipmentTable.getRows() * 2; i += 2) {
			if (Integer.parseInt(equipmentTable.getChildren().get(i).getName()) == playScreen.player.equipped().getMain().getId() ||
				Integer.parseInt(equipmentTable.getChildren().get(i).getName()) == playScreen.player.equipped().getOff().getId()) {
				((TextButton)equipmentTable.getChildren().get(i)).setChecked(true);
			} else {
				((TextButton)equipmentTable.getChildren().get(i)).setChecked(false);
			}
		}
	}
	
	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
		
		game.batch.begin();
		game.pointer.draw(game.batch);
//		RenderingUtils.renderBlackBars(game.batch);
		game.batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		updateViewport(width, height);
	}
	
	@Override
	public void pause() {
		//  Auto-generated method stub
		
	}
	
	@Override
	public void resume() {
		//  Auto-generated method stub
		
	}
	
	@Override
	public void hide() {
		//  Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
	
}
