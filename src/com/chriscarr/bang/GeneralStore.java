package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class GeneralStore extends Card implements Playable {
	public GeneralStore(String name, int suit, int value, int type) {
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
		List<Object> generalStoreCards = new ArrayList<Object>();
		for(int i = 0; i < players.size(); i++){
			generalStoreCards.add(deck.pull());
		}
		Player generalPlayer = currentPlayer;
		while(!generalStoreCards.isEmpty()){
			int chosenCard = -1;
			while(chosenCard < 0 || chosenCard > generalStoreCards.size() - 1){
				chosenCard = userInterface.chooseGeneralStoreCard(generalPlayer, generalStoreCards);
			}
			generalPlayer.getHand().add(generalStoreCards.remove(chosenCard));
			generalPlayer = Turn.getNextPlayer(generalPlayer, players);
		}
	}
}
