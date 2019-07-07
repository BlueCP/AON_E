package com.mygdx.game.serialisation;

import com.badlogic.gdx.utils.Queue;
import com.cyphercove.gdx.gdxtokryo.GdxToKryo;
import com.esotericsoftware.kryo.Kryo;
import com.mygdx.game.achievements.AchievementCollection;
import com.mygdx.game.entities.Dummy;
import com.mygdx.game.entities.Entities;
import com.mygdx.game.entities.Player;
import com.mygdx.game.particles.ParticleEngine;
import com.mygdx.game.physics.PhysicsManager;
import com.mygdx.game.projectiles.ProjectileManager;
import com.mygdx.game.quests.Quests;
import com.mygdx.game.screens.PlayScreen;

public class GameLoadingThread implements Runnable {

	private PlayScreen playScreen;

	public GameLoadingThread(PlayScreen playScreen) {
		this.playScreen = playScreen; // Keep a reference to playScreen.
	}

	@Override
	public void run() {
		Kryo kryo = new Kryo();

		GdxToKryo.registerAll(kryo);
		kryo.register(Queue.class, new MyQueueSerialiser());

		kryo.register(Player.class, new ObjectNoTransientsSerialiser(kryo, Player.class));
		kryo.register(Dummy.class, new ObjectNoTransientsSerialiser(kryo, Dummy.class));
		kryo.register(AchievementCollection.class, new ObjectNoTransientsSerialiser(kryo, AchievementCollection.class));
		kryo.register(Quests.class, new ObjectNoTransientsSerialiser(kryo, Quests.class));
		kryo.register(Entities.class, new ObjectNoTransientsSerialiser(kryo, Entities.class));
		kryo.register(ParticleEngine.class, new ObjectNoTransientsSerialiser(kryo, ParticleEngine.class));
		kryo.register(ProjectileManager.class, new ObjectNoTransientsSerialiser(kryo, ProjectileManager.class));
		kryo.register(PhysicsManager.class, new ObjectNoTransientsSerialiser(kryo, PhysicsManager.class));

		playScreen.loadGame(kryo);
	}

}
