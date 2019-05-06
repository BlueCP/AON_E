package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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
import com.mygdx.game.skills.*;
import com.mygdx.game.statuseffects.*;
import com.mygdx.game.utils.RenderMath;
import com.mygdx.game.utils.Util;

import java.util.HashMap;

public abstract class Entity extends WorldObject {
	
	static int targetTime; // The number of turns that an entity will aggressively follow another entity for
	
	protected String name;

//	public Vector3 pos;

	public StunnedEffect stunnedEffect;
	public SlowedEffect slowedEffect;
	public SpeedEffect speedEffect;
	public RootedEffect rootedEffect;
	public DamageEffect damageEffect;
//	public MagDamageEffect magDamageEffect;

	public BurningEffect burningEffect;

	public ChilledEffect chilledEffect;
	public FrozenEffect frozenEffect;

	public ChargedEffect chargedEffect;

	public SoulsEffect soulsEffect;
	public SoulsRegenEffect soulsRegenEffect;
	public WitherEffect witherEffect;

	public BlindedEffect blindedEffect;
	public LightFireEffect lightFireEffect;
	public PaladinShieldEffect paladinShieldEffect;
	
	float maxLife;
	float maxSpirit;
	float baseDamage;
//	private float baseMagDmg;
	float baseDefense;
	float baseWalkSpeed;

	float life;
	float spirit;
	protected float realDamage;
//	protected float realMagDamage;
	float realDefense;
	private float realWalkSpeed;

	private int xpLevel;
	private int xpProg;
	private int skillPoints;
	
	public Inventory inventory;
	private Equipped equipped  = new Equipped();
	private Weapon lastHitWith = null;
	private boolean followingPlayer;
	private boolean attacking = false;
	private int targetEntity = -1; // The targeted entity's id
	private Array<Integer> entitiesThatHit = new Array<>(); // An array of all the ids of entities that hit this entity (not necessarily damaged, could also be non-damage harmful effects).
	int lastHitBy = -1; // The id of the entity that last hit this entity.

	private boolean isProne = false; // If true, this entity is prone and will keep being prone until they stop moving.

	private boolean canWalk;
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
	public ActiveSkill currentSkill = new NullActiveSkill(); // The (active) skill that is currently being cast, or is on standby (waiting for input).
	// currentSkill is not reset, meaning that it also refers to the last skill cast if no skill is currently being cast.
	
	private String animationState;
	private String prevAnimationState;
	
	private HashMap<Entity, Integer> offensiveEnemies = new HashMap<>(); // Enemies which have recently attacked this entity
	
	private Vector3 movementVector = new Vector3();
//	public Vector3 additionalMovementVector = new Vector3(); // The additional movement vector exists only for the one tick: it is added, then removed, from the velocity of the object.
	private Vector3 parentVelocity = new Vector3(); // The velocity of the object the player is walking on, if any
	
	Matrix4 rigidBodyMatrix;
	private Vector3 linearVelocity;

	public Vector3 hitboxExtents; // The dimensions of the hitbox. Full extents, not half.
	public transient btRigidBody rigidBody;
	float stateTime = 0f;
	
	public Queue<EntityAction> actions;
	private Vector3 targetLocation = new Vector3(); // The location that the player has targeted in the world (by clicking there), or that this entity is targeting.
	private boolean targetedLocationThisTick = false; // True if the entity targeted a new location this tick, false if not.
	private Vector2 movementLocation = new Vector2(); // The location that this entity will move to (usually walking). For now it is a 2D vector, may change to 3D when adding pathfinding.
	
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
			/*applyMovementChanges();
			if (rootedEffect.isActive() || stunnedEffect.isActive() || frozenEffect.isActive()) {
				rigidBody.setLinearVelocity(parentVelocity);
			} else {
				rigidBody.setLinearVelocity(movementVector.add(parentVelocity));
			}*/
			rigidBody.setLinearVelocity(movementVector.add(parentVelocity));
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
	 * Knocks this entity back with the given velocity, and also makes them prone.
	 */
	public void knockBack(Vector3 velocity) {
		isProne = true;
		setAnimationType(AnimationType.PRONE); // Make the enitity prone.
		rigidBody.setLinearVelocity(velocity); // Change the velocity to the given one.
		currentSkill.failResolve(); // Stop the current skill.
		actions.clear(); // Stop all other actions.
	}

