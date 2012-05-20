package com.chriscarr.game;

import java.util.HashMap;
import java.util.Map;

import com.chriscarr.bang.Setup;
import com.chriscarr.bang.gamestate.GameStateListener;
import com.chriscarr.bang.userinterface.UserInterface;


public class WebInit {
	
	private static Map<Integer, UserInterface> userInterfaces = new HashMap<Integer, UserInterface>();
	private static Map<Integer, GameStateListener> gameStateListeners = new HashMap<Integer, GameStateListener>();	
		
	public void setup(int numPlayers, UserInterface userInterface, GameStateListener gameStateListener, int gameId){
		Thread gameThread = new GameThread(numPlayers, userInterface, gameStateListener);
		userInterfaces.put(gameId, userInterface);
		gameStateListeners.put(gameId, gameStateListener);
		gameThread.start();
	}
	
	public static UserInterface getUserInterface(int gameId){
		return WebInit.userInterfaces.get(gameId);
	}
	
	public static void remove(int gameId){
		userInterfaces.remove(gameId);
		gameStateListeners.remove(gameId);
	}
	
	class GameThread extends Thread{		
		int numPlayers;
		UserInterface userInterface;
		GameStateListener gameStateListener;
		GameThread(int numPlayers, UserInterface userInterface, GameStateListener gameStateListener){
				this.numPlayers = numPlayers;
				this.userInterface = userInterface;
				this.gameStateListener = gameStateListener;
		}
		public void run(){
			new Setup(numPlayers, userInterface, gameStateListener);
		}
	}
}


