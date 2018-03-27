package com.chriscarr.bang.userinterface;

import java.util.ArrayList;
import java.util.List;

import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.cards.Card;
import com.chriscarr.bang.gamestate.GameState;
import com.chriscarr.bang.gamestate.GameStateListener;

public class JSPUserInterface implements UserInterface, GameStateListener {

	public List<Message> messages;
	public List<Message> responses;
	protected Turn turn;
	
	public JSPUserInterface(){
		messages = new ArrayList<Message>();
		responses = new ArrayList<Message>();
	}

	public ArrayList<String> getRoles(){
		return turn.getRoles();
	}
	
	protected void waitForResponse(String player){
		while(responses.isEmpty()){
			Thread.yield();
		}
	}
	
	private List<Object> makeCardList(String remove, Player player) {
		List<Object> cardsToDiscard = new ArrayList<Object>();
		if(!"".equals(remove) && !"-1".equals(remove)){
			String[] removed = remove.split(",");
			Hand hand = player.getHand();
			for(int i = 0; i < removed.length; i++){
				if(!"".equals(removed[i])){
					if(!cardsToDiscard.contains(hand.get(Integer.parseInt(removed[i])))){
						cardsToDiscard.add(hand.get(Integer.parseInt(removed[i])));
					}
				}
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
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}
	@Override
	public int askBlueDiscard(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			handCards += ((Card)hand.get(i)).getName() + ", ";
		}
		sendMessage(player.getName(), "askBlueDiscard " + handCards);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}
	@Override
	public int askOthersCard(Player player, InPlay inPlay, boolean hasHand) {
		String inPlayCards = "";
		for(int i = 0; i < inPlay.count(); i++){
			inPlayCards += ((Card)inPlay.get(i)).getName() + ", ";
		}		
		boolean hasGun = inPlay.hasGun();
		sendMessage(player.getName(), "askOthersCard " + hasHand + ", " + hasGun + inPlay.getGunName() + ", " + inPlayCards);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
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
				targetString += otherName + "$";
			}
			handCards += name + "@" + canPlay + "@" + targetString + ", ";
		}
		InPlay inPlay = player.getInPlay();
		for(int i = 0; i < inPlay.size(); i++){
			Card card = (Card)inPlay.get(i);
			if(card.getType() == Card.TYPESINGLEUSEITEM){
				String name = card.getName();
				boolean canPlay = turn.canPlay(player, card);
				List<String> targets = turn.targets(player, card);
				String targetString = "";
				for(String otherName : targets){
					targetString += otherName + "$";
				}
				handCards += name + "@" + canPlay + "@" + targetString + ", ";
			}
		}
		if(Figure.CHUCKWENGAM.equals(player.getAbility())){
			handCards += "loselifefor2cards" + "@true@" + player.getName() + "$" + ", ";
		}
		if(Figure.JOSEDELGADO.equals(player.getAbility())){
			handCards += "discardbluetodraw2" + "@true@" + player.getName() + "$" + ", ";
		}
		if(Figure.DOCHOLYDAY.equals(player.getAbility())){
			handCards += "discardtwotoshoot" + "@true@" + player.getName() + "$" + ", ";
		}
		sendMessage(player.getName(), "askPlay " + handCards);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}

	@Override
	public int askPlayer(Player player, List<String> otherPlayers) {
		String names = "";
		for(String name : otherPlayers){
			names += name + ", ";
		}
		sendMessage(player.getName(), "askPlayer " + names);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}

	@Override
	public int chooseCardToPutBack(Player player, List<Object> cards) {
		String cardString = "";
		for(int i = 0; i < cards.size(); i++){
			cardString += ((Card)cards.get(i)).getName() + "^" + Card.suitToString(((Card)cards.get(i)).getSuit()) + "^" + Card.valueToString(((Card)cards.get(i)).getValue()) + ", ";
		}
		sendMessage(player.getName(), "chooseCardToPutBack " + cardString);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}

	@Override
	public boolean chooseDiscard(Player player, Object card) {
		sendMessage(player.getName(), "chooseDiscard " + ((Card)card).getName());
		waitForResponse(player.getName());
		String response = removeResponse(player.getName());
		if(response.equals("-1")){
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int chooseDrawCard(Player player, List<Object> cards) {
		String cardString = "";
		for(int i = 0; i < cards.size(); i++){
			cardString += ((Card)cards.get(i)).getName() + "^" + Card.suitToString(((Card)cards.get(i)).getSuit()) + "^" + Card.valueToString(((Card)cards.get(i)).getValue()) + ", ";
		}
		sendMessage(player.getName(), "chooseDrawCard " + cardString);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}

	@Override
	public boolean chooseFromPlayer(Player player) {
		sendMessage(player.getName(), "chooseFromPlayer");
		waitForResponse(player.getName());
		return Boolean.parseBoolean(removeResponse(player.getName()));
	}

	@Override
	public int chooseGeneralStoreCard(Player player, List<Object> cards) {
		String cardString = "";
		for(int i = 0; i < cards.size(); i++){
			cardString += ((Card)cards.get(i)).getName() + ", ";
		}
		sendMessage(player.getName(), "chooseGeneralStoreCard " + cardString);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}

	@Override
	public List<Object> chooseTwoDiscardForLife(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			handCards += ((Card)hand.get(i)).getName() + ", ";
		}
		sendMessage(player.getName(), "chooseTwoDiscardForLife " + handCards);
		waitForResponse(player.getName());
		return makeCardList(removeResponse(player.getName()), player);
	}

	@Override
	public List<Object> chooseTwoDiscardForShoot(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			handCards += ((Card)hand.get(i)).getName() + ", ";
		}
		sendMessage(player.getName(), "chooseTwoDiscardForShoot " + handCards);
		waitForResponse(player.getName());
		return makeCardList(removeResponse(player.getName()), player);
	}

	@Override
	public void printInfo(String info) {
		messages.add(new MessageImpl(info));
	}

	@Override
	public int respondBang(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			Card card = (Card)hand.get(i);
			String name = card.getName();
			boolean canPlay = Card.CARDBANG.equals(name) || (Card.CARDMISSED.equals(name) && Figure.CALAMITYJANET.equals(player.getAbility()));
			handCards += name + "@" + canPlay + ", ";
		}
		sendMessage(player.getName(), "respondBang " + handCards);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
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
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}

	@Override
	public int respondMiss(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			Card card = (Card)hand.get(i);
			String name = card.getName();
			boolean canPlay = Card.CARDMISSED.equals(name) || (Card.CARDBANG.equals(name) && Figure.CALAMITYJANET.equals(player.getAbility()));
			handCards += name + "@" + canPlay + ", ";
		}
		sendMessage(player.getName(), "respondMiss " + handCards);
		waitForResponse(player.getName());
		return Integer.parseInt(removeResponse(player.getName()));
	}

	@Override
	public void setTurn(Turn turn) {
		this.turn = turn;
	}
	
	public GameState getGameState(){
		return turn.getGameState();
	}
	
	public void sendMessage(String player, String message){
		messages.add(new MessageImpl(player + "-" + message));
	}

	@Override
	public List<Object> respondTwoMiss(Player player) {
		Hand hand = player.getHand();
		String handCards = "";
		for(int i = 0; i < hand.size(); i++){
			boolean canPlay = false;
			String cardName = ((Card)hand.get(i)).getName();
			if(Card.CARDMISSED.equals(cardName) || (Card.CARDBANG.equals(cardName) && Figure.CALAMITYJANET.equals(player.getAbility()))){
				canPlay = true;
			}
			handCards += cardName + "@" + canPlay + ", ";
		}
		sendMessage(player.getName(), "respondTwoMiss " + handCards);
		waitForResponse(player.getName());
		return makeCardList(removeResponse(player.getName()), player);
	}
	
	public String removeResponse(String playerName){
		return responses.remove(0).getMessage();
	}

	@Override
	public String getRoleForName(String name) {
		return turn.getRoleForName(name);
	}
	
	public String getGoalForName(String name){
		return turn.roleToGoal(name);
	}

	public GameState getGameState(boolean gameOver) {
		if(turn != null){
			return turn.getGameState(gameOver);
		}
		return null;
	}

	public String getTimeout() {
		return null;
	}
	
	public Hand getHandForUser(String playerName){
		return turn.getPlayerForName(playerName).getHand();
	}

	public boolean isPlayerAlive(String playerName) {
		if (turn.getPlayerForName(playerName) == null){
			return false;
		}
		return true;
	}

}