	/**
	 * Apply effects such as slows.
	 */
	private void applyMovementChanges() {
		realWalkSpeed *= (1 - (slowedEffect.movementDampening() + chilledEffect.movementDampening())) +
							1 + (speedEffect.movementBoost());
	}

	/**
	 * Sets the target location for movement for this entity to its current position.
	 * Useful to make the entity stop moving.
	 */
	public void resetMovementLocation() {
		movementLocation.set(pos.x, pos.z);
	}

	private void rotate(Vector2 diff) {
//		Vector2 diff = movementLocation.cpy().sub(new Vector2(pos.x, pos.z));
		if (canRotate()) {
			/*Vector2 renderPos2 = renderPos.cpy().add(getTexture().getRegionWidth()/2f * playScreen.isoRenderer.camera.getZoom(),
					getTexture().getRegionHeight()/2f * playScreen.isoRenderer.camera.getZoom());

			int screenX1 = (int) (playScreen.virtualCoords.x - renderPos2.x); // X distance relative to player
			int halfScreenX1 = screenX1 / 2; // Reasons for this can be found in one of the notebooks.
			int screenY1 = (int) (playScreen.virtualCoords.y - renderPos2.y); // Y distance relative to player*/

			// TAN2 ANGLE CALCULATION STARTS HERE //
//			Vector2 newVector = RenderMath.cartToInvertedIso(diff.x, diff.y);
			Vector2 newVector = RenderMath.cartToIso(diff.x, diff.y);

//			float angle = (float) Math.toDegrees(MathUtils.atan2(newVector.y, newVector.x / 2));
			float angle = (float) Math.toDegrees(MathUtils.atan2(newVector.y, newVector.x / 2));

			if (angle < 0) { // Adjust negative values for angle.
				angle += 360;
			}

			angle += 90;
			if (angle >= 360) {
				angle -= 360;
			}

//			angle = 360 - angle;

			/*angle += 90;
			if (angle >= 360) {
				angle -= 360;
			}*/
			// TAN2 ANGLE CALCULATION ENDS HERE //

			// COSINE ANGLE CALCULATION STARTS HERE //
			/*int b = (int) (renderPos.y);
			double c = Math.sqrt(halfScreenX1*halfScreenX1 + screenY1*screenY1); // Total distance relative to the player
			double a = Math.sqrt(halfScreenX1*halfScreenX1 + (virtualCoords.y)*(virtualCoords.y));
			double cosx = (float) ((b*b + c*c - a*a)/(2 * b * c));
			if (cosx > 1) { // In case a rounding error made cosx very slightly greater than 1
				cosx = 1;
			} else if (cosx < -1) {
				cosx = -1;
			}
			double angle = Math.toDegrees(Math.acos(cosx)); // Using the cosine theorem to find the angle of the click
			if (virtualCoords.x < player.renderPos.x) {
				angle = 360 - angle;
			}*/
			// COSINE ANGLE CALCULATION ENDS HERE //

			angle = (float) (Math.round(angle/22.5f) * 22.5); // Round to nearest 22.5 degrees (which is 360/number of directions).
			if (angle == 360) { // 0 = 360 when it comes to facing directions, as the maximum facing angle the system will recognise is 337.5.
				angle = 0;
			}

			String facing = "N" + String.valueOf(angle).replace('.', '_'); // Convert to the format for 'facing'
			for (Facing facing0: Facing.values()) {
				if (facing.equals(facing0.toString())) {
					setFacing(facing0);
				}
			}
		} // Don't bother rotating the player if they can't rotate

	}

