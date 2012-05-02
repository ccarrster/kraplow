package com.chriscarr.bang;

import java.util.List;

public class TestUserInterfaceBangBackTwice extends TestUserInterface implements
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
	public int respondBangMiss(Player otherPlayer, int bangs, int misses,
			int missesRequired) {
		Hand hand = otherPlayer.getHand();
		if(missesRequired == 2 && bangs >= 1 && misses >= 1){
			return Figure.PLAYONEEACH;
		} else if(hand.countBangs() > 0){
			return Figure.PLAYBANG;
		} else if(hand.countMisses() > 0){
			return Figure.PLAYMISSED;
		} else {
			return Figure.GETSHOT;
		}
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
	public boolean chooseDiscard(Player player) {
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
