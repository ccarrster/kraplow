package com.chriscarr.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chriscarr.bang.userinterface.WebGameUserInterface;

public class WebGame {
	private static int gameCounter = 0;
	private static int guestCounter = 0;
	private static Map<Integer, GamePrep> gamePreps = new ConcurrentHashMap<Integer, GamePrep>();
	private static List<ChatMessage> chatLog = new ArrayList<ChatMessage>();
	private static Map<String, Long> sessions = new ConcurrentHashMap<String, Long>();
	private static List<String> handles = new ArrayList<String>();
	
	public static int create(){
		int gameId = gameCounter;
		GamePrep gamePrep = new GamePrep();
		gamePreps.put(gameId, gamePrep);
		gameCounter++;
		return gameId;
	}
	
	public static List<String> getJoinedPlayers(int gameId){
		return gamePreps.get(gameId).getJoinedPlayers();
	}
	
	public static String getUniqueHandle(String handle){
		handle = handle.replace("AI", "ai");
		handle = handle.replaceAll("[^a-zA-Z0-9]", "");
		while(handles.contains(handle)){
			handle = handle + "_" + getNextGuestCounter();
		}
		handles.add(handle);
		return handle;
	}
	
	public static String join(int gameId, String handle){
		return gamePreps.get(gameId).join(getUniqueHandle(handle));
	}
	
	public static String joinAI(int gameId, String handle){
		return gamePreps.get(gameId).joinAI(getUniqueHandle(handle));
	}
	
	public static boolean canJoin(int gameId){
		return gamePreps.get(gameId).canJoin();
	}
	
	public static void leave(int gameId, String joinNumber){
		gamePreps.get(gameId).leave(joinNumber);
	}
	
	public static int getCountPlayers(int gameId){
		int timeoutMintues = 20;
		if(gamePreps.get(gameId) != null){
			if(gamePreps.get(gameId).getLastUpdated() + (1000 * 60 * timeoutMintues) < System.currentTimeMillis()){
				gamePreps.remove(gameId);
				return 0;
			}
			return gamePreps.get(gameId).getCountPlayers();
		}
		return 0;
	}
	
	public static boolean canStart(int gameId){
		if(gamePreps.get(gameId) != null){
			return gamePreps.get(gameId).canStart();
		}
		return false;
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
		chatLog.add(new ChatMessage(chat));
		if(chatLog.size() >10){
			chatLog.remove(0);
		}
	}
	
	public static List<ChatMessage> getChatLog(){
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
