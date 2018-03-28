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

public class Pepperbox extends SingleUse implements Playable{

	public Pepperbox(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	public List<Player> targets(Player player, List<Player> players){
		return Turn.getPlayersWithCards(Turn.getPlayersWithinRange(player, players, player.getGunRange()));
	}

	public boolean activate(Player currentPlayer, List<Player> players,
		UserInterface userInterface, Deck deck, Discard discard, Turn turn){

		Player otherPlayer = Turn.getValidChosenPlayer(currentPlayer, targets(currentPlayer, players), userInterface);
		if(!(otherPlayer instanceof CancelPlayer)){
			
			userInterface.printInfo(currentPlayer.getName() + " Shoots " + otherPlayer.getName() + " with "+this.getName());
			int missesRequired = 1;
			int barrelMisses = Turn.isBarrelSave(otherPlayer, deck, discard, userInterface, missesRequired, currentPlayer);
			missesRequired = missesRequired - barrelMisses;
			if(missesRequired <= 0){
				return true;
			} else if(missesRequired == 1){
				int missPlayed = Turn.validPlayMiss(otherPlayer, userInterface); 
				if(missPlayed == -1){
					turn.damagePlayer(otherPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
					userInterface.printInfo(otherPlayer.getName() + " is loses a health.");
				} else {
					for(int i = 0; i < missesRequired; i++){
						Card missCard = (Card)otherPlayer.getHand().remove(missPlayed);
						discard.add(missCard);
						if(missCard.getName().equals(CARDDODGE)){
							Hand otherHand = otherPlayer.getHand();
							otherHand.add(deck.pull());
							userInterface.printInfo(otherPlayer.getName() + " dodged " + currentPlayer.getName() + "'s " + Card.CARDBANG + " and draws a card");
						} else {
							userInterface.printInfo(otherPlayer.getName() + " plays a Missed!");
						}
						if(Figure.MOLLYSTARK.equals(otherPlayer.getAbility())){
							Hand otherHand = otherPlayer.getHand();
							otherHand.add(deck.pull());
							userInterface.printInfo(otherPlayer.getName() + " draws a card");
						}
					}
				}
			}

			removeFromInPlay(currentPlayer);
			discard.add(this);
			return true;
		} else {
			return false;
		}
	}

}
