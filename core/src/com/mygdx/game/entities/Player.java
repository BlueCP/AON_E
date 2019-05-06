package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.mobtypes.MobClass;
import com.mygdx.game.mobtypes.MobRace;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;
import com.mygdx.game.skills.SkillBar;
import com.mygdx.game.skills.cryomancer.BitingColdSkill;
import com.mygdx.game.skills.cryomancer.EncaseInIceSkill;
import com.mygdx.game.skills.cryomancer.FracturingBlastSkill;
import com.mygdx.game.skills.cryomancer.LastingChillSkill;
import com.mygdx.game.skills.electromancer.ChargeBuildupSkill;
import com.mygdx.game.skills.electromancer.ForkedLightningSkill;
import com.mygdx.game.skills.electromancer.SamePlaceThriceSkill;
import com.mygdx.game.skills.electromancer.VoltaicOverloadSkill;
import com.mygdx.game.skills.necromancer.*;
import com.mygdx.game.skills.paladin.*;
import com.mygdx.game.skills.pyromancer.FlamingBarrageSkill;
import com.mygdx.game.skills.pyromancer.StokeTheFlamesSkill;
import com.mygdx.game.skills.pyromancer.SupernovaSkill;
import com.mygdx.game.skills.pyromancer.VikingFuneralSkill;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
//	private MobRace mobRace;
//	private MobClass mobClass;
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

	private SamePlaceThriceSkill samePlaceThrice;
	private ForkedLightningSkill forkedLightning;
	private ChargeBuildupSkill chargeBuildup;
	private VoltaicOverloadSkill voltaicOverload;

	private SoulStealerSkill soulStealer;
	private UnderworldDenizenSkill underworldDenizen;
	private IntangibleGhostSkill intangibleGhost;
	private DrainEssenceSkill drainEssence;

	private DivineProtectionSkill divineProtectionSkill;
	private DivineRetaliationSkill divineRetaliationSkill;
	private DivinePunishmentSkill divinePunishmentSkill;
	private DivineBlessingSkill divineBlessingSkill;

	public Player() {
		id = 0;
		physicsId = 10000;
		
		super.name = "player";

		skillBar = new SkillBar();

		maxLife = 50;
		maxSpirit = 50;
		baseDefense = 5;
		
		baseWalkSpeed = 1.8f;

		baseDamage = 1;

//		System.out.println(inventory.weapons.size);

		inventory.addWeapon("Iron shortsword");
//		inventory.addWeapon("Iron shortsword");
//		newGameData.player.inventory.weapons.get(0).setDesc("Testing...");
		inventory.addOtherItem("Iron ore");

//		System.out.println(123456789);

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

		/*basicAttack = new CryomancerBasicAttack(this);

		skills.add(new IceShardSkill(this));
		skills.add(new BlizzardSkill(this));
		skills.add(new RapidDefrostingSkill(this));
		skills.add(new HailstormSkill(this));
		skills.add(new OverwhelmingFrostSkill(this));
		skills.add(new CryosleepSkill(this));
		skills.add(new ShatterSkill(this));
		skills.add(new GlacialWallSkill(this));*/


		// ELECTROMANCER

		/*basicAttack = new ElectromancerBasicAttack(this);

		skills.add(new ThunderstrikeSkill(this));
		skills.add(new StormcallerSkill(this));
		skills.add(new ReenergiseSkill(this));
		skills.add(new StaticShockSkill(this));
		skills.add(new RepulsionFieldSkill(this));
		skills.add(new CracklingOrbSkill(this));
		skills.add(new PowerGridSkill(this));
		skills.add(new EnergySurgeSkill(this));*/


		// NECROMANCER

		/*basicAttack = new NecromancerBasicAttack(this);

		skills.add(new RendSoulSkill(this));
		skills.add(new PossessSkill(this));
		skills.add(new RestlessDeadSkill(this));
		skills.add(new SiphoningStrikeSkill(this));
		skills.add(new DeathKnellSkill(this));
		skills.add(new UnearthlyMiasmaSkill(this));
		skills.add(new SacrificialPactSkill(this));
		skills.add(new SpiritSnareSkill(this));*/


		// PALADIN

		basicAttack = new PaladinBasicAttack(this);

		skills.add(new CleansingStrikeSkill(this));
		skills.add(new HolyFireSkill(this));
		skills.add(new RetributionSkill(this));
		skills.add(new SoothingLightSkill(this));
		skills.add(new DetainSkill(this));
		skills.add(new BlindingRaysSkill(this));
		skills.add(new EnforcementZoneSkill(this));
		skills.add(new SeraphicFlareSkill(this));


		// Note: the player must have all of the passives loaded in like this at the start of the game.
		// This is because we test for these passives, and only act if they have been learned.
		// The alternative is to make subclasses of player for each class. This would require copying all class-irrelevant
		// attributes to a new instance of that subclass when the player chooses a class, which could have serious
		// implications on game logic.
		vikingFuneral = new VikingFuneralSkill(this, false);
		stokeTheFlames = new StokeTheFlamesSkill(this, false);
		flamingBarrage = new FlamingBarrageSkill(this, false);
		supernovaSkill = new SupernovaSkill(this, false);

		lastingChill = new LastingChillSkill(this, false);
		bitingCold = new BitingColdSkill(this, false);
		encaseInIce = new EncaseInIceSkill(this, false);
		fracturingBlast = new FracturingBlastSkill(this, false);

		samePlaceThrice = new SamePlaceThriceSkill(this, false);
		forkedLightning = new ForkedLightningSkill(this, false);
		chargeBuildup = new ChargeBuildupSkill(this, false);
		voltaicOverload = new VoltaicOverloadSkill(this, false);

		soulStealer = new SoulStealerSkill(this, true);
		underworldDenizen = new UnderworldDenizenSkill(this, true);
		intangibleGhost = new IntangibleGhostSkill(this, true);
		drainEssence = new DrainEssenceSkill(this, true);

		divineProtectionSkill = new DivineProtectionSkill(this, false);
		divineRetaliationSkill = new DivineRetaliationSkill(this, false);
		divinePunishmentSkill = new DivinePunishmentSkill(this, false);
		divineBlessingSkill = new DivineBlessingSkill(this, false);
	}

	@Override
	void updatePassiveSkills(PlayScreen playScreen) {
		soulStealer.update();
		drainEssence.update();

		divineBlessingSkill.update();
	}

	@Override
	public float dealDamage(Entity entity, float damage) {
		float newDamage = damage;

		newDamage += stokeTheFlames.damage(entity, damage);

		newDamage += voltaicOverload.damage(entity, damage);

		newDamage += soulStealer.damage(damage);

		newDamage *= divineRetaliationSkill.damageBoost();
		newDamage *= divinePunishmentSkill.damage(entity);

//		entity.takeDamage(this, newDamage);
		newDamage = dealDamageBase(entity, newDamage);

		return newDamage;
	}

	@Override
	public void takeDamage(Entity entity, float damage) {
		float newDamage = damage;

		newDamage *= intangibleGhost.reduceDamage();

		takeDamageBase(entity, newDamage);

		divineBlessingSkill.recordDamageTaken();
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

		soulStealer.enemyKill();
	}

	@Override
	public void landAbility(Entity entity, PlayScreen playScreen) {
		vikingFuneral.testfor(entity);
		flamingBarrage.testfor();

		lastingChill.testfor(entity);

		samePlaceThrice.testfor(entity);
		chargeBuildup.testfor(entity);
	}

	@Override
	public float landAbilityDamage(Entity entity, float damage, PlayScreen playScreen) {
		forkedLightning.testfor(entity, damage, playScreen);

		float newDamage = 0; // Default to not dealing any extra damage, as damage will have already been done through dealDamage().

		newDamage += underworldDenizen.damage(damage);

		entity.takeDamage(this, newDamage);

		return 0; // No extra damage was added to entity; therefore, return 0.
	}

	@Override
	public void landBasicAttack(Entity entity, PlayScreen playScreen) {
		fracturingBlast.testfor(entity, playScreen);

		voltaicOverload.charge(entity);
	}

	@Override
	public float landBasicAttackDamage(Entity entity, float damage, PlayScreen playScreen) {
		soulStealer.basicAttackLifesteal(damage);
		return 0; // No extra damage was added to entity; therefore, return 0.
	}

	@Override
	void processAfterLoading() {
		processAfterLoadingBase();

		vikingFuneral.setEntity(this);
		stokeTheFlames.setEntity(this);
		flamingBarrage.setEntity(this);
		supernovaSkill.setEntity(this);

		lastingChill.setEntity(this);
		bitingCold.setEntity(this);
		encaseInIce.setEntity(this);
		fracturingBlast.setEntity(this);

		samePlaceThrice.setEntity(this);
		forkedLightning.setEntity(this);
		chargeBuildup.setEntity(this);
		voltaicOverload.setEntity(this);

		soulStealer.setEntity(this);
		underworldDenizen.setEntity(this);
		intangibleGhost.setEntity(this);
		drainEssence.setEntity(this);

		divineProtectionSkill.setEntity(this);
		divineRetaliationSkill.setEntity(this);
		divinePunishmentSkill.setEntity(this);
		divineBlessingSkill.setEntity(this);
	}

	/*
	 * Normal save, would be done from PlayScreen.
	 */
	public void saveAndExit() {
		try {
//			prepareForSaveAndExit();
			KryoManager.write(this, "saves/" + playerName + "/player.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
//			btRigidBody body = prepareForSave();
			KryoManager.write(this, "saves/" + playerName + "/player.txt");
//			rigidBody = body;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(Kryo kryo) {
		try {
			Output output = new Output(new FileOutputStream(Gdx.files.getLocalStoragePath() + "/saves/" + playerName + "/player.txt"));
			kryo.writeObject(output, this);
			output.close();
		} catch (FileNotFoundException e) {
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
			rigidBodyMatrix.setTranslation(new Vector3(0, 10f, 0));
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
//			player.loadRigidBody();
			player.processAfterLoading();
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
		
		universalUpdate(playScreen);
		individualUpdate(playScreen);
	}

	@Override
	public void individualUpdate(PlayScreen playScreen) {
		realDefense *= divineProtectionSkill.defenseBoost();
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
		damageRoll += this.eStats().getRealDamage();
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
		/*this.physDamage = this.eStats().getRealDamage();
		this.magDamage = this.eStats().getRealDamage();
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
		/*this.physDamage = this.eStats().getRealDamage();
		this.magDamage = this.eStats().getRealDamage();
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

}
