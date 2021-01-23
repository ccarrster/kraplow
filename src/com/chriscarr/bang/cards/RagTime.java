package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public class RagTime extends Card implements Playable {
	public RagTime(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#canPlay(com.chriscarr.bang.Player, java.util.List, int)
	 */
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed){			
		return player.getHand().size() >= 2;
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#targets(com.chriscarr.bang.Player, java.util.List)
	 */
	public List<Player> targets(Player player, List<Player> players){
		return Turn.othersWithCardsToTake(player, players);
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public boolean play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		//Choose card to discard
		int cardDiscard = userInterface.askDiscard(currentPlayer);
		if(cardDiscard == -1){
			return false;
		}
		//Choose player to take a card from
		Player otherPlayer = Turn.getValidChosenPlayer(currentPlayer, targets(currentPlayer, players), userInterface);
		//Steal from player
		if(!(otherPlayer instanceof CancelPlayer)){
			int chosenCard = -3;
			while(chosenCard < -2 || chosenCard > otherPlayer.getInPlay().size() - 1){
				chosenCard = userInterface.askOthersCard(currentPlayer, otherPlayer.getInPlay(), otherPlayer.getHand().size() > 0);
			}
			Hand hand = currentPlayer.getHand();
			if(chosenCard == -1){
				hand.add(otherPlayer.getHand().removeRandom());
				userInterface.printInfo(currentPlayer.getName() + " takes a card from " + otherPlayer.getName() + "'s hand with a "+this.getName());
			} else if(chosenCard == -2){
				Object card = otherPlayer.getInPlay().removeGun();
				hand.add(card);
				userInterface.printInfo(currentPlayer.getName() + " takes a " + ((Card)card).getName() + " from " + otherPlayer.getName() + " with a "+this.getName());
			} else {
				Object card = otherPlayer.getInPlay().remove(chosenCard);
				hand.add(card);
				userInterface.printInfo(currentPlayer.getName() + " takes a " + ((Card)card).getName() + " from " + otherPlayer.getName() + " with a "+this.getName());
			}
			//discard the card
			Hand currentHand = currentPlayer.getHand();
			Object card = currentHand.remove(cardDiscard);
			discard.add(card);
			discard.add(this);
			return true;
		} else {
			return false;
		}
	}
}