	private void testForWalk(Vector2 diff) {
		if (canWalk()) {
			setAnimationType(AnimationType.WALK);

			/*float angle = Float.parseFloat(getFacing().toString().substring(1).replace('_', '.'));
			int angle1 = Math.floorMod((int)angle, 90);
			double opposite = getRealWalkSpeed()*Math.sin(Math.toRadians(angle1));
			double adjacent = getRealWalkSpeed()*Math.cos(Math.toRadians(angle1));
			double xComponent = (angle < 90 || (angle >= 180 && angle < 270)) ? opposite : adjacent;
			xComponent = (angle < 180) ? xComponent : -xComponent;
			double yComponent = (angle < 90 || (angle >= 180 && angle < 270)) ? adjacent : opposite;
			yComponent = (angle > 90 && angle < 270) ? yComponent : -yComponent;
			Vector2 point = RenderMath.cartToInvertedIso((float)xComponent, (float)yComponent);*/

			Vector2 movement = diff.cpy().setLength(realWalkSpeed);

//			addMovementVector(point.x, 0, point.y);
			addMovementVector(movement.x, 0, movement.y);
		}  // Don't bother with walking logic if the player can't actually walk

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
		Vector2 coords = RenderMath.cartToScreen(renderer.camera, pos.x, pos.y, pos.z);
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
		STAND, // The entity will stand and attempt to move to its target movement location.
		WALK, // The entity is walking.
		MIDAIR, // The entity is free-falling through the air.
		PRONE, // The entity is prone (for example from knockback), and will resume standing when it stops moving.
		SHOOT_PROJECTILE, // The entity is throwing a projectile.
		NO_ANIMATION
	}
	
	/*
	 * Updates gameplay-affecting stats such as life.
	 */
	private void updateStats() {
		applyLifeCap();
		applySpiritCap();

		realDamage = (baseDamage + equipped.getWeapon().getDamage()) * damageEffect.totalBuff();
//		realMagDamage = (baseMagDmg + equipped.getWeapon().getMagDamage()) * magDamageEffect.totalBuff();

		realDefense = baseDefense;
		
		realWalkSpeed = baseWalkSpeed * (1 - (slowedEffect.movementDampening() + chilledEffect.movementDampening()))
				 						    + (speedEffect.movementBoost());
		
		//updateMovementFlags();
	}
	
	/*
	 * Updates the flags that say whether or not this entity can walk, rotate, and jump.
	 */
	private void updateMovementFlags() {
		canWalk = animationType != AnimationType.SHOOT_PROJECTILE && animationType != AnimationType.MIDAIR &&
		animationType != AnimationType.PRONE && !stunnedEffect.isActive() && !rootedEffect.isActive() &&
		!frozenEffect.isActive();

		canRotate = animationType != AnimationType.SHOOT_PROJECTILE && animationType != AnimationType.PRONE &&
		!stunnedEffect.isActive() && !frozenEffect.isActive();

		canJump = animationType != AnimationType.SHOOT_PROJECTILE && animationType != AnimationType.MIDAIR &&
		animationType != AnimationType.PRONE && !stunnedEffect.isActive() && !rootedEffect.isActive() &&
		!frozenEffect.isActive();
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
			setAnimationType(actions.first().getAnimationType());
			/*for (EntityAction action: actions.first()) {
				setAnimationType(action.getAnimationType());
			}*/
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
//		System.out.println(actions);
		if (actions.size > 0) {
			EntityAction action = actions.first();
			action.setCountUp(action.getCountUp() + Gdx.graphics.getDeltaTime());
			action.update(this, playScreen);
			if (actions.size > 0) {
				EntityAction action1 = actions.first();
				if (action1.wantsDeletion()) {
					actions.removeValue(action1, false);
					/*if (actions.first().size == 0) {
						actions.removeIndex(0);
						if (actions.size == 0) {
							break;
						}
					}*/
//					i--;
				}
			}
		}
	}
	
