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

public class Duel extends Card implements Playable {
	public Duel(String name, int suit, int value, int type) {
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
		List<Player> targets = Turn.others(player, players);
		return targets;
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public boolean play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		discard.add(this);
		List<Player> targets = Turn.others(currentPlayer, players);
		CancelPlayer cancelPlayer = new CancelPlayer();
		targets.add(0, cancelPlayer);
		Player other = Turn.getValidChosenPlayer(currentPlayer, targets, userInterface);
		if(!(other instanceof CancelPlayer)){
			userInterface.printInfo(currentPlayer.getName() + " duels " + other.getName());
			while(true){
				int bangPlayed = Turn.validPlayBang(other, userInterface);
				if(bangPlayed == -1){
					turn.damagePlayer(other, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
					userInterface.printInfo(other.getName() + " loses a health");
					return true;
				} else {
					Object card = other.getHand().remove(bangPlayed);
					discard.add(card);
					userInterface.printInfo(other.getName() + " plays a " + ((Card)card).getName());
					if(Figure.MOLLYSTARK.equals(other.getName())){
						Hand otherHand = other.getHand();
						otherHand.add(deck.pull());
						userInterface.printInfo(other.getName() + " draws a card");
					}
				}		
				int currentBangPlayed = Turn.validPlayBang(currentPlayer, userInterface);
				if(currentBangPlayed == -1){
					turn.damagePlayer(currentPlayer, players, currentPlayer, 1, null, deck, discard, userInterface);
					userInterface.printInfo(currentPlayer.getName() + " loses a health");
					return true;						
				} else {		
					Object card = currentPlayer.getHand().remove(currentBangPlayed);
					discard.add(card);
					userInterface.printInfo(currentPlayer.getName() + " plays a " + ((Card)card).getName());
				}
			}
		} else {
			currentPlayer.getHand().add(this);
			return false;
		}
	}
}
