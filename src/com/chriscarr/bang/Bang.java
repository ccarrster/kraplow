package com.chriscarr.bang;

import java.util.List;

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
		List<Player> others = Turn.getPlayersWithinRange(currentPlayer, players, currentPlayer.getInPlay().getGunRange());
		Player otherPlayer = Turn.getValidChosenPlayer(currentPlayer, others, userInterface);
		int missesRequired = 1;
		if(Figure.SLABTHEKILLER.equals(currentPlayer.getName())){
			missesRequired = 2;
		}
		int barrelMisses = Turn.isBarrelSave(otherPlayer, deck, discard, userInterface);
		missesRequired = missesRequired - barrelMisses;
		if(missesRequired <= 0){
			return;
		}
		if(Figure.CALAMITYJANET.equals(otherPlayer.getName())){
			Turn.calamityBangOrMiss(otherPlayer, players, currentPlayer, missesRequired, deck, discard, userInterface);
		} else {
			int misses = otherPlayer.countMisses();
			if(Turn.validPlayMiss(otherPlayer, misses, missesRequired, userInterface)){
				for(int i = 0; i < missesRequired; i++){
					discard.add(otherPlayer.getHand().removeMiss());
				}
			} else {
				Turn.damagePlayer(otherPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
			}
		}
	}
}
