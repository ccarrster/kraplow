package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.Figure;
import com.chriscarr.bang.userinterface.UserInterface;

public class Brawl extends Card implements Playable {
	public Brawl(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#canPlay(com.chriscarr.bang.Player, java.util.List, int)
	 */
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed){			
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#targets(com.chriscarr.bang.Player, java.util.List)
	 */
	public List<Player> targets(Player player, List<Player> players){
		return Turn.others(player, players);
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
		
		//discard the card
		Hand currentHand = currentPlayer.getHand();
		Object discardCard = currentHand.remove(cardDiscard);
		discard.add(discardCard);
		discard.add(this);
		//Discard card from players
		Player brawlPlayer = Turn.getNextPlayer(currentPlayer, players);
		while(brawlPlayer != currentPlayer){
			Player nextPlayer = Turn.getNextPlayer(brawlPlayer, players);

			if(brawlPlayer.getInPlay().count() == 0 && !brawlPlayer.getInPlay().hasGun() && brawlPlayer.getHand().size() == 0){
				userInterface.printInfo(brawlPlayer.getName() + " has nothing to discard");
				continue;
			}

			int chosenCard = -3;
			while(chosenCard < -2 || chosenCard > brawlPlayer.getInPlay().size() - 1){
				chosenCard = userInterface.askOthersCard(currentPlayer, brawlPlayer.getInPlay(), brawlPlayer.getHand().size() > 0);
			}
			if(chosenCard == -1){
				Object card = brawlPlayer.getHand().removeRandom();
				discard.add(card);
				userInterface.printInfo(currentPlayer.getName() + " discards a " + ((Card)card).getName() + " from " + brawlPlayer.getName() + "'s hand with a Brawl");
			} else if(chosenCard == -2){
				Object card = brawlPlayer.getInPlay().removeGun(); 
				discard.add(card);
				userInterface.printInfo(currentPlayer.getName() + " discards a " + ((Card)card).getName() + " from " + brawlPlayer.getName() + " with a Brawl");
			} else {
				Object card = brawlPlayer.getInPlay().remove(chosenCard);
				discard.add(card);
				userInterface.printInfo(currentPlayer.getName() + " discards a " + ((Card)card).getName() + " from " + brawlPlayer.getName() + " with a Brawl");
			}
			brawlPlayer = nextPlayer;
		}
		
		return true;
	}
}
