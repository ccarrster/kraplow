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
	private static Map<String, List<ChatMessage>> chatLogs = new ConcurrentHashMap<String, List<ChatMessage>>();
	private static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	private static Map<String, List<String>> gameHandles = new ConcurrentHashMap<String, List<String>>();
	
	static {
		chatLogs.put("lobby", new ArrayList<ChatMessage>());
	}
	
	public static int create(){
		int gameId = gameCounter;
		GamePrep gamePrep = new GamePrep();
		gamePreps.put(gameId, gamePrep);
		gameCounter++;
		chatLogs.put(Integer.toString(gameId), new ArrayList<ChatMessage>());
		gameHandles.put(Integer.toString(gameId), new ArrayList<String>());
		return gameId;
	}
	
	public static List<String> getJoinedPlayers(int gameId){
		GamePrep gamePrep = gamePreps.get(gameId);
		if(gamePrep != null){
			return gamePrep.getJoinedPlayers();
		} else {
			return new ArrayList<String>();
		}
	}
	
	public static String getUniqueHandle(int gameId, String handle){
		handle = handle.replace("AI", "ai");
		handle = handle.replaceAll("[^a-zA-Z0-9]", "");
		List<String> handles = gameHandles.get(Integer.toString(gameId));
		while(handles.contains(handle)){
			handle = handle + "_" + getNextGuestCounter();
		}
		handles.add(handle);
		return handle;
	}
	
	public static String join(int gameId, String handle){
		return gamePreps.get(gameId).join(getUniqueHandle(gameId, handle));
	}
	
	public static String joinAI(int gameId, String handle){
		return gamePreps.get(gameId).joinAI(getUniqueHandle(gameId, handle));
	}
	
	public static boolean canJoin(int gameId){
		return gamePreps.get(gameId).canJoin();
	}
	
	public static void leave(int gameId, String joinNumber){
		gamePreps.get(gameId).leave(joinNumber);
		List<String> handles = gameHandles.get(Integer.toString(gameId));
		handles.remove(joinNumber);
		if(gamePreps.get(gameId).getCountPlayers() == 0){
			gamePreps.remove(gameId);
		}
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
	
	public static void start(int gameId, int aiSleepMs){
		if(canStart(gameId)){
			WebInit webInit = new WebInit();
			WebGameUserInterface x = new WebGameUserInterface(gamePreps.get(gameId).getJoinedPlayers(), aiSleepMs);
			webInit.setup(getCountPlayers(gameId), x, x, gameId);
			gamePreps.remove(gameId);
		}
	}

	public static List<Integer> getAvailableGames() {
		return new ArrayList<Integer>(gamePreps.keySet());		
	}

	public static void addChat(String chat, String gameId) {
		List<ChatMessage> chatLog = chatLogs.get(gameId);
		chatLog.add(new ChatMessage(chat));
		if(chatLog.size() >50){
			chatLog.remove(0);
		}
	}
	
	public static List<ChatMessage> getChatLog(String gameId){
		return chatLogs.get(gameId);
	}

	public static void cleanSessions(){
		Iterator<String> sessionIter = sessions.keySet().iterator();
		long now = System.currentTimeMillis();
		while(sessionIter.hasNext()){
			String sessionId = sessionIter.next();			
			if((sessions.get(sessionId).lastUpdated + 5000) < now){
				Session session = sessions.get(sessionId);
				String handle = session.handle;
				ArrayList<GamePrep> allPreps = new ArrayList<GamePrep>(gamePreps.values());
				for(int i = 0; i < allPreps.size(); i++){
					GamePrep prep = allPreps.get(i);
					prep.leave(handle);
				}
				sessions.remove(sessionId);
			}
		}
	}
	
	public static void updateSession(String sessionId, String handle) {
		if(!sessionId.equals("null")){
			//Removed new Long deprecated
			sessions.put(sessionId, new Session(System.currentTimeMillis(), handle));
			cleanSessions();
		}
	}
	
	public static int getNextGuestCounter(){
		return guestCounter++;
	}

	public static List<Session> getSessions() {
		return new ArrayList<Session>(sessions.values());		
	}

	public static void removeGame(int gameId) {
		chatLogs.remove(Integer.toString(gameId));
		gameHandles.remove(Integer.toString(gameId));
	}
}