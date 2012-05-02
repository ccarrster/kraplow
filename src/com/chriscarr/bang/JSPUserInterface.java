package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class JSPUserInterface implements UserInterface, GameStateListener {

	public List<String> messages;
	public List<String> responses;
	private Turn turn;
	
	public JSPUserInterface(){
		messages = new ArrayList<String>();
		responses = new ArrayList<String>();
	}
	
	private void waitForResponse(){
		while(responses.isEmpty()){
			Thread.yield();
		}
	}
	
	private List<Object> makeCardList(String remove, Player player) {
		List<Object> cardsToDiscard = new ArrayList<Object>();
		if(!"".equals(remove)){
			String[] removed = remove.split(",");
			Hand hand = player.getHand();
			for(int i = 0; i < removed.length; i++){
				cardsToDiscard.add(hand.get(Integer.parseInt(removed[i])));
			}
		}
		return cardsToDiscard;
	}

	
	@Override
	public int askDiscard(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			handCards += ((Card)hand.get(i)).getName() + ", ";
		}
		sendMessage(player.getName(), "askDiscard " + handCards);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}
	@Override
	public int askOthersCard(Player player, InPlay inPlay, boolean hasHand) {
		String inPlayCards = "";
		for(int i = 0; i < inPlay.count(); i++){
			inPlayCards += ((Card)inPlay.get(i)).getName() + ", ";
		}		
		boolean hasGun = inPlay.hasGun();
		sendMessage(player.getName(), "askOthersCard " + hasHand + ", " + hasGun + ", " + inPlayCards);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public int askPlay(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			Card card = (Card)hand.get(i);
			String name = card.getName();
			boolean canPlay = turn.canPlay(player, card);
			List<String> targets = turn.targets(player, card);
			String targetString = "";
			for(String otherName : targets){
				targetString += otherName + " ";
			}
			handCards += name + "@" + canPlay + "@" + targetString + ", ";
		}
		sendMessage(player.getName(), "askPlay " + handCards);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public int askPlayer(Player player, List<String> otherPlayers) {
		String names = "";
		for(String name : otherPlayers){
			names += name + ", ";
		}
		sendMessage(player.getName(), "askPlayer " + names);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public int chooseCardToPutBack(Player player, List<Object> cards) {
		String cardString = "";
		for(int i = 0; i < cards.size(); i++){
			cardString += ((Card)cards.get(i)).getName() + " " + Card.suitToString(((Card)cards.get(i)).getSuit()) + " " + Card.valueToString(((Card)cards.get(i)).getValue()) + ", ";
		}
		sendMessage(player.getName(), "chooseCardToPutBack " + cardString);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public boolean chooseDiscard(Player player) {
		sendMessage(player.getName(), "chooseDiscard");
		waitForResponse();
		return Boolean.parseBoolean(responses.remove(0));
	}

	@Override
	public int chooseDrawCard(Player player, List<Object> cards) {
		String cardString = "";
		for(int i = 0; i < cards.size(); i++){
			cardString += ((Card)cards.get(i)).getName() + " " + Card.suitToString(((Card)cards.get(i)).getSuit()) + " " + Card.valueToString(((Card)cards.get(i)).getValue()) + ", ";
		}
		sendMessage(player.getName(), "chooseDrawCard " + cardString);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public boolean chooseFromPlayer(Player player) {
		sendMessage(player.getName(), "chooseFromPlayer");
		waitForResponse();
		return Boolean.parseBoolean(responses.remove(0));
	}

	@Override
	public int chooseGeneralStoreCard(Player player, List<Object> cards) {
		String cardString = "";
		for(int i = 0; i < cards.size(); i++){
			cardString += ((Card)cards.get(i)).getName() + ", ";
		}
		sendMessage(player.getName(), "chooseGeneralStoreCard " + cardString);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public List<Object> chooseTwoDiscardForLife(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			handCards += ((Card)hand.get(i)).getName() + ", ";
		}
		sendMessage(player.getName(), "chooseTwoDiscardForLife " + handCards);
		waitForResponse();
		return makeCardList(responses.remove(0), player);
	}

	@Override
	public void printInfo(String info) {
		messages.add(info);
	}

	@Override
	public int respondBang(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			Card card = (Card)hand.get(i);
			String name = card.getName();
			boolean canPlay = Card.CARDBANG.equals(name) || (Card.CARDMISSED.equals(name) && Figure.CALAMITYJANET.equals(player.getName()));
			handCards += name + "@" + canPlay + ", ";
		}
		sendMessage(player.getName(), "respondBang " + handCards);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public int respondBangMiss(Player player, int bangs, int misses,
			int missesRequired) {
		sendMessage(player.getName(), "respondBangMiss " + bangs + " " + misses + " " + missesRequired);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public int respondBeer(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			Card card = (Card)hand.get(i);
			String name = card.getName();
			boolean canPlay = Card.CARDBEER.equals(name);
			handCards += name + "@" + canPlay + ", ";
		}
		sendMessage(player.getName(), "respondBeer " + handCards);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public int respondMiss(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			Card card = (Card)hand.get(i);
			String name = card.getName();
			boolean canPlay = Card.CARDMISSED.equals(name) || (Card.CARDBANG.equals(name) && Figure.CALAMITYJANET.equals(player.getName()));
			handCards += name + "@" + canPlay + ", ";
		}
		sendMessage(player.getName(), "respondMiss " + handCards);
		waitForResponse();
		return Integer.parseInt(responses.remove(0));
	}

	@Override
	public void setTurn(Turn turn) {
		this.turn = turn;
	}
	
	public GameState getGameState(){
		return turn.getGameState();
	}
	
	public void sendMessage(String player, String message){
		messages.add(player + "-" + message);
	}

}
