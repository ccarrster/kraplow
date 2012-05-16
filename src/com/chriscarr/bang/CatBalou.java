package com.chriscarr.bang;

import java.util.List;

public class CatBalou extends Card implements Playable {
	public CatBalou(String name, int suit, int value, int type) {
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
		return Turn.othersWithCardsToTake(player, players);
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public void play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		discard.add(this);
		Player other = Turn.getValidChosenPlayer(currentPlayer, targets(currentPlayer, players), userInterface);
		int chosenCard = -3;
		while(chosenCard < -2 || chosenCard > other.getInPlay().size() - 1){
			chosenCard = userInterface.askOthersCard(currentPlayer, other.getInPlay(), other.getHand().size() > 0);
		}
		if(chosenCard == -1){
			discard.add(other.getHand().removeRandom());
		} else if(chosenCard == -2){
			discard.add(other.getInPlay().removeGun());
		} else {
			discard.add(other.getInPlay().remove(chosenCard));
		}
	}
}
