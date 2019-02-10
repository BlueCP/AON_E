package com.mygdx.game.playerattributes;

public class MoveBar {

	private Move[] moves;
	
	public MoveBar() {
		this.moves = new Move[10];
		for (int i = 0; i < 10; i ++) {
			this.moves[i] = null;
		}
	}

	public Move getMove(int i) {
		return moves[i - 1];
	}

	public void setMove(Move move, int i) {
		if (this.contains(move)) {
			this.moves[this.positionOf(move) - 1] = null;
		}
		this.moves[i - 1] = move;
	}
	
	public Move[] getMoves() {
		return moves;
	}
	
	public boolean contains(Move move) {
		for (int i = 0; i < 10; i ++) {
			if (moves[i] != null)
				if (move.getName().equals(moves[i].getName()))
					return true;
		}
		return false;
	}
	
	public int positionOf(Move move) {
		for (int i = 0; i < 10; i ++) {
			if (moves[i] != null)
				if (move.getName().equals(moves[i].getName()))
					return i + 1;
		}
		return -1; // To show that it is not there
	}
	
}
