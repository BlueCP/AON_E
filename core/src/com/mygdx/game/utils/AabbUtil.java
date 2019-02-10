package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector3;

public class AabbUtil {

	public static void aabbExpand(Vector3 aabbMin, Vector3 aabbMax, Vector3 expansionMin, Vector3 expansionMax) {
		aabbMin.add(expansionMin);
		aabbMax.add(expansionMax);
	}
	
	public static int outcode(Vector3 p, Vector3 halfExtent) {
		return (p.x < -halfExtent.x ? 0x01 : 0x0) |
				(p.x > halfExtent.x ? 0x08 : 0x0) |
				(p.y < -halfExtent.y ? 0x02 : 0x0) |
				(p.y > halfExtent.y ? 0x10 : 0x0) |
				(p.z < -halfExtent.z ? 0x4 : 0x0) |
				(p.z > halfExtent.z ? 0x20 : 0x0);
	}
	
	public static boolean rayAabb(Vector3 rayFrom, Vector3 rayTo, Vector3 aabbMin, Vector3 aabbMax, float[] param, Vector3 normal) {
		Vector3 aabbHalfExtent = new Vector3();
		Vector3 aabbCenter = new Vector3();
		Vector3 source = new Vector3();
		Vector3 target = new Vector3();
		Vector3 r = new Vector3();
		Vector3 hitNormal = new Vector3();

		aabbHalfExtent = aabbMax.cpy().sub(aabbMin);
		aabbHalfExtent.scl(0.5f);

		aabbCenter = aabbMax.cpy().add(aabbMin);
		aabbCenter.scl(0.5f);

		source = rayFrom.cpy().sub(aabbCenter);
		target = rayTo.cpy().sub(aabbCenter);

		int sourceOutcode = outcode(source, aabbHalfExtent);
		int targetOutcode = outcode(target, aabbHalfExtent);
		if ((sourceOutcode & targetOutcode) == 0x0) {
			float lambda_enter = 0f;
			float lambda_exit = param[0];
			r = target.cpy().sub(source);

			float normSign = 1f;
			hitNormal.set(0f, 0f, 0f);
			int bit = 1;

			for (int j = 0; j < 2; j++) {
				for (int i = 0; i != 3; ++i) {
					if ((sourceOutcode & bit) != 0) {
						float lambda = (-VectorUtil.getCoord(source, i) - VectorUtil.getCoord(aabbHalfExtent, i) * normSign) / VectorUtil.getCoord(r, i);
						if (lambda_enter <= lambda) {
							lambda_enter = lambda;
							hitNormal.set(0f, 0f, 0f);
							VectorUtil.setCoord(hitNormal, i, normSign);
						}
					}
					else if ((targetOutcode & bit) != 0) {
						float lambda = (-VectorUtil.getCoord(source, i) - VectorUtil.getCoord(aabbHalfExtent, i) * normSign) / VectorUtil.getCoord(r, i);
						//btSetMin(lambda_exit, lambda);
						lambda_exit = Math.min(lambda_exit, lambda);
					}
					bit <<= 1;
				}
				normSign = -1f;
			}
			if (lambda_enter <= lambda_exit) {
				param[0] = lambda_enter;
				normal.set(hitNormal);
				return true;
			}
		}
		return false;
	}
	
}
