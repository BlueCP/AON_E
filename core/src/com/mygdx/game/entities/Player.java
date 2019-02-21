package com.mygdx.game.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.mygdx.game.mobtypes.MobClass;
import com.mygdx.game.mobtypes.MobRace;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;
import com.mygdx.game.skills.SkillBar;
import com.mygdx.game.skills.cryomancer.*;
import com.mygdx.game.skills.pyromancer.FlamingBarrageSkill;
import com.mygdx.game.skills.pyromancer.StokeTheFlamesSkill;
import com.mygdx.game.skills.pyromancer.SupernovaSkill;
import com.mygdx.game.skills.pyromancer.VikingFuneralSkill;

import java.util.Map.Entry;

public class Player extends Entity {
	
	//public static String playerSymbol = "@"; // So that this can be changed at any time
	
	static final float frameInterval = 0.0625f;
	
	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	private String playerName;
	public SkillBar skillBar;
	private MobRace mobRace;
	private MobClass mobClass;
	public static final int basicLvlReq = 10;

	static final int fps = 24;
	static final int directions = 16;

	private VikingFuneralSkill vikingFuneral;
	private StokeTheFlamesSkill stokeTheFlames;
	private FlamingBarrageSkill flamingBarrage;
	private SupernovaSkill supernovaSkill;

	private LastingChillSkill lastingChill;
	private BitingColdSkill bitingCold;
	private EncaseInIceSkill encaseInIce;
	private FracturingBlastSkill fracturingBlast;
	
	public Player() {
		id = 0;
		physicsId = 10000;
		
		super.name = "player";

		skillBar = new SkillBar();

		maxLife = 50;
		maxSpirit = 50;
		
		baseWalkSpeed = 1.4f;

		inventory.addWeapon("Iron shortsword");
		inventory.addOtherItem("Iron ore");

		// PYROMANCER

		/*basicAttack = new PyromancerBasicAttack(this);

		skills.add(new FireballSkill(this));
		skills.add(new IncendiaryTrapSkill(this));
		skills.add(new FieryVortexSkill(this));
		skills.add(new HeatwaveSkill(this));
		skills.add(new IncinerateSkill(this));
		skills.add(new BurningBarrierSkill(this));
		skills.add(new FlamethrowerSkill(this));
		skills.add(new LavaSnareSkill(this));*/


		// CRYOMANCER

		basicAttack = new CryomancerBasicAttack(this);

		skills.add(new IceShardSkill(this));
		skills.add(new BlizzardSkill(this));
		skills.add(new RapidDefrostingSkill(this));
		skills.add(new HailstormSkill(this));
		skills.add(new OverwhelmingFrostSkill(this));
		skills.add(new CryosleepSkill(this));
		skills.add(new ShatterSkill(this));
		skills.add(new GlacialWallSkill(this));


		// Note: the player must have all of the passives loaded in like this at the start of the game.
		// This is because we test for these passives, and only act if they have been learned.
		// The alternative is to make subclasses of player for each class. This would require copying all class-irrelevant
		// attributes to a new instance of that subclass when the player chooses a class, which could have serious
		// implications on game logic.
		vikingFuneral = new VikingFuneralSkill(this, false);
		stokeTheFlames = new StokeTheFlamesSkill(this, false);
		flamingBarrage = new FlamingBarrageSkill(this, false);
		supernovaSkill = new SupernovaSkill(this, false);

		lastingChill = new LastingChillSkill(this, true);
		bitingCold = new BitingColdSkill(this, true);
		encaseInIce = new EncaseInIceSkill(this, true);
		fracturingBlast = new FracturingBlastSkill(this, true);
	}

	@Override
	public void dealDamage(Entity entity, float damage) {
		stokeTheFlames.damage(entity, damage);
	}

	@Override
	public void burn(Entity entity, int power, float duration) {
		stokeTheFlames.burn(entity, power, duration);
	}

	@Override
	public void chill(Entity entity, int power, float duration) {
		entity.chilledEffect.add(power, duration, bitingCold.isLearned(), encaseInIce.isLearned());
	}

	@Override
	public void procKillEffects(Entity entity, PlayScreen playScreen) {
		supernovaSkill.testfor(entity, playScreen.projectileManager, playScreen.physicsManager.getDynamicsWorld());
	}

	@Override
	public void landAbility(Entity entity, PlayScreen playScreen) {
		vikingFuneral.testfor(entity);
		flamingBarrage.testfor();
		lastingChill.testfor(entity);
	}

	@Override
	public void landBasicAttack(Entity entity, PlayScreen playScreen) {
		fracturingBlast.testfor(entity, playScreen);
	}
	
