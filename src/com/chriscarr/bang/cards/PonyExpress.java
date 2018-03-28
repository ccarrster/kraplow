package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.userinterface.UserInterface;

import java.util.logging.*;

public class PonyExpress extends SingleUse implements Playable{

	public PonyExpress(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	public List<Player> targets(Player player, List<Player> players){
		return Turn.othersWithCardsToTake(player, players);
	}

	public boolean activate(Player currentPlayer, List<Player> players,
		UserInterface userInterface, Deck deck, Discard discard, Turn turn){

		Hand currentHand = currentPlayer.getHand();
		currentHand.add(deck.pull());
		currentHand.add(deck.pull());
		currentHand.add(deck.pull());
		userInterface.printInfo(currentPlayer.getName() + " draws 3 cards");
		removeFromInPlay(currentPlayer);
		discard.add(this);
		return true;
	}

}
