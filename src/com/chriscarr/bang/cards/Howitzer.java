package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public class Howitzer extends SingleUse implements Playable{

	public Howitzer(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	public List<Player> targets(Player player, List<Player> players){
		return Turn.others(player, players);
	}

	public boolean activate(Player currentPlayer, List<Player> players,
		UserInterface userInterface, Deck deck, Discard discard, Turn turn){

		discard.add(this);
		removeFromInPlay(currentPlayer);
		Player gatlingPlayer = Turn.getNextPlayer(currentPlayer, players);
		while(gatlingPlayer != currentPlayer){
			Player nextPlayer = Turn.getNextPlayer(gatlingPlayer, players);
			shoot(currentPlayer, players, userInterface, deck, discard, turn, true, gatlingPlayer);
			gatlingPlayer = nextPlayer;
		}
		return true;
	}
}
