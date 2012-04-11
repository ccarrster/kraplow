package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class SomePlayers {

	private List<PlayerClassGroup> players = new ArrayList<PlayerClassGroup>();
	
	public boolean contains(PlayerClassGroup player) {
		return players.contains(player);
	}

	public void add(PlayerClassGroup player) {
		players.add(player);
	}

	public void remove(PlayerClassGroup player) {
		players.remove(player);
	}


	
}
