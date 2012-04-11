package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class Turn {

	List<Player> players;
	int currentTurn;
	boolean bangPlayed = false;
	UserInterface userInterface;
	boolean donePlaying = false;
	private Discard discard;
	private Deck deck;
	private int bangsPlayed = 0;
	String winner;
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Player getPlayersTurn() {		
		return players.get(currentTurn);
	}
	
	public int getNextTurn(int currentTurn, int numPlayers){
		if(currentTurn == numPlayers -1){
			currentTurn = 0;
		} else {
			currentTurn = currentTurn + 1;
		}
		return currentTurn;
	}

	public void nextTurn() {
		if(currentTurn == players.size() -1){
			currentTurn = 0;
		} else {
			currentTurn = currentTurn + 1;
		}
		Player currentPlayer = players.get(currentTurn);
		bangPlayed = false;
		donePlaying = false;
		bangsPlayed = 0;
		if(isDynamiteExplode()){
			Player explodePlayer = currentPlayer;
			damagePlayer(explodePlayer, 3, null);
		} else {
			passDynamite();
		}
		if(!isInJail() && players.contains(currentPlayer)){
			while(!donePlaying){
				play();
			}
		}
		if(players.contains(currentPlayer)){
			discard(currentPlayer);
		} else {
			//Died during your turn, current turn is broken so it's the end of the previous players turn
			if(currentTurn == 0){
				currentTurn = players.size() - 1;
			} else {
				currentTurn = currentTurn - 1;
			}
		}
		nextTurn();
	}

	public void setSheriff() {
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getRole() == Player.SHERIFF){
				currentTurn = i;
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
			List<Object> cards = new ArrayList<Object>();
			cards.add(deck.pull());
			cards.add(deck.pull());
			cards.add(deck.pull());
			int cardIndex = userInterface.chooseGeneralStoreCard(player, cards);
			deck.add(cards.remove(cardIndex));
			for(Object card : cards){
				hand.add(card);
			}
		} else if(Figure.JESSEJONES.equals(player.getFigure().getName())){
			boolean chosenDiscard = userInterface.chooseDiscard(player);
			if(chosenDiscard){
				List<Player> otherPlayers = new ArrayList<Player>();
				for(Player other: players){
					if(!other.equals(player)){
						otherPlayers.add(other);
					}
				}				
				int chosenPlayerIndex = userInterface.askPlayer(player, otherPlayers);
				Player chosenPlayer = otherPlayers.get(chosenPlayerIndex);
				Object randomCard = chosenPlayer.getHand().removeRandom();			
				hand.add(randomCard);
			} else {
				hand.add(deck.pull());
			}
			hand.add(deck.pull());
		} else if(Figure.PEDRORAMIREZ.equals(player.getFigure().getName())){
			boolean chosenDiscard = userInterface.chooseDiscard(player);
			if(chosenDiscard){
				hand.add(discard.remove());
			} else {
				hand.add(deck.pull());
			}
			hand.add(deck.pull());
		} else {
			hand.add(deck.pull());
			Object secondCard = deck.pull();		
			hand.add(secondCard);
			if(Figure.BLACKJACK.equals(player.getFigure().getName())){
				//TODO show other players black jacks second card
				int suit = ((Card)secondCard).getSuit();
				if(suit == Card.HEARTS || suit == Card.DIAMONDS){
					hand.add(deck.pull());
				}			
			}
		}
		
		
		
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void discard(Player player) {
		discardTwoCardsForLife(player);
		Hand hand = player.getHand();
		while(hand.size() > player.getHealth()){
			askPlayerToDiscard(player, discard);
		}
	}

	private void askPlayerToDiscard(Player player, Discard discard) {
		int card = userInterface.askDiscard(player);
		Object removedCard = (Card)player.getHand().remove(card);
		discard.add(removedCard);
	}

	public void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}

	public void play() {
		Player player = players.get(currentTurn);
		int card = userInterface.askPlay(player);
		if(card == -1){
			donePlaying = true;
			return;
		}
		Hand hand = player.getHand();
		Card playedCard = (Card)hand.get(card);
		if(playedCard.getType() == Card.TYPEGUN){
			hand.remove(card);
			playGun(playedCard, player.getInPlay(), discard);			
		} else if(playedCard.getType() == Card.TYPEITEM){		
			if(playedCard.getName() == Card.CARDJAIL){
				List<Player> others = new ArrayList<Player>();
				for(Player otherPlayer : players){
					if((!otherPlayer.getInPlay().hasItem(Card.CARDJAIL)) && (otherPlayer.getRole() != Player.SHERIFF) && (!otherPlayer.equals(player))){
						others.add(otherPlayer);
					}
				}
				if(others.isEmpty()){
					return;
				}
				hand.remove(card);
				int chosenPlayer = userInterface.askPlayer(player, others);
				others.get(chosenPlayer).getInPlay().add(playedCard);
			} else {
				if(!player.getInPlay().hasItem(playedCard.getName())){
					hand.remove(card);
					player.getInPlay().add(playedCard);
				}
			}
		} else {
			boolean missedBang = false;
			if(playedCard.getName() == Card.CARDMISSED){				
				if(Figure.CALAMITYJANET.equals(player.getFigure().getName())){
					missedBang = true;
				} else {
					return;
				}
			}
			if(playedCard.getName() == Card.CARDBANG){
				if(bangsPlayed > 0 && !(player.getInPlay().hasGun() && player.getInPlay().isGunVolcanic()) && !Figure.WILLYTHEKID.equals(player.getFigure().getName())){			
					return;
				}
				List<Player> others = getPlayersWithinRange(player, players, player.getInPlay().getGunRange());
				if(others.isEmpty()){
					return;
				}
			}
			if(playedCard.getName() == Card.CARDPANIC){
				List<Player> others = getPlayersWithinRange(player, players, 1);
				if(others.isEmpty()){
					return;
				}
			}
			discard.add(hand.remove(card));
			if(playedCard.getName() == Card.CARDBEER){
				if(players.size() > 2){
					if(player.getHealth() < player.getMaxHealth()){
						player.setHealth(player.getHealth() + 1);
					}
				}
			} else if(playedCard.getName() == Card.CARDSTAGECOACH){
				hand.add(deck.pull());
				hand.add(deck.pull());
			} else if(playedCard.getName() == Card.CARDWELLSFARGO){
				hand.add(deck.pull());
				hand.add(deck.pull());
				hand.add(deck.pull());
			} else if(playedCard.getName() == Card.CARDSALOON){
				for(Player saloon_player : players){
					if(saloon_player.getHealth() < saloon_player.getMaxHealth()){
						saloon_player.setHealth(saloon_player.getHealth() + 1);
					}
				}
			} else if(playedCard.getName() == Card.CARDINDIANS){
				int indiansTurn = getNextTurn(currentTurn, players.size());
				while(indiansTurn != currentTurn){
					Player indianPlayer = players.get(indiansTurn);
					int bangs = indianPlayer.getHand().countBangs();
					boolean respondBang = userInterface.respondBang(indianPlayer, bangs);
					if(respondBang){
						discard.add(indianPlayer.getHand().removeBang());
					} else {
						damagePlayer(indianPlayer, 1, player);
					}
					indiansTurn = getNextTurn(indiansTurn, players.size());
				}	
			} else if(playedCard.getName() == Card.CARDGATLING){
				int gatlingTurn = getNextTurn(currentTurn, players.size());
				while(gatlingTurn != currentTurn){
					Player gatlingPlayer = players.get(gatlingTurn);
					if (isBarrelSave(gatlingPlayer) > 0){
						return;
					}
					int misses = gatlingPlayer.getHand().countMisses();
					boolean respondMiss = userInterface.respondMiss(gatlingPlayer, misses, 1);
					if(respondMiss){
						discard.add(gatlingPlayer.getHand().removeMiss());
					} else {
						damagePlayer(gatlingPlayer, 1, player);
					}
					gatlingTurn = getNextTurn(gatlingTurn, players.size());
				}								
			} else if(playedCard.getName() == Card.CARDGENERALSTORE){
				int generalStoreTurn = currentTurn;
				List<Object> generalStoreCards = new ArrayList<Object>();
				for(int i = 0; i < players.size(); i++){
					generalStoreCards.add(deck.pull());
				}
				while(!generalStoreCards.isEmpty()){
					Player generalPlayer = players.get(generalStoreTurn);
					int chosenCard = userInterface.chooseGeneralStoreCard(generalPlayer, generalStoreCards);
					generalPlayer.getHand().add(generalStoreCards.remove(chosenCard));
					generalStoreTurn = getNextTurn(generalStoreTurn, players.size());
				}
			} else if(playedCard.getName() == Card.CARDDUEL){
				List<Player> others = new ArrayList<Player>();
				for(Player otherPlayer : players){
					others.add(otherPlayer);
				}
				others.remove(currentTurn);
				int playerIndex = userInterface.askPlayer(player, others);
				Player other = others.get(playerIndex);
				boolean someoneIsShot = false;
				while(!someoneIsShot){
					boolean otherShot = userInterface.respondBang(other, other.getHand().countBangs());
					if(otherShot){
						discard.add(other.getHand().removeBang());
						boolean playerShot = userInterface.respondBang(player, player.getHand().countBangs());
						if(!playerShot){
							someoneIsShot = true;
							damagePlayer(player, 1, other);
						} else {
							discard.add(player.getHand().removeBang());
						}
					} else {
						someoneIsShot = true;
						damagePlayer(other, 1, player);
					}
				}				
			} else if(playedCard.getName() == Card.CARDCATBALOU){
				List<Player> others = new ArrayList<Player>();
				for(Player otherPlayer : players){
					others.add(otherPlayer);
				}
				others.remove(currentTurn);
				int otherIndex = userInterface.askPlayer(player, others);
				Player otherPlayer = others.get(otherIndex);
				int chosenCard = userInterface.askOthersCard(player, otherPlayer.getInPlay());
				if(chosenCard == -1){
					discard.add(otherPlayer.getHand().removeRandom());
				} else if(chosenCard == -2){
					discard.add(otherPlayer.getInPlay().removeGun());
				} else {
					discard.add(otherPlayer.getInPlay().remove(chosenCard));
				}
			} else if(playedCard.getName() == Card.CARDPANIC){
				List<Player> others = getPlayersWithinRange(player, players, 1);
				int otherIndex = userInterface.askPlayer(player, others);
				Player otherPlayer = others.get(otherIndex);
				int chosenCard = userInterface.askOthersCard(player, otherPlayer.getInPlay());
				if(chosenCard == -1){
					hand.add(otherPlayer.getHand().removeRandom());
				} else if(chosenCard == -2){
					hand.add(otherPlayer.getInPlay().removeGun());
				} else {
					hand.add(otherPlayer.getInPlay().remove(chosenCard));
				}
			} else if(playedCard.getName() == Card.CARDBANG || missedBang){
				bangsPlayed = bangsPlayed + 1;
				List<Player> others = getPlayersWithinRange(player, players, player.getInPlay().getGunRange());
				int otherIndex = userInterface.askPlayer(player, others);
				Player otherPlayer = others.get(otherIndex);
				int missesRequired = 1;
				if(Figure.SLABTHEKILLER.equals(player.getFigure().getName())){
					missesRequired = 2;
				}
				int barrelMisses = isBarrelSave(player);
				missesRequired = missesRequired - barrelMisses;
				if(Figure.CALAMITYJANET.equals(otherPlayer.getFigure().getName())){
					int misses = otherPlayer.getHand().countMisses();
					int bangs = otherPlayer.getHand().countBangs();
					int playedBangMiss = userInterface.respondBangMiss(otherPlayer, bangs, misses, missesRequired);
					if(playedBangMiss == Figure.PLAYBANG){
						for(int i = 0; i < missesRequired; i++){
							discard.add(otherPlayer.getHand().removeMiss());
						}
					} else if(playedBangMiss == Figure.PLAYMISSED){
						for(int i = 0; i < missesRequired; i++){
							discard.add(otherPlayer.getHand().removeBang());
						}
					} else if(playedBangMiss == Figure.GETSHOT){
						damagePlayer(otherPlayer, 1, player);
					}
				} else {
					int misses = otherPlayer.getHand().countMisses();
					boolean playedMiss = userInterface.respondMiss(otherPlayer, misses, missesRequired);
					if(playedMiss){
						for(int i = 0; i < missesRequired; i++){
							discard.add(otherPlayer.getHand().removeMiss());
						}
					} else {
						damagePlayer(otherPlayer, 1, player);
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
		others.remove(player);
		return others;
	}

	public boolean isDynamiteExplode(){
		Player currentPlayer = players.get(currentTurn);
		InPlay currentInPlay = currentPlayer.getInPlay();
		if(currentInPlay.hasItem(Card.CARDDYNAMITE)){
			Card drawnCard = (Card)draw(currentPlayer);
			return Card.isExplode(drawnCard);
		}
		return false;
	}
	
	public void passDynamite() {
		Player currentPlayer = players.get(currentTurn);
		InPlay currentInPlay = currentPlayer.getInPlay();
		if(currentInPlay.hasItem(Card.CARDDYNAMITE)){
			Object dynamiteCard = currentInPlay.removeDynamite();
			Player nextPlayer = players.get(getNextTurn(currentTurn, players.size()));
			InPlay nextInPlay = nextPlayer.getInPlay();
			nextInPlay.add(dynamiteCard);
		}
	}

	public Object draw(Player player) {
		if(Figure.LUCKYDUKE.equals(player.getFigure().getName())){
			List<Object> cards = new ArrayList<Object>();			
			cards.add(deck.pull());
			cards.add(deck.pull());
			int chosenCard = userInterface.chooseGeneralStoreCard(player, cards);
			for(Object card : cards){
				discard.add(card);
			}
			return cards.get(chosenCard);
		} else {
			Object card = deck.pull();
			discard.add(card);
			return card;
		}
	}

	public boolean isInJail() {		
		Player currentPlayer = players.get(currentTurn);
		InPlay currentInPlay = currentPlayer.getInPlay();
		if(currentInPlay.hasItem(Card.CARDJAIL)){
			Object jailCard = currentInPlay.removeJail();
			discard.add(jailCard);
			Card drawn = (Card)draw(currentPlayer);
			return drawn.getSuit() != Card.HEARTS;
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
		discardTwoCardsForLife(player);
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
				boolean playBeer = userInterface.respondBeer(player, beers);
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
		players.remove(player);
		if(damager != null){
			if(damager.getRole() == Player.SHERIFF && player.getRole() == Player.DEPUTY){
				discardAll(damager);
			} else if(player.getRole() == Player.OUTLAW) {
				damager.getHand().add(deck.pull());
				damager.getHand().add(deck.pull());
				damager.getHand().add(deck.pull());
			}
			deadDiscardAll(player);
			if(isGameOver()){
				winner = getWinners();
			}
		}		
	}
	
	public void discardAll(Player player){
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
	
	public void deadDiscardAll(Player player){
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
	
	public boolean isGameOver() {
		return isDead(Player.SHERIFF) || isDead(Player.RENEGADE) && isDead(Player.OUTLAW);
	}
	
	private boolean isDead(int role) {
		for(Player player : players){
			if(player.getRole() == role){
				return false;
			}
		}
		return true;
	}

	public String getWinners() {
		if(isDead(Player.OUTLAW) && isDead(Player.RENEGADE)){
			return "Sheriff and Deputies";	
		} else if(isDead(Player.SHERIFF) && (!isDead(Player.DEPUTY) || !isDead(Player.OUTLAW))) {
			return "Outlaws";
		} else if(isDead(Player.DEPUTY) && isDead(Player.OUTLAW) && isDead(Player.SHERIFF)){
			return "Renegades";
		} else {
			throw new RuntimeException("No Winner");
		}
	}
	
	public void discardTwoCardsForLife(Player player){
		if(Figure.SIDKETCHUM.equals(player.getFigure().getName())){
			Hand hand = player.getHand();			
			List<Object> cardsToDiscard = userInterface.chooseTwoDiscardForLife(player);
			if(cardsToDiscard.size() % 2 == 0){
				for(Object card : cardsToDiscard){
					hand.remove(card);
				}
				player.setHealth(player.getHealth() + (cardsToDiscard.size() / 2));
			}
		}
	}
}
