package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebGame {
	private static int gameCounter = 0;
	private static int counter = 0;
	private static Map<Integer, List<String>> joinedPlayers = new HashMap<Integer, List<String>>();
	
	public static int create(){
		int gameId = gameCounter;
		joinedPlayers.put(gameId, new ArrayList<String>());
		gameCounter++;
		return gameId;
	}
	
	public static String join(int gameId){
		if(getCountPlayers(gameId) < 7){
			String result = Integer.toString(counter);
			joinedPlayers.get(gameId).add(result);
			counter = counter + 1;
			return result;
		} else {
			return null;
		}
	}
	
	public static void leave(int gameId, String joinNumber){
		joinedPlayers.get(gameId).remove(joinNumber);
	}
	
	public static int getCountPlayers(int gameId){
		return joinedPlayers.get(gameId).size();
	}
	
	public static boolean canStart(int gameId){
		return getCountPlayers(gameId) > 3;
	}
	
	public static void start(int gameId){
		if(canStart(gameId)){
			WebInit webInit = new WebInit();
			WebGameUserInterface x = new WebGameUserInterface(joinedPlayers.get(gameId));
			webInit.setup(getCountPlayers(gameId), x, x, gameId);
			joinedPlayers.remove(gameId);
		}
	}

	public static List<Integer> getAvailableGames() {
		return new ArrayList<Integer>(joinedPlayers.keySet());		
	}
}
