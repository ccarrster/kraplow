package com.chriscarr.bang;

import java.util.List;

public class WellsFargo extends Card implements Playable {

	public WellsFargo(String name, int suit, int value, int type) {
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
		Turn.deckToHand(currentPlayer.getHand(), deck, 3);
	}

	@Override
	public List<Player> targets(Player player, List<Player> players) {
		return null;
	}

}
