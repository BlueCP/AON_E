package com.mygdx.game.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Point3 {

	public float x;
	public float y;
	public float z;
	
	public Point3() {
		
	}
	
	public Point3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setCoords(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)x;
		result = prime * result + (int)y;
		result = prime * result + (int)z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point3))
			return false;
		Point3 other = (Point3) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public List<Point3> neighbors8(){
		List<Point3> points = new ArrayList<Point3>();
		
		for (int ox = -1; ox < 2; ox++){
			for (int oy = -1; oy < 2; oy++){
				if (ox == 0 && oy == 0)
					continue;
				
				points.add(new Point3(x+ox, y+oy, z));
			}
		}

		Collections.shuffle(points);
		return points;
	}
	
}