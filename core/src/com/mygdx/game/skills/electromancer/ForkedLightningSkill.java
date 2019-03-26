package com.mygdx.game.skills.electromancer;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.projectiles.electromancer.GenericLightning;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.PassiveSkill;

public class ForkedLightningSkill extends PassiveSkill {

	private static final float maxDistanceTotal = 10; // The maximum distance the lightning will travel from the starting point.
	private static final float maxDistanceSingle = 10; // The maximum distance a single lightning botl will travel.
	private static final int chainLength = 4; // The number of entities this spell can chain to.

	/**
	 * No-arg constructor for serialisation purposes.
	 */
	public ForkedLightningSkill() {
		super(null, false);
	}

	public ForkedLightningSkill(Entity entity, boolean learned) {
		super(entity, learned);
		name = "Forked Lightning";
		desc = "Spells cause chain lightning between enemies, damaging them all.";
	}

	/**
	 * A recursive method that chains lightning to the two nearest entities. If an entity has already been hit by lightning,
	 * then skip it, and only chain lightning to one entity.
	 * @param startingEntity the entity from which the lightning is to start.
	 */
	private void chainToNext(Entity startingEntity, Array<Entity> nearestEntities, Array<Integer> hitEntities, float damage, int chainSize, PlayScreen playScreen) {
		Array<Entity> entitiesToChain = new Array<>();
		for (Entity nearbyEntity: nearestEntities) {
			if (entitiesToChain.size < chainSize && !hitEntities.contains(nearbyEntity.id, true)) {
				entitiesToChain.add(nearbyEntity);
			} else if (!hitEntities.contains(nearbyEntity.id, true)) {
				int farthestAwayIndex = -1;
				float distance = 0;
				for (int i = 0; i < entitiesToChain.size; i ++) {
					if (entitiesToChain.get(i).pos.dst(startingEntity.pos) > distance) {
						farthestAwayIndex = i;
						distance = entitiesToChain.get(i).pos.dst(startingEntity.pos);
					}
				}
				if (farthestAwayIndex != -1) {
					if (entitiesToChain.get(farthestAwayIndex).pos.dst(startingEntity.pos) > nearbyEntity.pos.dst(startingEntity.pos)) {
						entitiesToChain.removeIndex(farthestAwayIndex);
						entitiesToChain.add(nearbyEntity);
					}
				}
			}
		}
		for (int i = 0; i < entitiesToChain.size; i ++) {
			Entity entityToChain = entitiesToChain.get(i);
			if (!hitEntities.contains(entityToChain.id, true)) {
				playScreen.projectileManager.addProjectileNow(new GenericLightning(entity, startingEntity.pos, entityToChain.pos, 0.2f, damage), playScreen.physicsManager.getDynamicsWorld());
				hitEntities.add(entityToChain.id);
			}
			/*else {
				entitiesToChain.removeIndex(i);
				i --;
			}*/
		}
		if (entitiesToChain.size > 0) {
			for (Entity entityToChain: entitiesToChain) {
				chainToNext(entityToChain, nearestEntities, hitEntities, damage, 1, playScreen);
			}
		}
	}

	public void testfor(Entity targetEntity, float damage, PlayScreen playScreen) {
		if (isLearned()) {
			/*Array<Entity> nearestEntities = new Array<>(); // The two nearest entities to the entity that has been damaged.
			for (Entity entity1: playScreen.entities.allEntities) {
				if (targetEntity.id == entity1.id) { // If the current entity is the same as the target entity
					continue; // Skip it, since we want nearby entities, not just the same entity.
				} else if (nearestEntities.size < chainLength && entity1.pos.dst(targetEntity.pos) <= maxDistanceTotal) {
					nearestEntities.add(entity1);
				} else {
					for (int i = 0; i < nearestEntities.size; i ++) {
						if (entity1.pos.dst(targetEntity.pos) < nearestEntities.get(i).pos.dst(targetEntity.pos) &&
							entity1.pos.dst(targetEntity.pos) <= maxDistanceTotal) { // If the entity is nearer to the target entity that the entity in nearestEntities AND within max range
							nearestEntities.removeIndex(i); // Remove the (farther away) entity.
							nearestEntities.insert(i, entity1); // Add the (nearer) entity.
							break;
						}
					}
				}
			}*/
			Array<Entity> nearestEntities = playScreen.entities.getNearestEntities(targetEntity, chainLength, maxDistanceTotal);
			Array<Integer> hitEntities = new Array<>();
//			Array<Entity> entitiesToChain = new Array<>();
			chainToNext(targetEntity, nearestEntities, hitEntities, damage, 1, playScreen);
			/*for (Entity entity2: nearestEntities) {
				Vector3 startPos = targetEntity.pos;
				Vector3 endPos = entity2.pos;
				playScreen.projectileManager.addProjectileNow(new GenericLightning(entity, startPos, endPos, 0.2f, damage), playScreen.physicsManager.getDynamicsWorld());
			}*/
		}
	}

}
