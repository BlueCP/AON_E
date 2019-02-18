package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.entityactions.EntityAction;
import com.mygdx.game.items.Equipped;
import com.mygdx.game.items.Inventory;
import com.mygdx.game.items.Weapon;
import com.mygdx.game.physics.Hitboxes;
import com.mygdx.game.physics.WorldObject;
import com.mygdx.game.rendering.IsometricRenderer;
import com.mygdx.game.rendering.IsometricRenderer.Visibility;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.skills.ActiveSkill;
import com.mygdx.game.skills.BasicAttack;
import com.mygdx.game.skills.NullBasicAttack;
import com.mygdx.game.statuseffects.*;
import com.mygdx.game.utils.Util;

import java.util.HashMap;

public abstract class Entity extends WorldObject {
	
	static int targetTime; // The number of turns that an entity will aggressively follow another entity for
	
	protected String name;
	//protected int id;
	
	float life;
	float spirit;
	public Vector3 pos;

//	private Array<StackEffect> stackEffects = new Array<>();
//	private Array<ProcEffect> procEffects = new Array<>();

	public BurningEffect burningEffect;
	public StunnedEffect stunnedEffect;
	public SlowedEffect slowedEffect;
	public RootedEffect rootedEffect;
	public ChilledEffect chilledEffect;
	public FrozenEffect frozenEffect;

//	private Array<Effect> activeEffects = new Array<>();
	
	protected float realPhysDamage;
	protected float realMagDamage;
	
	float maxLife;
	float maxSpirit;
	float basePhysDmg;
	private float baseMagDmg;
	float baseWalkSpeed;
	private int xpLevel;
	private int xpProg;
	private int skillPoints;
	
	protected Inventory inventory = new Inventory();
	private Equipped equipped  = new Equipped();
	private boolean damagedByPlayer = false;
	private Weapon lastHitWith = null;
	private boolean followingPlayer;
	private boolean attacking = false;
	private int targetEntity = -1; // The targeted entity's id
	private Array<Integer> entitiesThatHit = new Array<>(); // An array of all the ids of entities that hit this entity (not necessarily damaged, could also be non-damage harmful effects).
	Integer lastHitBy = -1; // The id of the entity that last hit this entity.

	private boolean canWalk;
	private float realWalkSpeed;
	private boolean canRotate;
	private boolean canJump;
	
	private boolean collidingWithWalkable = false;
	private boolean collidingWithClimbable = false;
	
	protected Behaviour behaviour;
	protected Nature nature;
	private Facing facing = Facing.N180_0;
	private AnimationType animationType = AnimationType.STAND;
	private AnimationType prevAnimationType;

	protected BasicAttack basicAttack;
	protected Array<ActiveSkill> skills;
	
	private String animationState;
	private String prevAnimationState;
	
	private HashMap<Entity, Integer> offensiveEnemies = new HashMap<>(); // Enemies which have recently attacked this entity
	
	private Vector3 movementVector = new Vector3();
//	public Vector3 additionalMovementVector = new Vector3(); // The additional movement vector exists only for the one tick: it is added, then removed, from the velocity of the object.
	private Vector3 parentVelocity = new Vector3(); // The velocity of the object the player is walking on, if any
	
	//private Vector3 walkImpulse = new Vector3();
	//private Vector3 prevWalkImpulse = new Vector3();
	
	Matrix4 rigidBodyMatrix;
	private Vector3 linearVelocity;
	
	public btRigidBody rigidBody;
	float stateTime = 0f;
	
	public Queue<Array<EntityAction>> actions;
	private Vector3 targetLocation = new Vector3(); // The location that the player has targeted in the world (by clicking there), or that this entity is targeting.
	
	private boolean movementVectorWasChanged = false;
	
