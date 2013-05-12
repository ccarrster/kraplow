package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

import com.chriscarr.bang.cards.Bang;
import com.chriscarr.bang.cards.Card;
import com.chriscarr.bang.cards.Missed;
import com.chriscarr.bang.gamestate.GameState;
import com.chriscarr.bang.gamestate.GameStateCard;
import com.chriscarr.bang.gamestate.GameStateImpl;
import com.chriscarr.bang.gamestate.GameStatePlayer;
import com.chriscarr.bang.userinterface.UserInterface;

public class Turn {

	private List<Player> players;
	private Player currentPlayer;
	private UserInterface userInterface;
	private boolean donePlaying = false;
	private Discard discard;
	private Deck deck;
	private int bangsPlayed = 0;

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Player getPlayersTurn() {
		return currentPlayer;
	}
	
	public Player getPlayerForName(String name){
		for(int i = 0; i < players.size(); i++){
			Player player = players.get(i);
			if(player.getName().equals(name)){
				return player;
			}
		}
		return null;
	}

	public static Player getNextPlayer(Player player, List<Player> players) {
		int index = players.indexOf(player);
		if (index == players.size() - 1) {
			index = 0;
		} else {
			index = index + 1;
		}
		return players.get(index);
	}

	public static Player getPreviousPlayer(Player player, List<Player> players) {
		int index = players.indexOf(player);
		if (index == 0) {
			index = players.size() - 1;
		} else {
			index = index - 1;
		}
		return players.get(index);
	}

	public void nextTurn() {
		currentPlayer = getNextPlayer(currentPlayer, players);
		donePlaying = false;
		bangsPlayed = 0;
		turnLoop(currentPlayer);
	}

	private void turnLoop(Player currentPlayer) {
		boolean inJail = false;
		try {
			userInterface.printInfo(currentPlayer.getName() + "'s turn.");
			if (isDynamiteExplode()) {
				discardDynamite();
				userInterface.printInfo("Dynamite Exploded on "
						+ currentPlayer.getName());
				damagePlayer(currentPlayer, players, currentPlayer, 3, null,
						deck, discard, userInterface);
				if (isGameOver(players)) {
					userInterface.printInfo("Winners are " + getWinners(players) + " " + getRoles(players));
					throw new EndOfGameException("Game over");
				}
			} else {
				passDynamite();
			}
			inJail = isInJail();
			if (!inJail && players.contains(currentPlayer)) {
				this.drawCards(currentPlayer, deck);
				while (!donePlaying && players.contains(currentPlayer)) {
					play();
					if (isGameOver(players)) {
						userInterface.printInfo("Winners are " + getWinners(players) + " " + getRoles(players));
						throw new EndOfGameException("Game over");
					}
				}
			}
		} catch (EndOfGameException e) {
			return;
		}
		if (players.contains(currentPlayer)) {
			if(!inJail){
				discard(currentPlayer);
			}
		}
		nextTurn();
	}

