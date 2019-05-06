package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.AON_E;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = AON_E.WIDTH;
		//config.height = AON_E.HEIGHT;
		System.out.println(config.width);
		System.out.println(config.height);
		// Default width and height: 640 x 480
//		config.width = 640;
//		config.height = 480;
//		config.width = 800;
//		config.height = 500;
//		config.width = 1920;
//		config.height = 1080;
//		config.width = Integer.MAX_VALUE;
//		config.height = Integer.MAX_VALUE;
		config.title = AON_E.TITLE;
		config.addIcon("textures/blueFlame.jpg", Files.FileType.Internal);
//		config.fullscreen = true;
		LwjglApplication app = new LwjglApplication(new AON_E(), config);
		/*
		Pixmap pm = new Pixmap(Gdx.files.internal("pointer.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		pm.dispose();
		*/
	}
}
