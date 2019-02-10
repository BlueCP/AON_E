package com.mygdx.game.utils;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.sun.javafx.geom.Matrix3f;
import com.sun.javafx.geom.Quat4f;

public class TransformUtil {

	public static void calculateVelocity(Matrix4 transform0, Matrix4 transform1, float timeStep, Vector3 linVel, Vector3 angVel) {
		/*linVel.sub(transform1.getrot, transform0.origin);
		linVel.scale(1f / timeStep);

		Vector3 axis = new Vector3();
		float[] angle = new float[1];
		calculateDiffAxisAngle(transform0, transform1, axis, angle);
		angVel.scale(angle[0] / timeStep, axis);*/
	}
	
	public static void calculateDiffAxisAngle(Matrix4 transform0, Matrix4 transform1, Vector3 axis, float[] angle) {
		/*Matrix3 tmp = new Matrix3();
		tmp.set(transform0.basis);
		MatrixUtil.invert(tmp);

		Matrix3f dmat = Stack.alloc(Matrix3f.class);
		dmat.mul(transform1.basis, tmp);

		Quat4f dorn = Stack.alloc(Quat4f.class);
		MatrixUtil.getRotation(dmat, dorn);

		dorn.normalize();

		angle[0] = QuaternionUtil.getAngle(dorn);
		axis.set(dorn.x, dorn.y, dorn.z);
		//axis[3] = btScalar(0.);

		// check for axis length
		float len = axis.lengthSquared();
		if (len < BulletGlobals.FLT_EPSILON * BulletGlobals.FLT_EPSILON) {
			axis.set(1f, 0f, 0f);
		}
		else {
			axis.scale(1f / (float) Math.sqrt(len));
		}*/
	}
	
}
