package com.chriscarr.bang.cards;

import java.util.List;

import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public class Jail extends Card implements Playable {

	public Jail(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	@Override
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed) {
		return targets(player, players).size() > 1;
	}

	@Override
	public boolean play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard, Turn turn) {
		Object target = Turn.getValidChosenPlayer(currentPlayer, targets(currentPlayer, players), userInterface);
		if(!(target instanceof CancelPlayer)){
			if (Figure.JOHNNYKISCH.equals(currentPlayer.getAbility())) {
				for (Player player : players) {
					int inPlayCount = player.getInPlay().count();
					for(int inPlayIndex = 0; inPlayIndex < inPlayCount; inPlayIndex++){
						Card peeked = (Card)player.getInPlay().peek(inPlayIndex);
						if(peeked.getName() == this.getName()){
							Card removed = (Card)player.getInPlay().remove(inPlayIndex);
							discard.add(removed);
							userInterface.printInfo(currentPlayer.getName() + " plays a " + this.getName() + " and forces " + player.getName() + " to discard one from play.");
						}
					}
				}
			}

			Player targetPlayer = (Player)target;
			targetPlayer.addInPlay(this);
			userInterface.printInfo(currentPlayer.getName() + " put " + targetPlayer.getName() + " in jail.");
			return true;
		} else {
			currentPlayer.getHand().add(this);
			return false;
		}
	}

	@Override
	public List<Player> targets(Player player, List<Player> players) {
		return Turn.getJailablePlayers(player, players);
	}

}
