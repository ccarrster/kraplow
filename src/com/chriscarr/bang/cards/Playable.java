package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public interface Playable {

	public abstract boolean canPlay(Player player, List<Player> players,
			int bangsPlayed);

	public abstract List<Player> targets(Player player, List<Player> players);

	public abstract void play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard, Turn turn);

}