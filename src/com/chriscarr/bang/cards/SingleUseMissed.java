package com.chriscarr.bang.cards;

import java.util.List;
import java.util.ArrayList;

import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.userinterface.UserInterface;

import java.util.logging.*;

public class SingleUseMissed extends SingleUse implements Playable{

	public SingleUseMissed(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	public boolean canPlay(Player player, List<Player> players, int bangsPlayed) {
		if(readyToPlay){
			return false;
		} 
		return !player.isInPlay(this.getName());
	}

	public List<Player> targets(Player player, List<Player> players){
		List<Player> targets = new ArrayList<Player>();
		targets.add(player);
		return targets;
	}

}