	/*
	 * Normal save, would be done from PlayScreen.
	 */
	public void saveAndExit() {
		try {
			prepareForSaveAndExit();
			KryoManager.write(this, "saves/" + playerName + "/player.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			btRigidBody body = prepareForSave();
			KryoManager.write(this, "saves/" + playerName + "/player.txt");
			rigidBody = body;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Only done once per save file, before that save has been loaded in PlayScreen.
	 */
	public void saveInitial() {
		try {
			rigidBody = null;
			rigidBodyMatrix = new Matrix4();
			rigidBodyMatrix.setTranslation(new Vector3(0, 10, 0));
			setLinearVelocity(new Vector3());

			KryoManager.write(this, "saves/" + playerName + "/player.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Load player data.
	 */
	public static Player load(PlayScreen session) {
		// We need to get session as a parameter because at initial loading, player would not have been updated and given session
		try {
			Player player = KryoManager.read("saves/" + session.playerName + "/player.txt", Player.class);
			player.loadRigidBody();
			return player;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void update(PlayScreen playScreen) {
		//this.getFov().update(session.map, this.getStatus().getXPos(), this.getStatus().getYPos(), this.getStatus().getZPos(), this.visionRadius());
		//this.eStats.updateStatsCopy(session.skillCollection);
		for (Entry<Entity, Integer> entry: getOffensiveEnemies().entrySet()) {
			entry.setValue(entry.getValue() + 1);
			if (entry.getValue() > Entity.targetTime) {
				getOffensiveEnemies().remove(entry.getKey());
			}
		}
		
		applyEffects(playScreen);
		
		universalUpdate(playScreen);
	}
	
	private void testforTurnHostile(Entity entity) {
		if (entity.getNature() == Nature.NEUTRAL) {
			entity.setNature(Nature.AGGRESSIVE);
			// Makes neutral entities that are damaged by the player aggressive
		}
	}
	
	public void basicAttack(boolean withMainHand) {
		/*if (noTargetSelected()) { return; }
		else if (noWeaponEquipped()) { noWeaponMessage(); return; }
		else if (noMainEquipped() && withMainHand == true) { noMainMessage(); return; }
		else if (noOffEquipped() && withMainHand == false) { noOffMessage(); return; }
		Weapon weapon;
		if (withMainHand) { weapon = this.equipped().getMain(); }
		else { weapon = this.equipped().getOff(); }
		if (outOfRange(weapon)) { return; }
		//////////////////////// DAMAGE CALUCLATION ///////////////////////////
		int damageRoll = 0;
		if (weapon.getPhysDamage() >= weapon.getMagDamage()) {
			// If phys damage is greater than or equal to mag damage, use it (phys damage is the default if phys and mag damage are equal)
			damageRoll += weapon.getPhysDamage();
		} else {
			// Otherwise, use mag damage
			damageRoll += weapon.getMagDamage();
		}
		damageRoll += this.eStats().getRealPhysDamage();
		int critRoll = ThreadLocalRandom.current().nextInt(1,101);
		if (critRoll <= this.eStats().getRealCritChance()) {
			damageRoll *= 1.2;
		}
		session.targetEntity.getStatus().changeStat("life", -(damageRoll));
		session.targetEntity.setDamagedByPlayer(true);
		session.targetEntity.setLastHitWith(weapon);
		this.testforTurnHostile(session.targetEntity);
		int lifeStolen = this.calcLifesteal(damageRoll);
		this.calcBurstSpeed();
		session.newMessage(String.format("You hit the %s for %s damage.", session.targetEntity.getName(), damageRoll), Color.YELLOW);
		if (lifeStolen > 0) {
			session.newMessage(String.format("You heal for %s life.", lifeStolen), Color.GREEN);
		}*/
	}
	
	public void doMove(int i) {
		/*this.physDamage = this.eStats().getRealPhysDamage();
		this.magDamage = this.eStats().getRealMagDamage();
		Move move = this.moveBar.getMove(i);
		if (move == null) {
			session.newMessage("No move equipped to this slot!", Color.RED);
			return;
		}
		switch (move.getName()) {
			case "Slam":
				doSlam();
				break;
			case "Lunge":
				doLunge();
				break;
			case "Cleave":
				doCleave();
				break;
		}*/
	}
	
	public void castSpell(int i) {
		/*this.physDamage = this.eStats().getRealPhysDamage();
		this.magDamage = this.eStats().getRealMagDamage();
		Spell spell = this.spellBar.getSpell(i);
		if (spell == null) {
			session.newMessage("No spell equipped to this slot!", Color.RED);
			return;
		}
		switch (spell.getName()) {
			case "FireballSkill":
				castFireball();
				break;
			case "Immolate":
				castImmolate();
				break;
			case "Blink":
				castBlink();
				break;
			case "Lightning Bolt":
				castLightningBolt();
				break;
			case "Shroud":
				castShroud();
				break;
		}*/
	}
	
	private void doSlam() {
		/*if (noTargetSelected()) { return; }
		if (noMainEquipped()) { noMainMessage(); return; }
		if (outOfRange(0)) { return; }
		this.testCost("energy", 10);
		if (session.continueConfirm == true) {
			return;
		}
		//////////////////// DAMAGE CALCULATION ///////////////////////
		int hitRoll = ThreadLocalRandom.current().nextInt(1,101);
		if (hitRoll <= 70) { // 70% chance of a hit
			int damageRoll = physDamage;
			damageRoll += session.equipped.getMain().getPhysDamage();
			damageRoll *= 1.2; // Guaranteed crit
			session.targetEntity.getStatus().changeStat("life", -(damageRoll));
			session.targetEntity.setDamagedByPlayer(true);
			session.targetEntity.setLastHitWith(session.equipped.getMain());
			this.testforTurnHostile(session.targetEntity);
			int lifeStolen = calcLifesteal(damageRoll);
			this.calcBurstSpeed();
			session.newMessage(String.format("You slam the %s for %s damage.", session.targetEntity.getName(), damageRoll), Color.YELLOW);
			if (lifeStolen > 0) {
				session.newMessage(String.format("You heal for %s life.", lifeStolen), Color.GREEN);
			}
		} else {
			session.newMessage("You miss the hit.", Color.ORANGE);
		}*/
	}
	
	private void doLunge() {
		/*if (noTargetSelected()) { return; }
		if (noMainEquipped()) { noMainMessage(); return; }
		if (outOfRange(1)) { return; }
		this.testCost("energy", 15);
		if (session.continueConfirm == true) {
			return;
		}
		// The player lunges to the enemy regardless if they hit or miss
		//this.getStatus().changeStat("yPos", session.targetEntity.getYDist());
		//this.getStatus().changeStat("xPos", session.targetEntity.getXDist());
		//////////////////////// DAMAGE CALCULATON /////////////////////////
		int hitRoll = ThreadLocalRandom.current().nextInt(1,101);
		if (hitRoll <= 85) { // 85% chance of a hit
			int damageRoll = physDamage;
			damageRoll += session.equipped.getMain().getPhysDamage();
			damageRoll *= 1.5; // 50% extra damage
			session.targetEntity.getStatus().changeStat("life", -(damageRoll));
			session.targetEntity.setDamagedByPlayer(true);
			session.targetEntity.setLastHitWith(session.equipped.getMain());
			this.testforTurnHostile(session.targetEntity);
			int lifeStolen = calcLifesteal(damageRoll);
			this.calcBurstSpeed();
			session.newMessage(String.format("You lunge to the %s for %s damage.", session.targetEntity.getName(), damageRoll), Color.YELLOW);
			if (lifeStolen > 0) {
				session.newMessage(String.format("You heal for %s life.", lifeStolen), Color.GREEN);
			}
		} else {
			session.newMessage("You miss the hit.", Color.ORANGE);
		}*/
	}
	
	private void doCleave() {
		/*if (noMainEquipped()) { noMainMessage(); return; }
		this.testCost("energy", 20);
		if (session.continueConfirm == true) {
			return;
		}
		// Range is effectively infinite because the move is not targeted, so you cannot check if the target enemy is in range
		////////////////////////// DAMAGE CALCULATION ///////////////////////
		int damageRoll = physDamage;
		damageRoll += session.equipped.getMain().getPhysDamage();
		damageRoll *= 0.75; // Damage is decreased by 25% because the move is aoe
		int totalDamage = 0;
		int noOfEnemiesHit = 0;
		for (Entity entity: session.entities.allEntities) {
			*//*
			if (entity.getPureDiff() <= 1.5) { // Can only hit enemies adjacent to the player, 1 tile range (including diagonally adjacent enemies)
				entity.getStatus().changeStat("life", -(damageRoll));
				entity.setDamagedByPlayer(true);
				entity.setLastHitWith(session.equipped.getMain());
				this.testforTurnHostile(entity);
				totalDamage += damageRoll;
				noOfEnemiesHit ++;
			}
			*//*
		}
		int lifeStolen = calcLifesteal(totalDamage);
		this.calcBurstSpeed();
		session.newMessage(String.format("You hit %s entities for %s damage each.", noOfEnemiesHit, damageRoll), Color.YELLOW);
		if (lifeStolen > 0) {
			session.newMessage(String.format("You heal for %s life.", lifeStolen), Color.GREEN);
		}*/
	}
	
	private void castFireball() {
		/*if (noTargetSelected()) { return; }
		if (outOfRange(15)) { return; }
		this.testCost("mana", 20);
		if (session.continueConfirm == true) { return; }
		//////////////////// DAMAGE CALCULATION ///////////////////////
		int hitRoll = ThreadLocalRandom.current().nextInt(1,101);
		if (hitRoll <= 90) { // 90% chance of a hit
			int damageRoll = magDamage;
			if (!(noMainEquipped())) {
				damageRoll += session.equipped.getMain().getMagDamage();
			}
			session.targetEntity.getStatus().changeStat("life", -(damageRoll));
			session.targetEntity.changeEffect(EffectType.BURNING, 3, 1);
			session.targetEntity.setDamagedByPlayer(true);
			if (!(noMainEquipped())) {
				session.targetEntity.setLastHitWith(session.equipped.getMain());
			}
			this.testforTurnHostile(session.targetEntity);
			int lifeStolen = calcLifesteal(damageRoll);
			this.calcBurstSpeed();
			session.newMessage(String.format("You slam the %s for %s damage.", session.targetEntity.getName(), damageRoll), Color.YELLOW);
			if (lifeStolen > 0) {
				session.newMessage(String.format("You heal for %s life.", lifeStolen), Color.GREEN);
			}
		} else {
			session.newMessage("You miss the hit.", Color.ORANGE);
		}*/
	}
	
	private void castImmolate() {
		/*this.testCost("mana", 20);
		if (session.continueConfirm == true) { return; }
		//////////////////// DAMAGE CALCULATION ///////////////////////
		int damageRoll = magDamage;
		if (!(noMainEquipped())) {
			damageRoll += session.equipped.getMain().getPhysDamage();
		}
		damageRoll *= 0.8; // Damage is decreased by 20% because the move is aoe
		int totalDamage = 0;
		int noOfEnemiesHit = 0;
		for (Enemy entity: session.entities.enemies.getAllEnemies()) {
			*//*
			if (entity.getPureDiff() <= 1.5) { // Can only hit enemies adjacent to the player, 1 tile range (including diagonally adjacent enemies)
				entity.getStatus().changeStat("life", -(damageRoll));
				entity.changeEffect(StatusEffect.BURNING, 4, 1);
				entity.setDamagedByPlayer(true);
				if (!(noMainEquipped())) {
					entity.setLastHitWith(session.equipped.getMain());
				}
				this.testforTurnHostile(entity);
				totalDamage += damageRoll;
				noOfEnemiesHit ++;
			}
			*//*
		}
		int lifeStolen = calcLifesteal(totalDamage);
		this.calcBurstSpeed();
		session.newMessage(String.format("You hit %s entities for %s damage each, also burning them.", noOfEnemiesHit, damageRoll), Color.YELLOW);
		if (lifeStolen > 0) {
			session.newMessage(String.format("You heal for %s life.", lifeStolen), Color.GREEN);
		}*/
	}
	
	private void castBlink() {
		/*this.testCost("mana", 20);
		if (session.continueConfirm == true) { return; }*/
	}
	
	private void castLightningBolt() {
		/*if (noTargetSelected()) { return; }
		if (outOfRange(20)) { return; }
		this.testCost("mana", 20);
		if (session.continueConfirm == true) { return; }
		//////////////////// DAMAGE CALCULATION ///////////////////////
		// 100% chance of a hit
		int damageRoll = magDamage;
		if (!(noMainEquipped())) {
			damageRoll += session.equipped.getMain().getMagDamage();
		}
		session.targetEntity.getStatus().changeStat("life", -(damageRoll));
		session.targetEntity.changeEffect(EffectType.STUNNED, 2, 1);
		session.targetEntity.setDamagedByPlayer(true);
		if (!(noMainEquipped())) {
			session.targetEntity.setLastHitWith(session.equipped.getMain());
		}
		this.testforTurnHostile(session.targetEntity);
		int lifeStolen = calcLifesteal(damageRoll);
		this.calcBurstSpeed();
		session.newMessage(String.format("You call lightning upon the %s for %s damage, also stunning them.", session.targetEntity.getName(), damageRoll), Color.YELLOW);
		if (lifeStolen > 0) {
			session.newMessage(String.format("You heal for %s life.", lifeStolen), Color.GREEN);
		}*/
	}
	
	private void castShroud() {
		/*this.testCost("mana", 15);
		if (session.continueConfirm == true) { return; }
		this.changeEffect(EffectType.INVISIBLE, 10, 1);*/
	}

	@Override
	public void onUpdate(PlayScreen session) {}

	@Override
	public void onInteract(PlayScreen session) {

	}

	@Override
	public void attack(Entity target, int range, PlayScreen session) {

	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public MobRace getMobRace() {
		return mobRace;
	}

	public void setMobRace(MobRace mobRace) {
		this.mobRace = mobRace;
	}

	public MobClass getMobClass() {
		return mobClass;
	}

	public void setMobClass(MobClass mobClass) {
		this.mobClass = mobClass;
	}

}