	public void setSheriff() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getRole() == Player.SHERIFF) {
				currentPlayer = players.get(i);
				turnLoop(currentPlayer);
			}
		}
	}

	public void setSheriffManualTest() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getRole() == Player.SHERIFF) {
				currentPlayer = players.get(i);
			}
		}
	}

	public void drawCards(Player player, Deck deck) {
		Hand hand = player.getHand();
		if (Figure.KITCARLSON.equals(player.getName())) {
			List<Object> cards = pullCards(deck, 3);
			Object cardToPutBack = chooseValidCardToPutBack(player, cards,
					userInterface);
			cards.remove(cardToPutBack);
			deck.add(cardToPutBack);
			for (Object card : cards) {
				hand.add(card);
			}
			userInterface.printInfo(Figure.KITCARLSON
					+ " put a card back on the draw pile");
		} else if (Figure.JESSEJONES.equals(player.getName())) {
			List<Player> otherPlayers = new ArrayList<Player>();
			for (Player other : players) {
				if (!other.equals(player) && other.getHand().size() > 0) {
					otherPlayers.add(other);
				}
			}
			boolean chosenFromPlayer = false;
			if (!otherPlayers.isEmpty()) {
				chosenFromPlayer = userInterface.chooseFromPlayer(player);
			}
			if (chosenFromPlayer) {
				Player chosenPlayer = getValidChosenPlayer(currentPlayer,
						otherPlayers, userInterface);
				Object randomCard = chosenPlayer.removeRandom();
				hand.add(randomCard);
				userInterface.printInfo(Figure.JESSEJONES
						+ " drew a card from " + chosenPlayer.getName()
						+ " hand.");
			} else {
				hand.add(deck.pull());
				userInterface.printInfo(Figure.JESSEJONES
						+ " drew a card from the deck.");
			}
			hand.add(deck.pull());
		} else if (Figure.PEDRORAMIREZ.equals(player.getName())) {
			if (!discard.isEmpty()) {
				boolean chosenDiscard = userInterface.chooseDiscard(player, discard.peek());
				if (chosenDiscard) {
					Object discardCard = discard.remove();
					hand.add(discardCard);
					userInterface.printInfo(Figure.PEDRORAMIREZ + " drew a "
							+ ((Card) discardCard).getName()
							+ " from the discard pile.");
				} else {
					hand.add(deck.pull());
					userInterface.printInfo(Figure.PEDRORAMIREZ
							+ " drew a card from the deck.");
				}
			} else {
				hand.add(deck.pull());
				userInterface.printInfo(Figure.PEDRORAMIREZ
						+ " drew a card from the deck.");
			}
			hand.add(deck.pull());
		} else {
			hand.add(deck.pull());
			Object secondCard = deck.pull();
			hand.add(secondCard);
			if (Figure.BLACKJACK.equals(player.getName())) {
				int suit = ((Card) secondCard).getSuit();
				userInterface.printInfo(Figure.BLACKJACK + " drew a "
						+ Card.suitToString(suit) + " "
						+ ((Card) secondCard).getName());
				if (suit == Card.HEARTS || suit == Card.DIAMONDS) {
					hand.add(deck.pull());
					userInterface.printInfo(Figure.BLACKJACK
							+ " drew a third card from the deck.");
				}
			}
		}
	}

	public void discard(Player player) {
		discardTwoCardsForLife(player, discard, userInterface);
		Hand hand = player.getHand();
		String discardedCards = "";
		while (hand.size() > player.getHealth()) {
			Object discardedCard = askPlayerToDiscard(player, discard);
			discardedCards += ((Card)discardedCard).getName() + ", ";
		}
		if(!discardedCards.equals("")){
			userInterface.printInfo(player.getName() + " discarded " + discardedCards.substring(0, discardedCards.length() - 2) + ".");
		}
	}

	private Object askPlayerToDiscard(Player player, Discard discard) {
		int card = -1;
		while (card < 0 || card > player.getHand().size() - 1) {
			card = userInterface.askDiscard(player);
		}
		Object removedCard = (Card) player.getHand().remove(card);
		discard.add(removedCard);
		return removedCard;
	}

	public void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}

	public void play() {
		for (Player player : players) {
			if (Figure.SUZYLAFAYETTE.equals(player.getName())) {
				Hand playerHand = player.getHand();
				if (playerHand.isEmpty()) {
					playerHand.add(deck.pull());
					userInterface.printInfo(player.getName()
							+ " ran out of cards and drew a card.");
				}
			}
		}
		Hand hand = currentPlayer.getHand();
		int card = -2;
		while (card < -1 || card > hand.size() - 1) {
			card = userInterface.askPlay(currentPlayer);
			System.out.println("Turn askPlay card: " + card);
		}
		if (hand.size() == 0 || card == -1) {
			donePlaying = true;
			userInterface.printInfo(currentPlayer.getName()
					+ " is finished playing.");
			return;
		}
		Card playedCard = (Card) hand.get(card);
		if (playedCard.canPlay(currentPlayer, players, bangsPlayed)) {
			hand.remove(card);
			if (playedCard.getName() == Card.CARDGENERALSTORE
					|| playedCard.getName() == Card.CARDGATLING
					|| playedCard.getName() == Card.CARDINDIANS) {
				userInterface.printInfo(currentPlayer.getName() + " played a "
						+ playedCard.getName() + ".");
			}
			boolean success = playedCard.play(currentPlayer, players, userInterface, deck,
					discard, this);
			if(success){
				if (playedCard instanceof Bang || playedCard instanceof Missed) {
					bangsPlayed++;
				}
			}
			if (playedCard.getName() != Card.CARDCATBALOU
					&& playedCard.getName() != Card.CARDPANIC
					&& playedCard.getName() != Card.CARDJAIL
					&& playedCard.getName() != Card.CARDMISSED
					&& playedCard.getName() != Card.CARDBANG
					&& playedCard.getName() != Card.CARDDUEL
					&& playedCard.getName() != Card.CARDGENERALSTORE
					&& playedCard.getName() != Card.CARDGATLING
					&& playedCard.getName() != Card.CARDINDIANS) {
				userInterface.printInfo(currentPlayer.getName() + " played a "
						+ playedCard.getName() + ".");
			}
		} else {
			System.out.println("**Turn - Weirdo can not play**");
		}
	}

	public static int validPlayMiss(Player player, UserInterface userInterface) {
		while (true) {
			int playedMiss = userInterface.respondMiss(player);
			if (playedMiss == -1) {
				return playedMiss;
			} else {
				Hand hand = player.getHand();
				Card card = (Card) hand.get(playedMiss);
				if (Card.CARDMISSED.equals(card.getName())) {
					return playedMiss;
				} else if (Card.CARDBANG.equals(card.getName())
						&& Figure.CALAMITYJANET.equals(player.getName())) {
					return playedMiss;
				}
			}
		}
	}

	static int validPlayBeer(Player player, UserInterface userInterface) {
		while (true) {
			int playedBeer = userInterface.respondBeer(player);
			if (playedBeer == -1
					|| Card.CARDBEER.equals(((Card) (player.getHand()
							.get(playedBeer))).getName())) {
				return playedBeer;
			}
		}
	}

	public static int validPlayBang(Player player, UserInterface userInterface) {
		while (true) {
			int playerShot = userInterface.respondBang(player);
			if (playerShot == -1) {
				return playerShot;
			} else {
				Hand hand = player.getHand();
				Card card = (Card) hand.get(playerShot);
				if (Card.CARDBANG.equals(card.getName())) {
					return playerShot;
				} else if (Card.CARDMISSED.equals(card.getName())
						&& Figure.CALAMITYJANET.equals(player.getName())) {
					return playerShot;
				}
			}
		}
	}

	public boolean isDonePlaying() {
		return donePlaying;
	}

	public void setDiscard(Discard discard) {
		this.discard = discard;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public static List<Player> getPlayersWithinRange(Player player,
			List<Player> players, int range) {
		List<Player> others = new ArrayList<Player>();
		Player cancelPlayer = new CancelPlayer();
		cancelPlayer.setHand(new Hand());
		cancelPlayer.setInPlay(new InPlay());
		others.add(cancelPlayer);
		for (Player otherPlayer : players) {
			int distance = AlivePlayers.getDistance(players.indexOf(player),
					players.indexOf(otherPlayer), players.size());
			if (otherPlayer.getInPlay().hasItem(Card.CARDMUSTANG)) {
				distance = distance + 1;
			}
			if (Figure.PAULREGRET.equals(otherPlayer.getName())) {
				distance = distance + 1;
			}
			if (player.getInPlay().hasItem(Card.CARDSCOPE)) {
				distance = distance - 1;
			}
			if (Figure.ROSEDOOLAN.equals(player.getName())) {
				distance = distance - 1;
			}
			if (distance <= range) {
				others.add(otherPlayer);
			}
		}
		return others(player, others);
	}

	public boolean isDynamiteExplode() {
		InPlay currentInPlay = currentPlayer.getInPlay();
		if (currentInPlay.hasItem(Card.CARDDYNAMITE)) {
			Card drawnCard = (Card) draw(currentPlayer, deck, discard,
					userInterface);
			return Card.isExplode(drawnCard);
		}
		return false;
	}

	public void passDynamite() {
		InPlay currentInPlay = currentPlayer.getInPlay();
		if (currentInPlay.hasItem(Card.CARDDYNAMITE)) {
			Object dynamiteCard = currentInPlay.removeDynamite();
			Player nextPlayer = getNextPlayer(currentPlayer, players);
			InPlay nextInPlay = nextPlayer.getInPlay();
			userInterface.printInfo("Dynamite Passed to "
					+ nextPlayer.getName());
			nextInPlay.add(dynamiteCard);
		}
	}

	public void discardDynamite() {
		InPlay currentInPlay = currentPlayer.getInPlay();
		if (currentInPlay.hasItem(Card.CARDDYNAMITE)) {
			Object dynamiteCard = currentInPlay.removeDynamite();
			discard.add(dynamiteCard);
		}
	}

	public static Object draw(Player player, Deck deck, Discard discard,
			UserInterface userInterface) {
		if (Figure.LUCKYDUKE.equals(player.getName())) {
			List<Object> cards = pullCards(deck, 2);
			int chosenCard = -1;
			while (chosenCard < 0 || chosenCard > (cards.size() - 1)) {
				chosenCard = userInterface.chooseDrawCard(player, cards);
			}
			for (Object card : cards) {
				discard.add(card);
			}
			Card drawnCard = (Card) cards.get(chosenCard);
			userInterface.printInfo(player.getName() + " drew a "
					+ Card.valueToString(drawnCard.getValue()) + " of "
					+ Card.suitToString(drawnCard.getSuit()) + " "
					+ drawnCard.getName());
			return cards.get(chosenCard);
		} else {
			Object card = deck.pull();
			Card drawnCard = (Card) card;
			userInterface.printInfo(player.getName() + " drew a "
					+ Card.valueToString(drawnCard.getValue()) + " of "
					+ Card.suitToString(drawnCard.getSuit()) + " "
					+ drawnCard.getName());
			discard.add(card);
			return card;
		}
	}

	public boolean isInJail() {
		InPlay currentInPlay = currentPlayer.getInPlay();
		if (currentInPlay.hasItem(Card.CARDJAIL)) {
			Object jailCard = currentInPlay.removeJail();
			discard.add(jailCard);
			Card drawn = (Card) draw(currentPlayer, deck, discard,
					userInterface);
			boolean inJail = drawn.getSuit() != Card.HEARTS;
			if (inJail) {
				userInterface.printInfo(currentPlayer.getName()
						+ " stays in jail");
			} else {
				userInterface.printInfo(currentPlayer.getName()
						+ " breaks out of jail");
			}
			return inJail;
		}
		return false;
	}

	public static int isBarrelSave(Player player, Deck deck, Discard discard,
			UserInterface userInterface, int missesRequired) {
		int misses = 0;
		if (Figure.JOURDONNAIS.equals(player.getName())) {
			Card drawn = (Card) draw(player, deck, discard, userInterface);
			if (drawn.getSuit() == Card.HEARTS) {
				misses = misses + 1;
				userInterface.printInfo(Figure.JOURDONNAIS + " drew a "
						+ Card.suitToString(Card.HEARTS) + " and was saved by his ability.");
			} else {
				userInterface.printInfo(Figure.JOURDONNAIS + " drew a "
						+ Card.suitToString(drawn.getSuit())
						+ " and was not saved by his ability.");
			}
		}
		if(misses >= missesRequired){
			return misses;
		}
		InPlay currentInPlay = player.getInPlay();
		if (currentInPlay.hasItem(Card.CARDBARREL)) {
			Card drawn = (Card) draw(player, deck, discard, userInterface);
			if (drawn.getSuit() == Card.HEARTS) {
				misses = misses + 1;
				userInterface.printInfo(player.getName() + " drew a "
						+ Card.suitToString(Card.HEARTS) + " and was saved by their barrel.");
			} else {
				userInterface.printInfo(player.getName() + " drew a "
						+ Card.suitToString(drawn.getSuit())
						+ " and was not saved by their barrel.");
			}
		}
		return misses;
	}

	public void damagePlayer(Player player, List<Player> players,
			Player currentPlayer, int damage, Player damager, Deck deck,
			Discard discard, UserInterface userInterface) {
		player.setHealth(player.getHealth() - damage);
		if (player.getHealth() <= 0 && players.size() > 2) {
			boolean doNotPlayBeer = false;
			while (!doNotPlayBeer && player.getHealth() <= 0) {
				int playedBeer = validPlayBeer(player, userInterface);
				if (playedBeer != -1) {
					player.setHealth(player.getHealth() + 1);
					discard.add(player.getHand().remove(playedBeer));
				} else {
					doNotPlayBeer = true;
				}
			}
		}
		discardTwoCardsForLife(player, discard, userInterface);
		if (player.getHealth() <= 0) {
			handleDeath(player, damager, currentPlayer, players, userInterface,
					deck, discard);
		} else {
			if (Figure.BARTCASSIDY.equals(player.getName())) {
				for (int i = 0; i < damage; i++) {
					player.getHand().add(deck.pull());
					userInterface
							.printInfo(Figure.BARTCASSIDY
									+ " draws a card from the deck because he was damaged.");
				}
			} else if (damager != null
					&& Figure.ELGRINGO.equals(player.getName())) {
				Hand otherHand = damager.getHand();
				if (otherHand.size() != 0) {
					Hand playerHand = player.getHand();
					playerHand.add(otherHand.removeRandom());
					userInterface.printInfo(Figure.ELGRINGO
							+ " draws a card from " + player.getName()
							+ " because he was damaged.");
				}
			}
		}
	}

	public void handleDeath(Player player, Player damager,
			Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard) {
		if (player.equals(currentPlayer)) {
			currentPlayer = getPreviousPlayer(currentPlayer, players);
		}
		players.remove(player);
		userInterface.printInfo(player.getName() + " is dead. Role was "
				+ Player.roleToString(player.getRole()));
		if (!isGameOver(players)) {
			deadDiscardAll(player, players, discard);
			if (damager != null) {
				if (damager.getRole() == Player.SHERIFF
						&& player.getRole() == Player.DEPUTY) {
					userInterface.printInfo(damager.getName()
							+ " killed own deputy, loses all cards");
					discardAll(damager, discard);
				} else if (player.getRole() == Player.OUTLAW) {
					userInterface.printInfo(damager.getName()
							+ " killed an outlaw, draws 3 cards");
					deckToHand(damager.getHand(), deck, 3);
				}
			}
		}
	}

	public void discardAll(Player player, Discard discard) {
		List<Object> discardCards = new ArrayList<Object>();
		Hand hand = player.getHand();
		while (hand.size() != 0) {
			discardCards.add(hand.remove(0));
		}
		InPlay inPlay = player.getInPlay();
		if (inPlay.hasGun()) {
			discardCards.add(inPlay.removeGun());
		}
		while (inPlay.count() > 0) {
			discardCards.add(inPlay.remove(0));
		}
		for (Object discardCard : discardCards) {
			hand.add(discardCard);
		}
		String discardedCards = "";
		while (hand.size() != 0) {
			Object discardedCard = askPlayerToDiscard(player, discard);
			discardedCards += ((Card)discardedCard).getName() + ", ";
		}
		if(!discardedCards.equals("")){
			userInterface.printInfo(player.getName() + " discarded " + discardedCards.substring(0, discardedCards.length() - 2) + ".");
		}
	}

	public void deadDiscardAll(Player player, List<Player> players,
			Discard discard) {
		List<Object> discardCards = new ArrayList<Object>();
		Hand hand = player.getHand();
		while (hand.size() != 0) {
			discardCards.add(hand.remove(0));
		}
		InPlay inPlay = player.getInPlay();
		if (inPlay.hasGun()) {
			discardCards.add(inPlay.removeGun());
		}
		while (inPlay.count() > 0) {
			discardCards.add(inPlay.remove(0));
		}
		Player vultureSam = null;
		for (Player alivePlayer : players) {
			if (Figure.VULTURESAM.equals(alivePlayer.getName())) {
				vultureSam = alivePlayer;
			}
		}
		if (vultureSam == null) {
			for (Object discardCard : discardCards) {
				hand.add(discardCard);
			}
			String discardedCards = "";
			while (hand.size() != 0) {
				Object discardedCard = askPlayerToDiscard(player, discard);
				discardedCards += ((Card)discardedCard).getName() + ", ";
			}
			if(!discardedCards.equals("")){
				userInterface.printInfo(player.getName() + " discarded " + discardedCards.substring(0, discardedCards.length() - 2) + ".");
			}
		} else {
			for (Object card : discardCards) {
				vultureSam.getHand().add(card);
			}
			userInterface.printInfo(Figure.VULTURESAM + " takes "
					+ player.getName() + "'s cards.");
		}
	}

	public static boolean isGameOver(List<Player> players) {
		return isDead(Player.SHERIFF, players)
				|| (isDead(Player.RENEGADE, players)
				&& isDead(Player.OUTLAW, players));
	}

	private static boolean isDead(int role, List<Player> players) {
		for (Player player : players) {
			if (player.getRole() == role) {
				return false;
			}
		}
		return true;
	}

	public static String getWinners(List<Player> players) {
		if (isDead(Player.DEPUTY, players)
				&& isDead(Player.OUTLAW, players)
				&& isDead(Player.SHERIFF, players)) {
			return "Renegades";
		} else if (isDead(Player.SHERIFF, players)
				&& (!isDead(Player.DEPUTY, players) || !isDead(Player.OUTLAW,
						players))) {
			return "Outlaws"; 
		} else if (isDead(Player.OUTLAW, players) && isDead(Player.RENEGADE, players)) {
			return "Sheriff and Deputies";
		} else {
			throw new RuntimeException("No Winner");
		}
	}
	
	public static String getRoles(List<Player> players) {
		String result = "";
		for(int i = 0; i < players.size(); i++){
			Player player = players.get(i);
			result += player.getFigure().getName() + " was a " + Player.roleToString(player.getRole()) + ". ";  
		}
		return result;
	}

	public static void discardTwoCardsForLife(Player player, Discard discard,
			UserInterface userInterface) {
		if (Figure.SIDKETCHUM.equals(player.getName())) {
			Hand hand = player.getHand();
			if (hand.size() >= 2) {
				List<Object> cardsToDiscard = null;
				while (cardsToDiscard == null || cardsToDiscard.size() % 2 != 0) {
					cardsToDiscard = userInterface
							.chooseTwoDiscardForLife(player);
				}
				if (cardsToDiscard.size() % 2 == 0) {
					for (Object card : cardsToDiscard) {
						hand.remove(card);
						discard.add(card);
						userInterface.printInfo(Figure.SIDKETCHUM
								+ " discards " + ((Card) card).getName()
								+ " for life.");
					}
					player.setHealth(player.getHealth()
							+ (cardsToDiscard.size() / 2));
					if (player.getHealth() > player.getMaxHealth()) {
						player.setHealth(player.getMaxHealth());
					}
				}
			}
		}
	}

	public static boolean playerHasCardsToTake(Player player) {
		boolean emptyHand = player.getHand().isEmpty();
		InPlay inPlay = player.getInPlay();
		boolean hasGun = inPlay.hasGun();
		boolean emptyInPlay = inPlay.isEmpty();
		return !emptyHand || hasGun || !emptyInPlay;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public static List<Player> othersWithCardsToTake(Player player,
			List<Player> others) {
		List<Player> othersCopy = new ArrayList<Player>();
		for (Player otherPlayer : others) {
			if (playerHasCardsToTake(otherPlayer)) {
				othersCopy.add(otherPlayer);
			}
		}
		othersCopy.remove(player);
		return othersCopy;
	}

	public static List<Player> others(Player player, List<Player> others) {
		List<Player> othersCopy = new ArrayList<Player>();
		for (Player otherPlayer : others) {
			othersCopy.add(otherPlayer);
		}
		othersCopy.remove(player);
		return othersCopy;
	}

	public static List<Object> pullCards(Deck deck, int countCards) {
		List<Object> cards = new ArrayList<Object>();
		for (int i = 0; i < countCards; i++) {
			cards.add(deck.pull());
		}
		return cards;
	}

	public static void deckToHand(Hand hand, Deck deck, int countCards) {
		for (int i = 0; i < countCards; i++) {
			hand.add(deck.pull());
		}
	}

	public static List<Player> getJailablePlayers(Player player,
			List<Player> players) {
		List<Player> others = new ArrayList<Player>();
		Player cancelPlayer = new CancelPlayer();
		cancelPlayer.setHand(new Hand());
		cancelPlayer.setInPlay(new InPlay());
		others.add(cancelPlayer);
		for (Player otherPlayer : players) {
			boolean isInJail = otherPlayer.getInPlay().hasItem(Card.CARDJAIL);
			boolean isSheriff = otherPlayer.getRole() == Player.SHERIFF;
			boolean isPlayer = otherPlayer.equals(player);
			if (!isInJail && !isSheriff && !isPlayer) {
				others.add(otherPlayer);
			}
		}
		return others;
	}

	public static List<Player> getPlayersWithCards(List<Player> players) {
		List<Player> playersWithCards = new ArrayList<Player>();
		for (Player player : players) {
			if(player instanceof CancelPlayer){
				playersWithCards.add(player);
			} else if (playerHasCardsToTake(player)) {
				playersWithCards.add(player);
			}
		}
		return playersWithCards;
	}

	public static List<String> getPlayersNames(List<Player> players) {
		List<String> names = new ArrayList<String>();
		for (Player player : players) {
			names.add(player.getName());
		}
		return names;
	}

	public static Player getValidChosenPlayer(Player player,
			List<Player> choosable, UserInterface userInterface) {
		int chosenPlayer = -1;
		while (chosenPlayer < 0 || chosenPlayer > choosable.size() - 1) {
			chosenPlayer = userInterface.askPlayer(player,
					getPlayersNames(choosable));
		}
		return choosable.get(chosenPlayer);
	}

	public static boolean isBeerGiveHealth(List<Player> players) {
		if (players.size() > 2) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMaxHealth(Player player) {
		return player.getHealth() == player.getMaxHealth();
	}

	public static Object chooseValidCardToPutBack(Player player,
			List<Object> cards, UserInterface userInterface) {
		int cardIndex = -1;
		while (cardIndex < 0 || cardIndex > cards.size() - 1) {
			cardIndex = userInterface.chooseCardToPutBack(player, cards);
		}
		return cards.get(cardIndex);
	}

	public GameState getGameState() {
		GameState gameState = new GameStateImpl(this);
		return gameState;
	}

	public GameStateCard getDiscardTopCard() {
		if (!discard.isEmpty()) {
			return cardToGameStateCard((Card) discard.peek());
		} else {
			return null;
		}
	}

	public static GameStateCard cardToGameStateCard(Card fromCard) {
		if (fromCard == null) {
			return null;
		}
		GameStateCard card = new GameStateCard();
		card.name = fromCard.getName();
		card.suit = Card.suitToString(fromCard.getSuit());
		card.value = Card.valueToString(fromCard.getValue());
		card.type = Card.typeToString(fromCard.getType());
		return card;
	}

	public int getDeckSize() {
		return deck.size();
	}

	public boolean isGameOver() {
		return isGameOver(players);
	}

	public List<GameStatePlayer> getGameStatePlayers() {
		List<GameStatePlayer> gameStatePlayers = new ArrayList<GameStatePlayer>();
		for (Player player : players) {
			GameStatePlayer gameStatePlayer = new GameStatePlayer();
			gameStatePlayer.name = player.getName();
			gameStatePlayer.health = player.getHealth();
			gameStatePlayer.maxHealth = player.getMaxHealth();
			gameStatePlayer.handSize = player.getHandSize();
			gameStatePlayer.gun = player.getGameStateGun();
			gameStatePlayer.isSheriff = player.isSheriff();
			gameStatePlayer.specialAbility = player.getSpecialAbility();
			gameStatePlayer.inPlay = player.getGameStateInPlay();
			gameStatePlayers.add(gameStatePlayer);
		}
		return gameStatePlayers;
	}

	public List<String> targets(Player player, Card card) {
		List<String> names = new ArrayList<String>();
		List<Player> targets = card.targets(player, players);
		for (Player target : targets) {
			names.add(target.getName());
		}
		return names;
	}

	public boolean canPlay(Player player, Card card) {
		return card.canPlay(player, players, bangsPlayed);
	}

	public static List<Object> validRespondTwoMiss(Player player,
			UserInterface userInterface) {
		List<Object> cards = null;
		boolean validCards = false;
		while (!validCards) {
			cards = userInterface.respondTwoMiss(player);
			if (cards.size() == 0) {
				validCards = true;
			} else if (cards.size() == 2) {
				validCards = true;
				for (Object card : cards) {
					Card missCard = (Card) card;
					if (!(Card.CARDMISSED.equals(missCard.getName()) || (Card.CARDBANG
							.equals(missCard.getName()) && Figure.CALAMITYJANET
							.equals(player.getName())))) {
						validCards = false;
					}
				}
			}
		}
		return cards;
	}

	public String getRoleForName(String name) {
		for (Player player : players) {
			if (player.getName().equals(name)) {
				return Player.roleToString(player.getRole());
			}
		}
		return null;
	}

	public String roleToGoal(String name) {
		for (Player player : players) {
			if (player.getName().equals(name)) {
				return Player.roleToGoal(player.getRole());
			}
		}
		return null;
	}

	public GameState getGameState(boolean gameOver) {
		GameState gameState = new GameStateImpl(this, gameOver);
		return gameState;
	}

	public String getTimeout() {
		return userInterface.getTimeout();
	}
}
