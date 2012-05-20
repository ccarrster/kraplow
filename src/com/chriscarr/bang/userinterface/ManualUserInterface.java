package com.chriscarr.bang.userinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.chriscarr.bang.Figure;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.cards.Card;
import com.chriscarr.bang.gamestate.GameState;
import com.chriscarr.bang.gamestate.GameStateCard;
import com.chriscarr.bang.gamestate.GameStateListener;
import com.chriscarr.bang.gamestate.GameStatePlayer;

public class ManualUserInterface implements UserInterface, GameStateListener {
	
	Turn turn;
	
	public void setTurn(Turn turn){
		this.turn = turn;
	}
	
	public void printInfo(String info){
		System.out.println(info);
	}
	
	public int askDiscard(Player player) {
		System.out.println(player.getFigure().getName());
		System.out.println("Discard");
		Hand hand = player.getHand();
		int handSize = hand.size();
		for(int i = 0; i < handSize; i++){
			System.out.println(i + ") " + ((Card)hand.get(i)).getName());
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= 0 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int askOthersCard(Player player, InPlay inPlay, boolean hasHand) {
		System.out.println(player.getFigure().getName());
		System.out.println("Choose Other Players Card");
		int handSize = inPlay.size();	
		if(hasHand){
			System.out.println("-1) Hand");
		}
		boolean hasGun = inPlay.hasGun();
		if(hasGun){
			System.out.println("-2) Gun");
		}
		for(int i = 0; i < handSize; i++){
			System.out.println(i + ") " + ((Card)inPlay.get(i)).getName());
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= -2 && cardNumber < handSize){
					if(cardNumber == -2 && !hasGun){
						continue;
					}
					if(cardNumber == -1 && !hasHand){
						continue;
					}
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int askPlay(Player player) {
		printGameState();
		printPrivateInfo(player);
		System.out.println("Play");
		Hand hand = player.getHand();
		int handSize = hand.size();
		System.out.println("-1) done playing");
		for(int i = 0; i < handSize; i++){
			Card card = ((Card)hand.get(i));
			boolean canPlay = turn.canPlay(player, card);
			System.out.print(i + ") " + card.getName() + " can play? " + canPlay);
			if(canPlay){
				System.out.print(" Targets: ");
				for(String name : turn.targets(player, card)){
					System.out.print(name + " ");
				}
			}
			System.out.println("");
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= -1 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void printPrivateInfo(Player player) {
		System.out.println(Player.roleToString(player.getRole()));
	}

	@Override
	public int askPlayer(Player player, List<String> otherPlayers) {
		System.out.println(player.getFigure().getName());
		System.out.println("Choose Player");
		int handSize = otherPlayers.size();
		for(int i = 0; i < handSize; i++){
			System.out.println(i + ") " + otherPlayers.get(i));
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= 0 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean chooseDiscard(Player player) {
		System.out.println(player.getFigure().getName());
		System.out.println("Draw Card From Discard");
		System.out.println("0) From Discard");
		System.out.println("1) From Deck");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber == 0){
					return true;
				} else if(cardNumber == 1) {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int chooseGeneralStoreCard(Player player, List<Object> cards) {
		System.out.println(player.getFigure().getName());
		System.out.println("Choose General Store Card");
		int handSize = cards.size();
		for(int i = 0; i < handSize; i++){
			System.out.println(i + ") " + ((Card)cards.get(i)).getName());
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= 0 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Object> chooseTwoDiscardForLife(Player player) {
		System.out.println(player.getFigure().getName());
		System.out.println("Discard Two cards for 1 Life, 4 for 2, etc");
		Hand hand = player.getHand();
		int handSize = hand.size();
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		List<Object> chosenCards = new ArrayList<Object>();
		while (true){
			System.out.println("-1) done choosing");
			for(int i = 0; i < handSize; i++){
				String chosen = " not chosen";
				if(chosenCards.contains(hand.get(i))){
					chosen = " chosen";
				}
				System.out.println(i + ") " + ((Card)hand.get(i)).getName() + chosen);
			}
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= -1 && cardNumber < handSize){
					if(cardNumber == -1){
						return chosenCards;
					} else {
						Object card = hand.get(cardNumber);
						if(chosenCards.contains(card)){
							chosenCards.remove(card);
						} else {
							chosenCards.add(card);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int respondBang(Player player) {
		printPrivateInfo(player);
		System.out.println("Respond Bang");
		Hand hand = player.getHand();
		int handSize = hand.size();
		System.out.println("-1) done playing");
		for(int i = 0; i < handSize; i++){
			Card card = ((Card)hand.get(i));
			boolean canPlay = Card.CARDBANG.equals(card.getName()) || (Card.CARDMISSED.equals(card.getName()) && Figure.CALAMITYJANET.equals(player.getName()));
			System.out.print(i + ") " + card.getName() + " can play? " + canPlay);
			if(canPlay){
				System.out.print(" Targets: ");
				for(String name : turn.targets(player, card)){
					System.out.print(name + " ");
				}
			}
			System.out.println("");
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= -1 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int respondBeer(Player player) {
		printPrivateInfo(player);
		System.out.println("Respond Beer");
		Hand hand = player.getHand();
		int handSize = hand.size();
		System.out.println("-1) done playing");
		for(int i = 0; i < handSize; i++){
			Card card = ((Card)hand.get(i));
			boolean canPlay = Card.CARDBEER.equals(card.getName());
			System.out.print(i + ") " + card.getName() + " can play? " + canPlay);
			if(canPlay){
				System.out.print(" Targets: ");
				for(String name : turn.targets(player, card)){
					System.out.print(name + " ");
				}
			}
			System.out.println("");
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= -1 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int respondMiss(Player player) {
		printPrivateInfo(player);
		System.out.println("Respond Miss");
		Hand hand = player.getHand();
		int handSize = hand.size();
		System.out.println("-1) done playing");
		for(int i = 0; i < handSize; i++){
			Card card = ((Card)hand.get(i));
			boolean canPlay = Card.CARDMISSED.equals(card.getName()) || (Card.CARDBANG.equals(card.getName()) && Figure.CALAMITYJANET.equals(player.getName()));
			System.out.print(i + ") " + card.getName() + " can play? " + canPlay);
			if(canPlay){
				System.out.print(" Targets: ");
				for(String name : turn.targets(player, card)){
					System.out.print(name + " ");
				}
			}
			System.out.println("");
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= -1 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean chooseFromPlayer(Player player) {
		System.out.println(player.getFigure().getName());
		System.out.println("Draw Card From Player");
		System.out.println("0) From Player");
		System.out.println("1) From Deck");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber == 0){
					return true;
				} else if(cardNumber == 1) {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int chooseDrawCard(Player player, List<Object> cards) {
		System.out.println(player.getFigure().getName());
		System.out.println("Choose Draw Card to keep");
		int handSize = cards.size();
		for(int i = 0; i < handSize; i++){
			System.out.println(i + ") " + ((Card)cards.get(i)).getName());
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= 0 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int chooseCardToPutBack(Player player, List<Object> cards) {
		System.out.println(player.getFigure().getName());
		System.out.println("Choose card put back");
		int handSize = cards.size();
		for(int i = 0; i < handSize; i++){
			System.out.println(i + ") " + ((Card)cards.get(i)).getName());
		}
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		while (true){
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= 0 && cardNumber < handSize){
					return cardNumber;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void printGameState(){
		GameState gameState = turn.getGameState();
		System.out.println("Current Turn: " + gameState.getCurrentName());
		System.out.println("Is game over: " + gameState.isGameOver());		
		System.out.println("Deck size: " + gameState.getDeckSize());
		System.out.println("Discard top card: " + gameState.discardTopCard());
		List<GameStatePlayer> players = gameState.getPlayers();
		for(GameStatePlayer player : players){
			System.out.println("Name: " + player.name);
			System.out.println("Is Sheriff: " + player.isSheriff);
			System.out.println("Ability: " + player.specialAbility);
			System.out.println("Health: " + player.health);
			System.out.println("Max: " + player.maxHealth);
			System.out.println("Hand: " + player.handSize);
			GameStateCard gun = player.gun;
			if(gun != null){
				System.out.println("Discard top card: " + gun.name);
				System.out.println("Discard top card: " + gun.suit);
				System.out.println("Discard top card: " + gun.type);
				System.out.println("Discard top card: " + gun.value);
			}
			List<GameStateCard> cards = player.inPlay;
			for(GameStateCard card : cards){
				System.out.println("name: " + card.name);
				System.out.println("suit: " + card.suit);
				System.out.println("type: " + card.type);
				System.out.println("value: " + card.value);
			}
		}
	}

	@Override
	public List<Object> respondTwoMiss(Player player) {
		System.out.println(player.getFigure().getName());
		System.out.println("Two misses or get shot");
		Hand hand = player.getHand();
		int handSize = hand.size();
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		List<Object> chosenCards = new ArrayList<Object>();
		while (true){
			System.out.println("-1) done choosing");
			for(int i = 0; i < handSize; i++){
				String chosen = " not chosen";
				if(chosenCards.contains(hand.get(i))){
					chosen = " chosen";
				}
				System.out.println(i + ") " + ((Card)hand.get(i)).getName() + chosen);
			}
			try {
				String line = in.readLine();
				int cardNumber = Integer.parseInt(line);
				if(cardNumber >= -1 && cardNumber < handSize){
					if(cardNumber == -1){
						return chosenCards;
					} else {
						Object card = hand.get(cardNumber);
						if(chosenCards.contains(card)){
							chosenCards.remove(card);
						} else {
							chosenCards.add(card);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public String getRoleForName(String name) {
		return turn.getRoleForName(name);
	}
	
	public String getGoalForName(String name){
		return turn.roleToGoal(name);
	}

}
