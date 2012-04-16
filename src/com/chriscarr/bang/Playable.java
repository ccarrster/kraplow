package com.chriscarr.bang;

import java.util.List;

public interface Playable {

	public abstract boolean canPlay(Player player, List<Player> players,
			int bangsPlayed);

	public abstract List<Player> targets(Player player, List<Player> players);

	public abstract void play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard);

}