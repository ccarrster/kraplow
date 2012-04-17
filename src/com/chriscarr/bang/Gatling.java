package com.chriscarr.bang;

import java.util.List;

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
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public void play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard){
		discard.add(this);
		Player gatlingPlayer = Turn.getNextPlayer(currentPlayer, players);
		while(gatlingPlayer != currentPlayer){
			if (Turn.isBarrelSave(gatlingPlayer, deck, discard, userInterface) > 0){
				return;
			}
			if(Figure.CALAMITYJANET.equals(gatlingPlayer.getName())){
				Turn.calamityBangOrMiss(gatlingPlayer, players, currentPlayer, 1, deck, discard, userInterface);
			} else {
				int misses = gatlingPlayer.countMisses();
				if(Turn.validPlayMiss(gatlingPlayer, misses, 1, userInterface)){
					discard.add(gatlingPlayer.getHand().removeMiss());
				} else {
					Turn.damagePlayer(gatlingPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
				}
			}
			gatlingPlayer = Turn.getNextPlayer(gatlingPlayer, players);
		}
	}
}
