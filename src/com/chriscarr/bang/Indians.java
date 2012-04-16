package com.chriscarr.bang;

import java.util.List;

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
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public void play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard){
		Player indianPlayer = Turn.getNextPlayer(currentPlayer, players);
		while(indianPlayer != currentPlayer){
			if(Figure.CALAMITYJANET.equals(indianPlayer.getName())){
				Turn.calamityBangOrMiss(indianPlayer, players, currentPlayer, 1, deck, discard, userInterface);
			} else {
				if(Turn.validPlayBang(indianPlayer, indianPlayer.countBangs(), userInterface)){
					discard.add(indianPlayer.getHand().removeBang());
				} else {
					Turn.damagePlayer(indianPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
				}
			}
			indianPlayer = Turn.getNextPlayer(indianPlayer, players);
		}
	}
}
