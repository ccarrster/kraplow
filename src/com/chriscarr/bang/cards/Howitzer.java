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

public class Howitzer extends SingleUse implements Playable{

	public Howitzer(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	public List<Player> targets(Player player, List<Player> players){
		return Turn.others(player, players);
	}

	public boolean activate(Player currentPlayer, List<Player> players,
		UserInterface userInterface, Deck deck, Discard discard, Turn turn){

		Player gatlingPlayer = Turn.getNextPlayer(currentPlayer, players);
		while(gatlingPlayer != currentPlayer){
			Player nextPlayer = Turn.getNextPlayer(gatlingPlayer, players);
			if (Turn.isBarrelSave(gatlingPlayer, deck, discard, userInterface, 1, currentPlayer) > 0){
				gatlingPlayer = nextPlayer;
				continue;
			}
			int missPlayed = Turn.validPlayMiss(gatlingPlayer, userInterface);
			if(missPlayed == -1){
				turn.damagePlayer(gatlingPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
				userInterface.printInfo(gatlingPlayer.getName() + " loses a health from " + currentPlayer.getName() + "'s " + this.getName());
			} else {
				Card missCard = (Card)gatlingPlayer.getHand().remove(missPlayed);
				discard.add(missCard);
				if(missCard.getName().equals(CARDDODGE)){
					Hand otherHand = gatlingPlayer.getHand();
					otherHand.add(deck.pull());
					userInterface.printInfo(gatlingPlayer.getName() + " dodged " + currentPlayer.getName() + "'s " + this.getName() + " and draws a card");
				} else {
					userInterface.printInfo(gatlingPlayer.getName() + " is missed by " + currentPlayer.getName() + "'s " + this.getName());	
				}	
				if(Figure.MOLLYSTARK.equals(gatlingPlayer.getAbility())){
					Hand otherHand = gatlingPlayer.getHand();
					otherHand.add(deck.pull());
					userInterface.printInfo(gatlingPlayer.getName() + " draws a card");
				}
			}
			gatlingPlayer = nextPlayer;
		}
		removeFromInPlay(currentPlayer);
		discard.add(this);
		return true;
	}
}
