package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public class Bang extends Card implements Playable {
	public Bang(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#canPlay(com.chriscarr.bang.Player, java.util.List, int)
	 */
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed){			
		if(bangsPlayed > 0 && !(player.getInPlay().hasGun() && player.getInPlay().isGunVolcanic()) && !Figure.WILLYTHEKID.equals(player.getName())){			
			return false;
		}
		return targets(player, players).size() > 1;
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#targets(com.chriscarr.bang.Player, java.util.List)
	 */
	public List<Player> targets(Player player, List<Player> players){
		return Turn.getPlayersWithinRange(player, players, player.getGunRange());
	}
	
	/* (non-Javadoc)
	 * @see com.chriscarr.bang.Playable#play(com.chriscarr.bang.Player, java.util.List, com.chriscarr.bang.UserInterface, com.chriscarr.bang.Deck, com.chriscarr.bang.Discard)
	 */
	public boolean play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		discard.add(this);
		List<Player> others = Turn.getPlayersWithinRange(currentPlayer, players, currentPlayer.getInPlay().getGunRange());
		Player otherPlayer = Turn.getValidChosenPlayer(currentPlayer, others, userInterface);
		if(!(otherPlayer instanceof CancelPlayer)){
			userInterface.printInfo(currentPlayer.getName() + " Shoots " + otherPlayer.getName());
			int missesRequired = 1;
			if(Figure.SLABTHEKILLER.equals(currentPlayer.getName())){
				missesRequired = 2;
			}
			int barrelMisses = Turn.isBarrelSave(otherPlayer, deck, discard, userInterface, missesRequired);
			missesRequired = missesRequired - barrelMisses;
			if(missesRequired <= 0){
				return true;
			} else if(missesRequired == 1){
				int missPlayed = Turn.validPlayMiss(otherPlayer, userInterface); 
				if(missPlayed == -1){
					turn.damagePlayer(otherPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
					userInterface.printInfo(otherPlayer.getName() + " is loses a health.");
				} else {
					for(int i = 0; i < missesRequired; i++){
						discard.add(otherPlayer.getHand().remove(missPlayed));
						userInterface.printInfo(otherPlayer.getName() + " plays a Missed!");
					}
				}
			} else if(missesRequired == 2){
				Hand hand = otherPlayer.getHand();
				List<Object> cardsToDiscard = null;			
				cardsToDiscard = Turn.validRespondTwoMiss(otherPlayer, userInterface);			
				if(cardsToDiscard.size() == 0){
					turn.damagePlayer(otherPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
					userInterface.printInfo(otherPlayer.getName() + " is loses a health.");
				} else {
					for(Object card : cardsToDiscard){
						hand.remove(card);
						discard.add(card);
						userInterface.printInfo(otherPlayer.getName() + " plays a Missed!");
					}
				}	
			}
			return true;
		} else {
			currentPlayer.getHand().add(this);
			return false;
		}
	}
}
