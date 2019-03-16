package com.mygdx.game.utils;

import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;
import com.mygdx.game.screens.PlayScreen;

import java.util.ArrayList;

public class Util {
	
	private static int getEnd(int base, int num) {
		return Integer.parseInt(String.valueOf(base).substring(num));
	}
	
	public static int getId(int base) {
		return getEnd(base, 4);
	}
	
	public static int getPhysicsId(int id, String prefix) {
		return Integer.parseInt(prefix + id);
	}

	public static int getEntityId(int id) {
		return getPhysicsId(id, "1000");
	}

	public static int getProjectileId(int id) {
		return getPhysicsId(id, "2000");
	}

	public static int getParticleId(int id) {
		return getPhysicsId(id, "3000");
	}

	public static int getDroppedItemId(int id) { return getPhysicsId(id, "4000"); }


	public static int getImmobileTerrainId(int id) {
		return getPhysicsId(id, "1100");
	}

	public static int getImmobileControllerId(int id) {
		return getPhysicsId(id, "2100");
	}

	public static int getImmobileControllableId(int id) {
		return getPhysicsId(id, "3100");
	}

	public static int getImmobileInteractiveId(int id) {
		return getPhysicsId(id, "4100");
	}

	public static int getMobileTerrainId(int id) {
		return getPhysicsId(id, "1200");
	}

	public static int getMobileControllerId(int id) {
		return getPhysicsId(id, "2200");
	}

	public static int getMobileControllableId(int id) {
		return getPhysicsId(id, "3200");
	}

	public static int getMobileInteractiveId(int id) {
		return getPhysicsId(id, "4200");
	}


	public static float toAngle(String facing) {
		return Float.parseFloat(facing.substring(1).replace('_', '.'));
	}

	public static String toFacing(float angle) {
		return "N" + String.valueOf(angle).replace('.', '_');
	}

	public static Entity getEntity(int id, PlayScreen playScreen) {
		if (id == 0) {
			return playScreen.player;
		} else {
			return playScreen.entities.getEntity(id);
		}
	}

	public static Player getPlayer(int id, PlayScreen playScreen) {
		if (id == 0) {
			return playScreen.player;
		} else {
			return null;
		}
	}

	public static Player getPlayer(int id, Player player) {
		if (id == 0) {
			return player;
		} else {
			return null;
		}
	}

	/*public static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		file.delete();
	}*/
	
	/*public static void changeLine(String dir, String targetLine, String mode) throws FileNotFoundException {
		// Reading lines from target file
		File dirN = new File(dir);
		Scanner file = new Scanner(dirN);
		LinkedList<String> lines = new LinkedList<String>();
		while (file.hasNextLine()) {
			lines.add(file.nextLine());
		}
		if ("add".equals(mode)) {
			lines.add(targetLine);
		} else if ("remove".equals(mode)) {
			lines.remove(targetLine);
		}
		file.close();
		// Rewriting lines to target file
		Formatter fileWrite = new Formatter(dir);
		for (String line: lines) {
			fileWrite.format(line + "\r\n");
		}
		fileWrite.close();
	}*/
	
	/*public static String capitalise(String toCapitalise) {
		String capitalised;
		try {
			capitalised = toCapitalise.substring(0,1).toUpperCase() + toCapitalise.substring(1).toLowerCase();
		} catch (Exception e) {
			capitalised = toCapitalise;
		}
		return capitalised;
	}*/

	public static ArrayList<String> generateArrayList(String[] array) {
		ArrayList<String> list = new ArrayList<String>();
		for (String item: array) {
			list.add(item);
		}
		return list;
	}
	
	/*public static LinkedList<String> generateLinkedList(String[] array) {
		LinkedList<String> list = new LinkedList<String>();
		for (String item: array) {
			list.add(item);
		}
		return list;
	}*/
	
	/*public static HashSet<String> generateHashSet(String[] array) {
		HashSet<String> list = new HashSet<String>();
		for (String item: array) {
			list.add(item);
		}
		return list;
	}*/

}
