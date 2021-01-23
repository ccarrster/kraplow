package com.chriscarr.bang.gamestate;

import java.util.List;

public interface GameState {

	List<GameStatePlayer> getPlayers();

	String getCurrentName();

	boolean isGameOver();

	int getDeckSize();

	GameStateCard discardTopCard();
	
	String timeout();

}
