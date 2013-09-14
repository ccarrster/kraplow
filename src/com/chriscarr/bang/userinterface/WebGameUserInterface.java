package com.chriscarr.bang.userinterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.chriscarr.bang.Player;
import com.chriscarr.bang.gamestate.GameState;
import com.chriscarr.bang.gamestate.GameStatePlayer;

public class WebGameUserInterface extends JSPUserInterface {

	Map<String, List<Message>> messages;
	Map<String, List<Message>> responses;
	public Map<String, String> userFigureNames = null;
	Map<String, String> figureNamesUser = null;
	boolean gameOver = false;
	String timeout = null;
	int aiSleepMs;

	public WebGameUserInterface(List<String> users, int aiSleepMs) {
		this.aiSleepMs = aiSleepMs;
		messages = new ConcurrentHashMap<String, List<Message>>();
		responses = new ConcurrentHashMap<String, List<Message>>();
		for (String user : users) {
			messages.put(user, new ArrayList<Message>());
			responses.put(user, new ArrayList<Message>());
		}
	}

	public String somethingAI(String player, String message) {
		try {
			Thread.sleep(this.aiSleepMs);
		} catch (InterruptedException e) {
			//ignore
		}
		Player aiPlayer = turn.getPlayerForName(player);
		if (message.indexOf("askOthersCard") == 0) {
			String[] splitMessage = message.split(",");
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
		} else if (message.indexOf("chooseGeneralStoreCard") == 0
				|| message.indexOf("chooseDrawCard") == 0
				|| message.indexOf("askDiscard") == 0
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
			String options = message.replace("respondBang", "");
			String[] cards = options.split(",");
			for (int i = 0; i < cards.length - 1; i++) {
				String card = cards[i].trim();
				if (card.indexOf("Bang!") == 0) {
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
					if (!aiPlayer.isInPlay("Remington")) {
						return Integer.toString(i);
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
					if (!aiPlayer.isInPlay("Schofield")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Volcanic") == 0) {
					if (!aiPlayer.isInPlay("Volcanic")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Winchester") == 0) {
					if (!aiPlayer.isInPlay("Winchester")) {
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Rev. Carbine") == 0) {
					if (!aiPlayer.isInPlay("Rev. Carbine")) {
						return Integer.toString(i);
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
					if(whoToHurt(aiPlayer, splitCard[2]) != -1){
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Cat Balou@true") == 0) {
					String[] splitCard = card.split("@");
					if(whoToHurt(aiPlayer, splitCard[2]) != -1){
						return Integer.toString(i);
					}
				}
				if (card.indexOf("Indians!") == 0) {
					return Integer.toString(i);
				}
				if (card.indexOf("Gatling") == 0) {
					return Integer.toString(i);
				}
				if (card.indexOf("Saloon") == 0) {
					return Integer.toString(i);
				}
				if(card.indexOf("Bang!@true") == 0){
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
					String[] splitCard = card.split("@");
					if(whoToHurt(aiPlayer, splitCard[2]) != -1){
						return Integer.toString(i);
					}
				}
				
			}
			return "-1";
		}
		return null;
	}

	public int whoToHurt(Player player, String namesString){
		int role = player.getRole();
		String[] names = namesString.split("\\$");
		for(int i = 0; i < names.length - 1; i++){
			String name = names[i];
			name = name.trim();
			if(!name.equals("Cancel")){
				Player other = turn.getPlayerForName(name);
				int otherRole = other.getRole();
				if(role == Player.OUTLAW && otherRole == Player.SHERIFF){
					return i;
				} if(role == Player.DEPUTY && otherRole != Player.SHERIFF){
					return i;
				} if(role == Player.SHERIFF){
					return i;
				} if(role == Player.RENEGADE && (turn.countPlayers() == 2 || otherRole != Player.SHERIFF)){
					return i;
				}
			}
		}
		for(int i = 0; i < names.length - 1; i++){
			if(!names[i].equals("Cancel")){
				if(role == Player.OUTLAW){
					System.out.println("PlayerRole: Outlaw don't care");
					return i;
				}
			}
		}
		return -1;
	}
	
	public void sendMessage(String player, String message) {
		if (userFigureNames == null) {
			setupMap();
		}

		List<Message> playerMessages = messages
				.get(userFigureNames.get(player));
		playerMessages.add(new MessageImpl(player + "-" + message));
		if (userFigureNames.get(player).contains("AI")) {
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
					gameOver = true;
					timeout = player;
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
