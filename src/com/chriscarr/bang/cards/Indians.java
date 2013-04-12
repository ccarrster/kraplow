package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
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
			int bangPlayed = Turn.validPlayBang(indianPlayer, userInterface);
			if(bangPlayed == -1){
				turn.damagePlayer(indianPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
				userInterface.printInfo(indianPlayer.getName() + " loses a health from " + currentPlayer.getName() + "'s " + Card.CARDINDIANS);
			} else {
				discard.add(indianPlayer.getHand().remove(bangPlayed));
				userInterface.printInfo(indianPlayer.getName() + " repels the attack from " + currentPlayer.getName() + "'s " + Card.CARDINDIANS);
			}
			indianPlayer = Turn.getNextPlayer(indianPlayer, players);
		}
		return true;
	}
}
