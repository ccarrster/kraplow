package com.chriscarr.bang.cards;

import java.util.List;
import java.util.ArrayList;

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
			ArrayList<Player> targetPlayer = new ArrayList<Player>();
			targetPlayer.add(gatlingPlayer);
			shoot(currentPlayer, targetPlayer, userInterface, deck, discard, turn, true);
			gatlingPlayer = nextPlayer;
		}
		return true;
	}
}
