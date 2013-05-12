package com.chriscarr.bang.test;

import java.util.List;

import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.userinterface.UserInterface;

public class TestPlayOneUserInterfaceChoosePlayer1 extends TestUserInterface implements UserInterface {

	public int askDiscard(Player player) {
		return 0;
	}

	public int askPlay() {
		return 0;
	}
	
	public int choosePlayer(List<Player> players){
		return 0;
	}

	public boolean respondBang(Player indianPlayer, int bangs) {
		return false;
	}

	public boolean respondMiss(Player gatlingPlayer, int misses) {
		return false;
	}

	@Override
	public int chooseGeneralStoreCard(Player generalPlayer,
			List<Object> generalStoreCards) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int askPlayer(Player player, List<String> otherPlayers) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int askOthersCard(Player player, InPlay inPlay, boolean hasHand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object> chooseTwoDiscardForLife(Player sidKetchum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int askPlay(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean chooseDiscard(Player player, Object card) {
		// TODO Auto-generated method stub
		return false;
	}
	public void printInfo(String info){
		//do nothing
	}

	@Override
	public boolean chooseFromPlayer(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int chooseDrawCard(Player player, List<Object> cards) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseCardToPutBack(Player player, List<Object> cards) {
		// TODO Auto-generated method stub
		return 0;
	}
}
