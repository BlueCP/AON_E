package com.mygdx.game.droppeditems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mygdx.game.items.ItemFactory;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.serialisation.KryoManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DroppedItemManager implements Disposable {

	public Array<DroppedItem> droppedItems;

	public DroppedItemManager() {
		droppedItems = new Array<>();
	}

	public void addDroppedWeapon(String name, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		droppedItems.add(new DroppedWeapon(ItemFactory.createWeapon(name), pos, dynamicsWorld));
	}

	public void addDroppedArmour(String name, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		droppedItems.add(new DroppedArmour(ItemFactory.createArmour(name), pos, dynamicsWorld));
	}

	public void addDroppedEquipment(String name, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		droppedItems.add(new DroppedEquipment(ItemFactory.createEquipment(name), pos, dynamicsWorld));
	}

	public void addDroppedConsumable(String name, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		droppedItems.add(new DroppedConsumable(ItemFactory.createConsumableItem(name), pos, dynamicsWorld));
	}

	public void addDroppedOtherItem(String name, Vector3 pos, btDynamicsWorld dynamicsWorld) {
		droppedItems.add(new DroppedOtherItem(ItemFactory.createOtherItem(name), pos, dynamicsWorld));
	}

	public void addDroppedWeaponFlyUp(String name, Vector3 pos, float speed, btDynamicsWorld dynamicsWorld) {
		DroppedWeapon droppedWeapon = new DroppedWeapon(ItemFactory.createWeapon(name), pos, dynamicsWorld);
		droppedWeapon.rigidBody.setLinearVelocity(
				new Vector3(0, 1, 0).add(new Vector3().setToRandomDirection().scl(0.2f)).nor().scl(speed));
		droppedItems.add(droppedWeapon);
	}

	public void addDroppedArmourFlyUp(String name, Vector3 pos, float speed, btDynamicsWorld dynamicsWorld) {
		DroppedArmour droppedArmour = new DroppedArmour(ItemFactory.createArmour(name), pos, dynamicsWorld);
		droppedArmour.rigidBody.setLinearVelocity(
				new Vector3(0, 1, 0).add(new Vector3().setToRandomDirection().scl(0.2f)).nor().scl(speed));
		droppedItems.add(droppedArmour);
	}

	public void addDroppedEquipmentFlyUp(String name, Vector3 pos, float speed, btDynamicsWorld dynamicsWorld) {
		DroppedEquipment droppedEquipment = new DroppedEquipment(ItemFactory.createEquipment(name), pos, dynamicsWorld);
		droppedEquipment.rigidBody.setLinearVelocity(
				new Vector3(0, 1, 0).add(new Vector3().setToRandomDirection().scl(0.2f)).nor().scl(speed));
		droppedItems.add(droppedEquipment);
	}

	public void addDroppedConsumableFlyUp(String name, Vector3 pos, float speed, btDynamicsWorld dynamicsWorld) {
		DroppedConsumable droppedConsumable = new DroppedConsumable(ItemFactory.createConsumableItem(name), pos, dynamicsWorld);
		droppedConsumable.rigidBody.setLinearVelocity(
				new Vector3(0, 1, 0).add(new Vector3().setToRandomDirection().scl(0.2f)).nor().scl(speed));
		droppedItems.add(droppedConsumable);
	}

	public void addDroppedOtherItemFlyUp(String name, Vector3 pos, float speed, btDynamicsWorld dynamicsWorld) {
		DroppedOtherItem droppedOtherItem = new DroppedOtherItem(ItemFactory.createOtherItem(name), pos, dynamicsWorld);
		droppedOtherItem.rigidBody.setLinearVelocity(
				new Vector3(0, 1, 0).add(new Vector3().setToRandomDirection().scl(0.2f)).nor().scl(speed));
		droppedItems.add(droppedOtherItem);
	}

	public void update(float delta, PlayScreen playScreen) {
		for (int i = 0; i < droppedItems.size; i ++) {
			DroppedItem droppedItem = droppedItems.get(i);

			droppedItem.universalUpdate(playScreen);
		}
	}

	public DroppedItem get(int id) {
		for (DroppedItem droppedItem: droppedItems) {
			if (droppedItem.getId() == id) {
				return droppedItem;
			}
		}
		return null;
	}

	public void saveAndExit(String dir) {
		try {
			KryoManager.write(this, "saves/" + dir + "/droppedItems.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(String dir) {
		try {
			KryoManager.write(this, "saves/" + dir + "/droppedItems.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(String name, Kryo kryo) {
		try {
			Output output = new Output(new FileOutputStream(Gdx.files.getLocalStoragePath() + "/saves/" + name + "/droppedItems.txt"));
			kryo.writeObject(output, this);
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static DroppedItemManager load(String dir) {
		try {
			DroppedItemManager droppedItemManager = KryoManager.read("saves/" + dir + "/droppedItems.txt", DroppedItemManager.class);

			for (DroppedItem droppedItem: droppedItemManager.droppedItems) {
				droppedItem.processAfterLoading();
			}
			return droppedItemManager;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void dispose() {
		for (DroppedItem droppedItem: droppedItems) {
			droppedItem.rigidBody.dispose();
		}
	}

}
