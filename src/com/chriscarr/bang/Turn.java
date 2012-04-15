package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class Turn {

	private List<Player> players;
	private Player currentPlayer; 
	private boolean bangPlayed = false;
	private UserInterface userInterface;
	private boolean donePlaying = false;
	private Discard discard;
	private Deck deck;
	private int bangsPlayed = 0;
	private String winner = null;
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Player getPlayersTurn() {		
		return currentPlayer;
	}
	
	public Player getNextPlayer(Player player){
		int index = players.indexOf(player);
		if(index == players.size() - 1){
			index = 0;
		} else {
			index = index + 1;
		}
		return players.get(index);
	}
	
	public Player getPreviousPlayer(Player player){
		int index = players.indexOf(player);
		if(index == 0){
			index = players.size() - 1;
		} else {
			index = index - 1;
		}
		return players.get(index);
	}

	public void nextTurn() {		
		currentPlayer = getNextPlayer(currentPlayer);
		bangPlayed = false;
		donePlaying = false;
		bangsPlayed = 0;
		turnLoop(currentPlayer);
	}
	
	private void turnLoop(Player currentPlayer){
		try{
			if(isDynamiteExplode()){
				Player explodePlayer = currentPlayer;
				userInterface.printInfo("Dynamite Exploded on " + explodePlayer.getName());
				damagePlayer(explodePlayer, 3, null);			
			} else {			
				passDynamite();
			}
			if(!isInJail() && players.contains(currentPlayer)){
				this.drawCards(currentPlayer, deck);
				while(!donePlaying && players.contains(currentPlayer)){
					play();
				}
			}
		}catch(EndOfGameException e){
			e.printStackTrace();
		}
		if(players.contains(currentPlayer)){
			discard(currentPlayer);
		}
		nextTurn();
	}

	public void setSheriff() {
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getRole() == Player.SHERIFF){
				currentPlayer = players.get(i);
				turnLoop(currentPlayer);
			}
		}
	}
	
	public void setSheriffManualTest() {
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getRole() == Player.SHERIFF){
				currentPlayer = players.get(i);
			}
		}
	}
	
	public int getSheriff(List<Player> players){
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getRole() == Player.SHERIFF){
				return i;
			}
		}
		return -1;
	}

	public boolean isBangPlayed() {
		return bangPlayed;
	}

	public void setBangPlayed(boolean bangPlayed) {
		this.bangPlayed = bangPlayed;
	}

	public void drawCards(Player player, Deck deck) {
		Hand hand = player.getHand();		
		if(Figure.KITCARLSON.equals(player.getName())){			
			List<Object> cards = pullCards(deck, 3);
			int cardIndex = -1;
			while(cardIndex < 0 || cardIndex > cards.size() - 1){
				cardIndex = userInterface.chooseCardToPutBack(player, cards);
			}
			deck.add(cards.remove(cardIndex));
			for(Object card : cards){
				hand.add(card);
			}
		} else if(Figure.JESSEJONES.equals(player.getName())){
			List<Player> otherPlayers = new ArrayList<Player>();
			for(Player other: players){
				if(!other.equals(player) && other.getHand().size() > 0){
					otherPlayers.add(other);
				}
			}
			boolean chosenFromPlayer = false;
			if(!otherPlayers.isEmpty()){
				chosenFromPlayer = userInterface.chooseFromPlayer(player);
			}
			if(chosenFromPlayer){	
				Player chosenPlayer = getValidChosenPlayer(currentPlayer, otherPlayers, userInterface);
				Object randomCard = chosenPlayer.removeRandom();			
				hand.add(randomCard);
			} else {
				hand.add(deck.pull());
			}
			hand.add(deck.pull());
		} else if(Figure.PEDRORAMIREZ.equals(player.getName())){
			if(!discard.isEmpty()){
				boolean chosenDiscard = userInterface.chooseDiscard(player);
				if(chosenDiscard){
					hand.add(discard.remove());
				} else {
					hand.add(deck.pull());
				}
			} else {
				hand.add(deck.pull());
			}
			hand.add(deck.pull());
		} else {
			hand.add(deck.pull());
			Object secondCard = deck.pull();		
			hand.add(secondCard);
			if(Figure.BLACKJACK.equals(player.getName())){				
				int suit = ((Card)secondCard).getSuit();
				userInterface.printInfo(Figure.BLACKJACK + " drew a " + Card.suitToString(suit) + " " + ((Card)secondCard).getName());
				if(suit == Card.HEARTS || suit == Card.DIAMONDS){
					hand.add(deck.pull());
				}			
			}
		}
	}

	public void discard(Player player) {
		discardTwoCardsForLife(player, userInterface);
		Hand hand = player.getHand();
		while(hand.size() > player.getHealth()){
			askPlayerToDiscard(player, discard);
		}
	}

	private void askPlayerToDiscard(Player player, Discard discard) {
		int card = -1;
		while(card < 0 || card > player.getHand().size() - 1){
			card = userInterface.askDiscard(player);
		}
		Object removedCard = (Card)player.getHand().remove(card);
		discard.add(removedCard);
	}

	public void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}

	public void play() throws EndOfGameException {
		Hand hand = currentPlayer.getHand();
		int card = -2;
		while(card < -1 || card > hand.size() - 1){
			card = userInterface.askPlay(currentPlayer);
		}
		if(hand.size() == 0 || card == -1){
			donePlaying = true;
			return;
		}		
		Card playedCard = (Card)hand.get(card);
		String cardName = playedCard.getName();
		if(playedCard.getType() == Card.TYPEGUN){
			hand.remove(card);
			playGun(playedCard, currentPlayer.getInPlay(), discard);			
		} else if(playedCard.getType() == Card.TYPEITEM){
			if(isItemPlayable(currentPlayer, cardName, players)){
				hand.remove(card);
				Player targetPlayer = currentPlayer;
				if(cardName == Card.CARDJAIL){
					List<Player> jailablePlayers = getPotentialTargets(currentPlayer, Card.CARDJAIL, players);					
					targetPlayer = getValidChosenPlayer(currentPlayer, jailablePlayers, userInterface);
				}
				targetPlayer.addInPlay(playedCard);
			}
		} else {			
			boolean playable = isPlayable(currentPlayer, cardName, players, bangsPlayed);
			if(!playable){
				return;
			}

			discard.add(hand.remove(card));
			if(cardName == Card.CARDBEER){
				if(isBeerGiveHealth(players)){
					if(!isMaxHealth(currentPlayer)){
						currentPlayer.addHealth(1);
					}
				}
			} else if(Card.CARDSTAGECOACH.equals(cardName)){
				deckToHand(hand, deck, 2);
			} else if(Card.CARDWELLSFARGO.equals(cardName)){
				deckToHand(hand, deck, 3);
			} else if(Card.CARDSALOON.equals(cardName)){
				giveEveryoneHealth(players);
			} else if(Card.CARDINDIANS.equals(cardName)){
				Player indianPlayer = getNextPlayer(currentPlayer);
				while(indianPlayer != currentPlayer){
					if(Figure.CALAMITYJANET.equals(indianPlayer.getName())){
						int playedBangMiss = -3;
						while(playedBangMiss != Figure.PLAYBANG && playedBangMiss != Figure.PLAYMISSED && playedBangMiss != Figure.GETSHOT){
							playedBangMiss = userInterface.respondBangMiss(indianPlayer, indianPlayer.getHand().countBangs(), indianPlayer.getHand().countMisses(), 1);
						}
						if(playedBangMiss == Figure.PLAYBANG){
							discard.add(indianPlayer.getHand().removeBang());
						} else if(playedBangMiss == Figure.PLAYMISSED){
							discard.add(indianPlayer.getHand().removeMiss());
						} else {
							damagePlayer(indianPlayer, 1, currentPlayer);
						}
					} else {
						if(validPlayBang(indianPlayer, indianPlayer.countBangs())){
							discard.add(indianPlayer.getHand().removeBang());
						} else {
							damagePlayer(indianPlayer, 1, currentPlayer);
						}
					}
					indianPlayer = getNextPlayer(indianPlayer);
				}	
			} else if(Card.CARDGATLING.equals(cardName)){
				Player gatlingPlayer = getNextPlayer(currentPlayer);
				while(gatlingPlayer != currentPlayer){
					if (isBarrelSave(gatlingPlayer) > 0){
						return;
					}
					if(Figure.CALAMITYJANET.equals(gatlingPlayer.getName())){
						int playedBangMiss = -3;
						while(playedBangMiss != Figure.PLAYBANG && playedBangMiss != Figure.PLAYMISSED && playedBangMiss != Figure.GETSHOT){
							playedBangMiss = userInterface.respondBangMiss(gatlingPlayer, gatlingPlayer.getHand().countBangs(), gatlingPlayer.getHand().countMisses(), 1);
						}
						if(playedBangMiss == Figure.PLAYBANG){
							discard.add(gatlingPlayer.getHand().removeBang());
						} else if(playedBangMiss == Figure.PLAYMISSED){
							discard.add(gatlingPlayer.getHand().removeMiss());
						} else {
							damagePlayer(gatlingPlayer, 1, currentPlayer);
						}
					} else {
						int misses = gatlingPlayer.getHand().countMisses();
						if(validPlayMiss(gatlingPlayer, misses, 1)){
							discard.add(gatlingPlayer.getHand().removeMiss());
						} else {
							damagePlayer(gatlingPlayer, 1, currentPlayer);
						}
					}
					gatlingPlayer = getNextPlayer(gatlingPlayer);
				}								
			} else if(Card.CARDGENERALSTORE.equals(cardName)){
				List<Object> generalStoreCards = new ArrayList<Object>();
				for(int i = 0; i < players.size(); i++){
					generalStoreCards.add(deck.pull());
				}
				Player generalPlayer = currentPlayer;
				while(!generalStoreCards.isEmpty()){
					int chosenCard = -1;
					while(chosenCard < 0 || chosenCard > generalStoreCards.size() - 1){
						chosenCard = userInterface.chooseGeneralStoreCard(generalPlayer, generalStoreCards);
					}
					generalPlayer.getHand().add(generalStoreCards.remove(chosenCard));
					generalPlayer = getNextPlayer(generalPlayer);
				}
			} else if(Card.CARDDUEL.equals(cardName)){
				Player other = getValidChosenPlayer(currentPlayer, others(currentPlayer, players), userInterface);				
				boolean currentCalamityJanet = false;
				boolean otherCalamityJanet = false;
				if(Figure.CALAMITYJANET.equals(currentPlayer.getName())){
					currentCalamityJanet = true;
				} else if(Figure.CALAMITYJANET.equals(other.getName())){
					otherCalamityJanet = true;
				}
				boolean someoneIsShot = false;
				while(!someoneIsShot){
					if(otherCalamityJanet){
						int playedBangMiss = -3;
						while(playedBangMiss != Figure.PLAYBANG && playedBangMiss != Figure.PLAYMISSED && playedBangMiss != Figure.GETSHOT){
							playedBangMiss = userInterface.respondBangMiss(other, other.getHand().countBangs(), other.getHand().countMisses(), 1);
						}
						if(playedBangMiss == Figure.PLAYBANG){
							discard.add(other.getHand().removeBang());
						} else if(playedBangMiss == Figure.PLAYMISSED){
							discard.add(other.getHand().removeMiss());
						} else {
							someoneIsShot = true;
						}
					} else {
						if(!validPlayBang(other, other.countBangs())){
							damagePlayer(other, 1, currentPlayer);
							someoneIsShot = true;
						} else {
							discard.add(other.getHand().removeBang());
						}
					}
					if(!someoneIsShot){						
						if(currentCalamityJanet){
							int playedBangMiss = -3;
							while(playedBangMiss != Figure.PLAYBANG && playedBangMiss != Figure.PLAYMISSED && playedBangMiss != Figure.GETSHOT){
								playedBangMiss = userInterface.respondBangMiss(currentPlayer, currentPlayer.getHand().countBangs(), currentPlayer.getHand().countMisses(), 1);
							}
							if(playedBangMiss == Figure.PLAYBANG){
								discard.add(currentPlayer.getHand().removeBang());
							} else if(playedBangMiss == Figure.PLAYMISSED){
								discard.add(currentPlayer.getHand().removeMiss());
							} else {
								damagePlayer(currentPlayer, 1, other);
								someoneIsShot = true;
							}
						} else {
							int bangs = currentPlayer.countBangs();
							if(validPlayBang(currentPlayer, bangs)){
								discard.add(currentPlayer.getHand().removeBang());
							} else {								
								damagePlayer(currentPlayer, 1, other);
								someoneIsShot = true;
							}
						}
					}
				}				
			} else if(Card.CARDCATBALOU.equals(cardName)){
				Player other = getValidChosenPlayer(currentPlayer, othersWithCardsToTake(currentPlayer, players), userInterface);
				int chosenCard = -3;
				while(chosenCard < -2 || chosenCard > other.getInPlay().size() - 1){
					chosenCard = userInterface.askOthersCard(currentPlayer, other.getInPlay(), other.getHand().size() > 0);
				}
				if(chosenCard == -1){
					discard.add(other.getHand().removeRandom());
				} else if(chosenCard == -2){
					discard.add(other.getInPlay().removeGun());
				} else {
					discard.add(other.getInPlay().remove(chosenCard));
				}
			} else if(Card.CARDPANIC.equals(cardName)){
				List<Player> others = getPotentialTargets(currentPlayer, cardName, players);
				Player otherPlayer = getValidChosenPlayer(currentPlayer, others, userInterface);
				int chosenCard = -3;
				while(chosenCard < -2 || chosenCard > otherPlayer.getInPlay().size() - 1){
					chosenCard = userInterface.askOthersCard(currentPlayer, otherPlayer.getInPlay(), otherPlayer.getHand().size() > 0);
				}
				if(chosenCard == -1){
					hand.add(otherPlayer.getHand().removeRandom());
				} else if(chosenCard == -2){
					hand.add(otherPlayer.getInPlay().removeGun());
				} else {
					hand.add(otherPlayer.getInPlay().remove(chosenCard));
				}
			} else if(Card.CARDBANG.equals(cardName) || Card.CARDMISSED.equals(cardName)){
				bangsPlayed = bangsPlayed + 1;
				List<Player> others = getPlayersWithinRange(currentPlayer, players, currentPlayer.getInPlay().getGunRange());
				Player otherPlayer = getValidChosenPlayer(currentPlayer, others, userInterface);
				int missesRequired = 1;
				if(Figure.SLABTHEKILLER.equals(currentPlayer.getName())){
					missesRequired = 2;
				}
				int barrelMisses = isBarrelSave(otherPlayer);
				missesRequired = missesRequired - barrelMisses;
				if(missesRequired <= 0){
					return;
				}
				if(Figure.CALAMITYJANET.equals(otherPlayer.getName())){
					int misses = otherPlayer.getHand().countMisses();
					int bangs = otherPlayer.getHand().countBangs();
					int playedBangMiss = -3;
					while(playedBangMiss != Figure.PLAYBANG && playedBangMiss != Figure.PLAYMISSED && playedBangMiss != Figure.GETSHOT){
						playedBangMiss = userInterface.respondBangMiss(otherPlayer, bangs, misses, missesRequired);
					}
					if(playedBangMiss == Figure.PLAYBANG){
						for(int i = 0; i < missesRequired; i++){
							discard.add(otherPlayer.getHand().removeBang());
						}
					} else if(playedBangMiss == Figure.PLAYMISSED){
						for(int i = 0; i < missesRequired; i++){
							discard.add(otherPlayer.getHand().removeMiss());
						}
					} else if(playedBangMiss == Figure.PLAYONEEACH){
						discard.add(otherPlayer.getHand().removeMiss());
						discard.add(otherPlayer.getHand().removeBang());
					} else {
						damagePlayer(otherPlayer, 1, currentPlayer);
					}
				} else {
					int misses = otherPlayer.getHand().countMisses();
					if(validPlayMiss(otherPlayer, misses, missesRequired)){
						for(int i = 0; i < missesRequired; i++){
							discard.add(otherPlayer.getHand().removeMiss());
						}
					} else {
						damagePlayer(otherPlayer, 1, currentPlayer);
					}
				}				
			}
		}
	}
	
	private boolean validPlayMiss(Player player, int misses, int missesRequired){
		while(true){
			boolean playedMiss = userInterface.respondMiss(player, misses, missesRequired);
			if(!playedMiss || (playedMiss && misses >= missesRequired)){
				return playedMiss;
			}
		}
	}
	
	private boolean validPlayBeer(Player player, int beers){
		while(true){
			boolean playedMiss = userInterface.respondBeer(player, beers);
			if(!playedMiss || (playedMiss && beers > 0)){
				return playedMiss;
			}
		}
	}
	
	private boolean validPlayBang(Player player, int bangs){
		while(true){
			boolean playerShot = userInterface.respondBang(player, bangs);
			if(!playerShot || (playerShot && bangs > 0)){
				return playerShot;
			}
		}
	}
	
	private void playGun(Card gun, InPlay inPlay, Discard discard){
		if(inPlay.hasGun()){
			discard.add(inPlay.removeGun());
		}
		inPlay.setGun(gun);
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

	public static List<Player> getPlayersWithinRange(Player player, List<Player> players, int range) {
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			int distance = AlivePlayers.getDistance(players.indexOf(player), players.indexOf(otherPlayer), players.size());
			if(otherPlayer.getInPlay().hasItem(Card.CARDMUSTANG)){
				distance = distance + 1;
			}
			if(Figure.PAULREGRET.equals(otherPlayer.getName())){
				distance = distance + 1;
			}
			if(player.getInPlay().hasItem(Card.CARDAPPALOOSA)){
				distance = distance - 1;
			}
			if(Figure.ROSEDOOLAN.equals(player.getName())){
				distance = distance - 1;
			}
			if(distance <= range){
				others.add(otherPlayer);
			}
		}
		return others(player, others);
	}

	public boolean isDynamiteExplode(){
		InPlay currentInPlay = currentPlayer.getInPlay();
		if(currentInPlay.hasItem(Card.CARDDYNAMITE)){
			Card drawnCard = (Card)draw(currentPlayer);
			return Card.isExplode(drawnCard);
		}
		return false;
	}
	
	public void passDynamite() {
		InPlay currentInPlay = currentPlayer.getInPlay();
		if(currentInPlay.hasItem(Card.CARDDYNAMITE)){
			Object dynamiteCard = currentInPlay.removeDynamite();
			Player nextPlayer = getNextPlayer(currentPlayer);
			InPlay nextInPlay = nextPlayer.getInPlay();
			userInterface.printInfo("Dynamite Passed to " + nextPlayer.getName());
			nextInPlay.add(dynamiteCard);
		}
	}

	public Object draw(Player player) {
		if(Figure.LUCKYDUKE.equals(player.getName())){
			List<Object> cards = pullCards(deck, 2);
			int chosenCard = -1;
			while(chosenCard < 0 || chosenCard > cards.size() - 1){
				chosenCard = userInterface.chooseDrawCard(player, cards);
			}
			for(Object card : cards){
				discard.add(card);
			}
			Card drawnCard = (Card)cards.get(chosenCard);
			userInterface.printInfo(player.getName() + " drew a " + Card.valueToString(drawnCard.getValue()) + " of " + Card.suitToString(drawnCard.getSuit()) + " " + drawnCard.getName());
			return cards.get(chosenCard);
		} else {
			Object card = deck.pull();
			Card drawnCard = (Card)card;
			userInterface.printInfo(player.getName() + " drew a " + Card.valueToString(drawnCard.getValue()) + " of " + Card.suitToString(drawnCard.getSuit()) + " " + drawnCard.getName());
			discard.add(card);			
			return card;
		}
	}

	public boolean isInJail() {		
		InPlay currentInPlay = currentPlayer.getInPlay();
		if(currentInPlay.hasItem(Card.CARDJAIL)){
			Object jailCard = currentInPlay.removeJail();
			discard.add(jailCard);
			Card drawn = (Card)draw(currentPlayer);
			boolean inJail =  drawn.getSuit() != Card.HEARTS;
			if(inJail){
				userInterface.printInfo(currentPlayer.getName() + " stays in jail");
			} else {
				userInterface.printInfo(currentPlayer.getName() + " breaks out of jail");
			}
			return inJail;
		}
		return false;
	}

	public int isBarrelSave(Player player) {
		int misses = 0;
		if(Figure.JOURDONNAIS.equals(player.getName())){
			Card drawn = (Card)draw(player);
			if(drawn.getSuit() == Card.HEARTS){
				misses = misses + 1;
			}
		}
		InPlay currentInPlay = player.getInPlay();		
		if(currentInPlay.hasItem(Card.CARDBARREL)){			
			Card drawn = (Card)draw(player);
			if(drawn.getSuit() == Card.HEARTS){
				misses = misses + 1;
			}
		}
		return misses;
	}
	
	public void damagePlayer(Player player, int damage, Player damager) throws EndOfGameException{
		discardTwoCardsForLife(player, userInterface);
		player.setHealth(player.getHealth() - damage);
		if(Figure.BARTCASSIDY.equals(player.getName())){
			for(int i = 0; i < damage; i++){
				player.getHand().add(deck.pull());
			}
		} else if(damager != null && Figure.ELGRINGO.equals(player.getName())){
			Hand otherHand = damager.getHand();
			if(otherHand.size() != 0){
				Hand playerHand = player.getHand();
				playerHand.add(otherHand.removeRandom());
			}
		}
		if(player.getHealth() <= 0 && players.size() > 2){
			boolean doNotPlayBeer = false;
			while(!doNotPlayBeer && player.getHealth() <= 0){
				if(validPlayBeer(player, player.countBeers())){
					player.setHealth(player.getHealth() + 1);
					discard.add(player.getHand().removeBeer());
				} else {
					doNotPlayBeer = true;
				}
			}
			if(player.getHealth() <= 0){
				handleDeath(player, damager);				
			}
		}
	}

	public void handleDeath(Player player, Player damager) throws EndOfGameException {
		if(player.equals(currentPlayer)){
			currentPlayer = getPreviousPlayer(currentPlayer);
		}
		players.remove(player);
		userInterface.printInfo(player.getName() + " is dead. Role was " + Player.roleToString(player.getRole()));
		if(damager != null){
			if(damager.getRole() == Player.SHERIFF && player.getRole() == Player.DEPUTY){
				userInterface.printInfo(damager.getName() + " killed own deputy, loses all cards");
				discardAll(damager, discard);
			} else if(player.getRole() == Player.OUTLAW) {
				userInterface.printInfo(damager.getName() + " killed an outlaw, draws 3 cards");
				deckToHand(damager.getHand(), deck, 3);
			}
			deadDiscardAll(player, players, discard);
			if(isGameOver(players)){
				winner = getWinners(players);
				userInterface.printInfo("Winners are " + winner);
				throw new EndOfGameException("Game over");
			}
		}		
	}
	
	public static void discardAll(Player player, Discard discard){
		List<Object> discardCards = new ArrayList<Object>();
		Hand hand = player.getHand();
		while(hand.size() != 0){
			discardCards.add(hand.remove(0));
		}
		InPlay inPlay = player.getInPlay();
		discardCards.add(inPlay.removeGun());
		while(inPlay.count() > 0){
			discardCards.add(inPlay.remove(0));
		}
		for(Object card : discardCards){
			discard.add(card);
		}
	}
	
	public static void deadDiscardAll(Player player, List<Player> players, Discard discard){
		List<Object> discardCards = new ArrayList<Object>();
		Hand hand = player.getHand();
		hand.setEmptyListener(new DoNothingEmptyHandListener());
		while(hand.size() != 0){
			discardCards.add(hand.remove(0));
		}
		InPlay inPlay = player.getInPlay();
		if(inPlay.hasGun()){
			discardCards.add(inPlay.removeGun());
		}
		while(inPlay.count() > 0){
			discardCards.add(inPlay.remove(0));
		}
		Player vultureSam = null;
		for(Player alivePlayer : players){
			if(Figure.VULTURESAM.equals(alivePlayer.getName())){
				vultureSam = alivePlayer;
			}
		}
		if(vultureSam == null){
			for(Object card : discardCards){
				discard.add(card);
			}
		} else {
			for(Object card : discardCards){
				vultureSam.getHand().add(card);
			}
		}
	}
	
	public static boolean isGameOver(List<Player> players) {
		return isDead(Player.SHERIFF, players) || isDead(Player.RENEGADE, players) && isDead(Player.OUTLAW, players);
	}
	
	private static boolean isDead(int role, List<Player> players) {
		for(Player player : players){
			if(player.getRole() == role){
				return false;
			}
		}
		return true;
	}

	public static String getWinners(List<Player> players) {
		if(isDead(Player.OUTLAW, players) && isDead(Player.RENEGADE, players)){
			return "Sheriff and Deputies";	
		} else if(isDead(Player.SHERIFF, players) && (!isDead(Player.DEPUTY, players) || !isDead(Player.OUTLAW, players))) {
			return "Outlaws";
		} else if(isDead(Player.DEPUTY, players) && isDead(Player.OUTLAW, players) && isDead(Player.SHERIFF, players)){
			return "Renegades";
		} else {
			throw new RuntimeException("No Winner");
		}
	}
	
	public static void discardTwoCardsForLife(Player player, UserInterface userInterface){
		if(Figure.SIDKETCHUM.equals(player.getName())){
			Hand hand = player.getHand();
			if(hand.size() >= 2){
				List<Object> cardsToDiscard = null;
				while(cardsToDiscard == null || cardsToDiscard.size() % 2 != 0){
					cardsToDiscard = userInterface.chooseTwoDiscardForLife(player);
				}
				if(cardsToDiscard.size() % 2 == 0){
					for(Object card : cardsToDiscard){
						hand.remove(card);
					}
					player.setHealth(player.getHealth() + (cardsToDiscard.size() / 2));
				}
			}
		}
	}
	
	public static boolean playerHasCardsToTake(Player player){
		boolean emptyHand = player.getHand().isEmpty();
		InPlay inPlay = player.getInPlay();
		boolean hasGun = inPlay.hasGun();
		boolean emptyInPlay = inPlay.isEmpty();
		return !emptyHand || hasGun || !emptyInPlay;
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	public static List<Player> othersWithCardsToTake(Player player, List<Player> others){
		List<Player> othersCopy = new ArrayList<Player>();
		for(Player otherPlayer : others){
			if(playerHasCardsToTake(otherPlayer)){
				othersCopy.add(otherPlayer);
			}
		}
		othersCopy.remove(player);
		return othersCopy;
	}
	
	public static List<Player> others(Player player, List<Player> others){
		List<Player> othersCopy = new ArrayList<Player>();
		for(Player otherPlayer : others){
			othersCopy.add(otherPlayer);
		}
		othersCopy.remove(player);
		return othersCopy;
	}
	
	public static List<Object> pullCards(Deck deck, int countCards){
		List<Object> cards = new ArrayList<Object>();
		for(int i = 0; i < countCards; i++){
			cards.add(deck.pull());
		}
		return cards;
	}
	
	public static void deckToHand(Hand hand, Deck deck, int countCards){
		for(int i = 0; i < countCards; i++){
			hand.add(deck.pull());
		}
	}
	
	public static List<Player> getJailablePlayers(Player player, List<Player> players){
		List<Player> others = new ArrayList<Player>();
		for(Player otherPlayer : players){
			boolean isInJail = otherPlayer.getInPlay().hasItem(Card.CARDJAIL);
			boolean isSheriff = otherPlayer.getRole() == Player.SHERIFF;
			boolean isPlayer = otherPlayer.equals(player);
			if(!isInJail && !isSheriff && !isPlayer){
				others.add(otherPlayer);
			}
		}
		return others;
	}
	
	public static List<Player> getPlayersWithCards(List<Player> players){
		List<Player> playersWithCards = new ArrayList<Player>();
		for(Player player : players){
			if(playerHasCardsToTake(player)){
				playersWithCards.add(player);
			}
		}
		return playersWithCards;
	}
	
	public static List<String> getPlayersNames(List<Player> players){
		List<String> names = new ArrayList<String>();
		for(Player player : players){
			names.add(player.getName());
		}
		return names;
	}
	
	public static boolean isItemPlayable(Player player, String cardName, List<Player> players){
		if(Card.CARDJAIL.equals(cardName)){
			List<Player> others = getPotentialTargets(player, cardName, players);
			return !others.isEmpty();				
		} else {
			return !player.isInPlay(cardName);
		}
	}
	
	public static boolean isPlayable(Player player, String cardName, List<Player> players, int bangsPlayed){
		if(Card.CARDBANG.equals(cardName) || Card.CARDMISSED.equals(cardName)){
			if(Card.CARDMISSED.equals(cardName) && !Figure.CALAMITYJANET.equals(player.getName())){
				return false;
			}
			if(bangsPlayed > 0 && !(player.getInPlay().hasGun() && player.getInPlay().isGunVolcanic()) && !Figure.WILLYTHEKID.equals(player.getName())){			
				return false;
			}
			List<Player> others = getPotentialTargets(player, cardName, players);
			return !others.isEmpty();
		} else if(Card.CARDPANIC.equals(cardName)){
			List<Player> others = getPotentialTargets(player, cardName, players);
			return !others.isEmpty();
		} else if(Card.CARDCATBALOU.equals(cardName)){
			List<Player> others = getPotentialTargets(player, cardName, players);
			return !others.isEmpty();
		} else {
			return true;
		}
	}
	
	public static List<Player> getPotentialTargets(Player player, String cardName, List<Player> players){
		if(Card.CARDJAIL.equals(cardName)){
			return getJailablePlayers(player, players);
		} else if(Card.CARDBANG.equals(cardName) || Card.CARDMISSED.equals(cardName)){
			return getPlayersWithinRange(player, players, player.getGunRange());
		} else if(Card.CARDPANIC.equals(cardName)){
			return getPlayersWithCards(getPlayersWithinRange(player, players, 1));
		} else if(Card.CARDCATBALOU.equals(cardName)){
			return othersWithCardsToTake(player, players);
		}
		return null;
	}
	
	public static Player getValidChosenPlayer(Player player, List<Player> choosable, UserInterface userInterface){
		int chosenPlayer = -1;
		while(chosenPlayer < 0 || chosenPlayer > choosable.size() - 1){
			chosenPlayer = userInterface.askPlayer(player, getPlayersNames(choosable));
		}
		return choosable.get(chosenPlayer);
	}
	
	public static boolean isBeerGiveHealth(List<Player> players){
		if(players.size() > 2){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isMaxHealth(Player player){
		return player.getHealth() == player.getMaxHealth();
	}
	
	public static void giveEveryoneHealth(List<Player> players){
		for(Player player : players){
			if(!isMaxHealth(player)){
				player.addHealth(1);
			}
		}
	}
}
