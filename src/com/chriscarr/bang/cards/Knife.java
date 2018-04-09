package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public class Knife extends SingleUse implements Playable{

	public Knife(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	public List<Player> targets(Player player, List<Player> players){
		return Turn.getPlayersWithCards(Turn.getPlayersWithinRange(player, players, 1));
	}

	public boolean activate(Player currentPlayer, List<Player> players,
		UserInterface userInterface, Deck deck, Discard discard, Turn turn){

		boolean result = this.shoot(currentPlayer, players, userInterface, deck, discard, turn, true);
		if(result){
			removeFromInPlay(currentPlayer);
			discard.add(this);
		}
		return result;
	}

}
