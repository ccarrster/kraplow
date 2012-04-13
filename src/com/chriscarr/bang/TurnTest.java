package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TurnTest extends TestCase{
	public void testTurn(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setPlayers(players);
		turn.setUserInterface(new TestUserInterface());
		turn.setSheriffManualTest();
		Player sheriff = turn.getPlayersTurn();
		assertEquals(sheriff.getRole(), Player.SHERIFF);
	}
	
	/*public void testTurnNext(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);		
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();		
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		UserInterface testUserInterface = new TestUserInterface();
		turn.setUserInterface(testUserInterface);
		turn.nextTurn();
		Player notSheriff =  turn.getPlayersTurn();
		assertFalse(notSheriff.getRole() == Player.SHERIFF);
	}*/
	
	/*public void testTurnLoop(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();		
		turn.nextTurn();
		turn.nextTurn();
		turn.nextTurn();
		turn.nextTurn();
		Player sheriff =  turn.getPlayersTurn();
		assertEquals(sheriff.getRole(), Player.SHERIFF);
	}*/
	
	/*public void testBangPlayed(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		assertFalse(turn.isBangPlayed());
		turn.setBangPlayed(true);
		assertTrue(turn.isBangPlayed());
		turn.nextTurn();
		assertFalse(turn.isBangPlayed());
	}*/
	
	public void testDrawCards(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();		
		
		Player player = turn.getCurrentPlayer();
		turn.drawCards(player, Setup.setupDeck());
		assertEquals(turn.getCurrentPlayer().getHand().size(), 2);
		turn.drawCards(player, Setup.setupDeck());
		assertEquals(turn.getCurrentPlayer().getHand().size(), 4);
	}
	
	public void testDiscardCards(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Deck deck = Setup.setupDeck();
		Player player = turn.getCurrentPlayer();
		turn.drawCards(player, deck);
		turn.drawCards(player, deck);
		turn.drawCards(player, deck);		
		int health = player.getHealth();
		UserInterface testUserInterface = new TestUserInterface();
		turn.setUserInterface(testUserInterface);
		assertTrue(player.getHand().size() != health);
		turn.discard(player);		
		assertEquals(player.getHand().size(), health);
	}
	
	/*public void testPlay(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		UserInterface testUserInterface = new TestUserInterface();
		turn.setUserInterface(testUserInterface);
		assertFalse(turn.isDonePlaying());
		turn.play();
		assertTrue(turn.isDonePlaying());
		turn.nextTurn();
		assertFalse(turn.isDonePlaying());
	}*/
	
 
	
	public void testPlayItem(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBARREL, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		UserInterface testUserInterface = new TestPlayOneUserInterface();
		turn.setUserInterface(testUserInterface);		
		turn.play();
		assertTrue(sheriff.getInPlay().hasItem(Card.CARDBARREL));
		assertFalse(sheriff.getInPlay().hasItem(Card.CARDAPPALOOSA));
	}
	
	public void testPlayItemTwice(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBARREL, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		sheriff.getHand().add(new Card(Card.CARDBARREL, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		UserInterface testUserInterface = new TestPlayOneUserInterface();
		turn.setUserInterface(testUserInterface);		
		turn.play();
		turn.play();
		assertEquals(1, sheriff.getHand().size());
	}
	
	public void testPlayItemJail(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDJAIL, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);		
		turn.play();
		int otherPlayer = 0;
		if(players.get(0).equals(sheriff)){
			otherPlayer = 1;
		}
		Player jailedPlayer = players.get(otherPlayer);
		
		InPlay otherInPlay = jailedPlayer.getInPlay();
		assertTrue(otherInPlay.hasItem(Card.CARDJAIL));
	}
	
	public void testPlayItemAlreadyInJail(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDJAIL, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);		
		for(Player player : players){
			player.getInPlay().add(new Card(Card.CARDJAIL, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		}
		turn.play();
		assertEquals(1, sheriff.getHand().size());
	}
	
	public void testPlayBeer(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBEER, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		int startingHealth = sheriff.getHealth();
		sheriff.setHealth(sheriff.getHealth() - 1);
		assertEquals(startingHealth - 1, sheriff.getHealth());
		turn.setDiscard(new Discard());
		turn.play();
		assertEquals(startingHealth, sheriff.getHealth());
	}
	
	public void testPlayBeerTwoPlayers(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		players.remove(sheriff);
		players.remove(0);
		players.remove(0);
		players.add(sheriff);
		sheriff.getHand().add(new Card(Card.CARDBEER, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		int startingHealth = sheriff.getHealth();
		sheriff.setHealth(sheriff.getHealth() - 1);
		assertEquals(startingHealth - 1, sheriff.getHealth());
		turn.setDiscard(new Discard());
		turn.play();
		assertEquals(startingHealth - 1, sheriff.getHealth());
	}
	
	public void testPlayBeerMaxHealth(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBEER, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		int startingHealth = sheriff.getHealth();
		sheriff.setHealth(sheriff.getHealth());
		assertEquals(startingHealth, sheriff.getHealth());
		turn.setDiscard(new Discard());
		turn.play();
		assertEquals(startingHealth, sheriff.getHealth());
	}
	
	public void testPlayStagecoach(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDSTAGECOACH, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));		
		assertEquals(sheriff.getHand().size(), 1);
		UserInterface testUserInterface = new TestPlayOneUserInterface();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();
		assertEquals(sheriff.getHand().size(), 2);
	}
	
	public void testPlayWellsFargo(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDWELLSFARGO, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));		
		assertEquals(sheriff.getHand().size(), 1);
		UserInterface testUserInterface = new TestPlayOneUserInterface();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();
		assertEquals(sheriff.getHand().size(), 3);
	}
	
	public void testPlaySaloon(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDSALOON, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		players.get(0).setHealth(players.get(0).getHealth() - 1);
		players.get(1).setHealth(players.get(1).getHealth() - 1);
		players.get(2).setHealth(players.get(2).getHealth() - 1);
		players.get(3).setHealth(players.get(3).getHealth() - 1);
		turn.setDiscard(new Discard());
		turn.play();
		assertEquals(players.get(0).getHealth(), players.get(0).getMaxHealth());
		assertEquals(players.get(1).getHealth(), players.get(1).getMaxHealth());
		assertEquals(players.get(2).getHealth(), players.get(2).getMaxHealth());
		assertEquals(players.get(3).getHealth(), players.get(3).getMaxHealth());
	}
	
	public void testPlaySaloonFullUp(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDSALOON, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.play();
		assertEquals(players.get(0).getHealth(), players.get(0).getMaxHealth());
		assertEquals(players.get(1).getHealth(), players.get(1).getMaxHealth());
		assertEquals(players.get(2).getHealth(), players.get(2).getMaxHealth());
		assertEquals(players.get(3).getHealth(), players.get(3).getMaxHealth());
	}
	
	public void testPlayIndians(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDINDIANS, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			others.add(otherPlayer);
		}
		others.remove(turn.getCurrentPlayer());
		
		turn.play();
		for(Player otherPlayer : others){
			assertEquals(otherPlayer.getHealth(), otherPlayer.getMaxHealth() - 1);
		}
	}
	
	public void testPlayIndiansBangBack(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDINDIANS, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayerBangBack();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			others.add(otherPlayer);
			otherPlayer.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		}
		others.remove(turn.getCurrentPlayer());
		
		turn.play();
		for(Player otherPlayer : others){
			assertEquals(otherPlayer.getHealth(), otherPlayer.getMaxHealth());
			assertEquals(otherPlayer.getHand().size(), 0);
		}
	}
	
	public void testPlayGatling(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDGATLING, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			others.add(otherPlayer);
		}
		others.remove(turn.getCurrentPlayer());
		turn.play();
		for(Player otherPlayer : others){
			assertEquals(otherPlayer.getHealth(), otherPlayer.getMaxHealth() - 1);
		}
	}
	
	public void testPlayGatlingBarrelSave(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		Deck deck = new Deck();
		deck.add(new Card(Card.CARDGATLING, Card.HEARTS, Card.VALUEQ, Card.TYPEPLAY));
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDGATLING, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			otherPlayer.getInPlay().add(new Card(Card.CARDBARREL, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
			others.add(otherPlayer);
		}
		others.remove(turn.getCurrentPlayer());
		turn.play();
		for(Player otherPlayer : others){
			assertEquals(otherPlayer.getHealth(), otherPlayer.getMaxHealth());
		}
	}
	
	public void testPlayGatlingMiss(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDGATLING, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayerBangBack();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			otherPlayer.getHand().add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
			others.add(otherPlayer);
		}
		others.remove(turn.getCurrentPlayer());
		turn.play();
		for(Player otherPlayer : others){
			assertEquals(otherPlayer.getHealth(), otherPlayer.getMaxHealth());
			assertEquals(otherPlayer.getHand().size(), 0);
		}
	}
	
	public void testPlayGeneralStore(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDGENERALSTORE, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();
		assertEquals(players.get(0).getHand().size(), 1);
		assertEquals(players.get(1).getHand().size(), 1);
		assertEquals(players.get(2).getHand().size(), 1);
		assertEquals(players.get(3).getHand().size(), 1);
	}
	
	public void testPlayDuelWinFirst(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDDUEL, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestPlayOneUserInterfaceChoosePlayer1();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();
		int otherPlayer = 0;
		if(players.get(0).equals(sheriff)){
			otherPlayer = 1;
		}
		Player enemy = players.get(otherPlayer);
		assertEquals(enemy.getMaxHealth() - 1, enemy.getHealth());
	}
	
	public void testPlayDuelLoseFirst(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDDUEL, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceBangBackOnce();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();
		assertEquals(sheriff.getMaxHealth() - 1, sheriff.getHealth());
	}
	
	public void testPlayDuelWinSecond(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDDUEL, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceBangBackTwice();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		int otherPlayer = 0;
		if(players.get(0).equals(sheriff)){
			otherPlayer = 1;
		}
		Player enemy = players.get(otherPlayer);
		enemy.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		assertEquals(enemy.getHand().size(), 1);
		assertEquals(sheriff.getHand().size(), 2);
		turn.play();
		assertEquals(enemy.getHand().size(), 0);
		assertEquals(sheriff.getHand().size(), 0);
		assertEquals(enemy.getMaxHealth() - 1, enemy.getHealth());
	}
	
	public void testCatbalu(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDCATBALOU, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceBangBackTwice();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		int otherPlayer = 0;
		if(players.get(0).equals(sheriff)){
			otherPlayer = 1;
		}
		Player enemy = players.get(otherPlayer);
		enemy.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		assertEquals(enemy.getHand().size(), 1);
		turn.play();
		assertEquals(enemy.getHand().size(), 0);		
	}
	
	public void testPanic(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDPANIC, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceBangBackTwice();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		
		Player enemy = others.get(0);
		enemy.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		assertEquals(enemy.getHand().size(), 1);
		turn.play();
		assertEquals(enemy.getHand().size(), 0);
		assertEquals(sheriff.getHand().size(), 1);
	}
	
	public void testBangHit(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		
		Player enemy = others.get(0);
		turn.play();
		assertEquals(enemy.getHealth(), enemy.getMaxHealth() - 1);
	}
	
	public void testBangMiss(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceBangBackTwice();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		
		Player enemy = others.get(0);
		enemy.getHand().add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		turn.play();
		assertEquals(enemy.getHealth(), enemy.getMaxHealth());
		assertEquals(enemy.getHand().size(), 0);
	}
	
	public void testTwoBangFail(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		turn.play();				
		assertEquals(sheriff.getHand().size(), 1);
		turn.play();
		assertEquals(sheriff.getHand().size(), 1);
	}
	
	public void testTwoBangVolcanic(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().setGun(new Card(Card.CARDVOLCANIC, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		turn.play();				
		assertEquals(sheriff.getHand().size(), 1);
		turn.play();
		assertEquals(sheriff.getHand().size(), 0);
	}
	
	public void testBangDistanceWithoutMustang(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, sheriff.getInPlay().getGunRange()).size(), 2);
	}
	
	public void testBangDistanceWithMustang(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		for(Player player : players){
			player.getInPlay().add(new Card(Card.CARDMUSTANG, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		}
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, sheriff.getInPlay().getGunRange()).size(), 0);
	}
	
	public void testBangDistanceWithAppaloosa(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().add(new Card(Card.CARDAPPALOOSA, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, sheriff.getInPlay().getGunRange()).size(), 3);
	}
	
	public void testPanicDistance(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();		
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, 1).size(), 2);
	}
	
	public void testBangDistance(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().setGun(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, sheriff.getInPlay().getGunRange()).size(), 3);
	}
	
	public void testBangDistanceRev(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(7);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().setGun(new Card(Card.CARDREVCARBINE, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, sheriff.getInPlay().getGunRange()).size(), 6);
	}
	
	public void testDynamiteTwo(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.SPADES, Card.VALUE2, Card.TYPEITEM);
		assertTrue(Card.isExplode(drawnCard));
	}
	
	public void testDynamiteNine(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		assertTrue(Card.isExplode(drawnCard));
	}
	
	public void testDynamiteTen(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.SPADES, Card.VALUE10, Card.TYPEITEM);
		assertFalse(Card.isExplode(drawnCard));
	}
	
	public void testDynamiteNineClubs(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUE9, Card.TYPEITEM);
		assertFalse(Card.isExplode(drawnCard));
	}
	
	public void testPassDynamite(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(7);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().add(new Card(Card.CARDDYNAMITE, Card.CLUBS, Card.VALUE9, Card.TYPEITEM));
		assertTrue(sheriff.getInPlay().hasItem(Card.CARDDYNAMITE));
		turn.setUserInterface(new TestUserInterface());
		turn.passDynamite();
		assertFalse(sheriff.getInPlay().hasItem(Card.CARDDYNAMITE));
		Player nextPlayer = turn.getNextPlayer(turn.getCurrentPlayer());
		assertTrue(nextPlayer.getInPlay().hasItem(Card.CARDDYNAMITE));
	}
	
	public void testDraw(){
		Turn turn = new Turn();
		Deck deck = new Deck();
		Card card = new Card(Card.CARDDYNAMITE, Card.CLUBS, Card.VALUE9, Card.TYPEITEM);
		deck.add(card);
		turn.setDeck(deck);		
		Discard discard = new Discard();
		turn.setDiscard(discard);
		Player player = new Player();
		player.setFigure(new Figure());
		turn.setUserInterface(new TestUserInterface());
		Card drawnCard = (Card)turn.draw(player);
		assertEquals(drawnCard, card);
		assertEquals(discard.peek(), card);
	}
	
	public void testDynamiteTurnExplode(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		turn.setUserInterface(new TestUserInterface());
		sheriff.getInPlay().add(new Card(Card.CARDDYNAMITE, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		assertTrue(turn.isDynamiteExplode());
	}
	
	public void testDynamiteTurnNotExplode(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		turn.setUserInterface(new TestUserInterface());
		sheriff.getInPlay().add(new Card(Card.CARDDYNAMITE, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		assertFalse(turn.isDynamiteExplode());
	}
	
	public void testInJail(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setSheriffManualTest();
		turn.setUserInterface(new TestUserInterface());
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().add(new Card(Card.CARDJAIL, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		assertTrue(turn.isInJail());
	}
	
	public void testOutOfJail(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setUserInterface(new TestUserInterface());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().add(new Card(Card.CARDJAIL, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		assertFalse(turn.isInJail());
	}
	
	public void testOutNoJail(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		turn.setPlayers(Setup.getNormalPlayers(4));
		turn.setSheriffManualTest();
		assertFalse(turn.isInJail());
	}
	
	public void testNoBarrel(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		turn.setPlayers(Setup.getNormalPlayers(4));
		turn.setSheriffManualTest();
		Player player = new Player();
		player.setInPlay(new InPlay());
		player.setFigure(new Figure());
		assertFalse(turn.isBarrelSave(player) != 0);
	}
	
	public void testSavedByBarrel(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setSheriffManualTest();
		turn.setUserInterface(new TestUserInterface());
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().add(new Card(Card.CARDBARREL, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		assertTrue(turn.isBarrelSave(sheriff) != 0);
	}
	
	public void testNotSavedByBarrel(){
		Card drawnCard = new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUE9, Card.TYPEITEM);
		Deck deck = new Deck();
		deck.add(drawnCard);
		Discard discard = new Discard();
		Turn turn = new Turn();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setSheriffManualTest();
		turn.setUserInterface(new TestUserInterface());
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getInPlay().add(new Card(Card.CARDBARREL, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		sheriff.setFigure(new Figure());
		assertFalse(turn.isBarrelSave(sheriff) != 0);
	}
	
	public void testNoBeerPlayer(){
		Player player = new Player();
		player.setFigure(new Figure());
		Turn turn = new Turn();
		player.setHealth(2);
		turn.damagePlayer(player, 1, null);
		assertEquals(player.getHealth(), 1);
	}
	
	public void testNoBeerPlayerKill(){
		Player player = new Player();
		Turn turn = new Turn();
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		players.add(new Player());
		players.add(new Player());
		player.setFigure(new Figure());
		turn.setPlayers(players);
		player.setHealth(1);
		player.setHand(new Hand());
		assertTrue(players.contains(player));
		turn.setUserInterface(new TestUserInterface());
		turn.setDiscard(new Discard());
		turn.damagePlayer(player, 1, null);
		assertFalse(players.contains(player));
	}
	
	public void testBeerPlayerKill(){
		Player player = new Player();
		player.setHand(new Hand());
		Turn turn = new Turn();
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		turn.setPlayers(players);
		player.setHealth(1);
		turn.setUserInterface(new TestUserInterface());
		turn.setDiscard(new Discard());
		player.getHand().add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		player.setFigure(new Figure());
		turn.damagePlayer(player, 1, null);
		turn.setUserInterface(new TestUserInterface());
		turn.setDiscard(new Discard());		
		assertTrue(players.contains(player));
	}
	
	public void testSheriffKillDeputy(){
		Player player = new Player();
		player.setHand(new Hand());
		player.setInPlay(new InPlay());
		player.setFigure(new Figure());
		player.setRole(Player.SHERIFF);
		Turn turn = new Turn();
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		Player deputy = new Player();
		deputy.setHand(new Hand());
		deputy.setInPlay(new InPlay());
		deputy.setRole(Player.DEPUTY);
		players.add(deputy);
		turn.setPlayers(players);
		player.setHealth(1);
		turn.setDiscard(new Discard());				
		turn.setUserInterface(new TestUserInterface());
		player.getHand().add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		player.getInPlay().add(new Card(Card.CARDBARREL, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		player.getInPlay().setGun((new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEGUN)));
		
		deputy.getHand().add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		deputy.getInPlay().add(new Card(Card.CARDBARREL, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		deputy.getInPlay().setGun((new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEGUN)));
		turn.handleDeath(deputy, player);
		assertEquals(0, player.getHand().size());
		assertFalse(player.getInPlay().hasGun());
		assertFalse(player.getInPlay().hasItem(Card.CARDBARREL));
		
		assertEquals(0, deputy.getHand().size());
		assertFalse(deputy.getInPlay().hasGun());
		assertFalse(deputy.getInPlay().hasItem(Card.CARDBARREL));
	}
	
	public void testSheriffKillOutlaw(){
		Player player = new Player();
		player.setHand(new Hand());
		player.setInPlay(new InPlay());
		player.setRole(Player.SHERIFF);
		player.setFigure(new Figure());
		Turn turn = new Turn();
		turn.setDeck(Setup.setupDeck());
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		Player deputy = new Player();
		deputy.setHand(new Hand());
		deputy.setInPlay(new InPlay());
		deputy.setRole(Player.OUTLAW);
		players.add(deputy);
		turn.setPlayers(players);
		player.setHealth(1);
		turn.setDiscard(new Discard());				
		turn.setUserInterface(new TestUserInterface());
		
		turn.handleDeath(deputy, player);
		assertEquals(3, player.getHand().size());
	}
	
	public void testNotGameOver(){
		List<Player> players = Setup.getNormalPlayers(4);
		Turn turn = new Turn();
		turn.setPlayers(players);
		assertFalse(Turn.isGameOver(players));
	}
	
	public void testRenegadeWin(){
		List<Player> players = new ArrayList<Player>();
		Player renegade = new Player();
		renegade.setRole(Player.RENEGADE);
		players.add(renegade);
		Turn turn = new Turn();
		turn.setPlayers(players);
		assertTrue(Turn.isGameOver(players));
		assertEquals("Renegades", Turn.getWinners(players));
	}
	
	public void testOutlawWin(){
		List<Player> players = new ArrayList<Player>();
		Player renegade = new Player();
		renegade.setRole(Player.OUTLAW);
		players.add(renegade);
		Turn turn = new Turn();
		turn.setPlayers(players);
		assertTrue(Turn.isGameOver(players));
		assertEquals("Outlaws", Turn.getWinners(players));
	}
	
	public void testSheriffDeputyWin(){
		List<Player> players = new ArrayList<Player>();
		Player renegade = new Player();
		renegade.setRole(Player.SHERIFF);
		players.add(renegade);
		Turn turn = new Turn();
		turn.setPlayers(players);
		assertTrue(Turn.isGameOver(players));
		assertEquals("Sheriff and Deputies", Turn.getWinners(players));
	}
	
	public void testBartCasidy(){
		Turn turn = new Turn();
		turn.setDeck(Setup.setupDeck());
		turn.setPlayers(new ArrayList<Player>());
		Player player = new Player();
		player.setHand(new Hand());
		Figure figure = new Figure();
		figure.setName(Figure.BARTCASSIDY);
		player.setFigure(figure);
		turn.damagePlayer(player, 1, null);
		assertEquals(player.getHand().size(), 1);
	}
	
	public void testJourdonnais(){
		Turn turn = new Turn();
		Player player = new Player();
		player.setInPlay(new InPlay());
		Figure figure = new Figure();
		figure.setName(Figure.JOURDONNAIS);
		player.setFigure(figure);
		Deck deck = new Deck();
		Card card = new Card();
		turn.setUserInterface(new TestUserInterface());
		card.setSuit(Card.HEARTS);
		deck.add(card);
		Discard discard = new Discard();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		assertTrue(turn.isBarrelSave(player) != 0);
	}
	
	public void testPaulRegred(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		for(Player player : players){
			Figure figure = new Figure();
			figure.setName(Figure.PAULREGRET);
			player.setFigure(figure);
		}
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, sheriff.getInPlay().getGunRange()).size(), 0);
	}
	
	public void testRoseDoolan(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getFigure().setName(Figure.ROSEDOOLAN);
		assertEquals(Turn.getPlayersWithinRange(sheriff, players, sheriff.getInPlay().getGunRange()).size(), 3);
	}
	
	public void testWillyTheKid(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();	
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getFigure().setName(Figure.WILLYTHEKID);
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		turn.play();				
		assertEquals(sheriff.getHand().size(), 1);
		turn.play();
		assertEquals(sheriff.getHand().size(), 0);
	}
	
	public void testVultureSam(){
		Player player = new Player();
		player.setHand(new Hand());
		Figure figure = new Figure();
		figure.setName(Figure.VULTURESAM);
		player.setFigure(figure);		
		Player other = new Player();
		Hand hand = new Hand();
		InPlay inPlay = new InPlay();
		other.setHand(hand);
		other.setInPlay(inPlay);		
		other.getHand().add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		other.getInPlay().add(new Card(Card.CARDBARREL, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		other.getInPlay().setGun((new Card(Card.CARDSCHOFIELD, Card.HEARTS, Card.VALUE9, Card.TYPEGUN)));
		Discard discard = new Discard();		
		List<Player> players = new ArrayList<Player>();
		players.add(player);		
		Turn.deadDiscardAll(other, players, discard);
		assertEquals(3, player.getHand().size());
	}
	
	public void testLuckyDuke(){
		Turn turn = new Turn();
		Deck deck = new Deck();
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.LUCKYDUKE);
		player.setFigure(figure);
		Card card1 = new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Card card2 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card1);
		deck.add(card2);
		Discard discard = new Discard();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		turn.setUserInterface(new TestUserInterface());
		assertEquals(card2, turn.draw(player));
		assertTrue(deck.isEmpty());
	}
	
	public void testLuckyDukeChooseOther(){
		Turn turn = new Turn();
		Deck deck = new Deck();
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.LUCKYDUKE);
		player.setFigure(figure);
		Card card1 = new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Card card2 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card1);
		deck.add(card2);
		Discard discard = new Discard();
		turn.setDeck(deck);
		turn.setDiscard(discard);
		turn.setUserInterface(new TestPlayOneUserInterface());
		assertEquals(card1, turn.draw(player));
		assertTrue(deck.isEmpty());
	}
	
	public void testBlackJackHearts(){
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.BLACKJACK);
		player.setFigure(figure);
		player.setHand(new Hand());
		Turn turn = new Turn();
		turn.setUserInterface(new TestUserInterface());
		Deck deck = new Deck();
		Card card1 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Card card2 = new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Card card3 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card1);
		deck.add(card2);
		deck.add(card3);
		turn.drawCards(player, deck);
		assertEquals(3, player.getHand().size());
	}
	
	public void testBlackJackDiamonds(){
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.BLACKJACK);
		player.setFigure(figure);
		player.setHand(new Hand());
		Turn turn = new Turn();
		turn.setUserInterface(new TestUserInterface());
		Deck deck = new Deck();
		Card card1 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Card card2 = new Card(Card.CARDBEER, Card.DIAMONDS, Card.VALUE9, Card.TYPEITEM);
		Card card3 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card1);
		deck.add(card2);
		deck.add(card3);
		turn.drawCards(player, deck);
		assertEquals(3, player.getHand().size());
	}
	
	public void testBlackJackDefault(){
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.BLACKJACK);
		player.setFigure(figure);
		player.setHand(new Hand());
		Turn turn = new Turn();
		turn.setUserInterface(new TestUserInterface());
		Deck deck = new Deck();
		Card card1 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Card card2 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Card card3 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card1);
		deck.add(card2);
		deck.add(card3);
		turn.drawCards(player, deck);
		assertEquals(2, player.getHand().size());
	}
	
	public void testPedroRamerez(){
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.PEDRORAMIREZ);
		player.setFigure(figure);
		player.setHand(new Hand());
		Turn turn = new Turn();
		Deck deck = new Deck();
		Card card1 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Card card2 = new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Card card3 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card1);
		deck.add(card3);
		Discard discard = new Discard();
		discard.add(card2);
		turn.setDiscard(discard);
		turn.setUserInterface(new TestUserInterface());
		turn.drawCards(player, deck);
		assertEquals(2, player.getHand().size());
		assertFalse(deck.isEmpty());
	}
	
	public void testPedroRamerezDefault(){
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.PEDRORAMIREZ);
		player.setFigure(figure);
		player.setHand(new Hand());
		Turn turn = new Turn();
		Deck deck = new Deck();
		Card card1 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Card card2 = new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEITEM);
		Card card3 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card1);
		deck.add(card3);
		Discard discard = new Discard();
		discard.add(card2);
		turn.setDiscard(discard);
		turn.setUserInterface(new TestUserInterfaceBangBackOnce());
		turn.drawCards(player, deck);
		assertEquals(2, player.getHand().size());
		assertTrue(deck.isEmpty());
	}
	
	public void testJesseJones(){
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.JESSEJONES);
		player.setFigure(figure);
		player.setHand(new Hand());
		
		Player other = new Player();
		Figure otherFigure = new Figure();
		otherFigure.setName(Figure.JESSEJONES);
		other.setFigure(otherFigure);
		Hand otherHand = new Hand();
		Card card1 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		otherHand.add(card1);
		other.setHand(otherHand);
		
		Turn turn = new Turn();
		Deck deck = new Deck();
		Card card2 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card2);
		Discard discard = new Discard();		
		turn.setDiscard(discard);
		turn.setUserInterface(new TestUserInterface());
		
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		players.add(other);
		turn.setPlayers(players);
		
		turn.drawCards(player, deck);
		assertEquals(2, player.getHand().size());
	}
	
	public void testJesseJonesDefault(){
		Player player = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.JESSEJONES);
		player.setFigure(figure);
		player.setHand(new Hand());
		
		Player other = new Player();
		Figure otherFigure = new Figure();
		otherFigure.setName(Figure.JESSEJONES);
		other.setFigure(otherFigure);
		
		Turn turn = new Turn();
		Deck deck = new Deck();
		Card card2 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		Card card1 = new Card(Card.CARDBEER, Card.SPADES, Card.VALUE9, Card.TYPEITEM);
		deck.add(card2);
		deck.add(card1);
		Discard discard = new Discard();		
		turn.setDiscard(discard);
		turn.setUserInterface(new TestUserInterfaceBangBackOnce());
		
		List<Player> players = new ArrayList<Player>();
		players.add(other);
		players.add(player);
		turn.setPlayers(players);
		
		turn.drawCards(player, deck);
		assertEquals(2, player.getHand().size());
	}
	
	public void testCalamityJanetBang(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getFigure().setName(Figure.CALAMITYJANET);
		sheriff.getHand().add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		
		Player enemy = others.get(0);
		turn.play();
		assertEquals(enemy.getHealth(), enemy.getMaxHealth() - 1);
	}
	
	public void testCalamityJanetMiss(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceBangBackTwice();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		
		Player enemy = others.get(0);
		enemy.getFigure().setName(Figure.CALAMITYJANET);
		enemy.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		turn.play();
		assertEquals(enemy.getHealth(), enemy.getMaxHealth());
		assertEquals(enemy.getHand().size(), 0);
	}
	
	public void testKitCarlson(){
		Deck deck = new Deck();
		Turn turn = new Turn();
		Card card2 = new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY);
		deck.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		deck.add(card2);
		deck.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		turn.setDeck(deck);
		Player player = new Player();
		player.setHand(new Hand());
		Figure figure = new Figure();
		figure.setName(Figure.KITCARLSON);
		player.setFigure(figure);
		turn.setUserInterface(new TestPlayOneUserInterfaceChoosePlayerBangBack());
		turn.drawCards(player, deck);
		assertEquals(2, player.getHand().size());
		assertFalse(deck.isEmpty());
		assertEquals(card2, deck.pull());
	}
	
	public void testElGringo(){
		Player elGringo = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.ELGRINGO);		
		elGringo.setFigure(figure);
		Hand gringoHand = new Hand();
		elGringo.setHand(gringoHand);
		Player other = new Player();
		Hand otherHand = new Hand();
		otherHand.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		other.setHand(otherHand);
		Turn turn = new Turn();
		turn.setPlayers(new ArrayList<Player>());
		turn.damagePlayer(elGringo, 1, other);
		assertTrue(otherHand.size() == 0);
		assertTrue(gringoHand.size() == 1);
	}
	
	public void testElGringoOtherHasNone(){
		Player elGringo = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.ELGRINGO);		
		elGringo.setFigure(figure);
		Hand gringoHand = new Hand();
		elGringo.setHand(gringoHand);
		Player other = new Player();
		Hand otherHand = new Hand();
		other.setHand(otherHand);
		Turn turn = new Turn();
		turn.setPlayers(new ArrayList<Player>());
		turn.damagePlayer(elGringo, 1, other);
		assertTrue(otherHand.size() == 0);
		assertTrue(gringoHand.size() == 0);
	}
	
	public void testSuzyLafayette(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		Deck deck = Setup.setupDeck();
		turn.setDeck(deck);
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		Figure figure = new Figure();
		figure.setName(Figure.SUZYLAFAYETTE);
		sheriff.setFigure(figure);
		sheriff.getHand().add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		sheriff.getHand().setEmptyListener(new DrawCardEmptyHandListener(deck, sheriff.getHand()));
		UserInterface testUserInterface = new TestPlayOneUserInterface();
		turn.setUserInterface(testUserInterface);
		turn.play();
		assertEquals(1, sheriff.getHand().size());
	}
	
	public void testSidKetchumUserInterface(){
		Player sidKetchum = new Player();
		Hand hand = new Hand();
		hand.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		hand.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		sidKetchum.setHand(hand);
		UserInterface userInterface = new TestUserInterface();
		List<Object> cardsToDiscard = userInterface.chooseTwoDiscardForLife(sidKetchum);
		for(Object card : cardsToDiscard){
			hand.remove(card);
		}
		assertEquals(0, hand.size());
	}
	
	public void testSidKetchumHurt(){
		Player sidKetchum = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.SIDKETCHUM);
		sidKetchum.setFigure(figure);
		Hand hand = new Hand();
		hand.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		hand.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		sidKetchum.setHand(hand);
		UserInterface userInterface = new TestUserInterface();
		Turn turn = new Turn();
		turn.setPlayers(new ArrayList<Player>());
		turn.setUserInterface(userInterface);
		sidKetchum.setMaxHealth(1);
		turn.damagePlayer(sidKetchum, 1, null);
		assertEquals(1, sidKetchum.getHealth());
		assertEquals(0, sidKetchum.getHand().size());
	}
	
	public void testSidKetchumDiscard(){
		Player sidKetchum = new Player();
		Figure figure = new Figure();
		figure.setName(Figure.SIDKETCHUM);
		sidKetchum.setFigure(figure);
		Hand hand = new Hand();
		hand.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		hand.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		sidKetchum.setHand(hand);
		UserInterface userInterface = new TestUserInterface();
		Turn turn = new Turn();
		turn.setPlayers(new ArrayList<Player>());
		turn.setUserInterface(userInterface);
		sidKetchum.setMaxHealth(2);
		sidKetchum.setHealth(1);
		turn.setDiscard(new Discard());
		turn.discard(sidKetchum);
		assertEquals(2, sidKetchum.getHealth());
		assertEquals(0, sidKetchum.getHand().size());
	}
	
	public void testSlabTheKiller(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		Figure figure = new Figure();
		figure.setName(Figure.SLABTHEKILLER);
		sheriff.setFigure(figure);
		UserInterface testUserInterface = new TestUserInterfaceBangBackTwice();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		
		Player enemy = others.get(0);
		enemy.getHand().add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		enemy.getHand().add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		turn.play();
		assertEquals(enemy.getHealth(), enemy.getMaxHealth());
		assertEquals(0, enemy.getHand().size());
	}
	
	public void testPlayMissDoNothing(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();
		sheriff.getHand().add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceBangBackTwice();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();		
		assertEquals(1, sheriff.getHand().size());
	}
	
	public void testVolcanic(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();	
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getFigure().setName(Figure.WILLYTHEKID);
		sheriff.getInPlay().setGun(new Card(Card.CARDVOLCANIC, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(sheriff), players.indexOf(otherPlayer), players.size());
			if(distance <= 1){
				others.add(otherPlayer);
			}
		}
		others.remove(sheriff);
		turn.play();				
		assertEquals(sheriff.getHand().size(), 1);
		turn.play();
		assertEquals(sheriff.getHand().size(), 0);
	}
	
	public void testTryMultiBangScofield(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();	
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		sheriff.getInPlay().setGun(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();				
		assertEquals(sheriff.getHand().size(), 1);
		turn.play();
		assertEquals(sheriff.getHand().size(), 1);
	}
	
	public void testBangNoOneInRange(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();	
		sheriff.getHand().add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		for(Player player : players){
			player.getInPlay().add(new Card(Card.CARDMUSTANG, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		}
		turn.play();				
		assertEquals(1, sheriff.getHand().size());				
	}
	
	public void testPanicNoOneInRange(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();	
		sheriff.getHand().add(new Card(Card.CARDPANIC, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		for(Player player : players){
			player.getInPlay().add(new Card(Card.CARDMUSTANG, Card.CLUBS, Card.VALUEQ, Card.TYPEITEM));
		}
		turn.play();				
		assertEquals(1, sheriff.getHand().size());				
	}
	
	public void testCatbalouNothingInPlay(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();	
		sheriff.getHand().add(new Card(Card.CARDCATBALOU, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();				
		assertEquals(1, sheriff.getHand().size());				
	}
	
	public void testPlayEmptyHand(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();		
		UserInterface testUserInterface = new TestUserInterfaceNoMiss();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();				
		assertTrue(turn.isDonePlaying());
	}
	
	public void testPlayDonePlaying(){
		Turn turn = new Turn();
		List<Player> players = Setup.getNormalPlayers(4);
		turn.setPlayers(players);
		turn.setDeck(Setup.setupDeck());
		turn.setDiscard(new Discard());
		turn.setSheriffManualTest();
		Player sheriff = turn.getCurrentPlayer();	
		sheriff.getHand().add(new Card(Card.CARDCATBALOU, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		UserInterface testUserInterface = new TestUserInterfaceDonePlaying();
		turn.setUserInterface(testUserInterface);
		turn.setDiscard(new Discard());
		turn.setDeck(Setup.setupDeck());
		turn.play();				
		assertTrue(turn.isDonePlaying());
	}
}
	
