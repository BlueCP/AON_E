package com.mygdx.game.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ConvexResultCallback;
import com.badlogic.gdx.physics.bullet.collision.LocalConvexResult;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;

public class AllHitsNotMeConvexResultCallback extends ConvexResultCallback {

	private boolean hasHit = false;
	private btCollisionObject me;
	//private Array<btCollisionObject> hitObjects;
	private Array<Integer> hitIds;
	private Array<Vector3> hitPoints;
	
	AllHitsNotMeConvexResultCallback(btCollisionObject me, Array<Integer> hitIds) {
		this.me = me;
		
		//hitObjects = new Array<btCollisionObject>();
		this.hitIds = hitIds;
		hitPoints = new Array<>();
	}
	
	@Override
	public float addSingleResult(LocalConvexResult convexResult, boolean normalInWorldSpace) {
		if (!hitIds.contains(convexResult.getHitCollisionObject().getUserValue(), true) && !convexResult.getHitCollisionObject().equals(me)) {
			hasHit = true;
			//hitObjects.add(convexResult.getHitCollisionObject());
			hitIds.add(convexResult.getHitCollisionObject().getUserValue());
			Vector3 vector = new Vector3();
			convexResult.getHitPointLocal(vector);
			hitPoints.add(vector);
//			System.out.println(vector + "                         n m");
		}

		return 0; // Not entirely sure what this does
	}
	
	@Override
	public boolean hasHit() {
		return hasHit;
	}

	/*public void setMe(btCollisionObject obj) {
		me = obj;
	}*/

	public Array<Integer> getHitIds() {
		return hitIds;
	}
	
}