	private void applyLifeCap() {
		if (life > maxLife) {
			life = maxLife;
		}
		if (life < 0) {
			life = 0;
		}
	}
	
	private void applySpiritCap() {
		if (spirit > maxSpirit) {
			spirit = maxSpirit;
		}
		if (spirit < 0) {
			spirit = 0;
		}
	}

	/**
	 * Update the cooldowns of the basic attack (may be 0) and all active skills, and updates all active skills.
	 */
	private void updateSkills(PlayScreen playScreen) {
		basicAttack.updateCooldown();

		for (ActiveSkill skill: skills) {
			skill.updateCooldown();
			skill.update(playScreen);
		}
	}

	/**
	 * Updates passive skills (unique to each entity). Overridable for that purpose.
	 */
	void updatePassiveSkills(PlayScreen playScreen) {

	}

	private void updateMovement() {
		Vector2 diff = movementLocation.cpy().sub(new Vector2(pos.x, pos.z));
//		System.out.println(diff.len());
//		System.out.println(realWalkSpeed * Gdx.graphics.getDeltaTime());
		if (diff.len() > realWalkSpeed * Gdx.graphics.getDeltaTime() * 1.5f) { // If the distance to the location is more than ~1.5 frames worth. Otherwise stop moving.
			rotate(diff);
			testForWalk(diff);
		}
	}

	private void updateProne() {
		if (rigidBody.getLinearVelocity().dst(parentVelocity) < 0.01f) { // If the entity has stopped moving.
			// We use dst instead of just using equals, as there are always very small values even when the player appears to be still.
			isProne = false; // Make the entity no longer prone.
		}
	}

	/*
	 * Update before anything else has happened in PlayScreen.executeLogic.
	 */
	public void preUpdate() {
//		System.out.println(animationType);
		updateProne();
		resetAnimationType();
		updateActionAnimations();
		targetedLocationThisTick = false; // Assume no target location this tick until shown otherwise.
		//updateAnimationType();
	}
	
	/*
	 * An update that all entities go through.
	 */
	void universalUpdate(PlayScreen playScreen) {
		updateStats();

		updateMovement();

		updateAnimationType();
		updateAnimationState();
		prevAnimationType = animationType;

		updateAllEffects();

		updateTargetEntity(playScreen.entities);
		equipped.update(inventory);
		updateActions(playScreen);
		updateSkills(playScreen);
		updatePassiveSkills(playScreen);
		
		moveByMovementVector();
		rigidBody.getWorldTransform().getTranslation(pos);
		rigidBody.getWorldTransform(rigidBodyMatrix);
		linearVelocity.set(rigidBody.getLinearVelocity());
		
		collidingWithWalkable = false;
		collidingWithClimbable = false;

		applyEffects(playScreen);
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
		if (isProne) {
			setAnimationType(AnimationType.PRONE);
		} else if (collidingWithWalkable) {
			setAnimationType(AnimationType.STAND);
			rigidBody.setFriction(0.7f);
		} else if (!collidingWithWalkable && !collidingWithClimbable) {
			setAnimationType(AnimationType.MIDAIR);
		}
	}

	/**
	 * Sets the current animation back to the starting frame.
	 */
	public void resetAnimation() {
		stateTime = 0;
	}

	/**
	 * Copies the non-transient fields of this Entity object to the given object.
	 *//*
	void copyFields(Entity entity) {

	}*/

	/**
	 * Each entity will have individual, different update logic.
	 */
	public abstract void individualUpdate(PlayScreen playScreen);
	
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
				//target.status.changeStat("life", -(this.eStats.getRealDamage())); // Target loses life
				if (target.name.equalsIgnoreCase("Player")) { // If the entity hit was the player
					session.newSubMessage(String.format("A %s hits you for %s damage.", this.name, this.eStats.getRealDamage()));
				} else {
					session.newSubMessage(String.format("A %s hits a %s for %s damage.", this.name, target.name, this.eStats.getRealDamage()));
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
		inventory = new Inventory();
		actions = new Queue<>();

		basicAttack = new NullBasicAttack();
		skills = new Array<>();
		
		rigidBodyMatrix = new Matrix4();
		linearVelocity = new Vector3();
	}
	
