package com.chriscarr.bang;

import java.util.List;

import junit.framework.TestCase;

public class GameStateTest extends TestCase {
	public void testGameState(){
		GameState gameState = new TestGameState();
		String currentName = gameState.getCurrentName();
		boolean gameOver = gameState.isGameOver();
		String winners = gameState.getWinners();
		int deckSize = gameState.getDeckSize();
		int discardSize = gameState.getDeckSize();
		GameStateCard discardCard = gameState.discardTopCard();
		String suit = discardCard.getSuit();
		String value = discardCard.getValue();
		String cardName = discardCard.getName();
		String description = discardCard.getDescription();
		String type = discardCard.getType();
		List<GameStatePlayer> players = gameState.getPlayers();
		for(GameStatePlayer player : players){
			String name = player.getName();
			String specialAbility = player.getSpecialAbility();
			int health = player.getHealth();
			int maxHealth = player.getMaxHealth();
			int handSize = player.getHandSize();
			GameStateCard gun = player.getGun();
			boolean isSheriff = player.isSheriff();
			List<GameStateCard> inPlayCards = player.getCardsInPlay();
			for(GameStateCard inPlayCard : inPlayCards){
				String inPlaySuit = inPlayCard.getSuit();
				String inPlayValue = inPlayCard.getValue();
				String inPlayName = inPlayCard.getName();
				String inPlayDescription = inPlayCard.getDescription();
				String inPlayType = inPlayCard.getType();
			}			
		}
	}
}
