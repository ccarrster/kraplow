package com.chriscarr.bang.test;

import java.util.ArrayList;
import java.util.List;

import com.chriscarr.bang.Hand;
import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.userinterface.UserInterface;

public class TestUserInterfaceCatBalu extends TestUserInterface implements
		UserInterface {

	@Override
	public int askDiscard(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	int banged = 0;
	@Override
	public int respondBang(Player player) {
		if(banged < 2){
			banged = banged + 1;
			return 0;
		} else {
			return -1;
		}
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
		return 0;
	}

	@Override
	public int askOthersCard(Player player, InPlay inPlay, boolean hasHand) {
		// TODO Auto-generated method stub
		return -1;
	}


	@Override
	public List<Object> chooseTwoDiscardForLife(Player sidKetchum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int respondMiss(Player miss) {
		// TODO Auto-generated method stub
		return 0;
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
	
	@Override
	public List<Object> respondTwoMiss(Player player) {
		List<Object> result = new ArrayList<Object>();
		Hand hand = player.getHand();
		result.add(hand.get(0));
		result.add(hand.get(1));
		return result;
	}
}
