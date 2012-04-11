package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class Turn {

	List<Player> players;
	private Player currentPlayer; 
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

	public void discard(Player player) {
		discardTwoCardsForLife(player, userInterface);
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
		Hand hand = currentPlayer.getHand();
		int card = userInterface.askPlay(currentPlayer);
		if(card == -1 || hand.size() == 0){
			donePlaying = true;
			return;
		}		
		Card playedCard = (Card)hand.get(card);
		if(playedCard.getType() == Card.TYPEGUN){
			hand.remove(card);
			playGun(playedCard, currentPlayer.getInPlay(), discard);			
		} else if(playedCard.getType() == Card.TYPEITEM){		
			if(playedCard.getName() == Card.CARDJAIL){
				List<Player> others = new ArrayList<Player>();
				for(Player otherPlayer : players){
					if((!otherPlayer.getInPlay().hasItem(Card.CARDJAIL)) && (otherPlayer.getRole() != Player.SHERIFF) && (!otherPlayer.equals(currentPlayer))){
						others.add(otherPlayer);
					}
				}
				if(others.isEmpty()){
					return;
				}
				hand.remove(card);
				int chosenPlayer = userInterface.askPlayer(currentPlayer, others);
				others.get(chosenPlayer).getInPlay().add(playedCard);
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
			if(playedCard.getName() == Card.CARDBANG){
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
				if(others.isEmpty()){
					return;
				}
			}
			if(playedCard.getName() == Card.CARDPANIC || playedCard.getName() == Card.CARDCATBALOU){
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
				Player indianPlayer = getNextPlayer(currentPlayer);
				while(indianPlayer != currentPlayer){
					int bangs = indianPlayer.getHand().countBangs();
					boolean respondBang = userInterface.respondBang(indianPlayer, bangs);
					if(respondBang){
						discard.add(indianPlayer.getHand().removeBang());
					} else {
						damagePlayer(indianPlayer, 1, currentPlayer);
					}
					indianPlayer = getNextPlayer(indianPlayer);
				}	
			} else if(playedCard.getName() == Card.CARDGATLING){
				Player gatlingPlayer = getNextPlayer(currentPlayer);
				while(gatlingPlayer != currentPlayer){
					if (isBarrelSave(gatlingPlayer) > 0){
						return;
					}
					int misses = gatlingPlayer.getHand().countMisses();
					boolean respondMiss = userInterface.respondMiss(gatlingPlayer, misses, 1);
					if(respondMiss){
						discard.add(gatlingPlayer.getHand().removeMiss());
					} else {
						damagePlayer(gatlingPlayer, 1, currentPlayer);
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
					int chosenCard = userInterface.chooseGeneralStoreCard(generalPlayer, generalStoreCards);
					generalPlayer.getHand().add(generalStoreCards.remove(chosenCard));
					generalPlayer = getNextPlayer(generalPlayer);
				}
			} else if(playedCard.getName() == Card.CARDDUEL){
				List<Player> others = Player.getOthers(currentPlayer, players);
				int playerIndex = userInterface.askPlayer(currentPlayer, others);
				Player other = others.get(playerIndex);
				boolean someoneIsShot = false;
				while(!someoneIsShot){
					boolean otherShot = userInterface.respondBang(other, other.getHand().countBangs());
					if(otherShot){
						discard.add(other.getHand().removeBang());
						boolean playerShot = userInterface.respondBang(currentPlayer, currentPlayer.getHand().countBangs());
						if(!playerShot){
							someoneIsShot = true;
							damagePlayer(currentPlayer, 1, other);
						} else {
							discard.add(currentPlayer.getHand().removeBang());
						}
					} else {
						someoneIsShot = true;
						damagePlayer(other, 1, currentPlayer);
					}
				}				
			} else if(playedCard.getName() == Card.CARDCATBALOU){
				List<Player> others = removeFromOthers(currentPlayer, players);
				int otherIndex = userInterface.askPlayer(currentPlayer, others);
				Player otherPlayer = others.get(otherIndex);
				int chosenCard = userInterface.askOthersCard(currentPlayer, otherPlayer.getInPlay());
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
				int otherIndex = userInterface.askPlayer(currentPlayer, others);
				Player otherPlayer = others.get(otherIndex);
				int chosenCard = userInterface.askOthersCard(currentPlayer, otherPlayer.getInPlay());
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
				int otherIndex = userInterface.askPlayer(currentPlayer, others);
				Player otherPlayer = others.get(otherIndex);
				int missesRequired = 1;
				if(Figure.SLABTHEKILLER.equals(currentPlayer.getFigure().getName())){
					missesRequired = 2;
				}
				int barrelMisses = isBarrelSave(currentPlayer);
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
						damagePlayer(otherPlayer, 1, currentPlayer);
					}
				} else {
					int misses = otherPlayer.getHand().countMisses();
					boolean playedMiss = userInterface.respondMiss(otherPlayer, misses, missesRequired);
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
		if(player.equals(currentPlayer)){
			currentPlayer = getPreviousPlayer(currentPlayer);
		}
		players.remove(player);
		if(damager != null){
			if(damager.getRole() == Player.SHERIFF && player.getRole() == Player.DEPUTY){
				discardAll(damager, discard);
			} else if(player.getRole() == Player.OUTLAW) {
				damager.getHand().add(deck.pull());
				damager.getHand().add(deck.pull());
				damager.getHand().add(deck.pull());
			}
			deadDiscardAll(player, players, discard);
			if(isGameOver(players)){
				winner = getWinners(players);
				System.out.println(winner);
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
			List<Object> cardsToDiscard = userInterface.chooseTwoDiscardForLife(player);
			if(cardsToDiscard.size() % 2 == 0){
				for(Object card : cardsToDiscard){
					hand.remove(card);
				}
				player.setHealth(player.getHealth() + (cardsToDiscard.size() / 2));
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
}
