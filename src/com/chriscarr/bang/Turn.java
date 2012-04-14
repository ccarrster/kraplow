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
	private String winner;
	
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
		if(isDynamiteExplode()){
			Player explodePlayer = currentPlayer;
			userInterface.printInfo("Dynamite Exploded on " + explodePlayer.getFigure().getName());
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
		if(Figure.KITCARLSON.equals(player.getFigure().getName())){			
			List<Object> cards = pullCards(deck, 3);
			int cardIndex = -1;
			while(cardIndex < 0 || cardIndex > cards.size() - 1){
				cardIndex = userInterface.chooseCardToPutBack(player, cards);
			}
			deck.add(cards.remove(cardIndex));
			for(Object card : cards){
				hand.add(card);
			}
		} else if(Figure.JESSEJONES.equals(player.getFigure().getName())){
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
				int chosenPlayerIndex = -1;
				while(chosenPlayerIndex < 0 || chosenPlayerIndex > otherPlayers.size() - 1){
					chosenPlayerIndex = userInterface.askPlayer(player, getPlayersNames(otherPlayers));
				}
				Player chosenPlayer = otherPlayers.get(chosenPlayerIndex);
				Object randomCard = chosenPlayer.getHand().removeRandom();			
				hand.add(randomCard);
			} else {
				hand.add(deck.pull());
			}
			hand.add(deck.pull());
		} else if(Figure.PEDRORAMIREZ.equals(player.getFigure().getName())){
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
			if(Figure.BLACKJACK.equals(player.getFigure().getName())){				
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

	public void play() {
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
		if(playedCard.getType() == Card.TYPEGUN){
			hand.remove(card);
			playGun(playedCard, currentPlayer.getInPlay(), discard);			
		} else if(playedCard.getType() == Card.TYPEITEM){		
			if(playedCard.getName() == Card.CARDJAIL){
				List<Player> others = getJailablePlayers(currentPlayer, players);
				if(others.isEmpty()){
					return;
				} else {
					hand.remove(card);
					int chosenPlayer = -1;
					while(chosenPlayer < 0 || chosenPlayer > others.size() - 1){
						chosenPlayer = userInterface.askPlayer(currentPlayer, getPlayersNames(others));
					}
					others.get(chosenPlayer).getInPlay().add(playedCard);
				}
			} else {
				if(!currentPlayer.getInPlay().hasItem(playedCard.getName())){
					hand.remove(card);
					currentPlayer.getInPlay().add(playedCard);
				}
			}
		} else {
			boolean missedBang = false;
			if(playedCard.getName() == Card.CARDMISSED){				
				if(Figure.CALAMITYJANET.equals(currentPlayer.getFigure().getName())){
					missedBang = true;
				} else {
					return;
				}
			}
			if(playedCard.getName() == Card.CARDBANG || missedBang){
				if(bangsPlayed > 0 && !(currentPlayer.getInPlay().hasGun() && currentPlayer.getInPlay().isGunVolcanic()) && !Figure.WILLYTHEKID.equals(currentPlayer.getFigure().getName())){			
					return;
				}
				List<Player> others = getPlayersWithinRange(currentPlayer, players, currentPlayer.getInPlay().getGunRange());
				if(others.isEmpty()){
					return;
				}
			}
			if(playedCard.getName() == Card.CARDPANIC){
				List<Player> others = getPlayersWithinRange(currentPlayer, players, 1);
				List<Player> othersWithCards = getPlayersWithCards(others);
				if(othersWithCards.isEmpty()){
					return;
				}
			}
			if(playedCard.getName() == Card.CARDCATBALOU){
				List<Player> others = new ArrayList<Player>();
				for(Player otherPlayer : players){
					if(!otherPlayer.equals(currentPlayer) && playerHasCardsToTake(otherPlayer)){
						others.add(otherPlayer);
					}
				}
				if(others.isEmpty()){
					return;
				}
				
			}
			
			
			discard.add(hand.remove(card));
			if(playedCard.getName() == Card.CARDBEER){
				if(players.size() > 2){
					if(currentPlayer.getHealth() < currentPlayer.getMaxHealth()){
						currentPlayer.setHealth(currentPlayer.getHealth() + 1);
					}
				}
			} else if(playedCard.getName() == Card.CARDSTAGECOACH){
				deckToHand(hand, deck, 2);
			} else if(playedCard.getName() == Card.CARDWELLSFARGO){
				deckToHand(hand, deck, 3);
			} else if(playedCard.getName() == Card.CARDSALOON){
				for(Player saloon_player : players){
					if(saloon_player.getHealth() < saloon_player.getMaxHealth()){
						saloon_player.setHealth(saloon_player.getHealth() + 1);
					}
				}
			} else if(playedCard.getName() == Card.CARDINDIANS){
				Player indianPlayer = getNextPlayer(currentPlayer);
				while(indianPlayer != currentPlayer){
					if(Figure.CALAMITYJANET.equals(indianPlayer.getFigure().getName())){
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
						int bangs = indianPlayer.getHand().countBangs();
						boolean validPlay = false;
						boolean respondBang = false;
						while(!validPlay){
							respondBang = userInterface.respondBang(indianPlayer, bangs);
							if(!respondBang || (respondBang && bangs > 0)){
								validPlay = true;
							}
						}
						if(respondBang){
							discard.add(indianPlayer.getHand().removeBang());
						} else {
							damagePlayer(indianPlayer, 1, currentPlayer);
						}
					}
					indianPlayer = getNextPlayer(indianPlayer);
				}	
			} else if(playedCard.getName() == Card.CARDGATLING){
				Player gatlingPlayer = getNextPlayer(currentPlayer);
				while(gatlingPlayer != currentPlayer){
					if (isBarrelSave(gatlingPlayer) > 0){
						return;
					}
					if(Figure.CALAMITYJANET.equals(gatlingPlayer.getFigure().getName())){
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
						boolean respondMiss = false;
						boolean validPlay = false;
						while(!validPlay){
							respondMiss = userInterface.respondMiss(gatlingPlayer, misses, 1);
							if(!respondMiss || (respondMiss && misses > 0)){
								validPlay = true;
							}
						}
						if(respondMiss){
							discard.add(gatlingPlayer.getHand().removeMiss());
						} else {
							damagePlayer(gatlingPlayer, 1, currentPlayer);
						}
					}
					gatlingPlayer = getNextPlayer(gatlingPlayer);
				}								
			} else if(playedCard.getName() == Card.CARDGENERALSTORE){
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
			} else if(playedCard.getName() == Card.CARDDUEL){
				List<Player> others = Player.getOthers(currentPlayer, players);
				int playerIndex = -1;
				while(playerIndex < 0 || playerIndex > players.size() - 1){
					playerIndex = userInterface.askPlayer(currentPlayer, getPlayersNames(others));
				}
				Player other = others.get(playerIndex);
				boolean currentCalamityJanet = false;
				boolean otherCalamityJanet = false;
				if(Figure.CALAMITYJANET.equals(currentPlayer.getFigure().getName())){
					currentCalamityJanet = true;
				} else if(Figure.CALAMITYJANET.equals(other.getFigure().getName())){
					otherCalamityJanet = true;
				}
				boolean someoneIsShot = false;
				while(!someoneIsShot){
					boolean otherShot = false;
					if(otherCalamityJanet){
						int playedBangMiss = -3;
						while(playedBangMiss != Figure.PLAYBANG && playedBangMiss != Figure.PLAYMISSED && playedBangMiss != Figure.GETSHOT){
							playedBangMiss = userInterface.respondBangMiss(other, other.getHand().countBangs(), other.getHand().countMisses(), 1);
						}
						if(playedBangMiss == Figure.PLAYBANG){
							discard.add(other.getHand().removeBang());
							otherShot = true;
						} else if(playedBangMiss == Figure.PLAYMISSED){
							discard.add(other.getHand().removeMiss());
							otherShot = true;
						} else {
							otherShot = false;
						}
					} else {
						boolean validPlay = false;
						int bangs = other.getHand().countBangs();
						while(!validPlay){
							otherShot = userInterface.respondBang(other, bangs);
							if(!otherShot || (otherShot && bangs > 0)){
								validPlay = true;
							}
						}
						if(!otherShot){
							someoneIsShot = true;
							damagePlayer(other, 1, currentPlayer);
						}
					}
					if(otherShot){
						discard.add(other.getHand().removeBang());
						boolean playerShot = false;
						if(currentCalamityJanet){
							int playedBangMiss = -3;
							while(playedBangMiss != Figure.PLAYBANG && playedBangMiss != Figure.PLAYMISSED && playedBangMiss != Figure.GETSHOT){
								playedBangMiss = userInterface.respondBangMiss(currentPlayer, currentPlayer.getHand().countBangs(), currentPlayer.getHand().countMisses(), 1);
							}
							if(playedBangMiss == Figure.PLAYBANG){
								discard.add(currentPlayer.getHand().removeBang());
								playerShot = true;
							} else if(playedBangMiss == Figure.PLAYMISSED){
								discard.add(currentPlayer.getHand().removeMiss());
								playerShot = true;
							} else {
								damagePlayer(currentPlayer, 1, other);
								playerShot = false;
							}
						} else {
							boolean validPlay = false;
							int bangs = currentPlayer.getHand().countBangs();
							while(!validPlay){
								playerShot = userInterface.respondBang(currentPlayer, bangs);
								if(!playerShot || (playerShot && bangs > 0)){
									validPlay = true;
								}
							}
							if(!playerShot){
								someoneIsShot = true;
								damagePlayer(currentPlayer, 1, other);
							} else {
								discard.add(currentPlayer.getHand().removeBang());
							}
						}
					}
				}				
			} else if(playedCard.getName() == Card.CARDCATBALOU){
				List<Player> others = removeFromOthers(currentPlayer, players);
				int otherIndex = -1;
				while(otherIndex < 0 || otherIndex > others.size() - 1){
					otherIndex = userInterface.askPlayer(currentPlayer, getPlayersNames(others));
				}
				Player otherPlayer = others.get(otherIndex);
				int chosenCard = -3;
				while(chosenCard < -2 || chosenCard > otherPlayer.getInPlay().size() - 1){
					chosenCard = userInterface.askOthersCard(currentPlayer, otherPlayer.getInPlay(), otherPlayer.getHand().size() > 0);
				}
				if(chosenCard == -1){
					discard.add(otherPlayer.getHand().removeRandom());
				} else if(chosenCard == -2){
					discard.add(otherPlayer.getInPlay().removeGun());
				} else {
					discard.add(otherPlayer.getInPlay().remove(chosenCard));
				}
			} else if(playedCard.getName() == Card.CARDPANIC){
				List<Player> others = getPlayersWithinRange(currentPlayer, players, 1);
				others = removeFromOthers(currentPlayer, others);
				int otherIndex = -1;
				while(otherIndex < 0 || otherIndex > others.size() - 1){
					otherIndex = userInterface.askPlayer(currentPlayer, getPlayersNames(others));
				}
				Player otherPlayer = others.get(otherIndex);
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
			} else if(playedCard.getName() == Card.CARDBANG || missedBang){
				bangsPlayed = bangsPlayed + 1;
				List<Player> others = getPlayersWithinRange(currentPlayer, players, currentPlayer.getInPlay().getGunRange());
				int otherIndex = -1;
				while(otherIndex < 0 || otherIndex > others.size() - 1){
					otherIndex = userInterface.askPlayer(currentPlayer, getPlayersNames(others));
				}
				Player otherPlayer = others.get(otherIndex);
				int missesRequired = 1;
				if(Figure.SLABTHEKILLER.equals(currentPlayer.getFigure().getName())){
					missesRequired = 2;
				}
				int barrelMisses = isBarrelSave(otherPlayer);
				missesRequired = missesRequired - barrelMisses;
				if(missesRequired <= 0){
					return;
				}
				if(Figure.CALAMITYJANET.equals(otherPlayer.getFigure().getName())){
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
					boolean validPlay = false;
					boolean playedMiss = false;
					while(!validPlay){
						playedMiss = userInterface.respondMiss(otherPlayer, misses, missesRequired);
						if(!playedMiss || (playedMiss && misses >= missesRequired)){
							validPlay = true;
						}
					}
					if(playedMiss){
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
			if(Figure.PAULREGRET.equals(otherPlayer.getFigure().getName())){
				distance = distance + 1;
			}
			if(player.getInPlay().hasItem(Card.CARDAPPALOOSA)){
				distance = distance - 1;
			}
			if(Figure.ROSEDOOLAN.equals(player.getFigure().getName())){
				distance = distance - 1;
			}
			if(distance <= range){
				others.add(otherPlayer);
			}
		}
		return Player.getOthers(player, others);
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
			userInterface.printInfo("Dynamite Passed to " + nextPlayer.getFigure().getName());
			nextInPlay.add(dynamiteCard);
		}
	}

	public Object draw(Player player) {
		if(Figure.LUCKYDUKE.equals(player.getFigure().getName())){
			List<Object> cards = pullCards(deck, 2);
			int chosenCard = -1;
			while(chosenCard < 0 || chosenCard > cards.size() - 1){
				chosenCard = userInterface.chooseDrawCard(player, cards);
			}
			for(Object card : cards){
				discard.add(card);
			}
			Card drawnCard = (Card)cards.get(chosenCard);
			userInterface.printInfo(player.getFigure().getName() + " drew a " + Card.valueToString(drawnCard.getValue()) + " of " + Card.suitToString(drawnCard.getSuit()) + " " + drawnCard.getName());
			return cards.get(chosenCard);
		} else {
			Object card = deck.pull();
			Card drawnCard = (Card)card;
			userInterface.printInfo(player.getFigure().getName() + " drew a " + Card.valueToString(drawnCard.getValue()) + " of " + Card.suitToString(drawnCard.getSuit()) + " " + drawnCard.getName());
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
				userInterface.printInfo(currentPlayer.getFigure().getName() + " stays in jail");
			} else {
				userInterface.printInfo(currentPlayer.getFigure().getName() + " breaks out of jail");
			}
			return inJail;
		}
		return false;
	}

	public int isBarrelSave(Player player) {
		int misses = 0;
		if(Figure.JOURDONNAIS.equals(player.getFigure().getName())){
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
	
	public void damagePlayer(Player player, int damage, Player damager){
		discardTwoCardsForLife(player, userInterface);
		player.setHealth(player.getHealth() - damage);
		if(Figure.BARTCASSIDY.equals(player.getFigure().getName())){
			for(int i = 0; i < damage; i++){
				player.getHand().add(deck.pull());
			}
		} else if(damager != null && Figure.ELGRINGO.equals(player.getFigure().getName())){
			Hand otherHand = damager.getHand();
			if(otherHand.size() != 0){
				Hand playerHand = player.getHand();
				playerHand.add(otherHand.removeRandom());
			}
		}
		if(player.getHealth() <= 0 && players.size() > 2){
			boolean doNotPlayBeer = false;
			while(!doNotPlayBeer && player.getHealth() <= 0){
				int beers = player.getHand().countBeers();
				boolean validPlay = false;
				boolean playBeer = false;
				while(!validPlay){
					playBeer = userInterface.respondBeer(player, beers);
					if(!playBeer || (playBeer && beers > 0)){
						validPlay = true;
					}
				}
				if(playBeer){
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

	public void handleDeath(Player player, Player damager) {
		if(player.equals(currentPlayer)){
			currentPlayer = getPreviousPlayer(currentPlayer);
		}
		players.remove(player);
		userInterface.printInfo(player.getFigure().getName() + " is dead. Role was " + Player.roleToString(player.getRole()));
		if(damager != null){
			if(damager.getRole() == Player.SHERIFF && player.getRole() == Player.DEPUTY){
				userInterface.printInfo(damager.getFigure().getName() + " killed own deputy, loses all cards");
				discardAll(damager, discard);
			} else if(player.getRole() == Player.OUTLAW) {
				userInterface.printInfo(damager.getFigure().getName() + " killed an outlaw, draws 3 cards");
				deckToHand(damager.getHand(), deck, 3);
			}
			deadDiscardAll(player, players, discard);
			if(isGameOver(players)){
				winner = getWinners(players);
				userInterface.printInfo("Winners are " + winner);
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
			if(Figure.VULTURESAM.equals(alivePlayer.getFigure().getName())){
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
		if(Figure.SIDKETCHUM.equals(player.getFigure().getName())){
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
	
	public static List<Player> removeFromOthers(Player player, List<Player> others){
		List<Player> othersCopy = new ArrayList<Player>();
		for(Player otherPlayer : others){
			if(playerHasCardsToTake(otherPlayer)){
				othersCopy.add(otherPlayer);
			}
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
			names.add(player.getFigure().getName());
		}
		return names;
	}
}
