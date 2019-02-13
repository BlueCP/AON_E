package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AON_E;
import com.mygdx.game.achievements.AchievementCollection;
import com.mygdx.game.entities.*;
import com.mygdx.game.entities.Entity.AnimationType;
import com.mygdx.game.entities.Entity.Facing;
import com.mygdx.game.items.AllItems;
import com.mygdx.game.particles.ParticleEngine;
import com.mygdx.game.particles.ParticleSprites;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.playerattributes.SkillCollection;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.projectiles.ProjectileSprites;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.ScreenShakeAction;
import com.mygdx.game.settings.ControlSettings;
import com.mygdx.game.stages.HudStage;
import com.mygdx.game.stages.OwnStage;
import com.mygdx.game.utils.Message;
import com.mygdx.game.utils.Util;
import com.mygdx.game.world.Time;
import javafx.util.Pair;

import java.security.Key;

public class PlayScreen extends MyScreen implements InputProcessor {

	public static int foo = 1;
	
//	public AON_E game;
	
	Music travelMusic;
	
	public Screen subscreen = null;
	public String playerName;
	public Player player;
	//public Equipped equipped; // Pointer to player's equipped items
	//public RealItems inventory; // Pointer to player's inventory
	public Time time; // Pointer to the time of the current world
	private AllItems allItems = new AllItems();
	AchievementCollection achievements = new AchievementCollection(); // New achievements object
//	private SkillCollection skillCollection = new SkillCollection(true); // New skills object
//	MoveCollection moveCollection = new MoveCollection(); // New actions object
//	SpellCollection spellCollection = new SpellCollection(); // New spells object

	private SkillCollection skillCollection;

	public Entities entities;
	private long tick = -1; // -1 is the tick even before anything has happened in the world. When everything is loaded in, the tick is updated to 0.
	private long localTick = -1; // The local tick is the tick starting from when the player first entered the current world (i.e. resets when the player worldwalks)
	//public boolean continueConfirm = false;
	//public Entity targetEntity = null;
	//public Weapon selectedWeapon = new Weapon(true);
//	public String baseDir;
//	public FieldOfView fov;
	private Array<Message> messages = new Array<>();
	private Array<Message> subMessages = new Array<>();
	public Array<OwnStage> ownStages = new Array<>();
//	public boolean isoMovement = false;
	Thread loadNewChunksThread;
	
	public IsometricRenderer isoRenderer;
	public PhysicsManager physicsManager;
	public ParticleEngine particleEngine;
	public ProjectileManager projectileManager;
	
	private OwnStage hudStage; // Stage is visible to classes in the package so that screens can access stage when setting the input processor back to stage and this (e.g. inventory screen)
	
	/*
	private Texture fullPlayerLifeBar;
	private TextureRegion lifeBar;
	
	private Texture fullPlayerSpiritBar;
	private TextureRegion spiritBar;
	*/
	
	//private Texture fullManaBar;
	//private TextureRegion manaBar;
	
	//btGhostObject ghostObj;
	
	private Array<Integer> keyboardEvents;
	private Array<Integer> pressedKeys;
	
	private Array<MouseEvent> mouseEvents;
	private Array<Integer> pressedMouseButtons;
	
	// Overflow is used when input is received during processing of input (so the events can be added later, when processing is finished)
	private Array<Integer> overflowKeyboardEvents;
	private Array<MouseEvent> overflowMouseEvents;
	
	private boolean processingKeyboardInput = false;
	private boolean processingMouseInput = false;
	
	private InputMultiplexer inputMultiplexer;
	
	public static class MouseEvent {
		public int screenX;
		public int screenY;
		public int pointer;
		public int button;
		
		MouseEvent(int screenX, int screenY, int pointer, int button) {
			this.screenX = screenX;
			this.screenY = screenY;
			this.pointer = pointer;
			this.button = button;
		}
	}
	
