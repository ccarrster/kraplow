package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Player;

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
