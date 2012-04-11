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
	public int askPlayer(Player player, List<Player> otherPlayers) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int askOthersCard(Player player, InPlay inPlay) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean respondBeer(Player player, int beers) {
		if(beers > 0){
			return true;
		} else {
			return false;
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

}
