package com.chriscarr.bang.cards;

import java.util.ArrayList;
import java.util.List;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;
import com.chriscarr.bang.Figure;

public class Beer extends Card implements Playable {

	public Beer(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	@Override
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed) {
		return true;
	}

	@Override
	public boolean play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard, Turn turn) {
		discard.add(this);
		if(Turn.isBeerGiveHealth(players)){
			if(!Turn.isMaxHealth(currentPlayer)){
				currentPlayer.addHealth(1);
			}
			if(Figure.TEQUILAJOE.equals(currentPlayer.getName())){
				if(!Turn.isMaxHealth(currentPlayer)){
					currentPlayer.addHealth(1);
				}	
			}
		}
		return true;
	}

	@Override
	public List<Player> targets(Player player, List<Player> players) {
		List<Player> targets = new ArrayList<Player>();
		targets.add(player);
		return targets;
	}

}