	/*
	 * Sets the linear velocity of the body to the movement vector, and clears the movement vector after.
	 */
	private void moveByMovementVector() {
		//Vector3 lV = rigidBody.getLinearVelocity();
		//float x = lV.x; float y = lV.y; float z = lV.z;
		//rigidBody.setLinearVelocity(new Vector3(lV.x + movementVector.x, lV.y + movementVector.y, lV.z + movementVector.z));
		/*
		if (lV.x > movementVector.x && Math.abs(lV.x - movementVector.x) > movementVector.x) {
			lV.x -= movementVector.x;
		} else if (lV.x < movementVector.x && Math.abs(lV.x - movementVector.x) > movementVector.x) {
			lV.x += movementVector.x;
		} else {
			lV.x = movementVector.x;
		}
		
		if (lV.y > movementVector.y && Math.abs(lV.y - movementVector.y) > movementVector.y) {
			lV.y -= movementVector.y;
		} else if (lV.y < movementVector.y && Math.abs(lV.y - movementVector.y) > movementVector.y) {
			lV.y += movementVector.y;
		} else {
			lV.y = movementVector.y;
		}
		
		if (lV.z > movementVector.z && Math.abs(lV.z - movementVector.z) > movementVector.z) {
			lV.z -= movementVector.z;
		} else if (lV.z < movementVector.z && Math.abs(lV.z - movementVector.z) > movementVector.z) {
			lV.z += movementVector.z;
		} else {
			lV.z = movementVector.z;
		}
		rigidBody.setLinearVelocity(lV);
		*/
		
		if (movementVectorWasChanged) {
			applyMovementChanges();
			if (rootedEffect.isActive() || stunnedEffect.isActive() || frozenEffect.isActive()) {
				rigidBody.setLinearVelocity(parentVelocity);
			} else {
				rigidBody.setLinearVelocity(movementVector.add(parentVelocity));
			}
		}
		
		movementVectorWasChanged = false;
		movementVector.setZero();
//		additionalMovementVector.setZero();
		parentVelocity.setZero();
		//additionalMovementVector.setZero();
	}
	
	/*
	 * This method exists so that movementVectorWasChanged can be updated automatically.
	 * Use this method instead of directly interacting with the vector to update movementVectorHasChanged.
	 */
	private void setMovementVector(float x, float y, float z) {
		movementVector.set(x, y, z);
		movementVectorWasChanged = true;
	}
	
	/*
	 * This method exists so that movementVectorWasChanged can be updated automatically.
	 * Use this method instead of directly interacting with the vector to update movementVectorHasChanged.
	 */
	public void addMovementVector(float x, float y, float z) {
		//additionalMovementVector.add(x, y, z);
		movementVector.add(x, y, z);
		movementVectorWasChanged = true;
	}

	/*
	 * This method exists so that movementVectorWasChanged can be updated automatically.
	 * Use this method instead of directly interacting with the vector to update movementVectorHasChanged.
	 */
	public void addMovementVector(Vector3 vector) {
		//additionalMovementVector.add(x, y, z);
		movementVector.add(vector);
		movementVectorWasChanged = true;
	}

	/*public void addAdditionalMovementVector(Vector3 vector) {
		additionalMovementVector.add(vector);
	}

	public void addAdditionalMovementVector(float x, float y, float z) {
		additionalMovementVector.add(x, y, z);
	}*/

	/**
	 * Apply effects such as slows.
	 */
	private void applyMovementChanges() {
		movementVector.scl(1 - (slowedEffect.movementDampening() + chilledEffect.movementDampening()));
	}
	
	void generateId(Entities entities) {
		if (entities.idPool.size > 0) {
			id = Integer.MAX_VALUE; // Set it to the max value so that we can't accidentally be lower than the lowest int in the pool
			for (Integer int0: entities.idPool) {
				if (int0 < id) {
					id = int0;
				}
			}
			entities.idPool.removeValue(id, true);
		} else {
			id = 0;
			for (int i = 0; i < entities.allEntities.size; i ++) {
				if (entities.allEntities.get(i).id > id) {
					id = entities.allEntities.get(i).id;
				}
			}
			id ++; // Since highest is already the id for an actual entity
			while (entities.pendingIds.containsKey(id)) {
				id ++;
			}
		}
		physicsId = Util.getEntityId(id);
	}
	
	@Override
	public void updateWorldObject(IsometricRenderer renderer) {
		//physicsId = rigidBody.getUserValue();
		//texture = getTexture();
		Vector2 coords = renderer.cartesianToScreen(pos.x, pos.y, pos.z);
		renderPos = new Vector2(coords.x - getTexture().getRegionWidth()/2f, coords.y - getTexture().getRegionHeight()/2f);
		visibility = Visibility.VISIBLE;
	}
	
