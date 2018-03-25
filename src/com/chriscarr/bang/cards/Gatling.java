package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.userinterface.UserInterface;

public class Gatling extends Card implements Playable {
	public Gatling(String name, int suit, int value, int type) {
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
		discard.add(this);
		Player gatlingPlayer = Turn.getNextPlayer(currentPlayer, players);
		while(gatlingPlayer != currentPlayer){
			Player nextPlayer = Turn.getNextPlayer(gatlingPlayer, players);
			if (Turn.isBarrelSave(gatlingPlayer, deck, discard, userInterface, 1) > 0){
				gatlingPlayer = nextPlayer;
				continue;
			}
			int missPlayed = Turn.validPlayMiss(gatlingPlayer, userInterface);
			if(missPlayed == -1){
				turn.damagePlayer(gatlingPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
				userInterface.printInfo(gatlingPlayer.getName() + " loses a health from " + currentPlayer.getName() + "'s " + Card.CARDGATLING);
			} else {
				discard.add(gatlingPlayer.getHand().remove(missPlayed));
				userInterface.printInfo(gatlingPlayer.getName() + " is missed by " + currentPlayer.getName() + "'s " + Card.CARDGATLING);
				if(Figure.MOLLYSTARK.equals(gatlingPlayer.getName())){
					Hand otherHand = gatlingPlayer.getHand();
					otherHand.add(deck.pull());
					userInterface.printInfo(gatlingPlayer.getName() + " draws a card");
				}
			}
			gatlingPlayer = nextPlayer;
		}
		return true;
	}
}
