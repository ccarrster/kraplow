package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.chriscarr.bang.cards.BangDeck;
import com.chriscarr.bang.cards.Card;
import com.chriscarr.bang.gamestate.GameStateListener;
import com.chriscarr.bang.userinterface.UserInterface;

public class Setup {
	
	private Deck deck;
	private List<Player> players;
	
	public Setup(int countPlayers, UserInterface userInterface, GameStateListener gameStateListener) {
		deck = setupDeck();
		deck.shuffle();
		Discard discard = new Discard();
		deck.setDiscard(discard);
		players = getPlayers(countPlayers, deck);
		drawHands(players, deck);
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		turn.setPlayers(players);
		turn.setUserInterface(userInterface);
		gameStateListener.setTurn(turn);
		turn.setSheriff();
	}

	public Setup(List<String> players2, UserInterface userInterface,
			GameStateListener gameStateListener) {
		// TODO Auto-generated constructor stub
	}

	public static Deck setupDeck(){
		Deck deck = new Deck();
		List<Card> cards = BangDeck.makeDeck();
		for(Card card : cards){
			deck.add(card);
		}
		return deck;
	}

	public static List<Player> getPlayers(int countCharacters, Deck deck) {
		ArrayList<Player> players = new ArrayList<Player>();
				
		List<String> characterList = Arrays.asList(Figure.CHARACTERS);
		Collections.shuffle(characterList);
		List<Integer> roles = getRoles(countCharacters);
		Collections.shuffle(roles);
		for(int i = 0; i < countCharacters; i++){
			Player player = new Player();
			
			Figure figure = new Figure();			
			figure.setName(characterList.get(i));
			int role = roles.get(i);
			player.setRole(role);
			player.setFigure(figure);
			int maxHealth = Figure.getStartingHealth(figure.getName());
			if(role == Player.SHERIFF){
				maxHealth = maxHealth + 1;
			}
			player.setMaxHealth(maxHealth);
						
			Hand hand = new Hand();
			
			player.setHand(hand);
			
			player.setInPlay(new InPlay());
			
			players.add(player);			
		}
		
		return players;
	}
	
	public static List<Integer> getRoles(int countPlayers){
		List<Integer> roles = new ArrayList<Integer>();
		roles.add(Player.SHERIFF);
		roles.add(Player.OUTLAW);
		roles.add(Player.OUTLAW);
		roles.add(Player.RENEGADE);
		if(countPlayers == 4){
			return roles;
		}
		roles.add(Player.DEPUTY);
		if(countPlayers == 5){
			return roles;
		}
		roles.add(Player.OUTLAW);
		if(countPlayers == 6){
			return roles;
		}
		roles.add(Player.DEPUTY);
		return roles;
	}
	
	public static void drawHands(List<Player> players, Deck deck){
		for(Player player : players){
			int maxHealth = player.getMaxHealth();
			Hand hand = player.getHand();
			for(int i = 0; i < maxHealth; i++){
				hand.add(deck.pull());
			}
		}
	}

	public static List<Player> getNormalPlayers(int countCharacters) {
		ArrayList<Player> players = new ArrayList<Player>();
				
		List<Integer> roles = getRoles(countCharacters);
		Collections.shuffle(roles);
		for(int i = 0; i < countCharacters; i++){
			Player player = new Player();
			
			Figure figure = new Figure();			
			figure.setName("Average Joe");
			int role = roles.get(i);
			player.setRole(role);
			player.setFigure(figure);
			int maxHealth = Figure.getStartingHealth(figure.getName());
			if(role == Player.SHERIFF){
				maxHealth = maxHealth + 1;
			}
			player.setMaxHealth(maxHealth);
						
			player.setHand(new Hand());
			
			player.setInPlay(new InPlay());
			
			players.add(player);			
		}
		
		return players;
	}
}
