package com.mygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.serialisation.KryoManager;

public class Time {
	
	private int day;
	private int hour;
	private int minute;
	private int second;
	
	private float decDay;
	private float decHour;
	
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
	
	public int getDay() {
		return day;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public int getSecond() {
		return second;
	}
	
	public float getSunlightLevel() {
		return sunlightLevel;
	}
	
	public float getDecDay() {
		return decDay;
	}
	
	public float getDecHour() {
		return decHour;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public void setSecond(int second) {
		this.second = second;
	}
	
	public void changeDay(int amount) {
		this.day += amount;
	}
	
	public void changeHour(int amount) {
		this.hour += amount;
	}
	
	public void changeMinute(int amount) {
		this.minute += amount;
	}
	
	public void changeSecond(int amount) {
		this.second += amount;
	}
	
	public Time() {
		day = 1;
		hour = 0;
		minute = 0;
		sunlightLevel = 0;
	}
	
	public void update(boolean fastTime) {
		if (fastTime) {
			minute ++;
		} else {
			second ++;
		}
		
		if (second >= 60) {
			second = 0;
			minute ++;
		}
		
		if (minute >= 60) {
			minute = 0;
			hour ++;
		}
		
		if (hour >= 24) {
			hour = 0;
			day ++;
		}
		
		decHour = hour + (minute / 60f) + (second / 3600f);
		decDay = day + (hour / 24f) + (minute / (24f*60f)) + (second / (24f*3600f));
		
		/*
		if (decHour >= 2 && decHour <= 14) {
			sunlightLevel = (decHour - 2) / 12f;
		} else if (decHour <= 1) {
			sunlightLevel = 1 - ((10 + decHour) / 12f);
		} else {
			sunlightLevel = 1 - ((decHour - 14) / 12f);
		}
		*/
		sunlightLevel = (MathUtils.sinDeg(((decHour - 8) * 15)) + 1) / 2;
		// The reason the 12's above are floats is so that decimal division, rather than integer division, is performed.
	}
	
}
