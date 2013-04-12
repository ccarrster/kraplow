package com.chriscarr.bang.userinterface;

import java.util.List;

import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.Player;

public interface UserInterface {

	int askDiscard(Player player);

	int askPlay(Player player);

	int askPlayer(Player player, List<String> otherPlayers);

	int respondBang(Player player);

	int respondMiss(Player player);
	
	List<Object> respondTwoMiss(Player player);

	int chooseGeneralStoreCard(Player player,
			List<Object> cards);

	int askOthersCard(Player player, InPlay inPlay, boolean hasHand);

	int respondBeer(Player player);

	boolean chooseDiscard(Player player);
	
	boolean chooseFromPlayer(Player player);	

	List<Object> chooseTwoDiscardForLife(Player player);
	
	public void printInfo(String info);

	int chooseDrawCard(Player player, List<Object> cards);

	int chooseCardToPutBack(Player player, List<Object> cards);
	
	String getRoleForName(String name);
	
	String getTimeout();
}
