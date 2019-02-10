package com.mygdx.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.AON_E;

public class RenderingUtils {

	private static BarType barType;
	private static Texture blackBar;
	
	public enum BarType {
		HORIZONTAL,
		VERTICAL,
		NONE
	}
	
	public static void renderBlackBars(SpriteBatch spriteBatch) {
		if (barType == BarType.HORIZONTAL) {
			spriteBatch.draw(blackBar, 0, 0);
			spriteBatch.draw(blackBar, 0, Gdx.graphics.getHeight() - blackBar.getHeight());
		} else if (barType == BarType.VERTICAL) {
			spriteBatch.draw(blackBar, 0, 0);
			spriteBatch.draw(blackBar, Gdx.graphics.getWidth() - blackBar.getWidth(), 0);
		}
	}
	
	public static void initialise() {
		blackBarsInit();
		
		moreScreenCalcs();
	}
	
	private static void blackBarsInit() {
		if (AON_E.widthFactor == AON_E.heightFactor) {
			barType = BarType.NONE;
		} else if (AON_E.widthFactor > AON_E.heightFactor){
			barType = BarType.VERTICAL;
			int width = (int) (Gdx.graphics.getWidth() - (AON_E.defaultScreenWidth * AON_E.heightFactor)) / 2;
			
			Pixmap pix = new Pixmap(width, Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
			pix.setColor(0, 0, 0, 1);
			pix.fill();
			
			blackBar = new Texture(pix);
		} else { // If heightFactor > widthFactor
			barType = BarType.HORIZONTAL;
			int height = (int) (Gdx.graphics.getHeight() - (AON_E.defaultScreenHeight * AON_E.widthFactor)) / 2;
			
			Pixmap pix = new Pixmap(Gdx.graphics.getWidth(), height, Pixmap.Format.RGBA8888);
			pix.setColor(0, 0, 0, 1);
			pix.fill();
			
			blackBar = new Texture(pix);
		}
	}
	
	private static void moreScreenCalcs() {
		if (barType == BarType.NONE) {
			AON_E.upperLimit = Gdx.graphics.getHeight();
			AON_E.lowerLimit = 0;
			AON_E.leftLimit = 0;
			AON_E.rightLimit = Gdx.graphics.getWidth();
			
			AON_E.topBottomBorders = 0;
			AON_E.sideBorders = 0;
		} else if (barType == BarType.HORIZONTAL) {
			AON_E.upperLimit = Gdx.graphics.getHeight() - blackBar.getHeight();
			AON_E.lowerLimit = blackBar.getHeight();
			AON_E.leftLimit = 0;
			AON_E.rightLimit = Gdx.graphics.getWidth();
			
			AON_E.topBottomBorders = blackBar.getHeight();
			AON_E.sideBorders = 0;
		} else { // if barType == BarType.VERTICAL
			AON_E.upperLimit = Gdx.graphics.getHeight();
			AON_E.lowerLimit = 0;
			AON_E.leftLimit = blackBar.getWidth();
			AON_E.rightLimit = Gdx.graphics.getWidth() - blackBar.getWidth();
			
			AON_E.topBottomBorders = 0;
			AON_E.sideBorders = blackBar.getWidth();
		}
		
		AON_E.effectiveScreenWidth = AON_E.rightLimit - AON_E.leftLimit;
		AON_E.effectiveScreenHeight = AON_E.upperLimit - AON_E.lowerLimit;
	}
	
}
