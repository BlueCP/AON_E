package com.mygdx.game.playerattributes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.serialisation.KryoManager;

import java.util.Scanner;

public class MoveCollection {

	private Array<Move> allMoves = new Array<>();
	
	public MoveCollection() {
		
	}
	
	public void savePlayerMoves(String name) {
		/*
		try {
			Formatter dirN = new Formatter(Gdx.files.getLocalStoragePath() + "/saves/" + dir + "/playerMoves.txt");
			for (Move moveData: this.getAllMoves()) {
				dirN.format(String.valueOf(moveData.isLearned()) + "\r\n");
			}
			dirN.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		KryoManager.write(this, "saves/" + name + "/playerMoves.txt");
	}
	
	public static MoveCollection loadAll(String dir) {
		MoveCollection moveCollection = loadPlayerMoves(dir);
		moveCollection.loadAllMoves();
		return moveCollection;
	}
	
	private static MoveCollection loadPlayerMoves(String name) {
		/*
		try {
			File dirN = new File(Gdx.files.getLocalStoragePath() + "/saves/" + dir + "/playerMoves.txt");
			Scanner filePlayerMoves = new Scanner(dirN);
			for (int i = 0; i < this.getAllMoves().size(); i++) {
				if ("true".equals(filePlayerMoves.next())) {
					this.getAllMoves().get(i).setLearned(true);
					this.getAllMoves().get(i).setDisplayed(true);
				} else {
					this.getAllMoves().get(i).setLearned(false);
					this.getAllMoves().get(i).setDisplayed(false);
				}
			}
			filePlayerMoves.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		return KryoManager.read("saves/" + name + "/playerMoves.txt", MoveCollection.class);
	}
	
	public void loadAllMoves() {
		String allMovesFile = Gdx.files.internal("data/allMoves.txt").readString();
		Scanner fileAllMoves = new Scanner(allMovesFile);
		String dataBit;
		Move move = new Move();
		while (fileAllMoves.hasNextLine()) {
			dataBit = fileAllMoves.nextLine();
			if (";".equals(dataBit)) {
				this.getAllMoves().add(move); // Add the completed weapon to items
				if (fileAllMoves.hasNextLine()) {
					move = new Move();
					continue;
				} else {
					break;
				}
			}
			move.setName(dataBit);
			move.setDesc(fileAllMoves.nextLine());
			move.setReq(fileAllMoves.nextLine());
		}
		fileAllMoves.close();
	}
	
	public Move getMove(String name) {
		for (Move move: this.getAllMoves()) {
			if (move.getName().equals(name)) {
				return move;
			}
		}
		return null;
	}
	
	public void updateMoveCondit() {
		for (Move move: this.getAllMoves()) {
			switch (move.getName()) {
				case "Cleave":
					if (move.getCondition1() < 1) {
						move.setCondition2(0);
					}
					if (move.getCondition1() > 0) {
						move.setCondition1(move.getCondition1() - 1);
					}
					break;
			}
		}
	}
	
	public void updateMoves(long tick) {
		/*
		ArrayList<Move> unlearnedMoves = new ArrayList<Move>();
		for (Move move: this.getAllMoves()) {
			if (move.isLearned() == false) {
				unlearnedMoves.add(move);
			}
		}
		*/
		for (Move move: allMoves) {
			switch (move.getName()) {
				case "Slam":
					if (move.getCondition1() == 1) {
						move.setLearned(true);
					}
					break;
				case "Lunge":
					if (move.getCondition1() == 1) {
						move.setLearned(true);
					}
					break;
				case "Cleave":
					if (move.getCondition1() > 0 && move.getCondition2() == 1) {
						// Condition1: How many ticks in which the player can kill the second enemy
						// Condition2: Whether or not the player killed the second enemy
						move.setLearned(true);
					}
					break;
			}
			if (tick == 1 && move.isLearned()) {
				move.setDisplayed(true);
			}
		}
	}
	
	/*
	public void testforNewMoves(AsciiPanel terminal, PlayScreen session) {
		for (Move move: this.getAllMoves()) {
			if (move.isLearned() == true && move.isDisplayed() == false) {
				session.newSubMessage(String.format("New move learned: %s!", move.getName().toUpperCase()), Color.green);
				move.setDisplayed(true);
			}
		}
	}
	*/

	public Array<Move> getAllMoves() {
		return allMoves;
	}

	public void setAllMoves(Array<Move> allMoves) {
		this.allMoves = allMoves;
	}
	
}
