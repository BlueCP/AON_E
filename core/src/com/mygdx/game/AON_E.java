package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.physics.Hitboxes;
import com.mygdx.game.rendering.RenderingUtils;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.serialisation.KryoManager;
import com.mygdx.game.utils.VideoSettings;

import javax.xml.soap.Text;

public class AON_E extends Game {

	//public static final int WIDTH = 1000;
	//public static final int HEIGHT = 563;
	public static final String TITLE = "AON_E";

	// There are floats so that float division rather than integer division is performed with them.
	public static final float defaultScreenWidth = 1920;
	public static final float defaultScreenHeight = 1080;

	public static int effectiveScreenWidth;
	public static int effectiveScreenHeight;

	public static float widthFactor;
	public static float heightFactor;
	public static float screenSizeFactor; // Higher = larger screen, lower = smaller screen

	// These limits refer to the boundaries of the (visible) screen. This takes black bars into account.
	public static int upperLimit;
	public static int lowerLimit;
	public static int leftLimit;
	public static int rightLimit;

	// These borders refer to the borders between the renderable area and the actual size of the screen.
	public static int topBottomBorders;
	public static int sideBorders;

	// Center of the screen.
	public static Vector2 center;

	public SpriteBatch batch;
	public AssetManager manager;

	public static BitmapFont DEFAULT_FONT;
	public static Skin SKIN;

//	public TextureAtlas tileAtlas;
	public TextureAtlas playerWalkingAtlas;
	public TextureAtlas playerAttackAtlas;

	public TextureAtlas dummyWalkingAtlas;
	public TextureAtlas fireParticlesAtlas;
	public TextureAtlas projectilesAtlas;

	public Texture pointerUp;
	public Texture pointerDown;
//	private Texture tilePointer;
//	private Texture tileShadowNorth;
//	private Texture tileShadowEast;
//	private Texture tileShadowSouth;
//	private Texture tileShadowWest;

	public Sprite pointer;

	public Sound click;

	// These imaginary units are the units for full HD for convenience.
	// It means the size of assets doesn't have to be changed, because they were all created for full HD by default anyway.
	// This makes code a lot cleaner and prevents avoidable scalings in the draw calls.
	public static final float WORLD_WIDTH = 1920;
	public static final float WORLD_HEIGHT = 1080;

	public OrthographicCamera camera;
	public Viewport viewport;

//	public VideoSettings videoSettings;

	@Override
	public void create () {

		// GitHub Push Test, Last time.

		KryoManager.initialise();

		Bullet.init();

		Hitboxes.initialise();

		//effectiveScreenWidth = Gdx.graphics.getWidth();
		//effectiveScreenHeight = Gdx.graphics.getHeight();

		initialScreenCalcs();

		RenderingUtils.initialise();

//		videoSettings = new VideoSettings();
		VideoSettings.init();

		camera = new OrthographicCamera();
//		viewport = new FitViewport(WORLD_WIDTH, WORLD_WIDTH * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth()), camera);
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
//		viewport.apply();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		camera.update();

		batch = new SpriteBatch();
		manager = new AssetManager();

		// Loading sprites
		manager.load("sprites/pointer.png", Texture.class);
		manager.load("sprites/pointerdown.png", Texture.class);
		manager.load("sprites/tilePointer.png", Texture.class);
		manager.load("sprites/tileShadowNorth.png", Texture.class);
		manager.load("sprites/tileShadowEast.png", Texture.class);
		manager.load("sprites/tileShadowSouth.png", Texture.class);
		manager.load("sprites/tileShadowWest.png", Texture.class);
		manager.load("sprites/fullPlayerLifeBar.png", Texture.class);
		manager.load("sprites/fullEntityLifeBar.png", Texture.class);
		manager.load("sprites/fullSpiritBar.png", Texture.class);
		//manager.load("sprites/fullManaBar.png", Texture.class);

		// Loading entity animations
		manager.load("sprites/playerAnimations/walking/playerWalking.atlas", TextureAtlas.class);
		manager.load("sprites/playerAnimations/attack/playerAttack.atlas", TextureAtlas.class);

		manager.load("sprites/dummyAnimations/dummyWalking.atlas", TextureAtlas.class);

		// Loading particle sprites
		manager.load("sprites/particles/fireParticles.atlas", TextureAtlas.class);

		// Loading projectile sprites
		manager.load("sprites/projectiles/projectiles.atlas", TextureAtlas.class);

		// Loading screen backgrounds
		manager.load("textures/titlescreen.jpg", Texture.class);
		manager.load("textures/chooseRAndCBackground.jpg", Texture.class);
		manager.load("textures/enterNameBackground.jpg", Texture.class);
		manager.load("textures/chooseCharacterBackground.jpg", Texture.class);
		manager.load("textures/deleteCharacterBackground.jpg", Texture.class);

		// Loading other textures
		manager.load("textures/vignette.png", Texture.class);

		// Loading tile textures
		manager.load("textures/tileTextures.atlas", TextureAtlas.class);

		// Loading music
		manager.load("music/titletheme.mp3", Music.class);
		manager.load("music/travelmusic.mp3", Music.class);

		// Loading sounds
		manager.load("soundfx/click.wav", Sound.class);

		// Loading world textures
		manager.load("world/constObjects/constObjects.atlas", TextureAtlas.class);

		manager.finishLoading();

		DEFAULT_FONT = new BitmapFont(Gdx.files.internal("arialfont.fnt"));
		SKIN = new Skin(Gdx.files.internal("uiskin.json"));

		pointerUp = manager.get("sprites/pointer.png");
		pointerDown = manager.get("sprites/pointerdown.png");
//		tilePointer = manager.get("sprites/tilePointer.png");
//		tileShadowNorth = manager.get("sprites/tileShadowNorth.png");
//		tileShadowEast = manager.get("sprites/tileShadowEast.png");
//		tileShadowSouth = manager.get("sprites/tileShadowSouth.png");
//		tileShadowWest = manager.get("sprites/tileShadowWest.png");

		playerWalkingAtlas = manager.get("sprites/playerAnimations/walking/playerWalking.atlas");
		playerAttackAtlas = manager.get("sprites/playerAnimations/attack/playerAttack.atlas");

		dummyWalkingAtlas = manager.get("sprites/dummyAnimations/dummyWalking.atlas");
		fireParticlesAtlas = manager.get("sprites/particles/fireParticles.atlas");
		projectilesAtlas = manager.get("sprites/projectiles/projectiles.atlas");

		pointer = new Sprite(pointerUp);

		click = manager.get("soundfx/click.wav");

//		viewport = new ScreenViewport();

		Gdx.gl.glClearColor(0, 0, 0, 1);

		setScreen(new MainMenuScreen(this));
	}

	private void initialScreenCalcs() {
		// The two black boxes are equal in size, so the center of the screen should remain constant regardless.
		center = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

		widthFactor = Gdx.graphics.getWidth() / defaultScreenWidth;
		heightFactor = Gdx.graphics.getHeight() / defaultScreenHeight;

		// Temporary testing code
		//widthFactor = 3;
		//heightFactor = 2;

		// Use the smaller factor in order to have black bars on the screen (if a different aspect ratio is being used),
		// instead of rendering things off-screen.
		screenSizeFactor = widthFactor > heightFactor ? heightFactor : widthFactor;
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}

}
