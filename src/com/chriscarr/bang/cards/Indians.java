package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.userinterface.UserInterface;

public class Indians extends Card implements Playable {
	public Indians(String name, int suit, int value, int type) {
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
		Player indianPlayer = Turn.getNextPlayer(currentPlayer, players);
		while(indianPlayer != currentPlayer){
			Player nextPlayer = Turn.getNextPlayer(indianPlayer, players); 
			if(Figure.APACHEKID.equals(indianPlayer.getAbility()) && this.getSuit() == Card.DIAMONDS){
				userInterface.printInfo(indianPlayer.getName() + " is unaffected by diamond Indians");
				indianPlayer = nextPlayer; 
				continue;
			}
			int bangPlayed = Turn.validPlayBang(indianPlayer, userInterface);
			if(bangPlayed == -1){
				userInterface.printInfo(indianPlayer.getName() + " loses a health from " + currentPlayer.getName() + "'s " + Card.CARDINDIANS);
				turn.damagePlayer(indianPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
			} else {
				discard.add(indianPlayer.getHand().remove(bangPlayed));
				userInterface.printInfo(indianPlayer.getName() + " repels the attack from " + currentPlayer.getName() + "'s " + Card.CARDINDIANS);
				if(Figure.MOLLYSTARK.equals(indianPlayer.getAbility())){
					turn.deckToHand(indianPlayer.getHand(), deck, 1, userInterface);
					userInterface.printInfo(indianPlayer.getName() + " draws a card");
				}
			}
			indianPlayer = nextPlayer; 
		}
		return true;
	}
}
