package com.chriscarr.bang;


public class WebInit {
	
	public static UserInterface userInterface;
	public static GameStateListener gameStateListener;	
	
	public void setup(int numPlayers, UserInterface userInterface, GameStateListener gameStateListener){
		Thread gameThread = new Banana(numPlayers, userInterface, gameStateListener);
		WebInit.userInterface = userInterface;
		WebInit.gameStateListener = gameStateListener;
		gameThread.start();
	}
	
	class Banana extends Thread{
		int numPlayers;
		UserInterface userInterface;
		GameStateListener gameStateListener;
		Banana(int numPlayers, UserInterface userInterface, GameStateListener gameStateListener){
				this.numPlayers = numPlayers;
				this.userInterface = userInterface;
				this.gameStateListener = gameStateListener;
		}
		public void run(){
			new Setup(numPlayers, userInterface, gameStateListener);
		}
	}
}


