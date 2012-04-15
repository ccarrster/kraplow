package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class GameStatePlayer {

	public String getName() {
		return "";
	}

	public String getSpecialAbility() {
		return "";
	}

	public int getHealth() {
		return 0;
	}

	public int getMaxHealth() {
		return 0;
	}

	public int getHandSize() {
		return 0;
	}

	public List<GameStateCard> getCardsInPlay() {
		return new ArrayList<GameStateCard>();
	}

	public boolean isSheriff() {
		return false;
	}

	public String getGunName() {
		return "";
	}

}
