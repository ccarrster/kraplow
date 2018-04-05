package com.chriscarr.bang.cards;

import java.util.List;
import java.util.ArrayList;

import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.Figure;
import com.chriscarr.bang.userinterface.UserInterface;

public class Whisky extends Card implements Playable {
	public Whisky(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#canPlay(com.chriscarr.bang.Player, java.util.List, int)
	 */
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed){			
		return player.getHand().size() >= 2;
	}
	
	@Override
	public List<Player> targets(Player player, List<Player> players) {
		List<Player> targets = new ArrayList<Player>();
		targets.add(player);
		return targets;
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public boolean play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		int cardDiscard = userInterface.askDiscard(currentPlayer);
		if(cardDiscard == -1){
			return false;
		}
		Hand currentHand = currentPlayer.getHand();
		Object card = currentHand.remove(cardDiscard);
		discard.add(card);
		discard.add(this);
		if(!Turn.isMaxHealth(currentPlayer)){
			currentPlayer.addHealth(1);
		}
		if(!Turn.isMaxHealth(currentPlayer)){
			currentPlayer.addHealth(1);
		}
		return true;
	}
}
