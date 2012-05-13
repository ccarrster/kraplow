package com.chriscarr.bang;

import java.util.List;

public class TestGameState implements GameState {

	Turn turn;
	boolean gameOver = false;
	
	public TestGameState(Turn turn) {
		this.turn = turn;
	}
	
	public TestGameState(Turn turn, boolean gameOver) {
		this.turn = turn;
		this.gameOver = gameOver;
	}

	@Override
	public List<GameStatePlayer> getPlayers() {
		return turn.getGameStatePlayers();
	}

	@Override
	public GameStateCard discardTopCard() {
		return turn.getDiscardTopCard();
	}

	@Override
	public String getCurrentName() {
		return turn.getCurrentPlayer().getName();
	}

	@Override
	public int getDeckSize() {
		return turn.getDeckSize();
	}

	@Override
	public boolean isGameOver() {
		if(gameOver){
			return gameOver;
		} else {
			return turn.isGameOver();
		}
	}

}
