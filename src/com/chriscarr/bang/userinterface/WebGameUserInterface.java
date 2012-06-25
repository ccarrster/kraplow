package com.chriscarr.bang.userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chriscarr.bang.gamestate.GameState;
import com.chriscarr.bang.gamestate.GameStatePlayer;

public class WebGameUserInterface extends JSPUserInterface {
	
	Map<String, List<String>> messages;
	Map<String, List<String>> responses;
	Map<String, String> userFigureNames = null;
	Map<String, String> figureNamesUser = null;
	boolean gameOver = false;
	
	public WebGameUserInterface(List<String> users){
		messages = new HashMap<String, List<String>>();
		responses = new HashMap<String, List<String>>();
		for(String user : users){
			messages.put(user, new ArrayList<String>());
			responses.put(user, new ArrayList<String>());
		}
	}
	
	public void sendMessage(String player, String message){
		System.out.println(player + " " + message);
		if(userFigureNames == null){
			setupMap();
		}
		if(!userFigureNames.get(player).contains("AI")){
			List<String> playerMessages = messages.get(userFigureNames.get(player));
			playerMessages.add(player + "-" + message);	
		} else {
			if(message.indexOf("askOthersCard") == 0){	
				addResponse(userFigureNames.get(player), Integer.toString((int)Math.floor(Math.random() * -3)));
				System.out.println("response ?");
			} else if(message.indexOf("chooseDiscard") == 0 || message.indexOf("chooseFromPlayer") == 0){
				addResponse(userFigureNames.get(player), "false");
				System.out.println("response false");
			} else if(message.indexOf("chooseGeneralStoreCard") == 0 || message.indexOf("chooseDrawCard") == 0 || message.indexOf("askDiscard") == 0 || message.indexOf("askPlayer") == 0 || message.indexOf("chooseCardToPutBack") == 0){
				addResponse(userFigureNames.get(player), "0");
				System.out.println("response 0");
			} else if(message.indexOf("askPlay") == 0 || message.indexOf("chooseTwoDiscardForLife") == 0 || message.indexOf("respondBang") == 0 || message.indexOf("respondBeer") == 0 || message.indexOf("respondMiss") == 0 || message.indexOf("respondTwoMiss") == 0){
				addResponse(userFigureNames.get(player), "-1");
				System.out.println("response -1");
			}
		}
	}
	
	public void addResponse(String user, String message){
		List<String> playerResponses = responses.get(user);		
		playerResponses.add(message);
	}
	
	public void printInfo(String info) {
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
		userFigureNames = new HashMap<String, String>();
		figureNamesUser = new HashMap<String, String>();
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
}
