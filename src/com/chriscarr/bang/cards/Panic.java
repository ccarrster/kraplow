package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public class Panic extends Card implements Playable {
	public Panic(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#canPlay(com.chriscarr.bang.Player, java.util.List, int)
	 */
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed){			
		List<Player> others = targets(player, players);
		return !others.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#targets(com.chriscarr.bang.Player, java.util.List)
	 */
	public List<Player> targets(Player player, List<Player> players){
		return Turn.getPlayersWithCards(Turn.getPlayersWithinRange(player, players, 1));
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public void play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		discard.add(this);
		Player otherPlayer = Turn.getValidChosenPlayer(currentPlayer, targets(currentPlayer, players), userInterface);
		int chosenCard = -3;
		while(chosenCard < -2 || chosenCard > otherPlayer.getInPlay().size() - 1){
			chosenCard = userInterface.askOthersCard(currentPlayer, otherPlayer.getInPlay(), otherPlayer.getHand().size() > 0);
		}
		Hand hand = currentPlayer.getHand();
		if(chosenCard == -1){
			hand.add(otherPlayer.getHand().removeRandom());
		} else if(chosenCard == -2){
			hand.add(otherPlayer.getInPlay().removeGun());
		} else {
			hand.add(otherPlayer.getInPlay().remove(chosenCard));
		}
	}
}
