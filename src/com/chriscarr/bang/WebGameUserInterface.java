package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebGameUserInterface extends JSPUserInterface {
	
	Map<String, List<String>> messages;
	Map<String, String> userFigureNames = null;
	
	public WebGameUserInterface(List<String> users){
		messages = new HashMap<String, List<String>>();
		for(String user : users){
			messages.put(user, new ArrayList<String>());
		}
	}
	
	public void sendMessage(String player, String message){
		if(userFigureNames == null){
			setupMap();
		}
		List<String> playerMessages = messages.get(userFigureNames.get(player));		
		playerMessages.add(player + "-" + message);
	}
	
	public void printInfo(String info) {
		Set<String> keys = messages.keySet();
		for(String key : keys){
			List<String> playerMessages = messages.get(key);		
			playerMessages.add(info);
		}
	}
	
	public List<String> getMessages(String user){
		return messages.get(user);
	}
	
	public GameState getGameState(){		
		return super.getGameState();
	}
	
	private void setupMap(){
		GameState gameState = super.getGameState();
		List<GameStatePlayer> players = gameState.getPlayers();
		Set<String> keys = messages.keySet();
		Iterator<String> userIter = keys.iterator();
		userFigureNames = new HashMap<String, String>();
		for(GameStatePlayer player : players){
			userFigureNames.put(player.name, userIter.next());
		}
	}
}
