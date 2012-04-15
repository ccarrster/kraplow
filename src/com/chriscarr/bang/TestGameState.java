package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class TestGameState implements GameState {

	@Override
	public List<GameStatePlayer> getPlayers() {
		// TODO Auto-generated method stub
		return new ArrayList<GameStatePlayer>();
	}

	@Override
	public GameStateCard discardTopCard() {
		// TODO Auto-generated method stub
		return new GameStateCard();
	}

	@Override
	public String getCurrentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDeckSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getWinners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return false;
	}

}
