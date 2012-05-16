package com.chriscarr.bang;

import java.util.List;

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
		return Turn.others(player, players);
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public void play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		discard.add(this);
		Player other = Turn.getValidChosenPlayer(currentPlayer, Turn.others(currentPlayer, players), userInterface);				
		while(true){
			int bangPlayed = Turn.validPlayBang(other, userInterface);
			if(bangPlayed == -1){
				turn.damagePlayer(other, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
				return;
			} else {
				discard.add(other.getHand().remove(bangPlayed));				
			}		
			int currentBangPlayed = Turn.validPlayBang(currentPlayer, userInterface);
			if(currentBangPlayed == -1){
				turn.damagePlayer(currentPlayer, players, currentPlayer, 1, null, deck, discard, userInterface);
				return;						
			} else {								
				discard.add(currentPlayer.getHand().remove(currentBangPlayed));
			}
		}
	}
}
