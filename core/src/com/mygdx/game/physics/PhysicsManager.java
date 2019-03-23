package com.mygdx.game.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.droppeditems.DroppedItem;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;
import com.mygdx.game.particles.Particle;
import com.mygdx.game.physicsobjects.*;
import com.mygdx.game.projectiles.Projectile;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;
import com.mygdx.game.utils.Util;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PhysicsManager {

	transient Array<ConstantObject> allConstantObjects;

	transient Array<ImmobileTerrain> immobileTerrain;
	transient Array<ImmobileController> immobileControllers;
	transient Array<ImmobileControllable> immobileControllables;
	transient Array<ImmobileInteractive> immobileInteractives;

	transient Array<MobileTerrain> mobileTerrain;
	transient Array<MobileController> mobileControllers;
	transient Array<MobileControllable> mobileControllables;
	transient Array<MobileInteractive> mobileInteractives;


	public transient Array<ImmobileObject> renderableImmobileObjects = new Array<>();
	public transient Array<MobileObject> renderableMobileObjects = new Array<>();

	public transient Array<Entity> renderableEntities = new Array<>();
	public transient Array<Particle> renderableParticles = new Array<>();
	public transient Array<Projectile> renderableProjectiles = new Array<>();
	public transient Array<DroppedItem> renderableDroppedItems = new Array<>();

	/**
	 * Key: this is the 'port'.
	 * Value: this is the value of the port. E.g. if the port is for a two-way switch, 0 = off, and 1 = on.
	 */
	private ObjectMap<Integer, Integer> controllableFlags = new ObjectMap<>();

	private Entities entities;

    private transient btCollisionShape renderableFinderOrig;
    private transient btConvexShape renderableFinder;
    
//    private MyContactListener contactListener;
    
    public final static short ALL_FLAG = -1;
    public final static short TERRAIN_FLAG = 1<<8;
    public final static short HITBOX_FLAG = 1<<9;
    public final static short PARTICLE_FLAG = 1<<10;
    public final static short PROJECTILE_FLAG = 1<<11;
    public final static short DROPPED_ITEM_FLAG = 1<<12;
    //public final static short TESTER_FLAG = 1<<12;
    public final static short IGNORE_FLAG = 1<<12;
    
    public transient PhysicsWorldBuilder physicsWorldBuilder;
    
    public static final Vector3 gravity = new Vector3(0, -5f, 0);
    public static final float lowestPoint = -10;
    public static final float highestPoint = 50;
    
    private Array<Integer> hitIds;

	private transient btCollisionConfiguration collisionConfig;
	private transient btDispatcher dispatcher;
	private transient btBroadphaseInterface broadphase;
	private transient btDynamicsWorld dynamicsWorld;
	private transient btConstraintSolver constraintSolver;
    
    /*
     * Tags are used to specify behaviours of static objects.
     */
    public enum Tag {
    	WALKABLE("walk"),
    	CLIMBABLE("climb"),
    	INVISIBLE("invis");
    	
    	private String id;
    	public String id() { return id; }
    	
    	Tag(String id) {
    		this.id = id;
    	}
    }
    
    /*
     * Used for ray testing and convex sweep testing. Refers to which direction the test should perform in.
     */
    public enum TestMode {
    	TOP_TO_BOTTOM, // From max y to min y
    	UPWARDS, // From the player's y to max y
    	DOWNWARDS; // From the player's y to min y
    }
    
    public static boolean isConstObject(int id) { return String.valueOf(id).charAt(1) == '1'; }

    public static boolean isImmobileObject(int id) { return String.valueOf(id).charAt(1) == '1'; }

    public static boolean isMobileObject(int id) { return String.valueOf(id).charAt(1) == '2'; }

    public static boolean isImmobileTerrain(int id) { return String.valueOf(id).startsWith("11"); }

    public static boolean isImmobileController(int id) { return String.valueOf(id).startsWith("21"); }

    public static boolean isImmobileControllable(int id) { return String.valueOf(id).startsWith("31"); }

    public static boolean isImmobileInteractive(int id) { return String.valueOf(id).startsWith("41"); }

    public static boolean isMobileTerrain(int id) { return String.valueOf(id).startsWith("12"); }

    public static boolean isMobileController(int id) { return String.valueOf(id).startsWith("22"); }

    public static boolean isMobileControllable(int id) { return String.valueOf(id).startsWith("32"); }

    public static boolean isMobileInteractive(int id) { return String.valueOf(id).startsWith("42"); }

    public static boolean isTerrainObject(int id) { return isImmobileTerrain(id) || isMobileTerrain(id); }

    public static boolean isControllerObject(int id) { return isImmobileController(id) || isMobileController(id); }

    public static boolean isControllableObject(int id) { return isImmobileControllable(id) || isMobileControllable(id); }

    public static boolean isInteractiveObject(int id) { return isImmobileInteractive(id) || isMobileInteractive(id); }

    
    public static boolean isPlayer(int id) {
    	return id == 10000;
    }
    
    public static boolean isNonPlayerEntity(int id) {
    	return String.valueOf(id).startsWith("1000") && !isPlayer(id);
    }
    
    public static boolean isEntityOrPlayer(int id) {
    	return String.valueOf(id).startsWith("1000");
    }

	public static boolean isProjectile(int id) {
    	return String.valueOf(id).startsWith("2000");
    }

	public static boolean isParticle(int id) {
    	return String.valueOf(id).startsWith("3000");
    }

	public static boolean isDroppedItem(int id) {
    	return String.valueOf(id).startsWith("4000");
	}

    void removeImmobileTerrain(ImmobileTerrain obj) {
    	allConstantObjects.removeValue(obj, false);
    	immobileTerrain.removeValue(obj, false);
	}

	void removeImmobileController(ImmobileController obj) {
    	allConstantObjects.removeValue(obj, false);
    	immobileControllers.removeValue(obj, false);
	}

	void removeImmobileControllable(ImmobileControllable obj) {
    	allConstantObjects.removeValue(obj, false);
    	immobileControllables.removeValue(obj, false);
	}

	void removeImmobileInteractive(ImmobileInteractive obj) {
    	allConstantObjects.removeValue(obj, false);
    	immobileInteractives.removeValue(obj, false);
	}

	void removeMobileTerrain(MobileTerrain obj) {
		allConstantObjects.removeValue(obj, false);
		mobileTerrain.removeValue(obj, false);
	}

	void removeMobileController(MobileController obj) {
		allConstantObjects.removeValue(obj, false);
		mobileControllers.removeValue(obj, false);
	}

	void removeMobileControllable(MobileControllable obj) {
		allConstantObjects.removeValue(obj, false);
		mobileControllables.removeValue(obj, false);
	}

	void removeMobileInteractive(MobileInteractive obj) {
		allConstantObjects.removeValue(obj, false);
		mobileInteractives.removeValue(obj, false);
	}

    public void importNearbyChunks(Player player) {
    	physicsWorldBuilder.importNearbyChunks(this, player);
	}

	public void unloadFarAwayChunks(PlayScreen playScreen, Vector3 pos) {
    	physicsWorldBuilder.unloadFarAwayChunks(playScreen, pos);
	}

    public void saveConstantObjectData(String name) {
		try {
//			Gdx.files.local("saves/" + name + "/constObjData").mkdirs();
			Kryo kryo = KryoManager.getKryo();
			Output output = new Output(new FileOutputStream("saves/" + name + "/constObjData.txt"));

			for (ConstantObject constantObject: allConstantObjects) {
				constantObject.save(kryo, output);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

    	/*Array<Integer> ids = new Array<>();
    	Array<Matrix4> transforms = new Array<>();
    	Array<Vector3> velocities = new Array<>();

    	for (MobileObject obj: mobileTerrain) {
    		ids.add(obj.physicsId);
    		transforms.add(obj.collisionObject.getWorldTransform());
    		velocities.add(obj.getLinearVelocity());
		}
		for (MobileObject obj: mobileControllers) {
			ids.add(obj.physicsId);
			transforms.add(obj.collisionObject.getWorldTransform());
			velocities.add(obj.getLinearVelocity());
		}
		for (MobileObject obj: mobileControllables) {
			ids.add(obj.physicsId);
			transforms.add(obj.collisionObject.getWorldTransform());
			velocities.add(obj.getLinearVelocity());
		}
		for (MobileObject obj: mobileInteractives) {
			ids.add(obj.physicsId);
			transforms.add(obj.collisionObject.getWorldTransform());
			velocities.add(obj.getLinearVelocity());
		}

		Gdx.files.local("saves/" + name + "/mobileObjData").mkdirs();

		KryoManager.write(ids, "saves/" + name + "/mobileObjData/ids.txt");
		KryoManager.write(transforms, "saves/" + name + "/mobileObjData/transforms.txt");
		KryoManager.write(velocities, "saves/" + name + "/mobileObjData/velocities.txt");*/
	}

	public void saveConstantObjectData(String name, Kryo kryo) {
		try {
			Output output = new Output(new FileOutputStream("saves/" + name + "/constObjData.txt"));

			for (ConstantObject constantObject: allConstantObjects) {
				constantObject.save(kryo, output);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*void loadConstantObjectData(String name, int x, int y) {
		*//*if (Gdx.files.local("saves/" + name + "/mobileObjData/ids.txt").exists()) {
			Array<Integer> ids = KryoManager.read("saves/" + name + "/mobileObjData/ids.txt", Array.class);
			Array<Matrix4> transforms = KryoManager.read("saves/" + name + "/mobileObjData/transforms.txt", Array.class);
			Array<Vector3> velocities = KryoManager.read("saves/" + name + "/mobileObjData/velocities.txt", Array.class);

			for (int i = 0; i < ids.size; i ++) {
				MobileObject obj = findMobileObject(ids.get(i));
				obj.collisionObject.setWorldTransform(transforms.get(i));
				obj.setLinearVelocity(velocities.get(i));
			}
		}*//*

		if (Gdx.files.local("saves/" + name + "constObjData.txt").exists()) {
			try {
				Kryo kryo = KryoManager.getKryo();
				Input input = new Input(new FileInputStream("saves/" + name + "/world/chunks/" + x + "_" + y + "/constObjData.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}*/
    
    /*
     * Of all objects in the world, find those which can be rendered on the screen.
     */
	private void findRenderable(PlayScreen playScreen) {
		renderableImmobileObjects.clear();
    	renderableMobileObjects.clear();
    	renderableEntities.clear();
    	renderableParticles.clear();
    	renderableProjectiles.clear();
    	renderableDroppedItems.clear();
    	
    	Array<Integer> hitIds = convexSweepTestAll(renderableFinder, playScreen.player.pos.cpy(), TestMode.TOP_TO_BOTTOM);
    	for (int i = 0; i < hitIds.size; i ++) {
    		int id = hitIds.get(i);
    		if (isImmobileObject(id)) {
    			renderableImmobileObjects.add(findImmobileObject(id));
			} else if (isMobileObject(id)) {
    			renderableMobileObjects.add(findMobileObject(id));
			} else if (isNonPlayerEntity(id)) {
				for (Entity entity: playScreen.entities.allEntities) {
					if (entity.physicsId == id) {
						renderableEntities.add(entity);
					}
				}
			} else if (isParticle(id)) {
				for (Particle particle: playScreen.particleEngine.particles) {
					if (particle.physicsId == id) {
						renderableParticles.add(particle);
					}
				}
			} else if (isProjectile(id)) {
				for (Projectile projectile: playScreen.projectileManager.projectiles) {
					if (projectile.physicsId == id) {
						renderableProjectiles.add(projectile);
					}
				}
			} else if (isDroppedItem(id)) {
    			for (DroppedItem droppedItem: playScreen.droppedItemManager.droppedItems) {
    				if (droppedItem.physicsId == id) {
    					renderableDroppedItems.add(droppedItem);
					}
				}
			}
    	}
    	
    	/*
    	renderableFinder.setWorldTransform(renderableFinder.getWorldTransform().setTranslation(playScreen.player.pos.x + highestPoint - playScreen.player.pos.y, highestPoint, playScreen.player.pos.z - (highestPoint - playScreen.player.pos.y)));
    	dynamicsWorld.addCollisionObject(renderableFinder, ALL_FLAG, ALL_FLAG);
		while (renderableFinder.getWorldTransform().getTranslation(new Vector3()).y >= lowestPoint) {
			renderableFinder.setWorldTransform(renderableFinder.getWorldTransform().translate(-0.5f, -0.5f, 0.5f));
			//dynamicsWorld.updateAabbs();
			dynamicsWorld.performDiscreteCollisionDetection();
			for (int i = 0; i < renderableFinder.getNumOverlappingObjects(); i ++) {
				btCollisionObject object = renderableFinder.getOverlappingObject(i);
				
				if (isStaticObject(object.getUserValue())) {
					for (StaticObject staticObject: immobileTerrain) {
						if (staticObject.id == Util.getEnd(renderableFinder.getOverlappingObject(i).getUserValue(), 4) &&
							!renderableStaticObjects.contains(staticObject, false)) {
							renderableStaticObjects.add(staticObject);
						}
					}
				} else if (isNonPlayerEntity(object.getUserValue())) {
					for (Entity entity: playScreen.entities.allEntities) {
						if (entity.getId() == Util.getEnd(renderableFinder.getOverlappingObject(i).getUserValue(), 4) &&
							!renderableEntities.contains(entity, false)) {
							renderableEntities.add(entity);
						}
					}
				} else if (isParticle(object.getUserValue())) {
					for (Particle particle: playScreen.particleEngine.particles) {
						if (particle.id == Util.getEnd(renderableFinder.getOverlappingObject(i).getUserValue(), 4) &&
							!renderableParticles.contains(particle, false)) {
							renderableParticles.add(particle);
						}
					}
				} else if (isProjectile(object.getUserValue())) {
					for (Projectile projectile: playScreen.projectileManager.projectiles) {
						if (projectile.id == Util.getEnd(renderableFinder.getOverlappingObject(i).getUserValue(), 4) &&
							!renderableProjectiles.contains(projectile, false)) {
							renderableProjectiles.add(projectile);
						}
					}
				}
			}
			//dynamicsWorld.updateAabbs();
			// The above also works in place of performDiscreteCollisionDetection()
		}
		dynamicsWorld.removeCollisionObject(renderableFinder);
		*/
    }
    
    public static class MyMotionState extends btMotionState {
    	public Matrix4 transform;
    	
    	@Override
		public void getWorldTransform(Matrix4 worldTrans) {
			//worldTrans.set(transform);
		}

		@Override
		public void setWorldTransform(Matrix4 worldTrans) {
			//transform.set(worldTrans);
		}
    }

	/**
	 * No-arg constructor, mainly for copying purposes.
	 */
	private PhysicsManager() { }
	
	public PhysicsManager(PlayScreen playScreen) {
		this.entities = playScreen.entities;
		
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(gravity.cpy());
//        contactListener = new MyContactListener();
        
        btGhostPairCallback ghostCall = new btGhostPairCallback();
		dynamicsWorld.getBroadphase().getOverlappingPairCache().setInternalGhostPairCallback(ghostCall);
		
		renderableFinderOrig = new btBoxShape(new Vector3(16, 0.5f, 16));
		renderableFinder = (btConvexShape)renderableFinderOrig;

        allConstantObjects = new Array<>();

		immobileTerrain = new Array<>();
		immobileControllers = new Array<>();
		immobileControllables = new Array<>();
		immobileInteractives = new Array<>();

		mobileTerrain = new Array<>();
		mobileControllers = new Array<>();
		mobileControllables = new Array<>();
		mobileInteractives = new Array<>();
        
        physicsWorldBuilder = new PhysicsWorldBuilder(playScreen.game.manager, this);
        importNearbyChunks(playScreen.player);

        /*immobileTerrain = physicsWorldBuilder.immobileTerrain;
        immobileControllers = physicsWorldBuilder.immobileControllers;
        immobileControllables = physicsWorldBuilder.immobileControllables;
        immobileInteractives = physicsWorldBuilder.immobileInteractives;

        mobileTerrain = physicsWorldBuilder.mobileTerrain;
        mobileControllers = physicsWorldBuilder.mobileControllers;
        mobileControllables = physicsWorldBuilder.mobileControllables;
        mobileInteractives = physicsWorldBuilder.mobileInteractives;*/

		/*Vector2 chunkCoords = new Vector2(Math.floorDiv((int) player.pos.x, PhysicsWorldBuilder.chunkSize),
										   Math.floorDiv((int) player.pos.z, PhysicsWorldBuilder.chunkSize));
        loadConstantObjectData(player.getPlayerName(), (int)chunkCoords.x, (int)chunkCoords.y);*/

//        immobileTerrain = physicsWorldBuilder.loadStatics(dynamicsWorld);
        //allConstantObjects.get(0).getCollisionObject().setWorldTransform(allConstantObjects.get(0).getCollisionObject().getWorldTransform().setToRotation(new Vector3(0, 1, 0), 180));
        //immobileControllers = physicsWorldBuilder.loadControllers(dynamicsWorld);
        //immobileControllables = physicsWorldBuilder.loadControllables(dynamicsWorld);

		/*ObjectMap<Integer, Integer> immobileRenderingIndexes = physicsWorldBuilder.loadImmobileRenderingOrder((int)chunkCoords.x, (int)chunkCoords.y);
		for (int i = 0; i < immobileTerrain.size + immobileControllers.size + immobileControllables.size + immobileInteractives.size; i ++) {
			ImmobileObject obj = findImmobileObject(Util.getImmobileTerrainId(i + 1));
			obj.setRenderingIndex(immobileRenderingIndexes.get(i));
//			allConstantObjects.add(obj);
//			dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
		}*/

		/*ObjectMap<Integer, Integer> immobileRenderingIndexes = physicsWorldBuilder.loadImmobileRenderingOrder((int)chunkCoords.x, (int)chunkCoords.y);
		Iterator<ObjectMap.Entry<Integer, Integer>> iterator = immobileRenderingIndexes.entries().iterator();
		while (iterator.hasNext()) {
			ObjectMap.Entry<Integer, Integer> entry = iterator.next();
			findImmobileObjectNoPrefix(entry.key).setRenderingIndex(entry.value);
		}*/

		/*Array<Integer> terrainRenderingIndexes = physicsWorldBuilder.loadTerrainRenderingOrder();
		for (int i = 0; i < immobileTerrain.size; i ++) {
			ImmobileTerrain obj = immobileTerrain.get(i);
			obj.setRenderingIndex(terrainRenderingIndexes.get(i));
			allConstantObjects.add(obj);
			dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
		}

		Array<Integer> controllerRenderingIndexes = physicsWorldBuilder.loadControllerRenderingOrder();
		for (int i = 0; i < immobileControllers.size; i ++) {
			ImmobileController obj = immobileControllers.get(i);
			obj.setRenderingIndex(controllerRenderingIndexes.get(i));
			allConstantObjects.add(obj);
			dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
		}

		Array<Integer> controllableRenderingIndexes = physicsWorldBuilder.loadControllableRenderingOrder();
		for (int i = 0; i < immobileControllables.size; i ++) {
			ImmobileControllable obj = immobileControllables.get(i);
			obj.setRenderingIndex(controllableRenderingIndexes.get(i));
			allConstantObjects.add(obj);
			dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
		}

		Array<Integer> interactiveRenderingIndexes = physicsWorldBuilder.loadInteractiveRenderingOrder();
		for (int i = 0; i < immobileInteractives.size; i ++) {
			ImmobileInteractive obj = immobileInteractives.get(i);
			obj.setRenderingIndex(interactiveRenderingIndexes.get(i));
			allConstantObjects.add(obj);
			dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
		}*/

        dynamicsWorld.addRigidBody(playScreen.player.rigidBody, HITBOX_FLAG, ALL_FLAG);

        for (Entity entity: entities.allEntities) {
        	dynamicsWorld.addRigidBody(entity.rigidBody, HITBOX_FLAG, ALL_FLAG);
        }
        
        for (Particle particle: playScreen.particleEngine.particles) {
        	dynamicsWorld.addRigidBody(particle.rigidBody, PARTICLE_FLAG, TERRAIN_FLAG);
        }
        
        for (Projectile projectile: playScreen.projectileManager.projectiles) {
        	projectile.addToDynamicsWorld(dynamicsWorld, PROJECTILE_FLAG, ALL_FLAG);
        }

        for (DroppedItem droppedItem: playScreen.droppedItemManager.droppedItems) {
        	dynamicsWorld.addRigidBody(droppedItem.rigidBody, DROPPED_ITEM_FLAG, ALL_FLAG ^ DROPPED_ITEM_FLAG);
		}
        
        /*for (StaticObject obj: immobileTerrain) {
        	allConstantObjects.add(obj);
        	dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
        }*/
        /*
        for (ControllerObject obj: immobileControllers) {
        	immobileTerrain.add(obj);
        	dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
        }
        for (ControllableObject obj: immobileControllables) {
        	immobileTerrain.add(obj);
        	dynamicsWorld.addCollisionObject(obj.getCollisionObject(), TERRAIN_FLAG, ALL_FLAG);
        }
        */
        
        hitIds = new Array<>();
	}

	/**
	 * Creates initial files.
	 * @param name the name of the player.
	 */
	public static void createInitialFiles(String name) {
		Gdx.files.local("saves/" + name + "/world/chunks/start").mkdirs();
		FileHandle fileHandle = new FileHandle("saves/" + name + "/world/chunks/start/constObjData.txt");
		fileHandle.writeString("", false);
	}
	
	public void update(float delta, PlayScreen playScreen) {
		/*for (Entity entity: this.entities.allEntities) {
			if (!entities.allEntities.contains(entity, false)) {
				this.entities.allEntities.removeValue(entity, false);
				dynamicsWorld.removeRigidBody(entity.rigidBody);
			}
		}
		for (Entity entity: entities.allEntities) {
			if (!this.entities.allEntities.contains(entity, false)) {
				this.entities.allEntities.add(entity);
				dynamicsWorld.addRigidBody(entity.rigidBody, HITBOX_FLAG, ALL_FLAG);
			}
		}*/

		/*for (Entity entity: entities.allEntities) {
//			System.out.println(entity.additionalMovementVector);
//			entity.rigidBody.setLinearVelocity(entity.rigidBody.getLinearVelocity().add(entity.additionalMovementVector));
		}*/
		
		final float newDelta = Math.min(1f/30f, delta);
		
		dynamicsWorld.stepSimulation(newDelta, 5, 1/60f);
		
		collisionLogic(playScreen);
		
		findRenderable(playScreen);

		/*for (Entity entity: entities.allEntities) {
//			entity.rigidBody.setLinearVelocity(entity.rigidBody.getLinearVelocity().sub(entity.additionalMovementVector));
		}*/
	}
	
	private ConstantObject findStaticObject(int physicsId) {
		int id = Util.getId(physicsId);
		for (ConstantObject obj: allConstantObjects) {
			if (obj.id == id) {
				return obj;
			}
		}
		return null;
	}

	private ConstantObject findConstantObject(int physicsId) {
		if (isImmobileTerrain(physicsId)) {
			for (ImmobileTerrain obj: immobileTerrain) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isImmobileController(physicsId)) {
			for (ImmobileController obj: immobileControllers) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isImmobileControllable(physicsId)) {
			for (ImmobileControllable obj: immobileControllables) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isImmobileInteractive(physicsId)) {
			for (ImmobileInteractive obj: immobileInteractives) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isMobileTerrain(physicsId)) {
			for (MobileTerrain obj: mobileTerrain) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isMobileController(physicsId)) {
			for (MobileController obj: mobileControllers) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isMobileControllable(physicsId)) {
			for (MobileControllable obj: mobileControllables) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isMobileInteractive(physicsId)) {
			for (MobileInteractive obj: mobileInteractives) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		}
		return null;
	}

	private ImmobileObject findImmobileObject(int physicsId) {
		if (isImmobileTerrain(physicsId)) {
			for (ImmobileTerrain obj: immobileTerrain) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isImmobileController(physicsId)) {
			for (ImmobileController obj: immobileControllers) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isImmobileControllable(physicsId)) {
			for (ImmobileControllable obj: immobileControllables) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isImmobileInteractive(physicsId)) {
			for (ImmobileInteractive obj: immobileInteractives) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		}
		return null;
	}

	/**
	 * Finds the immobile object with the given id; the normal id, not the physics id.
	 */
	private ImmobileObject findImmobileObjectNoPrefix(int id) {
		for (ImmobileTerrain obj: immobileTerrain) {
			if (obj.physicsId == id) {
				return obj;
			}
		}
		for (ImmobileController obj: immobileControllers) {
			if (obj.physicsId == id) {
				return obj;
			}
		}
		for (ImmobileControllable obj: immobileControllables) {
			if (obj.physicsId == id) {
				return obj;
			}
		}
		for (ImmobileInteractive obj: immobileInteractives) {
			if (obj.physicsId == id) {
				return obj;
			}
		}
		return null;
	}

	private MobileObject findMobileObject(int physicsId) {
		if (isMobileTerrain(physicsId)) {
			for (MobileTerrain obj: mobileTerrain) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isMobileController(physicsId)) {
			for (MobileController obj: mobileControllers) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isMobileControllable(physicsId)) {
			for (MobileControllable obj: mobileControllables) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		} else if (isMobileInteractive(physicsId)) {
			for (MobileInteractive obj: mobileInteractives) {
				if (obj.physicsId == physicsId) {
					return obj;
				}
			}
		}
		return null;
	}
	
	/*
	 * Executes gameplay logic based on narrow phase collision between objects.
	 */
	private void collisionLogic(PlayScreen playScreen) {
		dynamicsWorld.performDiscreteCollisionDetection();
		
		int numManifolds = dynamicsWorld.getDispatcher().getNumManifolds();
		for (int i = 0; i < numManifolds; i ++) {
			btPersistentManifold contactManifold = dynamicsWorld.getDispatcher().getManifoldByIndexInternal(i);
			btCollisionObject objA = contactManifold.getBody0(); // The first body involved with the collision
			btCollisionObject objB = contactManifold.getBody1(); // The second body involved with the collision
			
			if (contactManifold.getNumContacts() > 0) { // If the collision is true, and not just aabb
				testProjectileEntityCollision(objA, objB, playScreen);
				testEntityStaticObjectCollision(objA, objB, playScreen);
				testProjectileProjectileCollision(objA, objB, playScreen);
				testEntityDroppedItemCollision(objA, objB, playScreen);
			} else { // If the collision is just aabb
				
			}
			
			//addPlayerManifolds(objA, objB, contactManifold);
		}
	}
	
	/*
	 * Test if the 'collidingWith...' flags of this entity can be updated.
	 */
	private void testEntityStaticObjectCollision(btCollisionObject objA, btCollisionObject objB, PlayScreen playScreen) {
		if (isEntityOrPlayer(objA.getUserValue()) && isConstObject(objB.getUserValue())) {
			Entity entity = playScreen.entities.getEntity(Util.getId(objA.getUserValue()), playScreen.player);
			ConstantObject obj = findStaticObject(objB.getUserValue());
			for (Tag tag: obj.getTags()) {
				if (tag == Tag.WALKABLE) {
					entity.setCollidingWithWalkable(true);
					/*if (obj.collisionObject.isKinematicObject()) {
						entity.setParentVelocity(((KinematicObject)obj).getLinearVelocity());
					}*/
					if (isMobileObject(obj.physicsId)) {
						entity.setParentVelocity(findMobileObject(obj.physicsId).getLinearVelocity());
					}
				} else if (tag == Tag.CLIMBABLE) {
					entity.setCollidingWithClimbable(true);
				}
			}
		} else if (isEntityOrPlayer(objB.getUserValue()) && isConstObject(objA.getUserValue())) {
			testEntityStaticObjectCollision(objB, objA, playScreen);
		}
	}
	
	/*
	 * Test if a projectile which is destroyed upon impact has hit an entity.
	 */
	private void testProjectileEntityCollision(btCollisionObject objA, btCollisionObject objB, PlayScreen playScreen) {
		if (isProjectile(objA.getUserValue()) && isEntityOrPlayer(objB.getUserValue())) {
			Projectile projectile = playScreen.projectileManager.get(Util.getId(objA.getUserValue()));
			Entity entity = playScreen.entities.getEntity(Util.getId(objB.getUserValue()), playScreen.player);
			projectile.onHitEntity(entity, playScreen);
			// Execute the logic for the projectile colliding with the entity
		} else if (isProjectile(objB.getUserValue()) && isEntityOrPlayer(objA.getUserValue())) {
			testProjectileEntityCollision(objB, objA, playScreen);
		}
	}

	private void testProjectileProjectileCollision(btCollisionObject objA, btCollisionObject objB, PlayScreen playScreen) {
		if (isProjectile(objA.getUserValue()) && isProjectile(objB.getUserValue())) {
			Projectile projectileA = playScreen.projectileManager.get(Util.getId(objA.getUserValue()));
			Projectile projectileB = playScreen.projectileManager.get(Util.getId(objB.getUserValue()));
			projectileA.onHitProjectile(projectileB, playScreen);
			projectileB.onHitProjectile(projectileA, playScreen);
		}
	}

	private void testEntityDroppedItemCollision(btCollisionObject objA, btCollisionObject objB, PlayScreen playScreen) {
		if (isEntityOrPlayer(objA.getUserValue()) && isDroppedItem(objB.getUserValue())) {
			Entity entity = playScreen.entities.getEntity(Util.getId(objA.getUserValue()), playScreen.player);
			DroppedItem droppedItem = playScreen.droppedItemManager.get(Util.getId(objB.getUserValue()));
			droppedItem.pickedUpBy(entity, playScreen.physicsManager.getDynamicsWorld(), playScreen.droppedItemManager);
		} else if (isEntityOrPlayer(objB.getUserValue()) && isDroppedItem(objA.getUserValue())) {
			testEntityDroppedItemCollision(objB, objA, playScreen);
		}
	}
	
	/*
	private void addPlayerManifolds(btCollisionObject objA, btCollisionObject objB, btPersistentManifold manifold) {
		Vector3 vector = new Vector3();
		if (isPlayer(objA.getUserValue())) {
			playerManifolds.add(manifold);
			manifold.getContactPointConst(0).getPositionWorldOnA(vector);
			System.out.println(vector + " " + manifold.getNumContacts());
			objA.setWorldTransform(objA.getWorldTransform().translate(0, 10, 0));
			dynamicsWorld.performDiscreteCollisionDetection();
			manifold.getContactPointConst(0).getPositionWorldOnA(vector);
			System.out.println(vector + " " + manifold.getNumContacts());
			objA.setWorldTransform(objA.getWorldTransform().translate(0, -10, 0));
			dynamicsWorld.performDiscreteCollisionDetection();
		} else if (isPlayer(objB.getUserValue())) {
			playerManifolds.add(manifold);
			manifold.getContactPointConst(0).getPositionWorldOnB(vector);
			System.out.println(vector + " " + manifold.getNumContacts());
			objB.setWorldTransform(objB.getWorldTransform().translate(0, 10, 0));
			dynamicsWorld.performDiscreteCollisionDetection();
			manifold.getContactPointConst(0).getPositionWorldOnB(vector);
			System.out.println(vector + " " + manifold.getNumContacts());
			objB.setWorldTransform(objB.getWorldTransform().translate(0, -10, 0));
			dynamicsWorld.performDiscreteCollisionDetection();
		}
	}
	*/
	
	public Pair<Integer, Vector3> rayTestFirst(float x, float y, float z) {
		/*
		btGhostPairCallback ghostCall = new btGhostPairCallback();
		dynamicsWorld.getBroadphase().getOverlappingPairCache().setInternalGhostPairCallback(ghostCall);
		
		btGhostObject ghostObj = new btGhostObject();
		btCollisionShape shape = new btSphereShape(0.01f);
		ghostObj.setCollisionShape(shape);
		
		ghostObj.setWorldTransform(ghostObj.getWorldTransform().setTranslation(x + highestPoint - y, highestPoint, z - (highestPoint - y)));
		ghostObj.setCollisionFlags(ghostObj.getCollisionFlags() | CollisionFlags.CF_NO_CONTACT_RESPONSE);
		//ghostObj.setActivationState(Collision.DISABLE_DEACTIVATION);
		dynamicsWorld.addCollisionObject(ghostObj);
		//dynamicsWorld.addCollisionObject(ghostObj, CollisionFilterGroups.SensorTrigger, CollisionFilterGroups.AllFilter & CollisionFilterGroups.SensorTrigger);
		while (ghostObj.getWorldTransform().getTranslation(emptyVector).y > lowestPoint) {
			//System.out.println(ghostObj.getWorldTransform().getTranslation(new Vector3()));
			if (ghostObj.getNumOverlappingObjects() > 0) {
				int id = ghostObj.getOverlappingObject(0).getUserValue();
				dynamicsWorld.removeCollisionObject(ghostObj);
				return id;
			}
			ghostObj.setWorldTransform(ghostObj.getWorldTransform().translate(-0.01f, -0.01f, 0.01f));
			dynamicsWorld.performDiscreteCollisionDetection();
			//dynamicsWorld.updateAabbs();
			// The above also works in place of performDiscreteCollisionDetection()
		}
		dynamicsWorld.removeCollisionObject(ghostObj);
		*/
		ClosestRayResultCallback rayTest = new ClosestRayResultCallback(Vector3.Zero, Vector3.Zero);
		Vector3 rayFrom = new Vector3(x + highestPoint - y, highestPoint, z - (highestPoint - y));
		Vector3 rayTo = new Vector3(x - (y - lowestPoint), lowestPoint, z + y - lowestPoint);
		rayTest.setClosestHitFraction(1f);
		rayTest.setRayFromWorld(rayFrom);
		rayTest.setRayToWorld(rayTo);
		
		dynamicsWorld.rayTest(rayFrom, rayTo, rayTest);
		
		if (rayTest.hasHit()) {
			Vector3 vector = new Vector3();
			rayTest.getHitPointWorld(vector);
			return new Pair<>(rayTest.getCollisionObject().getUserValue(), vector);
		} else {
			return null;
		}
	}
	
	public int rayTestFirst(Vector3 pos) {
		ClosestRayResultCallback rayTest = new ClosestRayResultCallback(Vector3.Zero, Vector3.Zero);
		Vector3 rayFrom = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
		Vector3 rayTo = new Vector3(pos.x - (pos.y - lowestPoint), lowestPoint, pos.z + pos.y - lowestPoint);
		rayTest.setClosestHitFraction(1f);
		
		dynamicsWorld.rayTest(rayFrom, rayTo, rayTest);
		if (rayTest.hasHit()) {
			return rayTest.getCollisionObject().getUserValue();
		} else {
			return -1;
		}
	}
	
	public Array<Integer> rayTestAll(float x, float y, float z, TestMode mode) {
		hitIds.clear();
		
		AllHitsRayResultCallback rayTest = new AllHitsRayResultCallback(Vector3.Zero, Vector3.Zero);
		
		Vector3 rayFrom;
		Vector3 rayTo;
		switch (mode) {
			case TOP_TO_BOTTOM:
				rayFrom = new Vector3(x + highestPoint - y, highestPoint, z - (highestPoint - y));
				rayTo = new Vector3(x - (y - lowestPoint), lowestPoint, z + y - lowestPoint);
				break;
			case UPWARDS:
				rayFrom = new Vector3(x, y, z);
				rayTo = new Vector3(x + highestPoint - y, highestPoint, z - (highestPoint - y));
				break;
			case DOWNWARDS:
				rayFrom = new Vector3(x, y, z);
				rayTo = new Vector3(x - (y - lowestPoint), lowestPoint, z + y - lowestPoint);
				break;
			default:
				rayFrom = null;
				rayTo = null;
		}
		
		rayTest.setClosestHitFraction(1f);

		dynamicsWorld.rayTest(rayFrom, rayTo, rayTest);
		
		for (int i = 0; i < rayTest.getCollisionObjects().size(); i ++) {
			hitIds.add(rayTest.getCollisionObjects().atConst(i).getUserValue());
		}
		return hitIds;
	}
	
	public Array<Integer> rayTestAll(Vector3 pos, TestMode mode) {
		hitIds.clear();
		
		AllHitsRayResultCallback rayTest = new AllHitsRayResultCallback(Vector3.Zero, Vector3.Zero);
		
		Vector3 rayFrom;
		Vector3 rayTo;
		
		switch (mode) {
			case TOP_TO_BOTTOM:
				rayFrom = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
				rayTo = new Vector3(pos.x - (pos.y - lowestPoint), lowestPoint, pos.z + pos.y - lowestPoint);
				break;
			case UPWARDS:
				rayFrom = pos.cpy();
				rayTo = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
				break;
			case DOWNWARDS:
				rayFrom = pos.cpy();
				rayTo = new Vector3(pos.x - (pos.y - lowestPoint), lowestPoint, pos.z + pos.y - lowestPoint);
				break;
			default:
				rayFrom = null;
				rayTo = null;
		}
		
		rayTest.setClosestHitFraction(1f);
		rayTest.setRayFromWorld(rayFrom);
		rayTest.setRayToWorld(rayTo);

		dynamicsWorld.rayTest(rayFrom, rayTo, rayTest);
		
		for (int i = 0; i < rayTest.getCollisionObjects().size(); i ++) {
			hitIds.add(rayTest.getCollisionObjects().atConst(i).getUserValue());
		}
		
		return hitIds;
	}
	
	public int convexSweepTestFirst(btCollisionShape collisionShape, Vector3 pos) {
		btConvexShape convexShape = (btConvexShape)collisionShape;
		
		Vector3 vectorFrom = new Vector3(pos.x, pos.y, pos.z);
		Vector3 vectorTo = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
		ClosestConvexResultCallback sweepTest = new ClosestConvexResultCallback(vectorFrom, vectorTo);
		Matrix4 matrixFrom = new Matrix4();
		matrixFrom.setTranslation(vectorFrom);
		Matrix4 matrixTo = new Matrix4();
		matrixTo.setTranslation(vectorTo);
		sweepTest.setClosestHitFraction(1f);
		
		dynamicsWorld.convexSweepTest(convexShape, matrixFrom, matrixTo, sweepTest);
		collisionShape.dispose();
		convexShape.dispose();
		if (sweepTest.hasHit()) {
			return sweepTest.getHitCollisionObject().getUserValue();
		} else {
			return -1;
		}
	}
	
	public int convexSweepTestFirst(btCollisionObject collisionObject, Vector3 pos) {
		btConvexShape convexShape = (btConvexShape)collisionObject.getCollisionShape();
		
		Vector3 vectorFrom = pos.cpy();
		Vector3 vectorTo = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
		ClosestNotMeConvexResultCallback sweepTest = new ClosestNotMeConvexResultCallback(collisionObject, vectorFrom, vectorTo);
		Matrix4 matrixFrom = new Matrix4();
		matrixFrom.setTranslation(vectorFrom);
		Matrix4 matrixTo = new Matrix4();
		matrixTo.setTranslation(vectorTo);
		sweepTest.setClosestHitFraction(1f);
		
		dynamicsWorld.convexSweepTest(convexShape, matrixFrom, matrixTo, sweepTest);
		
		if (sweepTest.hasHit()) {
			return sweepTest.getHitCollisionObject().getUserValue();
		} else {
			return -1;
		}
	}
	
	public Array<Integer> OLDconvexSweepTestAll(btCollisionObject collisionObject, Vector3 pos) {
		/*
		Array<Integer> ids = new Array<Integer>();
		btConvexShape convexShape = (btConvexShape)collisionObject.getCollisionShape();
		
		Vector3 vectorFrom = new Vector3(pos.x, pos.y, pos.z);
		Vector3 vectorTo = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
		ClosestNotMeConvexResultCallback sweepTest = new ClosestNotMeConvexResultCallback(collisionObject, vectorFrom, vectorTo);
		Matrix4 matrixFrom = new Matrix4();
		matrixFrom.setTranslation(vectorFrom);
		Matrix4 matrixTo = new Matrix4();
		matrixTo.setTranslation(vectorTo);
		sweepTest.setClosestHitFraction(1f);
 		dynamicsWorld.convexSweepTest(convexShape, matrixFrom, matrixTo, sweepTest);
 		
 		if (sweepTest.hasHit()) {
 			ids.add(sweepTest.getHitCollisionObject().getUserValue());
			int id = 0;
			while (id != -1) {
				sweepTest.getHitCollisionObject().getWorldTransform().getTranslation(vectorFrom);
				sweepTest = new ClosestNotMeConvexResultCallback(sweepTest.getHitCollisionObject(), vectorFrom, vectorTo);
				matrixFrom.setTranslation(vectorFrom);
				matrixTo.setTranslation(vectorTo);
				sweepTest.setClosestHitFraction(1f);
		 		dynamicsWorld.convexSweepTest(convexShape, matrixFrom, matrixTo, sweepTest);
		 		if (sweepTest.hasHit()) {
		 			id = sweepTest.getHitCollisionObject().getUserValue();
		 			ids.add(sweepTest.getHitCollisionObject().getUserValue());
		 		} else {
		 			id = -1;
		 		}
			}
			return ids;
		} else {
			return ids;
		}
		*/
 		hitIds.clear();
 		Vector3 vectorFrom = pos.cpy();
		//Vector3 vectorTo = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
		
		//btGhostPairCallback ghostCall = new btGhostPairCallback();
		//dynamicsWorld.getBroadphase().getOverlappingPairCache().setInternalGhostPairCallback(ghostCall);
		
		btGhostObject ghostObj = new btGhostObject();
		ghostObj.setCollisionShape(collisionObject.getCollisionShape());
		
		ghostObj.setUserValue(50000);
		ghostObj.setWorldTransform(ghostObj.getWorldTransform().setTranslation(vectorFrom));
		ghostObj.setCollisionFlags(ghostObj.getCollisionFlags() | CollisionFlags.CF_NO_CONTACT_RESPONSE);
		//ghostObj.setWorldTransform(ghostObj.getWorldTransform().translate(0.2f, 0.2f, -0.2f));
		dynamicsWorld.addCollisionObject(ghostObj);
		
		if (isEntityOrPlayer(collisionObject.getUserValue())) {
			//System.out.println(123);
			// Finding out where the ghostObj should start (making sure it overlaps nothing but the player)
			while (ghostObj.getNumOverlappingObjects() > 1) { // While there is more than only 1 object its overlapping (that 1 being the player)
				for (int i = 0; i < ghostObj.getNumOverlappingObjects(); i ++) {
					//System.out.println(ghostObj.getOverlappingObject(i).getUserValue());
					//System.out.println(ghostObj.getOverlappingObject(i).getWorldTransform().getTranslation(new Vector3()));
				}
				ghostObj.setWorldTransform(ghostObj.getWorldTransform().translate(0.2f, 0.2f, -0.2f));
				dynamicsWorld.performDiscreteCollisionDetection();
				//dynamicsWorld.updateAabbs();
				//System.out.println(ghostObj.getWorldTransform().getTranslation(new Vector3()));
				//dynamicsWorld.updateSingleAabb(ghostObj);
				//System.out.println(1234567890);
			}
		}
		
		while (ghostObj.getWorldTransform().getTranslation(new Vector3()).y <= highestPoint) {
			ghostObj.setWorldTransform(ghostObj.getWorldTransform().translate(0.2f, 0.2f, -0.2f));
			//dynamicsWorld.updateAabbs();
			dynamicsWorld.performDiscreteCollisionDetection();
			for (int i = 0; i < ghostObj.getNumOverlappingObjects(); i ++) {
				if (!hitIds.contains(ghostObj.getOverlappingObject(i).getUserValue(), false) &&
					ghostObj.getOverlappingObject(i).getUserValue() != collisionObject.getUserValue()) {
					hitIds.add(ghostObj.getOverlappingObject(i).getUserValue());
				}
			}
			//dynamicsWorld.updateAabbs();
			// The above also works in place of performDiscreteCollisionDetection()
		}
		dynamicsWorld.removeCollisionObject(ghostObj);
		ghostObj.dispose();
		return hitIds;
	}
	
	public Array<Integer> convexSweepTestAll(btCollisionObject collisionObject, Vector3 pos) {
		hitIds.clear();
		
		btConvexShape convexShape = (btConvexShape)collisionObject.getCollisionShape();
		
		Vector3 vectorFrom = pos.cpy().add(0.01f, 0.01f, -0.01f);
		Vector3 vectorTo = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
		
		AllHitsNotMeConvexResultCallback sweepTest = new AllHitsNotMeConvexResultCallback(collisionObject, hitIds);
		Matrix4 matrixFrom = new Matrix4();
		matrixFrom.setTranslation(vectorFrom);
		Matrix4 matrixTo = new Matrix4();
		matrixTo.setTranslation(vectorTo);

		sweepTest.setClosestHitFraction(1f);

		dynamicsWorld.convexSweepTest(convexShape, matrixFrom, matrixTo, sweepTest);

		sweepTest.dispose();
		return hitIds;
	}
	
	private Array<Integer> convexSweepTestAll(btConvexShape shape, Vector3 pos, TestMode mode) {
		hitIds.clear();
		
		Vector3 vectorFrom;
		Vector3 vectorTo;
		
		switch (mode) {
			case TOP_TO_BOTTOM:
				vectorFrom = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
				vectorTo = new Vector3(pos.x - (pos.y - lowestPoint), lowestPoint, pos.z + pos.y - lowestPoint);
				break;
			case UPWARDS:
				vectorFrom = pos.cpy();
				vectorTo = new Vector3(pos.x + highestPoint - pos.y, highestPoint, pos.z - (highestPoint - pos.y));
				break;
			case DOWNWARDS:
				vectorFrom = pos.cpy();
				vectorTo = new Vector3(pos.x - (pos.y - lowestPoint), lowestPoint, pos.z + pos.y - lowestPoint);
				break;
			default:
				vectorFrom = null;
				vectorTo = null;
		}
		
		AllHitsConvexResultCallback sweepTest = new AllHitsConvexResultCallback(hitIds);
		// Set the group and mask so that it can collide with, for example, particles.
		sweepTest.setCollisionFilterGroup(PhysicsManager.ALL_FLAG);
		sweepTest.setCollisionFilterMask(PhysicsManager.ALL_FLAG);
		
		Matrix4 matrixFrom = new Matrix4();
		matrixFrom.setTranslation(vectorFrom);
		Matrix4 matrixTo = new Matrix4();
		matrixTo.setTranslation(vectorTo);
		sweepTest.setClosestHitFraction(1f);
		//sweepTest.setMe(collisionObject);
		
		dynamicsWorld.convexSweepTest(shape, matrixFrom, matrixTo, sweepTest);

		sweepTest.dispose();
		return hitIds;
	}

	public Array<Integer> convexSweepTestAll(btConvexShape shape, Vector3 startPos, Vector3 endPos) {
		hitIds.clear();

		AllHitsConvexResultCallback sweepTest = new AllHitsConvexResultCallback(hitIds);
		// Set the group and mask so that it can collide with, for example, particles.
		sweepTest.setCollisionFilterGroup(PhysicsManager.ALL_FLAG);
		sweepTest.setCollisionFilterMask(PhysicsManager.ALL_FLAG);

		Matrix4 matrixFrom = new Matrix4();
		matrixFrom.setTranslation(startPos.cpy());
		Matrix4 matrixTo = new Matrix4();
		matrixTo.setTranslation(endPos.cpy());
		sweepTest.setClosestHitFraction(1f);
		//sweepTest.setMe(collisionObject);

		dynamicsWorld.convexSweepTest(shape, matrixFrom, matrixTo, sweepTest);

		sweepTest.dispose();
		return hitIds;
	}

	public Array<Integer> convexSweepTestAll(btCollisionShape shape, Vector3 startPos, Vector3 endPos) {
		hitIds.clear();

		AllHitsConvexResultCallback sweepTest = new AllHitsConvexResultCallback(hitIds);
		// Set the group and mask so that it can collide with, for example, particles.
		sweepTest.setCollisionFilterGroup(PhysicsManager.ALL_FLAG);
		sweepTest.setCollisionFilterMask(PhysicsManager.ALL_FLAG);

		Matrix4 matrixFrom = new Matrix4();
		matrixFrom.setTranslation(startPos.cpy());
		Matrix4 matrixTo = new Matrix4();
		matrixTo.setTranslation(endPos.cpy());
		sweepTest.setClosestHitFraction(1f);
		//sweepTest.setMe(collisionObject);

		dynamicsWorld.convexSweepTest((btConvexShape)shape, matrixFrom, matrixTo, sweepTest);

		sweepTest.dispose();
		return hitIds;
	}
	
	public Array<Integer> testCollision(btCollisionShape shape, Vector3 pos) {
		btGhostObject ghostObj = new btGhostObject();
		ghostObj.setCollisionShape(shape);
		
		ghostObj.setWorldTransform(ghostObj.getWorldTransform().setTranslation(pos));
		ghostObj.setUserValue(50000);
		ghostObj.setCollisionFlags(ghostObj.getCollisionFlags() | CollisionFlags.CF_NO_CONTACT_RESPONSE);
		dynamicsWorld.addCollisionObject(ghostObj);
		dynamicsWorld.performDiscreteCollisionDetection(); // Update the collisions (as we've just added the ghost object)
		if (ghostObj.getNumOverlappingObjects() > 0) {
			Array<Integer> objs = new Array<>();
			for (int i = 0; i < ghostObj.getNumOverlappingObjects(); i ++) {
				objs.add(ghostObj.getOverlappingObject(i).getUserValue());
			}
			dynamicsWorld.removeCollisionObject(ghostObj);
			return objs;
		} else {
			dynamicsWorld.removeCollisionObject(ghostObj);
			return null;
		}
	}
	
	public void dispose() {
		for (ConstantObject obj: allConstantObjects) {
			obj.dispose();
		}
		allConstantObjects.clear();

		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();
//		contactListener.dispose();
		physicsWorldBuilder.importer.dispose();
		
		renderableFinder.dispose();
		renderableFinderOrig.dispose();
	}

	public btDynamicsWorld getDynamicsWorld() {
		return dynamicsWorld;
	}

}
