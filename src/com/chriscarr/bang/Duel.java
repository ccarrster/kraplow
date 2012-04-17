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
		if(bangsPlayed > 0 && !(player.getInPlay().hasGun() && player.getInPlay().isGunVolcanic()) && !Figure.WILLYTHEKID.equals(player.getName())){			
			return false;
		}
		return !targets(player, players).isEmpty();
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
	public void play(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard){
		discard.add(this);
		Player other = Turn.getValidChosenPlayer(currentPlayer, Turn.others(currentPlayer, players), userInterface);				
		boolean currentCalamityJanet = false;
		boolean otherCalamityJanet = false;
		if(Figure.CALAMITYJANET.equals(currentPlayer.getName())){
			currentCalamityJanet = true;
		} else if(Figure.CALAMITYJANET.equals(other.getName())){
			otherCalamityJanet = true;
		}
		boolean someoneIsShot = false;
		while(!someoneIsShot){
			if(otherCalamityJanet){
				someoneIsShot = !Turn.calamityBangOrMiss(other, players, currentPlayer, 1, deck, discard, userInterface);
			} else {
				if(!Turn.validPlayBang(other, other.countBangs(), userInterface)){
					Turn.damagePlayer(other, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
					someoneIsShot = true;
				} else {
					discard.add(other.getHand().removeBang());
				}
			}
			if(!someoneIsShot){						
				if(currentCalamityJanet){
					someoneIsShot = !Turn.calamityBangOrMiss(currentPlayer, players, currentPlayer, 1, deck, discard, userInterface);
				} else {
					int bangs = currentPlayer.countBangs();
					if(Turn.validPlayBang(currentPlayer, bangs, userInterface)){
						discard.add(currentPlayer.getHand().removeBang());
					} else {								
						Turn.damagePlayer(currentPlayer, players, currentPlayer, 1, other, deck, discard, userInterface);
						someoneIsShot = true;
					}
				}
			}
		}
	}
}
