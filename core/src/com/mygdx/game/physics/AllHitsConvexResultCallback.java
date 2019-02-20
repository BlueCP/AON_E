package com.mygdx.game.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ConvexResultCallback;
import com.badlogic.gdx.physics.bullet.collision.LocalConvexResult;
import com.badlogic.gdx.utils.Array;

public class AllHitsConvexResultCallback extends ConvexResultCallback {

	private boolean hasHit = false;
	//private Array<btCollisionObject> hitObjects;
	private Array<Integer> hitIds;
	private Array<Vector3> hitPoints;
	
	AllHitsConvexResultCallback(Array<Integer> hitIds) {
		//hitObjects = new Array<btCollisionObject>();
		this.hitIds = hitIds;
		hitPoints = new Array<>();
	}
	
	@Override
	public float addSingleResult(LocalConvexResult convexResult, boolean normalInWorldSpace) {
		if (!hitIds.contains(convexResult.getHitCollisionObject().getUserValue(), false)) {
			hasHit = true;
			//hitObjects.add(convexResult.getHitCollisionObject());
			hitIds.add(convexResult.getHitCollisionObject().getUserValue());
			Vector3 vector = new Vector3();
			convexResult.getHitPointLocal(vector);
			hitPoints.add(vector);
		}

		return 0; // Not entirely sure what this does
	}
	
	@Override
	public boolean hasHit() {
		return hasHit;
	}

	public Array<Integer> getHitIds() {
		return hitIds;
	}
	
}