	HashMap<Entity, Integer> getOffensiveEnemies() {
		return offensiveEnemies;
	}
	
	Entity getClosestOffensiveEnemy() {
		Entity closest = null;
		/*
		for (Entity entity: this.offensiveEnemies.keySet()) {
			if (zPos != entity.status.getZPos()) {
				continue;
			}
			if (closest == null) {
				closest = entity;
			} else if (Math.pow(Math.abs(entity.status.getXPos() - this.status.getXPos()), 2) + 
					   Math.pow(Math.abs(entity.status.getYPos() - this.status.getYPos()), 2) < 
					   Math.pow(Math.abs(closest.status.getXPos() - this.status.getXPos()), 2) + 
					   Math.pow(Math.abs(closest.status.getYPos() - this.status.getYPos()), 2)) {
				// If entity is closer to this entity than closest
				closest = entity;
			}
		}
		*/
		return closest;
	}
	
	/*
	 * How the entity moves in relation to the player
	 */
	public enum Behaviour {
		FOLLOW, // Follows the player
		FLEE, // Flees from the player
		IDLE // Stays still
	}
	
	/*
	 * How to entity interacts with the player, i.e. alignment
	 */
	public enum Nature {
		AGGRESSIVE, // Attacks the player
		PASSIVE, // Doesn't attack the player when the player attacks it
		NEUTRAL, // Attacks the player when the player attacks it
		FRIENDLY // Helps the player
	}
	
	/*
	 * Which direction the entity is facing. 0 is straight down (or facing the camera), moving anticlockwise.
	 */
	public enum Facing {
		N0_0,
		N22_5,
		N45_0,
		N67_5,
		N90_0,
		N112_5,
		N135_0,
		N157_5,
		N180_0,
		N202_5,
		N225_0,
		N247_5,
		N270_0,
		N292_5,
		N315_0,
		N337_5
	}
	
	/*
	 * The animation type the entity is in.
	 */
	public enum AnimationType {
		STAND,
		WALK,
		MIDAIR,
		SHOOT_PROJECTILE,
		NO_ANIMATION
	}
	
	/*
	 * Updates gameplay-affecting stats such as life.
	 */
	private void updateStats() {
		updateLife();
		updateSpirit();
		
		realPhysDamage = basePhysDmg; // In future this calculation may take into account modifiers, buffs, etc
		
		realWalkSpeed = baseWalkSpeed;
		
		//updateMovementFlags();
	}
	
	/*
	 * Updates the flags that say whether or not this entity can walk, rotate, and jump.
	 */
	private void updateMovementFlags() {
		canWalk = animationType != AnimationType.SHOOT_PROJECTILE && animationType != AnimationType.MIDAIR &&
		!stunnedEffect.isActive() && !rootedEffect.isActive() && !frozenEffect.isActive();

		canRotate = animationType != AnimationType.SHOOT_PROJECTILE && !stunnedEffect.isActive() &&
		!frozenEffect.isActive();

		canJump = animationType != AnimationType.SHOOT_PROJECTILE && animationType != AnimationType.MIDAIR &&
		!stunnedEffect.isActive() && !rootedEffect.isActive() && !frozenEffect.isActive();
	}
	
	/*
	 * Check if the target entity still exists (if not, reset targetEntity to -1).
	 */
	private void updateTargetEntity(Entities entities) {
		for (int i = 0; i < entities.allEntities.size; i ++) {
			if (entities.allEntities.get(i).id == targetEntity) {
				return;
			}
		}
		targetEntity = -1;
	}
	
	/*
	 * Iterate through actions and update animation type accordingly.
	 */
	private void updateActionAnimations() {
		if (actions.size > 0) {
			for (EntityAction action: actions.first()) {
				setAnimationType(action.getAnimationType());
			}
		}
	}
	
	/*
	 * The complete collection of logic to change the animation type from the default if needed.
	 */
	private void updateAnimationType() {
		updateActionAnimations();
		
		if (prevAnimationType == AnimationType.WALK &&
			(animationType == AnimationType.STAND || animationType == AnimationType.SHOOT_PROJECTILE)) { // If the entity has stopped moving
			setMovementVector(0, 0, 0);
		}
	}
	
