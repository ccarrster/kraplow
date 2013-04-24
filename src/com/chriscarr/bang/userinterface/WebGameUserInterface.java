package com.chriscarr.bang.userinterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.chriscarr.bang.gamestate.GameState;
import com.chriscarr.bang.gamestate.GameStatePlayer;

public class WebGameUserInterface extends JSPUserInterface {
	
	Map<String, List<String>> messages;
	Map<String, List<String>> responses;
	Map<String, String> userFigureNames = null;
	Map<String, String> figureNamesUser = null;
	boolean gameOver = false;
	String timeout = null;
	
	public WebGameUserInterface(List<String> users){
		messages = new ConcurrentHashMap<String, List<String>>();
		responses = new ConcurrentHashMap<String, List<String>>();
		for(String user : users){
			messages.put(user, new ArrayList<String>());
			responses.put(user, new ArrayList<String>());
		}
	}
	
	public String somethingAI(String message){		
		if(message.indexOf("askOthersCard") == 0){	
			return Integer.toString((int)Math.floor(Math.random() * -3));
		} else if(message.indexOf("chooseDiscard") == 0 || message.indexOf("chooseFromPlayer") == 0){
			return "false";
		} else if(message.indexOf("chooseGeneralStoreCard") == 0 || message.indexOf("chooseDrawCard") == 0 || message.indexOf("askDiscard") == 0 || message.indexOf("askPlayer") == 0 || message.indexOf("chooseCardToPutBack") == 0){
			return "0";
		} else if(message.indexOf("askPlay") == 0 || message.indexOf("chooseTwoDiscardForLife") == 0 || message.indexOf("respondTwoMiss") == 0){
			return "-1";
		} else if(message.indexOf("respondMiss") == 0){
			String options = message.replace("respondMiss", "");
			String[] cards = options.split(",");
			for(int i = 0; i < cards.length - 1; i++){
				String card = cards[i].trim();
				if(card.indexOf("Missed!") == 0){
					return Integer.toString(i);
				}
			}
			return "-1";
		} else if(message.indexOf("respondBang") == 0){
			String options = message.replace("respondBang", "");
			String[] cards = options.split(",");
			for(int i = 0; i < cards.length - 1; i++){
				String card = cards[i].trim();
				if(card.indexOf("Bang!") == 0){
					return Integer.toString(i);
				}
			}
			return "-1";
		} else if(message.indexOf("respondBeer") == 0){
			String options = message.replace("respondBeer", "");
			String[] cards = options.split(",");
			for(int i = 0; i < cards.length - 1; i++){
				String card = cards[i].trim();
				if(card.indexOf("Beer") == 0){
					return Integer.toString(i);
				}
			}
			return "-1";
		}
		return null;
	}
	
	public void sendMessage(String player, String message){
		if(userFigureNames == null){
			setupMap();
		}
		
		List<String> playerMessages = messages.get(userFigureNames.get(player));
		playerMessages.add(player + "-" + message);
		if(userFigureNames.get(player).contains("AI")){
			while(messages.isEmpty()){
				System.out.println("Messages Is Empty");
			}
			addResponse(userFigureNames.get(player), somethingAI(message));
		}
	}
	
	public void addResponse(String user, String message){
		System.out.println("Response " + user + " " + message);
		if(!getMessages(user).isEmpty()){
			System.out.println("Response " + getMessages(user).get(0));
		} else {
			System.out.println("**Weirdo response to empty message queue in WebGameUserInterface**");
		}
		List<String> playerResponses = responses.get(user);		
		playerResponses.add(message);
	}
	
	synchronized public void printInfo(String info) {
		Set<String> keys = messages.keySet();
		for(String key : keys){
			if(!key.contains("AI")){
				List<String> playerMessages = messages.get(key);		
				playerMessages.add(info);
			}
		}
	}
	
	public List<String> getMessages(String user){
		return messages.get(user);
	}
	
	public GameState getGameState(){		
		GameState gameState = super.getGameState(gameOver);
		return gameState;
	}
	
	private void setupMap(){
		GameState gameState = super.getGameState();
		List<GameStatePlayer> players = gameState.getPlayers();
		Set<String> keys = messages.keySet();
		Iterator<String> userIter = keys.iterator();
		userFigureNames = new ConcurrentHashMap<String, String>();
		figureNamesUser = new ConcurrentHashMap<String, String>();
		for(GameStatePlayer player : players){
			String user = userIter.next();
			userFigureNames.put(player.name, user);
			figureNamesUser.put(user, player.name);
		}
	}
	
	protected void waitForResponse(String player){
		int maxWait = 180000;
		int wait = 100;
		int waitCount = 0;
		while(responses.get(userFigureNames.get(player)).isEmpty()){
			try {
				Thread.sleep(wait);
				waitCount += wait;
				if(waitCount > maxWait){
					gameOver = true;
					timeout = player;
				}
			} catch (InterruptedException e) {
				//ignore
			}
		}
	}
	
	public String removeResponse(String player){
		return responses.get(userFigureNames.get(player)).remove(0);
	}
	
	public String getPlayerForUser(String user){
		return figureNamesUser.get(user);
	}
	
	public String getTimeout(){
		return timeout;
	}
}
