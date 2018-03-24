package com.chriscarr.bang.userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.cards.Card;
import com.chriscarr.bang.gamestate.GameState;
import com.chriscarr.bang.gamestate.GameStatePlayer;

public class WebGameUserInterface extends JSPUserInterface {

	Map<String, List<Message>> messages;
	Map<String, List<Message>> responses;
	public Map<String, String> userFigureNames = null;
	Map<String, String> figureNamesUser = null;
	List<String> timedOutPlayers;
	boolean gameOver = false;
	String timeout = null;
	int aiSleepMs;
	List<String> infoHistory;

	public WebGameUserInterface(List<String> users, int aiSleepMs) {
		infoHistory = new ArrayList<String>();
		timedOutPlayers = new ArrayList<String>();
		this.aiSleepMs = aiSleepMs;
		messages = new ConcurrentHashMap<String, List<Message>>();
		responses = new ConcurrentHashMap<String, List<Message>>();
		for (String user : users) {
			messages.put(user, new ArrayList<Message>());
			responses.put(user, new ArrayList<Message>());
		}
	}

	//This is the AI logic
	public String somethingAI(String player, String message) {
		String lastMessage = infoHistory.get(infoHistory.size() - 1);
		try {
			Thread.sleep(this.aiSleepMs);
		} catch (InterruptedException e) {
			//ignore
		}
		Player aiPlayer = turn.getPlayerForName(player);
		if (message.indexOf("askOthersCard") == 0) {
			String[] splitMessage = message.split(",");
			
			for(int i = 2; i < splitMessage.length - 1; i++){
				if(!splitMessage[i].equals(" Jail") && !splitMessage[i].equals(" Dynamite")){
					return Integer.toString(i - 2);
				}
			}
			
			if(splitMessage[0].indexOf("true") != -1){
				return "-1";
			}
			if(splitMessage[1].indexOf("true") != -1){
				return "-2";
			}
			return "0";
		} else if (message.indexOf("chooseDiscard") == 0
				|| message.indexOf("chooseFromPlayer") == 0) {
			return "false";
		} else if (message.indexOf("askDiscard") == 0) {
			String commandStripped = message.replace("askDiscard ", "");
			String[] cards = commandStripped.split(", ");
			if(turn.countPlayers() == 2){
				for(int i = 0; i < cards.length - 1; i++){
					if(cards[i].equals("Beer")){
						return Integer.toString(i);
					}
				}
				for(int i = 0; i < cards.length - 1; i++){
					if(!aiPlayer.isSheriff() && cards[i].equals("Jail")){
						return Integer.toString(i);
					}
				}
			}
			for(int i = 0; i < cards.length - 1; i++){
				InPlay inPlay = aiPlayer.getInPlay();
				if(inPlay.hasItem(cards[i]) || inPlay.getGunName().equals(cards[i])){
					return Integer.toString(i);
				}
			}
			for(int i = 0; i < cards.length - 1; i++){
				if(isGun(cards[i]) && isThisGunBetter(cards[i], aiPlayer.getInPlay().getGunName())){
					return Integer.toString(i);
				}
			}
			for(int i = 0; i < cards.length - 1; i++){
				if(!(cards[i].equals("Beer") || cards[i].equals("Missed!") || cards[i].equals("Shoot"))){
					return Integer.toString(i);
				}
			}
			return "0";
		} else if (message.indexOf("chooseGeneralStoreCard") == 0
				|| message.indexOf("chooseDrawCard") == 0
				|| message.indexOf("chooseCardToPutBack") == 0) {
			return "0";
		} else if (message.indexOf("askPlayer") == 0){
			String commandStripped = message.replace("askPlayer ", "");
			String dollarsReplaced = commandStripped.replace(", ", "$");
			int playerToHurt = whoToHurt(aiPlayer, dollarsReplaced);
			return Integer.toString(playerToHurt);
		} else if (message.indexOf("chooseTwoDiscardForLife") == 0
				|| message.indexOf("respondTwoMiss") == 0) {
			return "-1";
		} else if (message.indexOf("respondMiss") == 0) {
			String options = message.replace("respondMiss", "");
			String[] cards = options.split(",");
			for (int i = 0; i < cards.length - 1; i++) {
				String card = cards[i].trim();
				if (card.indexOf("Missed!") == 0) {
					return Integer.toString(i);
				}
			}
			return "-1";
		} else if (message.indexOf("respondBang") == 0) {
			//TODO if this is a duel, and you are a deputy, and the other is the sheriff, take the hit
			int duelIndex = lastMessage.indexOf(" duels ");
			if (duelIndex != -1) {
				String otherPlayer = lastMessage.substring(0, duelIndex);
				Player other = turn.getPlayerForName(otherPlayer);
				if(aiPlayer.getRole() == Player.DEPUTY && other.getRole() == Player.SHERIFF) {
					//Let the sheriff kill you(Not great for the sheriff)
					return "-1";
				}
			}

			String options = message.replace("respondBang", "");
			String[] cards = options.split(",");
			for (int i = 0; i < cards.length - 1; i++) {
				String card = cards[i].trim();
				if (card.indexOf("Shoot") == 0) {
					return Integer.toString(i);
				}
			}
			return "-1";
		} else if (message.indexOf("respondBeer") == 0) {
			String options = message.replace("respondBeer", "");
			String[] cards = options.split(",");
			for (int i = 0; i < cards.length - 1; i++) {
				String card = cards[i].trim();
				if (card.indexOf("Beer") == 0) {
					return Integer.toString(i);
				}
			}
			return "-1";
		} else if (message.indexOf("askPlay") == 0
				&& message.indexOf("askPlayer") != 0) {
			String options = message.replace("askPlay", "");
			String[] cards = options.split(",");
			for (int i = 0; i < cards.length - 1; i++) {
				String card = cards[i].trim();
				if (card.indexOf("Stagecoach") == 0) {
					return Integer.toString(i);
				}
				if (card.indexOf("Remington") == 0) {
					if(!aiPlayer.hasGun() || aiPlayer.isInPlay("Volcanic") || aiPlayer.isInPlay("Schofield")){
						if (!aiPlayer.isInPlay("Remington")) {
							return Integer.toString(i);
						}
					}
				}
				if (card.indexOf("Scope") == 0) {
					if (!aiPlayer.isInPlay("Scope")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Mustang") == 0) {
					if (!aiPlayer.isInPlay("Mustang")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Barrel") == 0) {
					if (!aiPlayer.isInPlay("Barrel")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Beer") == 0) {
					if (aiPlayer.getHealth() < aiPlayer.getMaxHealth()) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Dynamite") == 0) {
					if (!aiPlayer.isInPlay("Dynamite")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Schofield") == 0) {
					if(!aiPlayer.hasGun() || aiPlayer.isInPlay("Volcanic")){
						if (!aiPlayer.isInPlay("Schofield")) {
							return Integer.toString(i);
						}
					}
				}
				if (card.indexOf("Volcanic") == 0) {
					if (!aiPlayer.hasGun() && !aiPlayer.isInPlay("Volcanic")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Winchester") == 0) {
					if (!aiPlayer.isInPlay("Winchester")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Rev. Carbine") == 0) {
					if(!aiPlayer.hasGun() || aiPlayer.isInPlay("Volcanic") || aiPlayer.isInPlay("Schofield") || aiPlayer.isInPlay("Remington")){
						if (!aiPlayer.isInPlay("Rev. Carbine")) {
							return Integer.toString(i);
						}
					}
				}
				if (card.indexOf("Wells Fargo") == 0) {
					return Integer.toString(i);
				}
				if (card.indexOf("General Store") == 0) {
					return Integer.toString(i);
				}
				if (card.indexOf("Panic!@true") == 0) {
					String[] splitCard = card.split("@");
					if(whoToHurtCardTake(aiPlayer, splitCard[2], true) != -1){
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Cat Balou@true") == 0) {

					String[] splitCard = card.split("@");
					if(whoToHurtCardTake(aiPlayer, splitCard[2], true) != -1){
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Indians!") == 0) {
					if(hurtEveryone(aiPlayer)){
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Gatling") == 0) {
					if(hurtEveryone(aiPlayer)){
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Saloon") == 0) {
					if(healEveryone(aiPlayer)){
						return Integer.toString(i);	
					}
					
				}
				if(card.indexOf("Shoot@true") == 0){
					String[] splitCard = card.split("@");
					if(whoToHurt(aiPlayer, splitCard[2]) != -1){
						return Integer.toString(i);
					}
				}
				if(card.indexOf("Missed!@true") == 0){
					String[] splitCard = card.split("@");
					if(whoToHurt(aiPlayer, splitCard[2]) != -1){
						return Integer.toString(i);
					}
				}
				if(card.indexOf("Jail@true") == 0){
					String[] splitCard = card.split("@");
					if(whoToHurt(aiPlayer, splitCard[2]) != -1){
						return Integer.toString(i);
					}
				}
				if(card.indexOf("Duel@true") == 0){
					//Don't duel on one health
					if(aiPlayer.getHealth() > 1) {
						String[] splitCard = card.split("@");
						if(whoToHurt(aiPlayer, splitCard[2]) != -1){
							return Integer.toString(i);
						}
					}
				}
				
			}
			return "-1";
		}
		return null;
	}

	private boolean isGun(String cardName) {
		return cardName.equals(Card.CARDVOLCANIC) || cardName.equals(Card.CARDSCHOFIELD) || cardName.equals(Card.CARDREMINGTON) || cardName.equals(Card.CARDREVCARBINE) || cardName.equals(Card.CARDWINCHESTER);
	}
	
	private boolean isThisGunBetter(String thisGun, String thatGun){
		Map<String, Integer> gunRank = new HashMap<String, Integer>();
		gunRank.put("Colt .45", 0);
		gunRank.put(Card.CARDVOLCANIC, 1);
		gunRank.put(Card.CARDSCHOFIELD, 2);
		gunRank.put(Card.CARDREMINGTON, 3);
		gunRank.put(Card.CARDREVCARBINE, 4);
		gunRank.put(Card.CARDWINCHESTER, 5);
		if(gunRank.get(thisGun) - gunRank.get(thatGun) > 0){
			return true;
		} else {
			return false;
		}
	}
	

	public boolean hurtEveryone(Player player){
		int role = player.getRole();
		if(role == Player.DEPUTY || (role == Player.RENEGADE && turn.countPlayers() > 2)){
			Player sheriff = turn.getSheriff();
			if(sheriff.getHealth() > 3){
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean healEveryone(Player player){
		int role = player.getRole();
		if(role == Player.DEPUTY || (role == Player.RENEGADE && turn.countPlayers() > 2)){
			Player sheriff = turn.getSheriff();
			if(sheriff.getHealth() < 3){
				return true;
			} else {
				return false;
			}
		}
		if(player.getHealth() < player.getMaxHealth()){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean playerGotCardIWantToTake(Player me, Player them){
		if(them.getHandSize() > 0){
			return true;
		} else if(them.hasGun()){
			return true;
		} 
		InPlay inPlay = them.getInPlay();
		int cardsInPlay = inPlay.count();
		if(inPlay.hasItem(Card.CARDJAIL)){
			cardsInPlay--;
		}
		if(inPlay.hasItem(Card.CARDDYNAMITE)){
			cardsInPlay--;
		}
		if(cardsInPlay > 0){
			return true;
		}
		return false;
	}
	
	public int whoToHurt(Player player, String namesString){
		return whoToHurtCardTake(player, namesString, false);
	}
	
	public int whoToHurtCardTake(Player player, String namesString, boolean takeCard){
		ArrayList<Integer> targets = new ArrayList<Integer>();
		int role = player.getRole();
		String[] names = namesString.split("\\$");
		for(int i = 0; i < names.length; i++){
			String name = names[i];
			name = name.trim();
			if(!name.equals("Cancel")){
				Player other = turn.getPlayerForName(name);
				int otherRole = other.getRole();
				if(role == Player.OUTLAW && otherRole == Player.SHERIFF){
					if(!takeCard || playerGotCardIWantToTake(player, other)){
						return i;
					}
				} if(role == Player.DEPUTY && otherRole != Player.SHERIFF){
					if(!takeCard || playerGotCardIWantToTake(player, other)){
						targets.add(i);
					}
				} if(role == Player.SHERIFF){
					if(!takeCard || playerGotCardIWantToTake(player, other)){
						targets.add(i);
					}
				} if(role == Player.RENEGADE && (turn.countPlayers() == 2 || otherRole != Player.SHERIFF)){
					if(!takeCard || playerGotCardIWantToTake(player, other)){
						targets.add(i);
					}
				}
			}
		}
		for(int i = 0; i < names.length - 1; i++){
			if(!names[i].equals("Cancel")){
				if(role == Player.OUTLAW){
					Player other = turn.getPlayerForName(names[i]);
					if(!takeCard || playerGotCardIWantToTake(player, other)){
						targets.add(i);
					}
				}
			}
		}
		if(targets.size() == 0){
			return -1;
		} else {
			//Random target instead of first
			Collections.shuffle(targets);
			return targets.get(0);
		}
	}
	
	public void sendMessage(String player, String message) {
		if (userFigureNames == null) {
			setupMap();
		}

		List<Message> playerMessages = messages
				.get(userFigureNames.get(player));
		playerMessages.add(new MessageImpl(player + "-" + message));
		if (userFigureNames.get(player).contains("AI") || timedOutPlayers.contains(userFigureNames.get(player))) {
			while (messages.isEmpty()) {
				
			}
			addResponse(userFigureNames.get(player), somethingAI(player,
					message));
		}
	}

	public void addResponse(String user, String message) {
		System.out.println("Response " + user + " " + message);
		if (!getMessages(user).isEmpty()) {
			System.out.println("Response " + getMessages(user).get(0));
		} else {
			System.out
					.println("**Weirdo response to empty message queue in WebGameUserInterface**");
		}
		List<Message> playerResponses = responses.get(user);
		playerResponses.add(new MessageImpl(message));
	}

	synchronized public void printInfo(String info) {
		Set<String> keys = messages.keySet();
		for (String key : keys) {
			if (!key.contains("AI")) {
				List<Message> playerMessages = messages.get(key);
				playerMessages.add(new MessageImpl(info));
			}
		}
		infoHistory.add(info);
	}

	public List<Message> getMessages(String user) {
		return messages.get(user);
	}

	public GameState getGameState() {
		GameState gameState = super.getGameState(gameOver);
		return gameState;
	}

	private void setupMap() {
		GameState gameState = super.getGameState();
		List<GameStatePlayer> players = gameState.getPlayers();
		Set<String> keys = messages.keySet();
		Iterator<String> userIter = keys.iterator();
		userFigureNames = new ConcurrentHashMap<String, String>();
		figureNamesUser = new ConcurrentHashMap<String, String>();
		for (GameStatePlayer player : players) {
			String user = userIter.next();
			userFigureNames.put(player.name, user);
			figureNamesUser.put(user, player.name);
		}
	}

	protected void waitForResponse(String player) {
		int maxWait = 360000;
		int wait = 100;
		int waitCount = 0;
		while (responses.get(userFigureNames.get(player)).isEmpty()) {
			try {
				Thread.sleep(wait);
				waitCount += wait;
				if (waitCount > maxWait) {
					printInfo(player + " has timed out and AI has taken over for them.");
					timedOutPlayers.add(userFigureNames.get(player));
					//-5 is never a valid response, but it will trigger the AI to make a valid one
					addResponse(userFigureNames.get(player), "-5");
				}
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

	public String removeResponse(String player) {
		return responses.get(userFigureNames.get(player)).remove(0)
				.getMessage();
	}

	public String getPlayerForUser(String user) {
		return figureNamesUser.get(user);
	}

	public String getTimeout() {
		return timeout;
	}
}
