package com.chriscarr.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chriscarr.bang.userinterface.WebGameUserInterface;

public class WebGame {
	private static int gameCounter = 0;
	private static Map<Integer, GamePrep> gamePreps = new HashMap<Integer, GamePrep>();
	private static List<String> chatLog = new ArrayList<String>();
	
	public static int create(){
		int gameId = gameCounter;
		GamePrep gamePrep = new GamePrep();
		gamePreps.put(gameId, gamePrep);
		gameCounter++;
		return gameId;
	}
	
	public static String join(int gameId){
			return gamePreps.get(gameId).join();
	}
	
	public static void leave(int gameId, String joinNumber){
		gamePreps.get(gameId).leave(joinNumber);
	}
	
	public static int getCountPlayers(int gameId){
		int timeoutMintues = 20;
		if(gamePreps.get(gameId).getLastUpdated() + (1000 * 60 * timeoutMintues) < System.currentTimeMillis()){
			gamePreps.remove(gameId);
			return 0;
		}
		return gamePreps.get(gameId).getCountPlayers();		
	}
	
	public static boolean canStart(int gameId){
		return gamePreps.get(gameId).canStart();
	}
	
	public static void start(int gameId){
		if(canStart(gameId)){
			WebInit webInit = new WebInit();
			WebGameUserInterface x = new WebGameUserInterface(gamePreps.get(gameId).getJoinedPlayers());
			webInit.setup(getCountPlayers(gameId), x, x, gameId);
			gamePreps.remove(gameId);
		}
	}

	public static List<Integer> getAvailableGames() {
		return new ArrayList<Integer>(gamePreps.keySet());		
	}

	public static void addChat(String chat) {	
		chatLog.add(chat);
		if(chatLog.size() >10){
			chatLog.remove(0);
		}
	}
	
	public static List<String> getChatLog(){
		return chatLog;
	}
}
