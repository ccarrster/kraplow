package com.chriscarr.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.chriscarr.bang.userinterface.WebGameUserInterface;

public class WebGame {
	private static int gameCounter = 0;
	private static int guestCounter = 0;
	private static Map<Integer, GamePrep> gamePreps = new HashMap<Integer, GamePrep>();
	private static List<String> chatLog = new ArrayList<String>();
	private static Map<String, Long> sessions = new HashMap<String, Long>();
	
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

	public static void cleanSessions(){
		Iterator<String> sessionIter = sessions.keySet().iterator();
		long now = System.currentTimeMillis();
		while(sessionIter.hasNext()){
			String sessionId = sessionIter.next();			
			if((sessions.get(sessionId) + 5000) < now){
				sessions.remove(sessionId);
			}
		}
	}
	
	public static void updateSession(String sessionId) {
		if(!sessionId.equals("null")){
			sessions.put(sessionId, System.currentTimeMillis());
			cleanSessions();
		}
	}
	
	public static int getNextGuestCounter(){
		return guestCounter++;
	}

	public static List<String> getSessions() {
		return new ArrayList<String>(sessions.keySet());		
	}
}
