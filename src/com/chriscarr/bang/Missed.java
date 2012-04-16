package com.chriscarr.bang;

import java.util.List;

public class Missed extends Bang implements Playable{

	public Missed(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}
	
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed){
		if(!Figure.CALAMITYJANET.equals(player.getName())){
			return false;
		} else {
			return super.canPlay(player, players, bangsPlayed);
		}
	}
}
