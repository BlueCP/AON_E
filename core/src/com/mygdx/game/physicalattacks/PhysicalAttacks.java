package com.mygdx.game.physicalattacks;

import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.utils.Util;

public class PhysicalAttacks {

	public static void slash(Entity attacker, Entity target, Entities entities, PhysicsManager physicsManager) {
		btCollisionShape shape = new btSphereShape(1f);
		Array<Integer> ids = physicsManager.testCollision(shape, attacker.pos);
		if (ids.size < 1) {
			return;
		}
		for (int i = 0; i < ids.size; i ++) {
			if (String.valueOf(ids.get(i)).startsWith("1000") && ids.get(i) != Util.getPhysicsId(attacker.getId(), "1000")) {
				// If the collided object is an entity, and that entity is not the attacker
//				entities.getEntity(Util.getId(ids.get(i))).dealDamageOLD(1);
			}
		}
	}
	
}