	/*
	 * Construct the animation state (a combination of animation type and which direction this entity is facing).
	 */
	private void updateAnimationState() {
		animationState = animationType.toString() + facing.toString();
		
		if ("".equals(prevAnimationState)) { // If this is the first time entities is being updated
			prevAnimationState = animationState;
		}

		stateTime = animationType.equals(prevAnimationType) ? stateTime + Gdx.graphics.getDeltaTime() : 0;
		prevAnimationState = animationState;
	}
	
	/*
	 * Iterate through and 1) update and 2) remove completed actions.
	 */
	private void updateActions(PlayScreen playScreen) {
		if (actions.size > 0) {
			for (int i = 0; i < actions.first().size; i ++) {
				EntityAction action = actions.first().get(i);
				action.setCountUp(action.getCountUp() + Gdx.graphics.getDeltaTime());
				action.update(this, playScreen);
				if (action.wantsDeletion()) {
					actions.first().removeIndex(i);
					if (actions.first().size == 0) {
						actions.removeIndex(0);
						if (actions.size == 0) {
							break;
						}
					}
					i --;
				}
			}
		}
	}
	
	private void updateLife() {
		if (life > maxLife) {
			life = maxLife;
		}
	}
	
	private void updateSpirit() {
		if (spirit > maxSpirit) {
			spirit = maxSpirit;
		}
	}

	/**
	 * Update the cooldowns of the basic attack (may be 0) and all active skills.
	 */
	private void updateSkillCooldowns() {
		basicAttack.updateCooldown();

		for (ActiveSkill skill: skills) {
			skill.updateCooldown();
		}
	}
	
	/*
	 * An update that all entities go through.
	 */
	void universalUpdate(PlayScreen playScreen) {
		updateAnimationType();
		updateAnimationState();
		prevAnimationType = animationType;

		updateStats();
		updateTargetEntity(playScreen.entities);
		equipped.update(inventory);
		updateActions(playScreen);
		updateSkillCooldowns();
		
		moveByMovementVector();
		rigidBody.getWorldTransform().getTranslation(pos);
		
		collidingWithWalkable = false;
		collidingWithClimbable = false;
	}
	
	/*
	 * Not useful at the moment, but may be in future.
	 */
	public void postUpdate() {

	}
	
	/*
	 * Reset animation type to whatever it would be if the player did not do anything.
	 * E.g. standing and being in mid-air only depend on the player's situation in the world, not on their actions.
	 * This is used as a baseline. If there are no other animation types to take into account, it will stay as this.
	 */
	private void resetAnimationType() {
		if (collidingWithWalkable) {
			setAnimationType(AnimationType.STAND);
			rigidBody.setFriction(0.7f);
		} else if (!collidingWithWalkable && !collidingWithClimbable) {
			setAnimationType(AnimationType.MIDAIR);
		}
	}
	
	/*
	 * Update before anything else has happened in PlayScreen.executeLogic.
	 */
	public void preUpdate() {
		//setAnimationType(AnimationType.STAND);
		resetAnimationType();
		updateActionAnimations();
		//updateAnimationType();
		//resetAnimationType();
	}
	
	public abstract void onUpdate(PlayScreen session);
	
	public abstract void onInteract(PlayScreen session);
	
	public abstract void attack(Entity target, int range, PlayScreen session);
	
	boolean basicAttack(Entity target, int range, PlayScreen session) {
		/*
		boolean hit = false;
		if (target.activeEffects.contains(target.findEffect(StatusEffect.INVISIBLE)) ||
			Math.pow(Math.abs(target.pos.x - pos.x), 2) + Math.pow(Math.abs(target.pos.y - pos.y), 2) > Math.pow(range, 2)) {
			// Just give up trying to attack if the player is invisible or out of range
			target.attacking = false;
			target.setFollowingPlayer(false);
			return false;
		}
		if (this.attacking) { // If the entity is attacking its target
			int enemyAttackRoll = ThreadLocalRandom.current().nextInt(1,11);
			if (enemyAttackRoll >= target.eStats.getRealDex()) { // If the entity's attack roll is >= the target's dexterity
				hit = true; // Sucessful hit
				//target.status.changeStat("life", -(this.eStats.getRealPhysDamage())); // Target loses life
				if (target.name.equalsIgnoreCase("Player")) { // If the entity hit was the player
					session.newSubMessage(String.format("A %s hits you for %s damage.", this.name, this.eStats.getRealPhysDamage()));
				} else {
					session.newSubMessage(String.format("A %s hits a %s for %s damage.", this.name, target.name, this.eStats.getRealPhysDamage()));
				}
			} else { // If the entity failed to hit its target
				if (target.name.equalsIgnoreCase("Player")) {
					session.newSubMessage(String.format("You dodge a %s's attack.", this.name));
				} else {
					session.newSubMessage(String.format("A %s dodges a %s's attack.", target.name, this.name));
				}
			}
		} else { // If the enemy is moving in to attack
			if (target.name.equalsIgnoreCase("Player")) {
				session.newSubMessage(String.format("A %s moves in to attack.", this.name));
			} else {
				session.newSubMessage(String.format("A %s moves in to attack a %s.", this.name, target.name));
			}
			this.setAttacking(true);
		}
		target.getOffensiveEnemies().put(this, 0); // Add this attacking entity to the target entity's list of 'offensive entities'
		return hit; // Return whether or not the attack was successful
		*/
		return false;
	}
	
