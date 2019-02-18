package com.mygdx.game.spells;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.ProjectileManager;

public class Spells {

	private static boolean noResource(Entity entity, int cost) {
		if (entity.getSpirit() >= cost) {
			entity.changeSpirit(-cost);
			return false;
		} else if (entity.getLife() >= cost) {
			entity.changeLife(-cost);
			return false;
		} else {
			return true;
		}
	}
	
	public static void castImmolate(Entity caster, Vector3 pos, ProjectileManager projectileManager, btDynamicsWorld dynamicsWorld) {
		if (noResource(caster, 5)) {
			return;
		}
		projectileManager.addImmolateZone(caster, dynamicsWorld, pos);
	}
	
	public static void castRaze(Entity caster, Vector3 pos, ProjectileManager projectileManager, btDynamicsWorld dynamicsWorld) {
		if (noResource(caster, 5)) {
			return;
		}
		projectileManager.addRazeZone(caster, dynamicsWorld, pos, 5);
	}
	
	public static void castFirebolt(Entity caster, Entity target, ProjectileManager projectileManager, btDynamicsWorld dynamicsWorld) {
//		projectileManager.addFireball(caster, dynamicsWorld, caster.pos, target.pos, 5);
	}
	
	public static void castDamage(Entity caster, Entity target) {
		if (noResource(caster, 0)) {
			return;
		}
//		target.dealDamageOLD(2);
	}
	
	public static void castFire(Entity caster, Entity target) {
		if (noResource(caster, 5)) {
			return;
		}
//		target.changeEffect(EffectType.BURNING, 4, 1);
	}
	
}