	/*void prepareForSaveAndExit() {
		rigidBody = null;
	}*/

	/**
	 * @return the rigid body of this entity, which was set to null during serialisation, and is now being put back in.
	 */
	/*btRigidBody prepareForSave() {
		btRigidBody body = rigidBody;
		prepareForSaveAndExit();
		return body;
	}*/

	private void calculateHitboxExtents() {
		Vector3 min = new Vector3();
		Vector3 max = new Vector3();
		rigidBody.getCollisionShape().getAabb(new Matrix4(), min, max);
		hitboxExtents = max.sub(min);
	}
	
	final void processAfterLoadingBase() {
		loadRigidBody();
		calculateHitboxExtents();
//		System.out.println(hitboxExtents);

		// The entity field of a Skill object is transient (to prevent headaches with circular references).
		// Thus the reference to this entity must be reconstructed.
		for (Skill skill: skills) {
			skill.setEntity(this);
		}
		basicAttack.setEntity(this);
	}

	/**
	 * By default, use the basic method for processing the object after deserialisation. Overridable if desired.
	 */
	void processAfterLoading() {
		processAfterLoadingBase();
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
		rigidBody.setUserValue(physicsId);
	}
	
	private void initialiseEffects() {
		stunnedEffect = new StunnedEffect(this);
		slowedEffect = new SlowedEffect(this);
		speedEffect = new SpeedEffect(this);
		rootedEffect = new RootedEffect(this);
		damageEffect = new DamageEffect(this);
//		magDamageEffect = new MagDamageEffect(this);

		burningEffect = new BurningEffect(this);

		chilledEffect = new ChilledEffect(this);
		frozenEffect = new FrozenEffect(this);

		chargedEffect = new ChargedEffect(this);

		soulsEffect = new SoulsEffect(this);
		soulsRegenEffect = new SoulsRegenEffect(this);
		witherEffect = new WitherEffect(this);

		blindedEffect = new BlindedEffect(this);
		lightFireEffect = new LightFireEffect(this);
		paladinShieldEffect = new PaladinShieldEffect(this);
	}
	
	/*
	 * Iterate through and apply effects.
	 */
	void applyEffects(PlayScreen playScreen) {
		burningEffect.apply(playScreen);

		chilledEffect.bitingColdDamage();
		chilledEffect.testEncaseInIce(playScreen);

		chargedEffect.randomDischarge(playScreen);

		witherEffect.apply(playScreen);

		lightFireEffect.apply(playScreen);
	}
	
	/*
	 * Update the duration for all effects.
	 */
	private void updateAllEffects() {
		stunnedEffect.update();
		slowedEffect.update();
		speedEffect.update();
		rootedEffect.update();
		damageEffect.update();
//		magDamageEffect.update();

		burningEffect.update();

		chilledEffect.update();
		frozenEffect.update();

		chargedEffect.update();

		soulsEffect.update();
		soulsRegenEffect.update();
		witherEffect.update();

		blindedEffect.update();
		lightFireEffect.update();
		paladinShieldEffect.update();
	}

	/**
	 * Allows custom behaviours for each entity when it's damaged.
	 */
	public void takeDamage(Entity entity, float damage) {
		takeDamageBase(entity, damage);
	}

	/**
	 * This method also makes sure that the offending entity's actions have been recorded (for death procs, for example).
	 */
	public void takeDamageBase(Entity entity, float damage) {
		float newDamage = paladinShieldEffect.reflectDamage(entity, damage);
		newDamage *= Math.pow(1.1, -realDefense); // Graph of damage reduction against defense follows exponential decay.

		takeDamageNoEntity(newDamage);

		entitiesThatHit.add(entity.id);
		lastHitBy = entity.id;
	}

