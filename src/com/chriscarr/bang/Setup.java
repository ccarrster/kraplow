package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.*;

import com.chriscarr.bang.cards.BangDeck;
import com.chriscarr.bang.cards.Card;
import com.chriscarr.bang.gamestate.GameStateListener;
import com.chriscarr.bang.userinterface.UserInterface;

public class Setup {
	
	private Deck deck;
	private List<Player> players;
	
	public Setup(int countPlayers, UserInterface userInterface, GameStateListener gameStateListener) {
		new Setup(countPlayers, userInterface, gameStateListener, false, "random", "random");
	}

	public Setup(int countPlayers, UserInterface userInterface, GameStateListener gameStateListener, boolean sidestep, String pRole, String pChar) {
		deck = setupDeck(sidestep);
		deck.shuffle();
		Discard discard = new Discard();
		deck.setDiscard(discard);
		players = getPlayers(countPlayers, deck, sidestep, pRole, pChar);
		drawHands(players, deck);
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		turn.setPlayers(players);
		turn.setUserInterface(userInterface);
		gameStateListener.setTurn(turn);
		//This starts the game loop
		turn.setSheriff();
	}

	public Setup(List<String> players2, UserInterface userInterface,
			GameStateListener gameStateListener) {
		// TODO Auto-generated constructor stub
	}

	public static Deck setupDeck(boolean sidestep){
		Deck deck = new Deck();
		List<Card> cards = BangDeck.makeDeck();
		for(Card card : cards){
			deck.add(card);
		}
		if(sidestep){
			List<Card> sidestepCards = BangDeck.makeSidestepDeck();
				for(Card card : sidestepCards){
				deck.add(card);
			}
		}
		return deck;
	}

	public static List<Player> getPlayers(int countCharacters, Deck deck) {
		return getPlayers(countCharacters, deck, false, "random", "random");
	}

	public static List<Player> getPlayers(int countCharacters, Deck deck, boolean sidestep, String pRole, String pChar) {
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<String> characterList = new ArrayList<String>();
		characterList.addAll(Arrays.asList(Figure.CHARACTERS));
		try{
			if(sidestep){
				characterList.addAll(Arrays.asList(Figure.CHARACTERSSIDESTEP));
			}
		} catch(Exception e){
			Logger logger = Logger.getLogger(Setup.class.getName());
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		Collections.shuffle(characterList);
		List<Integer> roles = getRoles(countCharacters);
		Collections.shuffle(roles);
		for(int i = 0; i < countCharacters; i++){
			Player player = new Player();
			
			Figure figure = new Figure();
			if(i == 0 && !pChar.equals("random") && characterList.contains(pChar)){
				characterList.remove(pChar);
				characterList.add(0, pChar);
			}
			figure.setName(characterList.get(i));
			if(i == 0 && !pRole.equals("random") && roles.contains(Player.stringToRole(pRole))){
				roles.remove(Player.stringToRole(pRole));
				roles.add(0, Player.stringToRole(pRole));
			}
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
		if(countPlayers == 7){
			return roles;
		}
		roles.add(Player.RENEGADE);
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
