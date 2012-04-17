package com.chriscarr.bang;

import java.util.List;

public class Saloon extends Card implements Playable {

	public Saloon(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	@Override
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed) {
		return true;
	}

	@Override
	public void play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard) {
		discard.add(this);
		for(Player player : players){
			if(!Turn.isMaxHealth(player)){
				player.addHealth(1);
			}
		}
	}

	@Override
	public List<Player> targets(Player player, List<Player> players) {
		return players;
	}

}
