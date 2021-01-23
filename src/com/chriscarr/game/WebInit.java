package com.chriscarr.game;

import java.util.HashMap;
import java.util.Map;

import com.chriscarr.bang.Setup;
import com.chriscarr.bang.gamestate.GameStateListener;
import com.chriscarr.bang.userinterface.UserInterface;


public class WebInit {
	
	private static Map<Integer, UserInterface> userInterfaces = new HashMap<Integer, UserInterface>();
	private static Map<Integer, GameStateListener> gameStateListeners = new HashMap<Integer, GameStateListener>();	
		
	public void setup(int numPlayers, UserInterface userInterface, GameStateListener gameStateListener, int gameId, boolean sidestep, String pRole, String pChar){
		Thread gameThread = new GameThread(numPlayers, userInterface, gameStateListener, sidestep, pRole, pChar);
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
		boolean sidestep;
		String pRole;
		String pChar;
		GameThread(int numPlayers, UserInterface userInterface, GameStateListener gameStateListener, boolean sidestep, String pRole, String pChar){
				this.numPlayers = numPlayers;
				this.userInterface = userInterface;
				this.gameStateListener = gameStateListener;
				this.sidestep = sidestep;
				this.pRole = pRole;
				this.pChar = pChar;
		}
		public void run(){
			new Setup(numPlayers, userInterface, gameStateListener, sidestep, pRole, pChar);
		}
	}
}


