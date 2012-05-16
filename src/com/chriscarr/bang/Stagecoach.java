package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class Stagecoach extends Card implements Playable {

	public Stagecoach(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	
	@Override
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed) {
		return true;
	}

	@Override
	public void play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard, Turn turn) {
		discard.add(this);
		Turn.deckToHand(currentPlayer.getHand(), deck, 2);
	}

	@Override
	public List<Player> targets(Player player, List<Player> players) {
		List<Player> targets = new ArrayList<Player>();
		targets.add(player);
		return targets;
	}

}
