package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Player;

public class Dodge extends Bang implements Playable{

	public Dodge(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}
	
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed){
		return false;
	}
}
