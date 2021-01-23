package com.chriscarr.bang.cards;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.userinterface.UserInterface;

public class SingleUse extends Card implements Playable{

	protected boolean readyToPlay = false;

	public SingleUse(String name, int suit, int value, int type) {
		super(name, suit, value, type);
	}

	public boolean canPlay(Player player, List<Player> players, int bangsPlayed) {
		if(readyToPlay){
			return true;
		}
		return !player.isInPlay(this.getName());
	}

	public boolean play(Player currentPlayer, List<Player> players,
		UserInterface userInterface, Deck deck, Discard discard, Turn turn) {
		if(readyToPlay){
			Logger logger = Logger.getLogger(SingleUse.class.getName());
			logger.log(Level.SEVERE, "Playing Ready To Play");
			return activate(currentPlayer, players, userInterface, deck, discard, turn);
		} else {
			currentPlayer.addInPlay(this);
			readyToPlay = false;
			return true;
		}
	}

	public void setReadyToPlay(boolean readyToPlay){
		this.readyToPlay = readyToPlay;
	}

	public boolean activate(Player currentPlayer, List<Player> players,
		UserInterface userInterface, Deck deck, Discard discard, Turn turn){
		return false;
	}

	public void removeFromInPlay(Player currentPlayer){
		InPlay currentInPlay = currentPlayer.getInPlay();
		for(int i = 0; i < currentInPlay.size(); i++){
			if(currentInPlay.get(i) == this){
				currentInPlay.remove(i);
			}
		}
	}

}