	public PlayScreen(AON_E game, String name) {
		super(game);

//		this.game = game;
		
		/*
		fullPlayerLifeBar = game.manager.get("sprites/fullPlayerLifeBar.png");
		lifeBar = new TextureRegion(fullPlayerLifeBar);
		fullPlayerSpiritBar = game.manager.get("sprites/fullSpiritBar.png");
		spiritBar = new TextureRegion(fullPlayerSpiritBar);
		*/
		//fullManaBar = game.manager.get("sprites/fullManaBar.png");
		//manaBar = new TextureRegion(fullManaBar);
		
		travelMusic = game.manager.get("music/travelmusic.mp3");
		travelMusic.setLooping(true);
		//travelMusic.play();
		
		initialiseBlueprints(game);
		ParticleSprites.initialise(game);
		ProjectileSprites.initialise(game);
		
		playerName = name;
		loadGame();

		//this.pStats = this.player.eStats();
		//this.inventory = this.player.inventory();
		//this.equipped = this.player.equipped();
		
		//particleEngine = new ParticleEngine();
		
		//projectileManager = new ProjectileManager();

//		System.out.println(123);
//		System.out.println(player.actions.size);
//		player.actions.ensureCapacity(1);
//		player.actions.addFirst(new Array<>());

		skillCollection = new SkillCollection();
		
		physicsManager = new PhysicsManager(game.manager, player, entities, particleEngine, projectileManager);
		
		//entities.addEntity(player, physicsManager);
		
		// TEMPORARY ENTITIES
		//entities.addEntity(new Dummy(entities, new Vector3(0, 5, 0)), physicsManager);
		//entities.addEntity(new Dummy(entities, new Vector3(0, 50, 0)), physicsManager);
		
		hudStage = new HudStage(game, this);
		ownStages.add(hudStage);
		
		keyboardEvents = new Array<>();
		pressedKeys = new Array<>();
		
		mouseEvents = new Array<>();
		pressedMouseButtons = new Array<>();
		
		overflowKeyboardEvents = new Array<>();
		overflowMouseEvents = new Array<>();
		
		executeLogic(0);
		
		isoRenderer = new IsometricRenderer(game, time);
		
		//travelMusic.play(); // So that it only starts playing when the player can see the world, i.e. when it has been loaded
		
		inputMultiplexer = new InputMultiplexer(hudStage.stage, this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (processingKeyboardInput) {
			overflowKeyboardEvents.add(keycode);
		} else {
			keyboardEvents.add(keycode);
			pressedKeys.add(keycode);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		pressedKeys.removeValue(keycode, true);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (processingMouseInput) {
			overflowMouseEvents.add(new MouseEvent(screenX, screenY, pointer, button));
		} else {
			Vector2 virtual = screenToVirtual(screenX, screenY);
			MouseEvent event = new MouseEvent((int)virtual.x, (int)virtual.y, pointer, button);
			mouseEvents.add(event);
			pressedMouseButtons.add(button);
		}
		/*
		game.pointer.setRegion(game.pointerDown);
		game.click.play();
		if (button == Buttons.RIGHT) {
			rayPick(screenX, Gdx.graphics.getHeight() - screenY);
		}
		//isoRenderer.interactWithClickedTile(this, screenX, screenY);
		*/
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//game.pointer.setRegion(game.pointerUp);
		//MouseEvent event = new MouseEvent(screenX, screenY, pointer, button);
		pressedMouseButtons.removeValue(button, true);
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
		Gdx.input.setInputProcessor(new InputMultiplexer(hudStage.stage, this));
		//travelMusic.play();
	}
	
	void update() {
		universalUpdate();

		testforMovement();

		executeLogic(Gdx.graphics.getDeltaTime());

		/*
		if (Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
			player.rigidBody.setLinearVelocity(new Vector3(0, -30, 0));
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			//player.rigidBody.applyForce(new Vector3(2, 0, 0), new Vector3(0, 0, 0));
			player.rigidBody.applyImpulse(new Vector3(0.05f, 0, 0), new Vector3(0, 0, 0));
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			//player.rigidBody.applyForce(new Vector3(2, 0, 0), new Vector3(0, 0, 0));
			player.rigidBody.applyImpulse(new Vector3(-0.05f, 0, 0), new Vector3(0, 0, 0));
		}
		*/
		//System.out.println(player.rigidBody.getLinearVelocity());
	}

	@Override
	public void render(float delta) {
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		isoRenderer.render(this);

		/*
		game.batch.begin();
		printMessages();
		printSubMessages();
		displayCriticalStats();
		game.batch.end();
		*/
		
		for (OwnStage ownStage: ownStages) {
			ownStage.render(this);
		}

//		System.out.println(player.getSkills().get(0).getCooldown());
		
//		game.batch.begin();
//		RenderingUtils.renderBlackBars(game.batch);
//		game.batch.end();

//		((TargetedSkill) player.getSkills().get(0)).faceTarget(entities.allEntities.get(0));

		//System.out.println(player.isCollidingWithWalkable());
		
		/*
		game.batch.begin();
		game.pointer.draw(game.batch);
		game.batch.end();
		*/
		
		//achievements.testforNewAchievements(terminal, this); // Tells the player if they have unlocked any new achievements
		//moves.testforNewMoves(terminal, this); // Informs player of new moves
		//displayCriticalStats(terminal); // Print critical stats after enemies have attacked
		//displayGold(terminal);
		//displayLvl(terminal);
		//displayTime(terminal);
		//displayEffects(terminal);
		//printMessages(terminal);
		//printSubMessages(terminal);
		/*
		if (this.testforDeath(pSts, status)) { // If the player has died (because testforDeath returns true if player is dead)
			// Put something here at some point
		}
		*/
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
		keyboardEvents.clear();
		pressedKeys.clear();
		mouseEvents.clear();
		pressedMouseButtons.clear();
		overflowKeyboardEvents.clear();
		overflowMouseEvents.clear();
		//travelMusic.pause();
	}

	@Override
	public void dispose() {
		isoRenderer.dispose();
		physicsManager.dispose();
		particleEngine.dispose();
		projectileManager.dispose();
		
		hudStage.stage.dispose();
	}
	
	private void updateTick() {
		try {
			tick ++; // Add 1 to tick
		} catch (Exception e) {
			tick = 0; // If the value becomes too large to be contained within a 64-bit long, reset it to 0 (in case someone actually tries this)
		}
		try {
			localTick ++;
		} catch (Exception e) {
			localTick = 0;
		}
	}
	
	private void rotatePlayer() {
		if (player.canRotate()) {
			Vector2 renderPos = player.renderPos.cpy().add(player.getTexture().getRegionWidth()/2f * isoRenderer.getEffectiveZoom(),
														   player.getTexture().getRegionHeight()/2f * isoRenderer.getEffectiveZoom());
			int screenX1 = (int) (virtualCoords.x - renderPos.x); // X distance relative to player
			int halfScreenX1 = screenX1 / 2; // Reasons for this can be found in one of the notebooks.
			int screenY1 = (int) (virtualCoords.y - renderPos.y); // Y distance relative to player
			/*
			System.out.println(screenX);
			System.out.println(screenY);
			System.out.println(screenX1);
			System.out.println(screenY1);
			*/
			int b = (int) (renderPos.y);
			double c = Math.sqrt(halfScreenX1*halfScreenX1 + screenY1*screenY1); // Total distance relative to the player
			double a = Math.sqrt(halfScreenX1*halfScreenX1 + (virtualCoords.y)*(virtualCoords.y));
			double cosx = (float) ((b*b + c*c - a*a)/(2 * b * c));
			if (cosx > 1) { // In case a rounding error made cosx very slightly greater than 1
				cosx = 1;
			} else if (cosx < -1) {
				cosx = -1;
			}
			double angle = Math.toDegrees(Math.acos(cosx)); // Using the cosine theorem to find the angle of the click
			if (virtualCoords.x < player.renderPos.x) {
				angle = 360 - angle;
			}
			angle = Math.round(angle/22.5) * 22.5; // Round to nearest 22.5 degrees (which is 360/number of directions)
			String facing = "N" + String.valueOf(angle).replace('.', '_'); // Convert to the format for 'facing'
			for (Facing facing0: Facing.values()) {
				if (facing.equals(facing0.toString())) {
					player.setFacing(facing0);
				}
			}
		} // Don't bother rotating the player if they can't rotate

	}
	
	private void testForWalk() {
		if (player.canWalk()) {
			player.setAnimationType(AnimationType.WALK);
			float angle = Float.parseFloat(player.getFacing().toString().substring(1).replace('_', '.'));
			int angle1 = Math.floorMod((int)angle, 90);
			double opposite = player.getRealWalkSpeed()*Math.sin(Math.toRadians(angle1));
			double adjacent = player.getRealWalkSpeed()*Math.cos(Math.toRadians(angle1));
			double xComponent = (angle < 90 || (angle >= 180 && angle < 270)) ? opposite : adjacent;
			xComponent = (angle < 180) ? xComponent : -xComponent;
			double yComponent = (angle < 90 || (angle >= 180 && angle < 270)) ? adjacent : opposite;
			yComponent = (angle > 90 && angle < 270) ? yComponent : -yComponent;
			Vector2 point = isoRenderer.cartToInvertedIso((float)xComponent, (float)yComponent);
			player.addMovementVector(point.x, 0, point.y);
			//player.moveByMovementVector();
			//player.setWalkImpulse(point.x, 0, point.y);
			//System.out.println("--------");
			//System.out.println(player.getWalkImpulse());
			//System.out.println(player.getPrevWalkImpulse());
			//player.applyWalkImpulse();
		}  // Don't bother with walking logic if the player can't actually walk

	}
	
	private void testForJump() {
		// TEMPORARY CODE
		if (player.canJump()) {
			player.rigidBody.applyCentralImpulse(new Vector3(0, 5, 0));
			player.setAnimationType(AnimationType.MIDAIR);
			//player.moveByMovementVector();
		}
	}
	
	/*
	private void mouseClickMovement() {
		//
		if (Gdx.input.isButtonPressed(1)) {
			player.rigidBody.setLinearVelocity(new Vector3(0.3f, 0.3f, -0.3f));
		}
		//
		testForWalk();
		testForJump();
	}
	*/
	
	private void testforMovement() {
		//player.setMovementState(MovementState.WALKING);
		/*
		if (isoMovement == true) {
			if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.A)) {
				player.getStatus().changeStat("yPos", -num);
				player.getStatus().changeStat("xPos", -num);
				player.setFacing(Facing.NORTHWEST);
			} else if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.D)) {
				player.getStatus().changeStat("yPos", -num);
				player.getStatus().changeStat("xPos", num);
				player.setFacing(Facing.NORTHEAST);
			} else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.A)) {
				player.getStatus().changeStat("yPos", num);
				player.getStatus().changeStat("xPos", -num);
				player.setFacing(Facing.SOUTHWEST);
			} else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.D)) {
				player.getStatus().changeStat("yPos", num);
				player.getStatus().changeStat("xPos", num);
				player.setFacing(Facing.SOUTHEAST);
			} else if (Gdx.input.isKeyPressed(Keys.W)) {
				player.getStatus().changeStat("yPos", -0.1f);
				player.setFacing(Facing.NORTH);
			} else if (Gdx.input.isKeyPressed(Keys.A)) {
				player.getStatus().changeStat("xPos", -0.1f);
				player.setFacing(Facing.WEST);
			} else if (Gdx.input.isKeyPressed(Keys.S)) {
				player.getStatus().changeStat("yPos", 0.1f);
				player.setFacing(Facing.SOUTH);
			} else if (Gdx.input.isKeyPressed(Keys.D)) {
				player.getStatus().changeStat("xPos", 0.1f);
				player.setFacing(Facing.EAST);
			} else {
				player.setMovementState(MovementState.STANDING);
			}
		} else {
			if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.A)) {
				player.getStatus().changeStat("xPos", -0.1f);
				player.setFacing(Facing.WEST);
			} else if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.D)) {
				player.getStatus().changeStat("yPos", -0.1f);
				player.setFacing(Facing.NORTH);
			} else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.A)) {
				player.getStatus().changeStat("yPos", 0.1f);
				player.setFacing(Facing.SOUTH);
			} else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.D)) {
				player.getStatus().changeStat("xPos", 0.1f);
				player.setFacing(Facing.EAST);
			} else if (Gdx.input.isKeyPressed(Keys.W)) {
				player.getStatus().changeStat("yPos", -num);
				player.getStatus().changeStat("xPos", -num);
				player.setFacing(Facing.NORTHWEST);
			} else if (Gdx.input.isKeyPressed(Keys.A)) {
				player.getStatus().changeStat("yPos", num);
				player.getStatus().changeStat("xPos", -num);
				player.setFacing(Facing.SOUTHWEST);
			} else if (Gdx.input.isKeyPressed(Keys.S)) {
				player.getStatus().changeStat("yPos", num);
				player.getStatus().changeStat("xPos", num);
				player.setFacing(Facing.SOUTHEAST);
			} else if (Gdx.input.isKeyPressed(Keys.D)) {
				player.getStatus().changeStat("yPos", -num);
				player.getStatus().changeStat("xPos", num);
				player.setFacing(Facing.NORTHEAST);
			} else {
				player.setMovementState(MovementState.STANDING);
			}
		}
		*/
	}
	
	private void executeLogic(float delta) {
		/*
		if (delta !=0 && !(loadNewChunksThread.isAlive())) {
			loadNewChunksThread = new Thread(new LoadNewChunksRunner(playerName, player.getStatus().getYMap(), player.getStatus().getXMap(), player.getStatus().getYPos(), player.getStatus().getXPos(), map));
			loadNewChunksThread.start();
		} else if (delta == 0) {
			loadNewChunksThread = new Thread(new LoadNewChunksRunner(playerName, player.getStatus().getYMap(), player.getStatus().getXMap(), player.getStatus().getYPos(), player.getStatus().getXPos(), map));
			loadNewChunksThread.start();
			while (loadNewChunksThread.isAlive()) {}
		}
		*/
		//movementLogic();
		player.preUpdate();
		entities.preUpdate();

		processKeyboardInput();
		//keyboardEvents.clear();
		
		processMouseInput();
		//mouseEvents.clear();

		updateTick();
		physicsManager.importNearbyChunks(player.pos);
		physicsManager.unloadFarAwayChunks(this, player.pos);
		time.update(((HudStage)hudStage).fastTime.isChecked());
		player.update(this);
		entities.update(this);
		achievements.updateAchievements(this);
//		moveCollection.updateMoves(tick);
		regenCriticalStats();

		particleEngine.update(delta, physicsManager.getDynamicsWorld());
		projectileManager.update(delta, this);
		physicsManager.update(delta, this);

		player.postUpdate();
		entities.postUpdate(this);
		//entities.spawnEntities(player.getStatus(), map.chunkNum(), map); // Spawn enemies after moving them, so player has a chance to react to newly spawned enemies
	}
	
	private void processKeyboardInput() {
		processingKeyboardInput = true;
		boolean skip;

		for (Integer keycode: keyboardEvents) {
			skip = false;

			for (int i = 0; i < 8; i ++) {
				if (keycode == ControlSettings.abilityKey(i + 1)) {
					player.getSkills().get(i).start(this);
					skip = true;
				}
			}
			if (skip) {
				continue;
			}

			if (keycode == ControlSettings.basicAttackKey()) {
				player.getBasicAttack().start(this);
			}
			else if (keycode == Keys.SHIFT_LEFT) {
				entities.addEntity(new Dummy(entities, new Vector3(0, 5, 0)), physicsManager);
			}
			else if (keycode == Keys.ESCAPE) {
				game.setScreen(new OptionsScreen(game, this));
			}
			else if (keycode == ControlSettings.openInventoryKey()) {
				game.setScreen(new InventoryScreen(game, this));
			}
			else if (keycode == Keys.SPACE) {
				testForJump();
			}
			/*switch (keycode) {
				case Keys.W:
					player.getBasicAttack().start(this);
					break;
				case Keys.NUM_1:
					player.getSkills().get(0).start(this);
					break;
				case Keys.NUM_2:
					player.getSkills().get(1).start(this);
					break;
				case Keys.NUM_3:
					player.getSkills().get(2).start(this);
					break;
				case Keys.NUM_4:
					player.getSkills().get(3).start(this);
					break;
				case Keys.NUM_5:
					player.getSkills().get(4).start(this);
					break;
				case Keys.NUM_6:
					player.getSkills().get(5).start(this);
					break;
				case Keys.NUM_7:
					player.getSkills().get(6).start(this);
					break;
				case Keys.NUM_8:
					player.getSkills().get(7).start(this);
					break;
				case Keys.SHIFT_LEFT:
					entities.addEntity(new Dummy(entities, new Vector3(0, 5, 0)), physicsManager);
					break;
				case Keys.ESCAPE:
					game.setScreen(new OptionsScreen(game, this));
					break;
				case Keys.I:
					game.setScreen(new InventoryScreen(game, this));
					break;
				*//*
				case Keys.Q:
					game.setScreen(new AchievementsScreen(game, this));
					break;
				case Keys.L:
					game.setScreen(new SpellsScreen(game, this));
					break;
				case Keys.K:
					game.setScreen(new MovesScreen(game, this));
					break;
				*//*
				case Keys.SPACE:
					testForJump();
					break;
				*//*
				case Keys.Z:
					// Below is temporary testing code
					for (OwnStage stage: ownStages) {
						if ("anvil".equals(stage.getName())) {
							ownStages.remove(stage);
							break; // If a furnace crafting window was already open, don't then immediately open another
						}
					}
					ownStages.add(new AnvilStage(game, player.inventory()));
					Gdx.input.setInputProcessor(new InputMultiplexer(ownStages.get(ownStages.size() - 1).stage, this));
					// End of temporary testing code
					//interactWithTile();
					break;
				case Keys.X:
					// Below is temporary testing code
					for (OwnStage stage: ownStages) {
						if ("furnace".equals(stage.getName())) {
							ownStages.remove(stage);
							break; // If a furnace crafting window was already open, don't then immediately open another
						}
					}
					ownStages.add(new FurnaceStage(game, player.inventory()));
					Gdx.input.setInputProcessor(new InputMultiplexer(ownStages.get(ownStages.size() - 1).stage, this));
					// End of temporary testing code
					//interactWithTile();
					break;
				case Keys.C:
					// Below is temporary testing code
					for (OwnStage stage: ownStages) {
						if ("polishing stone".equals(stage.getName())) {
							ownStages.remove(stage);
							break; // If a furnace crafting window was already open, don't then immediately open another
						}
					}
					ownStages.add(new PolishingStoneStage(game, player.inventory()));
					Gdx.input.setInputProcessor(new InputMultiplexer(ownStages.get(ownStages.size() - 1).stage, this));
					// End of temporary testing code
					//interactWithTile();
					break;
				case Keys.V:
					// Below is temporary testing code
					for (OwnStage stage: ownStages) {
						if ("selfcrafting".equals(stage.getName())) {
							ownStages.remove(stage);
							break; // If a furnace crafting window was already open, don't then immediately open another
						}
					}
					ownStages.add(new SelfcraftingStage(game, player.inventory()));
					Gdx.input.setInputProcessor(new InputMultiplexer(ownStages.get(ownStages.size() - 1).stage, this));
					// End of temporary testing code
					//interactWithTile();
					break;
				case Keys.B:
					// Below is temporary testing code
					for (OwnStage stage: ownStages) {
						if ("workbench".equals(stage.getName())) {
							ownStages.remove(stage);
							break; // If a furnace crafting window was already open, don't then immediately open another
						}
					}
					ownStages.add(new WorkbenchStage(game, player.inventory()));
					Gdx.input.setInputProcessor(new InputMultiplexer(ownStages.get(ownStages.size() - 1).stage, this));
					// End of temporary testing code
					//interactWithTile();
					break;
				*//*
			}*/
		}
		keyboardEvents.clear();
		processingKeyboardInput = false;
		for (int i = 0; i < overflowKeyboardEvents.size; i ++) {
			keyboardEvents.add(overflowKeyboardEvents.get(i));
		}
		overflowKeyboardEvents.clear();
	}
	
	private void processMouseInput() {
		processingMouseInput = true;
		if (mouseEvents.size > 0) {
			game.click.play();
		}
		for (MouseEvent event: mouseEvents) {
			if (event.button == Buttons.RIGHT) {
				rayPick(event.screenX, event.screenY);
			}
		}
		
		for (Integer button: pressedMouseButtons) {
			if (button == Buttons.LEFT) {
				rotatePlayer();
				testForWalk();
			}
		}
		mouseEvents.clear();
		processingMouseInput = false;
		for (int i = 0; i < overflowMouseEvents.size; i ++) {
			mouseEvents.add(overflowMouseEvents.get(i));
		}
		overflowMouseEvents.clear();
	}
	
	/*
	private void movementLogic() {
		if (Gdx.input.isButtonPressed(0)) {
			rotatePlayer(Gdx.input.getX(), Gdx.input.getY());
		}
		mouseClickMovement();
	}
	*/
	
	private void regenCriticalStats() {
		// Regen life
		/*
		if (tick % 10 == 0) {
			this.player.getStatus().changeStat("life", 1);
		}
		if (this.player.getStatus().getLife() > pStats.getRealMaxLife()) {
			this.player.getStatus().setLife(pStats.getRealMaxLife());
		}
		
		// Regen energy
		if (tick % 5 == 0) {
			this.player.getStatus().changeStat("energy", (int)(1 + pStats.getRealEnergyRegen())); // (int) to round down
		}
		if (this.player.getStatus().getEnergy() > pStats.getRealMaxEnergy()) {
			this.player.getStatus().setEnergy(pStats.getRealMaxEnergy());
		}
		
		// Regen mana
		if (tick % 10 == 0) {
			this.player.getStatus().changeStat("mana", 1);
		}
		if (this.player.getStatus().getMana() > pStats.getRealMaxMana()) {
			this.player.getStatus().setMana(pStats.getRealMaxMana());
		}
		*/
		
	}
	
	public void addMessagesToHud() {
		for (int i = 0; i < messages.size; i ++) {
			//terminal.write(messages.get(i).getText(), PlayScreen.leftAlign, PlayScreen.mainGuiDownAlign - 1 - messages.size() + i, messages.get(i).getColour());
			Label label = new Label(messages.get(i).getText(), new LabelStyle(AON_E.DEFAULT_FONT, messages.get(i).getColour()));
			label.setFontScale(0.2f);
			label.setX(AON_E.WORLD_WIDTH/2, Align.center);
			label.setY(AON_E.WORLD_HEIGHT - (150 + i * 50), Align.center);
			hudStage.stage.addActor(label);
		}
		//this.messages.clear();
	}
	
	public void addSubMessagesToHud() {
		for (int i = 0; i < subMessages.size; i ++) {
			//terminal.write(subMessages.get(i).getText(), PlayScreen.leftAlign, PlayScreen.messagesDownAlign + i, subMessages.get(i).getColour());
			Label label = new Label(subMessages.get(i).getText(), new LabelStyle(AON_E.DEFAULT_FONT, subMessages.get(i).getColour()));
			label.setFontScale(0.2f);
			label.setX(AON_E.WORLD_WIDTH/2, Align.center);
			label.setY(150 + i * 50, Align.center);
			hudStage.stage.addActor(label);
		}
		//this.subMessages.clear();
	}
	
	public void newMessage(String text) {
		Message message = new Message();
		message.setText(text);
		message.setColour(Color.WHITE);
		message.setDuration(3);
		this.messages.add(message);
	}
	
	public void newMessage(String text, Color colour) {
		Message message = new Message();
		message.setText(text);
		message.setColour(colour);
		message.setDuration(3);
		this.messages.add(message);
	}
	
	public void newMessage(String text, float duration) {
		Message message = new Message();
		message.setText(text);
		message.setColour(Color.WHITE);
		message.setDuration(duration);
		this.messages.add(message);
	}
	
	public void newMessage(String text, Color colour, float duration) {
		Message message = new Message();
		message.setText(text);
		message.setColour(colour);
		message.setDuration(duration);
		this.messages.add(message);
	}
	
	public void newSubMessage(String text) {
		Message message = new Message();
		message.setText(text);
		message.setColour(Color.WHITE);
		message.setDuration(3);
		this.subMessages.add(message);
	}
	
	public void newSubMessage(String text, Color colour) {
		Message message = new Message();
		message.setText(text);
		message.setColour(colour);
		message.setDuration(3);
		this.subMessages.add(message);
	}
	
	public void newSubMessage(String text, float duration) {
		Message message = new Message();
		message.setText(text);
		message.setColour(Color.WHITE);
		message.setDuration(duration);
		this.subMessages.add(message);
	}
	
	public void newSubMessage(String text, Color colour, float duration) {
		Message message = new Message();
		message.setText(text);
		message.setColour(colour);
		message.setDuration(duration);
		this.subMessages.add(message);
	}
	
	void saveAndExit() {
		try {
			// Saving stuff to player.txt
			player.saveAndExit();
			
			// Saving stuff to playerAchievements.txt
			achievements.savePlayerAchievements(playerName);
			
			// Saving stuff to skills.txt
//			skillCollection.saveSkills(playerName);
			
			/*// Saving stuff to playerMoves.txt
			moveCollection.savePlayerMoves(playerName);
			
			// Saving stuff to allSpells.txt
			spellCollection.savePlayerSpells(playerName);*/
			
			// Saving stuff to entities.txt
			entities.saveEntitiesAndExit(playerName);
			
			// Saving stuff to particles.txt
			particleEngine.saveAndExit(playerName);
			
			// Saving stuff to projectiles.txt
			projectileManager.saveAndExit(playerName);
			
			// Saving stuff to time.txt
			time.saveTime(playerName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {

			player.save();

			achievements.savePlayerAchievements(playerName);

//			skillCollection.saveSkills(playerName);

			/*moveCollection.savePlayerMoves(playerName);

			spellCollection.savePlayerSpells(playerName);*/

			entities.saveEntities(playerName);

			particleEngine.save(playerName);

			projectileManager.save(playerName);

			physicsManager.saveMobileObjectData(playerName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadGame() {
		updateTick();
		
		try {
		
			// Loading stuff from player.txt
			player = Player.load(this);
			
			// Loading stuff from allItems.txt
			allItems = AllItems.loadAll();
			
			// Loading stuff from playerAchievements.txt and allAchievements.txt
			achievements = AchievementCollection.loadAll(playerName);
			
			/*// Loading stuff from skills.txt
			skillCollection = SkillCollection.loadSkills(playerName);*/
			
			/*// Loading stuff from playerMoves.txt and allMoves.txt
			moveCollection = MoveCollection.loadAll(playerName);
			
			// Loading stuff from playerSpells.txt and allSpells.txt
			spellCollection = SpellCollection.loadAll(playerName);*/
			
			// Loading stuff from entities.txt
			entities = Entities.loadEntities(playerName);
			
			// Loading stuff from particles.txt
			particleEngine = ParticleEngine.load(playerName);
			
			// Loading stuff from projectiles.txt
			projectileManager = ProjectileManager.load(playerName);
			
			// Loading time
			time = Time.loadTime(playerName);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initialiseBlueprints(AON_E game) {
		PlayerBlueprint.initialise(game);
		DummyBlueprint.initialise(game);
	}
	
	private void rayPick(int x, int y) {
		Vector2 relativeCartPoint = isoRenderer.screenToRelativeCartesian(x, y);
		Pair<Integer, Vector3> pair = physicsManager.rayTestFirst(relativeCartPoint.x, isoRenderer.camera.pos.y, relativeCartPoint.y);
		if (pair != null) {
			if (PhysicsManager.isNonPlayerEntity(pair.getKey())) { // If the object is an entity which is not the player
				int id = Util.getId(pair.getKey());
				player.setTargetEntity(id);
			} else {
				player.setTargetLocation(pair.getValue());
	//			System.out.println(pair.getValue());
			}
		}
	}
	
}
