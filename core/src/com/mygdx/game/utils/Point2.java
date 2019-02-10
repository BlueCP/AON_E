package com.mygdx.game.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Point2 {

	public float x;
	public float y;
	
	public Point2() {
		
	}
	
	public Point2(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setCoords(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)x;
		result = prime * result + (int)y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point2))
			return false;
		Point2 other = (Point2) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public List<Point2> neighbors8(){
		List<Point2> points = new ArrayList<Point2>();
		
		for (int ox = -1; ox < 2; ox++){
			for (int oy = -1; oy < 2; oy++){
				if (ox == 0 && oy == 0)
					continue;
				
				points.add(new Point2(x+ox, y+oy));
			}
		}

		Collections.shuffle(points);
		return points;
	}
	
}
