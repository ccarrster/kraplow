package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class TestUserInterface implements UserInterface {

	public int askDiscard(Player player) {
		return 0;
	}

	public int askPlay() {
		return -1;
	}

	@Override
	public boolean respondBang(Player indianPlayer, int bangs) {
		// TODO Auto-generated method stub
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
		return 0;
	}

	@Override
	public int askOthersCard(Player player, InPlay inPlay, boolean hasHand) {
		if(!inPlay.isEmpty()){
			return 0;
		} else if(inPlay.hasGun()){
			return -2;			
		} else {
			return -1;
		}
		
	}

	public int respondBeer(Player player) {
		if(player.countBeers() > 0){
			return 0;
		} else {
			return -1;
		}
	}


	@Override
	public List<Object> chooseTwoDiscardForLife(Player player) {
		List<Object> cards = new ArrayList<Object>();
		Hand hand = player.getHand();
		cards.add(hand.get(0));
		cards.add(hand.get(1));
		return cards;
	}

	@Override
	public int respondBangMiss(Player otherPlayer, int bangs, int misses,
			int missesRequired) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean respondMiss(Player miss, int misses, int missesRequired) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int askPlay(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean chooseDiscard(Player player) {
		// TODO Auto-generated method stub
		return true;
	}
	public void printInfo(String info){
		//do nothing
	}

	@Override
	public boolean chooseFromPlayer(Player player) {
		// TODO Auto-generated method stub
		return true;
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
