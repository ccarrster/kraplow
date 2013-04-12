package com.chriscarr.bang.gamestate;

import java.util.List;

import com.chriscarr.bang.Turn;

public class GameStateImpl implements GameState {

	Turn turn;
	boolean gameOver = false;
	
	public GameStateImpl(Turn turn) {
		this.turn = turn;
	}
	
	public GameStateImpl(Turn turn, boolean gameOver) {
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
	
	public String timeout(){
		return turn.getTimeout();
	}

}