	private int visionRadius = 15;
	public int visionRadius() { return visionRadius; }
	
	public Entity() {
		life = 50;
		spirit = 50;
		pos = new Vector3();
		this.initialiseEffects();
		
		renderPos = new Vector2();
		
		actions = new Queue<>();

		basicAttack = new NullBasicAttack();
		skills = new Array<>();
		
		rigidBodyMatrix = new Matrix4();
		linearVelocity = new Vector3();
	}
	
	void prepareForSaveAndExit() {
		//rigidBodyMatrix = rigidBody.getWorldTransform();
		rigidBody.getWorldTransform(rigidBodyMatrix);
		linearVelocity.set(rigidBody.getLinearVelocity());
		rigidBody = null;
	}

	/**
	 * @return the rigid body of this entity, which was set to null during serialisation, and is now being put back in.
	 */
	btRigidBody prepareForSave() {
		btRigidBody body = rigidBody;
		prepareForSaveAndExit();
		return body;
	}
	
	void processAfterLoading() {
		loadRigidBody();
	}
	
	/*
	 * Create a new rigid body according to the correct hitbox for this type of entity and the transform.
	 */
	void loadRigidBody() {
		Vector3 localInertia = new Vector3();
		Hitboxes.hitboxes.get(name).calculateLocalInertia(1, localInertia);
		btRigidBody.btRigidBodyConstructionInfo constructionInfo;
		if (name.equals("player")) {
			constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1, null, Hitboxes.hitboxes.get(name), localInertia);
		} else {
			constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1, null, Hitboxes.hitboxes.get(name), localInertia);
		}
		rigidBody = new btRigidBody(constructionInfo);
		constructionInfo.dispose();
		rigidBody.setWorldTransform(rigidBodyMatrix);
		rigidBody.setLinearVelocity(linearVelocity);
		rigidBody.setAngularFactor(new Vector3(0, 0, 0)); // Disable rotation in all axis
		rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION); // Prevent the body from being deactivated, otherwise it wouldn't respond to changes in velocity
