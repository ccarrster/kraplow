package com.chriscarr.bang;

import java.util.List;

public class TestPlayOneUserInterface implements UserInterface {

	boolean askedPlay = false;
	
	public int askDiscard(Player player) {
		return 0;
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
		return 1;
	}

	@Override
	public int askPlayer(Player player, List<Player> otherPlayers) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int askOthersCard(Player player, InPlay inPlay, boolean hasHand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean respondBeer(Player player, int beers) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Object> chooseTwoDiscardForLife(Player sidKetchum) {
		// TODO Auto-generated method stub
		return null;
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
		if(!askedPlay){
			return 0;	
		}
		return -1;
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
}
