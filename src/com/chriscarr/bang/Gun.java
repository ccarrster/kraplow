package com.chriscarr.bang;

import java.util.List;

public class Gun extends Card {
	public Gun(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}
	
	public void play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard) {
		if(currentPlayer.hasGun()){			
			discard.add(currentPlayer.removeGun());
		}
		currentPlayer.setGun(this);
	}
}