//		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags() | CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK); // Enable contact callbacks
		rigidBody.setUserValue(physicsId);
	}

	/*public StackEffect findStackEffect(EffectType name) {
		for (StackEffect effect: stackEffects) {
			if (effect.getStatusEffect() == name) {
				return effect;
			}
		}
		return null;
	}

	public ProcEffect findProcEffect(EffectType name) {
		for (ProcEffect effect: procEffects) {
			if (effect.getStatusEffect() == name) {
				return effect;
			}
		}
		return null;
	}*/
	
	private void initialiseEffects() {
		/*for (EffectType effectType: EffectType.values()) {
			this.allEffects.add(new Effect(effectType, 0, 0));
		}*/
		/*procEffects.add(new ProcEffect(EffectType.BURNING));
		procEffects.add(new ProcEffect(EffectType.SLOWED));
		procEffects.add(new ProcEffect(EffectType.STUNNED));
		procEffects.add(new ProcEffect(EffectType.ROOTED));*/

		burningEffect = new BurningEffect(this);
		stunnedEffect = new StunnedEffect(this);
		slowedEffect = new SlowedEffect(this);
		rootedEffect = new RootedEffect(this);
		chilledEffect = new ChilledEffect(this);
		frozenEffect = new FrozenEffect(this);
	}
	
	/*
	 * Iterate through and apply effects.
	 */
	void applyEffects(PlayScreen playScreen) {
		/*for (StackEffect effect: stackEffects) {
			switch (effect.getStatusEffect()) {
				default:
					break;
			}
		}*/

		/*boolean burningParticlesAdded = false;
		for (ProcEffect effect: procEffects) {
			switch (effect.getStatusEffect()) {
				case BURNING:
					for (int power: effect.powers) {
						changeLife(-2 * Gdx.graphics.getDeltaTime() * power);
						if (!burningParticlesAdded) {
							playScreen.particleEngine.addFlyUpPillar(playScreen.physicsManager.getDynamicsWorld(), pos, 1, 4,
									1f, Particle.Sprite.FIRE, Particle.Behaviour.GRAVITY);
							burningParticlesAdded = true;
						}
					}
					break;
			}
		}*/

		burningEffect.apply(playScreen);
		chilledEffect.bitingColdDamage();
		chilledEffect.testEncaseInIce(playScreen);

		updateAllEffects();
	}
	
	/*
	 * Add currently active effects to the list active effects, and remove inactive effects from that list.
	 */
	/*void updateActiveEffects() {
		activeEffects.clear(); // Reset active effects so only active ones can be re-entered there
		for (Effect effect: allEffects) {
			if (effect.getDuration() > 0 && effect.getPower() > 0) { // If the effect is active
				activeEffects.add(new Effect(effect.getStatusEffect(), effect.getDuration(), effect.getPower()));
			} else {
				// If the effect is not active, just make sure everything to do with it is set to 0
				effect.setDuration(0);
				effect.setPower(0);
			}
		}
	}*/
	
	/*
	 * Update the duration for all effects.
	 */
	private void updateAllEffects() {
		/*for (StackEffect effect: stackEffects) {
			if (effect.getDuration() > 0) { // And the effect is still active
				effect.setDuration(effect.getDuration() - Gdx.graphics.getDeltaTime()); // Decrease the duration
			} else {
				effect.setStacks(0); // Otherwise (i.e. if the effect is no longer active) set the power to 0 (to reset the effect)
			}
		}

		for (int i = 0; i < procEffects.size; i ++) {
			ProcEffect effect = procEffects.get(i);
			for (int a = 0; a < effect.powers.size; a ++) {
				if (effect.durations.get(a) > 0) {
					effect.durations.set(a, effect.durations.get(a) - Gdx.graphics.getDeltaTime());
				} else {
					effect.powers.removeIndex(a);
					effect.durations.removeIndex(a);
				}
			}
		}*/

		burningEffect.update();
		stunnedEffect.update();
		slowedEffect.update();
		rootedEffect.update();
		chilledEffect.update();
		frozenEffect.update();
	}

	/**
	 * This entity deals damage to the given entity. Doesn't check whether this entity is null or not (that's done in another method).
	 * A default behaviour is defined here as most entities will use this default behaviours.
	 * If an entity wants a custom behaviour (which is the minority of entities), that class can override this method.
	 */
	/*void dealDamageNoCheck(Entity entity, float damage) {
		entity.takeDamage(this, damage); // Assuming there are no behaviours on the attacking side to proc.
	}*/

	/**
	 * Allows custom behaviours for each entity when it's damaged.
	 */
	public void takeDamage(Entity entity, float damage) {
		takeDamageBase(damage);
		entitiesThatHit.add(entity.id);
		lastHitBy = entity.id;
	}

	/**
	 * This entity deals damage to the given entity. Doesn't check whether this entity is null or not (that's done in another method).
	 * A default behaviour is defined here as most entities will use this default behaviours.
	 * If an entity wants a custom behaviour (which is the minority of entities), that class can override this method.
	 */
	public void dealDamage(Entity entity, float damage) {
		entity.takeDamage(this, damage); // Assuming there are no behaviours on the attacking side to proc.
	}

	/**
	 * A reversed dealDamage(). Useful if you don't know whether the parameter 'entity' is null or not.
	 */
	public void dealtDamageBy(Entity entity, float damage) {
		entity.dealDamage(this, damage);
		/*if (entity != null) {
			entity.dealDamageNoCheck(this, damage);
		} else if (damageIfNull) {
			takeDamageBase(damage);
		}*/
	}

	/**
	 * The receiving end of the damage, universal to all entities. In future, this will take into account things like armour.
	 */
	public void takeDamageBase(float damage) {
		changeLife(-damage);
	}

	/**
	 * Custom implementations may take into account things that interact with burning, such as the Pyromancer passive 'Stoke the Flames'.
	 */
	/*public void burnNoCheck(Entity entity, int power, float duration) {
		burnBase(power, duration);
	}*/

	/**
	 * Custom implementations may take into account things that interact with burning, such as the Pyromancer passive 'Stoke the Flames'.
	 */
	public void burn(Entity entity, int power, float duration) {
		burnBase(power, duration);
	}

	public void burnedBy(Entity entity, int power, float duration) {
		entity.burn(this, power, duration);
		/*if (entity != null) {
			entity.burnNoCheck(this, power, duration);
		} else if (burnIfNull) {
			burnBase(power, duration);
		}*/
	}

	private void burnBase(int power, float duration) {
		burningEffect.add(power, duration);
	}

	/*public void chillNoCheck(Entity entity, int power, float duration) {
		chillBase(power, duration);
	}*/

	public void chill(Entity entity, int power, float duration) {
		chillBase(power, duration);
	}

	public void chilledBy(Entity entity, int power, float duration) {
		entity.chill(this, power, duration);
		/*if (entity != null) {
			entity.chillNoCheck(this, power, duration);
		} else if (chillIfNull) {
			chillBase(power, duration);
		}*/
	}

	private void chillBase(int power, float duration) {
		chilledEffect.add(power, duration, false, false);
	}

	/**
	 * What the offending entity should do to the defending entity. Called when the entity is removed from existence.
	 * Proc if this entity damaged the entity at any point.
	 */
	public void procDamageEffects(Entity entity, PlayScreen playScreen) {
		// By default, does nothing. Custom implementations if wanted in subclasses.
	}

	/**
	 * Proc only if this entity killed the entity.
	 */
	public void procKillEffects(Entity entity, PlayScreen playScreen) {
		// By default, does nothing. Custom implementations if wanted in subclasses.
	}

	/**
	 * Iterate through all offending entities and proc their kill effects.
	 */
	public void procDeathEffects(PlayScreen playScreen) {
		playScreen.entities.getEntity(lastHitBy, playScreen.player).procKillEffects(this, playScreen);

		for (Integer id: entitiesThatHit) {
			playScreen.entities.getEntity(id, playScreen.player).procDamageEffects(this, playScreen);
		}
	}

	/**
	 * Procs any passives when an ability lands on an entity.
	 */
	public void landAbility(Entity entity, PlayScreen playScreen) {
		// By default, does nothing. Custom implementations if wanted in subclasses.
	}

	/**
	 * Procs any passives when this entity lands a basic attack on another entity.
	 */
	public void landBasicAttack(Entity entity, PlayScreen playScreen) {
		// By default, does nothing. Custom implementations if wanted in subclasses.
	}
	
	@Override
	public TextureRegion getTexture() {
		switch(name) {
			case "player":
				return PlayerBlueprint.getCurrentTexture(animationState, facing, stateTime);
			case "dummy":
				return DummyBlueprint.getCurrentTexture(animationState, facing, stateTime);
			default:
				return null;
		}
	}

	public AnimationType getAnimationType() {
		return animationType;
	}

	public void setAnimationType(AnimationType animationType) {
		this.animationType = animationType;
		updateMovementFlags();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Weapon getLastHitWith() {
		return lastHitWith;
	}

	public void setLastHitWith(Weapon lastHitWith) {
		this.lastHitWith = lastHitWith;
	}

	public Inventory inventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Equipped equipped() {
		return equipped;
	}

	public void setEquipped(Equipped equipped) {
		this.equipped = equipped;
	}

	public boolean isDamagedByPlayer() {
		return damagedByPlayer;
	}

	public void setDamagedByPlayer(boolean damagedByPlayer) {
		this.damagedByPlayer = damagedByPlayer;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public Behaviour getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
	}
	
	public Nature getNature() {
		return nature;
	}
	
	public void setNature(Nature nature) {
		this.nature = nature;
	}

	public boolean isFollowingPlayer() {
		return followingPlayer;
	}

	public void setFollowingPlayer(boolean followingPlayer) {
		this.followingPlayer = followingPlayer;
	}
	
	public Facing getFacing() {
		return facing;
	}
	
	public void setFacing(Facing facing) {
		this.facing = facing;
	}

	public String getCurrentAnimationState() {
		return animationState;
	}

	public void setCurrentAnimationState(String currentAnimationState) {
		this.animationState = currentAnimationState;
	}

	public String getPrevAnimationState() {
		return prevAnimationState;
	}

	public void setPrevAnimationState(String prevAnimationState) {
		this.prevAnimationState = prevAnimationState;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public float getSpirit() {
		return spirit;
	}

	public void setSpirit(float spirit) {
		this.spirit = spirit;
	}
	
	public void changeLife(float change) {
		life += change;
		updateLife();
	}
	
	public void changeSpirit(float change) {
		spirit += change;
		updateSpirit();
	}

	public float getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(float maxLife) {
		this.maxLife = maxLife;
	}

	public float getMaxSpirit() {
		return maxSpirit;
	}

	public void setMaxSpirit(float maxSpirit) {
		this.maxSpirit = maxSpirit;
	}

	public float getBasePhysDmg() {
		return basePhysDmg;
	}

	public void setBasePhysDmg(float basePhysDmg) {
		this.basePhysDmg = basePhysDmg;
	}

	public float getBaseMagDmg() {
		return baseMagDmg;
	}

	public void setBaseMagDmg(float baseMagDmg) {
		this.baseMagDmg = baseMagDmg;
	}

	public int getXpLevel() {
		return xpLevel;
	}

	public void setXpLevel(int xpLevel) {
		this.xpLevel = xpLevel;
	}

	public int getXpProg() {
		return xpProg;
	}

	public void setXpProg(int xpProg) {
		this.xpProg = xpProg;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}
	
	public void changeXpLevel(int change) {
		xpLevel += change;
	}
	
	public void changeXpProg(int change) {
		xpProg += change;
	}
	
	public void changeSkillPoints(int change) {
		skillPoints += change;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(int targetEntity) {
		this.targetEntity = targetEntity;
	}

	public boolean canWalk() {
		return canWalk;
	}

	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	public float getRealWalkSpeed() {
		return realWalkSpeed;
	}

	public void setRealWalkSpeed(float realWalkSpeed) {
		this.realWalkSpeed = realWalkSpeed;
	}

	public boolean canRotate() {
		return canRotate;
	}

	public void setCanRotate(boolean canRotate) {
		this.canRotate = canRotate;
	}

	public boolean canJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public boolean isCollidingWithWalkable() {
		return collidingWithWalkable;
	}

	public void setCollidingWithWalkable(boolean collidingWithWalkable) {
		this.collidingWithWalkable = collidingWithWalkable;
	}

	public boolean isCollidingWithClimbable() {
		return collidingWithClimbable;
	}

	public void setCollidingWithClimbable(boolean collidingWithClimbable) {
		this.collidingWithClimbable = collidingWithClimbable;
	}

	public AnimationType getPrevAnimationType() {
		return prevAnimationType;
	}

	public void setPrevAnimationType(AnimationType prevAnimationType) {
		this.prevAnimationType = prevAnimationType;
	}

	public Matrix4 getRigidBodyMatrix() {
		return rigidBodyMatrix;
	}

	public void setRigidBodyMatrix(Matrix4 rigidBodyMatrix) {
		this.rigidBodyMatrix = rigidBodyMatrix;
	}

	public Vector3 getLinearVelocity() {
		return linearVelocity;
	}

	public void setLinearVelocity(Vector3 linearVelocity) {
		this.linearVelocity = linearVelocity;
	}

	public Vector3 getParentVelocity() {
		return parentVelocity;
	}

	public void setParentVelocity(Vector3 parentVelocity) {
		this.parentVelocity = parentVelocity;
	}

	public Array<ActiveSkill> getSkills() {
		return skills;
	}

	public Vector3 getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Vector3 targetLocation) {
		this.targetLocation = targetLocation;
	}

	public BasicAttack getBasicAttack() {
		return basicAttack;
	}

	public void setBasicAttack(BasicAttack basicAttack) {
		this.basicAttack = basicAttack;
	}

}