	/**
	 * The receiving end of the damage, universal to all entities. In future, this will take into account things like armour.
	 * The 'no entity' refers to the fact that this method doesn't care about the offending entity; it only takes
	 * into account the reaction of the defending (this) entity.
	 */
	public void takeDamageNoEntity(float damage) {
		changeLife(-damage);
	}

	/**
	 * This entity deals damage to the given entity. Doesn't check whether this entity is null or not (that's done in another method).
	 * A default behaviour is defined here as most entities will use this default behaviours.
	 * If an entity wants a custom behaviour (which is the minority of entities), that class can override this method.
	 * @return the final amount of damage dealt to the defending entity. Useful when damage is manipulated in custom implementations (this method is overridden).
	 */
	public float dealDamage(Entity entity, float damage) {
		return dealDamageBase(entity, damage);
	}

	public float dealDamageBase(Entity entity, float damage) {
		float finalDamage = damage * blindedEffect.damageDamping();
		entity.takeDamage(this, finalDamage); // Assuming there are no behaviours on the attacking side to proc.
		return finalDamage;
	}

	/**
	 * A reversed dealDamage(). Useful if you don't know whether the parameter 'entity' is null or not.
	 */
//	public float dealtDamageBy(Entity entity, float damage) {
//		return entity.dealDamage(this, damage);
//	}

	/**
	 * Custom implementations may take into account things that interact with burning, such as the Pyromancer passive 'Stoke the Flames'.
	 */
	public void burn(Entity entity, int power, float duration) {
		entity.burnBase(power, duration);
	}

	/*public void burnedBy(Entity entity, int power, float duration) {
		entity.burn(this, power, duration);
	}*/

	private void burnBase(int power, float duration) {
		burningEffect.add(power, duration);
	}

	public void chill(Entity entity, int power, float duration) {
		entity.chillBase(power, duration);
	}

//	public void chilledBy(Entity entity, int power, float duration) {
//		entity.chill(this, power, duration);
//	}

	private void chillBase(int power, float duration) {
		chilledEffect.add(power, duration, false, false);
	}

	public void stun(Entity entity, float duration) {
		entity.stunBase(duration);
	}

//	public void stunnedBy(Entity entity, float duration) {
//		entity.stun(this, duration);
//	}

	private void stunBase(float duration) {
		stunnedEffect.add(duration);
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
	 * Procs any passives when an ability deals damage to an entity.
	 */
	public float landAbilityDamage(Entity entity, float damage, PlayScreen playScreen) {
		// By default, does nothing. Custom implementations if wanted in subclasses.
		return 0;
	}

	/**
	 * Procs any passives when this entity lands a basic attack on another entity.
	 */
	public void landBasicAttack(Entity entity, PlayScreen playScreen) {
		// By default, does nothing. Custom implementations if wanted in subclasses.
	}

	/**
	 * Procs any passives when this entity damages another entity with a basic attack.
	 */
	public float landBasicAttackDamage(Entity entity, float damage, PlayScreen playScreen) {
		// By default, does nothing. Custom implementations if wanted in subclasses.
		return 0;
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
		applyLifeCap();
	}
	
	public void changeSpirit(float change) {
		spirit += change;
		applySpiritCap();
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

	public float getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(float baseDamage) {
		this.baseDamage = baseDamage;
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

	public boolean hasTargetedLocationThisTick() {
		return targetedLocationThisTick;
	}

	public void setTargetedLocationThisTick(boolean targetedLocationThisTick) {
		this.targetedLocationThisTick = targetedLocationThisTick;
	}

	public Vector2 getMovementLocation() {
		return movementLocation;
	}

	public void setMovementLocation(Vector2 movementLocation) {
		this.movementLocation = movementLocation;
	}

	public float getRealDamage() {
		return realDamage;
	}

	public void setRealDamage(float realDamage) {
		this.realDamage = realDamage;
	}

}
