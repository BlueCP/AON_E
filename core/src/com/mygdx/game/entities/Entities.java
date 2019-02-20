package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;

import java.util.Iterator;
import java.util.Map.Entry;

public class Entities {
	
	public Array<Entity> allEntities = new Array<>();
	
	Array<Integer> idPool;
	ObjectMap<Integer, Float> pendingIds; // Ids which will be added back into the id pool after a period of time.
	
	public Entities() {
		idPool = new Array<>();
		pendingIds = new ObjectMap<>();
	}
	
	public void clearOffensiveEnemies() {
		for (Entity entity: allEntities) {
			entity.getOffensiveEnemies().clear();
		}
	}

	private void updatePendingIds() {
		Iterator<ObjectMap.Entry<Integer, Float>> iterator = pendingIds.entries().iterator();
		while (iterator.hasNext()) {
			ObjectMap.Entry<Integer, Float> entry = iterator.next();
			if (entry.value > 0) {
				pendingIds.put(entry.key, entry.value - Gdx.graphics.getDeltaTime());
			} else {
				idPool.add(entry.key);
				iterator.remove();
			}
		}
	}
	
	public void update(PlayScreen playScreen) {
		updatePendingIds();

		for (Entity entity: allEntities) {
			/*
			if (entity.canSee(player, map) && entity.getBehaviour() == Behaviour.FOLLOW) {
				entity.setFollowingPlayer(true);
			}
			*/
			//entity.eStats().updateStatsCopy(); // Update the copy of the entity stats (things like max health)
			for (Entry<Entity, Integer> entry: entity.getOffensiveEnemies().entrySet()) { // For each entity which has attacked this entity
				entry.setValue(entry.getValue() + 1); // Add 1 to the time since the offensive entity attacked
				if (entry.getValue() > Entity.targetTime) { // If the time since the entity attacked exceeds the amount of time for which this entity will remember the attack (targetTime)
					entity.getOffensiveEnemies().remove(entry.getKey()); // Remove the offensive entity from memory
				}
			}
		}
		
		removeDeadEntities(playScreen);
		removeLostEntities(playScreen);
		
		for (Entity entity: allEntities) {
			entity.universalUpdate(playScreen);
			entity.onUpdate(playScreen);
		}
		
		for (Entity entity: allEntities) {
			entity.applyEffects(playScreen);
		}
	}
	
	public void preUpdate() {
		for (Entity entity: allEntities) {
			entity.preUpdate();
//			entity.setAnimationType(AnimationType.STAND);
		}
	}
	
	public void postUpdate(PlayScreen playScreen) {
		for (Entity entity: allEntities) {
			entity.postUpdate();
		}
	}
	
	public void addEntity(Entity entity, PhysicsManager physicsManager) {
		allEntities.add(entity);
		/*if (entity instanceof Player) {
			return;
		}*/
		physicsManager.getDynamicsWorld().addRigidBody(entity.rigidBody, PhysicsManager.HITBOX_FLAG, PhysicsManager.ALL_FLAG);
	}
	
	public Entity getEntity(int id) {
		for (Entity entity: allEntities) {
			if (entity.getId() == id) {
				return entity;
			}
		}
		return new NullEntity();
	}

	/**
	 * If the id refers to the player, return the player. Otherwise, call getEntity(id) as standard.
	 */
	public Entity getEntity(int id, Player player) {
		if (id == 0) {
			return player;
		} else {
			return getEntity(id);
		}
	}

	public void saveEntities(String dir) {
		Array<btRigidBody> tempBodies = new Array<>();

		for (Entity entity: allEntities) {
			tempBodies.add(entity.prepareForSave());
		}

		try {
			KryoManager.write(this, "saves/" + dir + "/world/entities.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < tempBodies.size; i ++) {
			allEntities.get(i).rigidBody = tempBodies.get(i);
		}
	}
	
	public void saveEntitiesAndExit(String dir) {
		for (Entity entity: allEntities) {
			entity.prepareForSaveAndExit();
		}
		
		try {
			if (!(Gdx.files.local("saves/" + dir + "/world").exists())) {
				Gdx.files.local("saves/" + dir + "/world").mkdirs();
			}
			KryoManager.write(this, "saves/" + dir + "/world/entities.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Entities loadEntities(String dir) {
		Entities entities;
		
		try {
			entities = KryoManager.read("saves/" + dir + "/world/entities.txt", Entities.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		for (Entity entity: entities.allEntities) {
			entity.processAfterLoading();
		}
		
		return entities;
	}
	
	public void removeEntity(Entity entity, PlayScreen playScreen) {
		entity.procDeathEffects(playScreen);
		allEntities.removeValue(entity, false);
		playScreen.physicsManager.getDynamicsWorld().removeRigidBody(entity.rigidBody);
		pendingIds.put(entity.getId(), 60f); // Adds the id to the pending ids. It will be added to the id pool after 60 seconds.
	}
	
	private void removeDeadEntities(PlayScreen playScreen) {
		//MoveCollection moveCollection = playScreen.moveCollection;
		for (int i = 0; i < allEntities.size; i ++) {
			Entity entity = allEntities.get(i);
			if (entity.getLife() <= 0) { // If the entity is on less than 1 life, i.e. dead
				// Next few lines test if the player killed the entity and whether the player gets a move as a reward
				/*
				if (entity.isDamagedByPlayer() == true && "melee".equals(entity.getLastHitWith().getRangeType())) {
					moveCollection.getMove("Slam").setCondition1(1);
				} else if (entity.isDamagedByPlayer() == true && "ranged".equals(entity.getLastHitWith().getRangeType())) {
					moveCollection.getMove("Lunge").setCondition1(1);
				}
				if (moveCollection.getMove("Cleave").getCondition1() == 0) {
					moveCollection.getMove("Cleave").setCondition1(5);
				} else {
					moveCollection.getMove("Cleave").setCondition2(1);
				}
				*/
				/*
				if (playScreen.targetEntity == entity) { // If the entity had been targetted
					playScreen.targetEntity = null; // Set the target entity to null
				}
				*/
				removeEntity(entity, playScreen);
				i --; // Since we have decreased the size of the list, we need to adjust the counter
			}
		}
	}
	
	/*
	 * Lost entities are entities which have passed above or below the limits of the world.
	 */
	private void removeLostEntities(PlayScreen playScreen) {
		for (int i = 0; i < allEntities.size; i ++) {
			Entity entity = allEntities.get(i);
			if (entity.pos.y < PhysicsManager.lowestPoint || entity.pos.y > PhysicsManager.highestPoint) {
				removeEntity(entity, playScreen);
				i --;
			}
		}
	}
	
}
