package com.mygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.mygdx.game.serialisation.KryoManager;

import java.io.FileInputStream;

public class Time {
	
	private int day;
//	private int hour;
//	private int minute;
//	private int second;

//	private float decDay;
//	private float decHour;

	private float minute; // Resets every day, the number of minutes that have passed since 12am (can be <1).
	
	private float sunlightLevel;
	
	public void saveTime(String dir) {
		try {
			if (!(Gdx.files.local("saves/" + dir + "/world").exists())) {
				Gdx.files.local("saves/" + dir + "/world").mkdirs();
			}
			KryoManager.write(this, "saves/" + dir + "/world/time.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Time loadTime(String dir) {
		try {
			return KryoManager.read("saves/" + dir + "/world/time.txt", Time.class);
		} catch (Exception e) {
			return null;
		}
	}

	public static Time loadTime(String dir, Kryo kryo) {
		try {
			Input input = new Input(new FileInputStream(Gdx.files.getLocalStoragePath() + "saves/" + dir + "/world/time.txt"));
			return kryo.readObject(input, Time.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	public int getDay() {
		return day;
	}
	
	public int getHour() {
		return (int) (minute / 60);
	}
	
	public int getMinute() {
		return Math.floorMod((int) minute, 60);
	}
	
	public float getSunlightLevel() {
		return sunlightLevel;
	}
	
	public void setHour(float hour) {
		this.minute = hour * 60;
	}
	
	public Time() {
		day = 1;
//		hour = 0;
		minute = 0;
		sunlightLevel = 0;
	}
	
	public void update(boolean fastTime) {
		if (fastTime) {
			minute += Gdx.graphics.getDeltaTime() * 60;
		} else {
			minute += Gdx.graphics.getDeltaTime();
		}
		
		/*if (second >= 60) {
			second = 0;
			minute ++;
		}*/
		
		/*if (minute >= 60) {
			minute = 0;
			hour ++;
		}*/
		
		if (minute >= 60*24) {
//			hour = 0;
			minute = 0;
			day ++;
		}
		
//		decHour = hour + (minute / 60f) + (second / 3600f);
//		decDay = day + (hour / 24f) + (minute / (24f*60f)) + (second / (24f*3600f));
		
		/*
		if (decHour >= 2 && decHour <= 14) {
			sunlightLevel = (decHour - 2) / 12f;
		} else if (decHour <= 1) {
			sunlightLevel = 1 - ((10 + decHour) / 12f);
		} else {
			sunlightLevel = 1 - ((decHour - 14) / 12f);
		}
		*/
		sunlightLevel = (MathUtils.sinDeg(((minute/60 - 8) * 15)) + 1) / 2;
		// The reason the 12's above are floats is so that decimal division, rather than integer division, is performed.
	}
	
}
