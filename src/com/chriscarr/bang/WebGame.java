package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class WebGame {
	private static int counter = 0;
	
	private static List<String> joinedPlayers = new ArrayList<String>();
	
	public static String join(){
		if(getCountPlayers() < 7){
			String result = Integer.toString(counter);
			joinedPlayers.add(result);
			counter = counter + 1;
			return result;
		} else {
			return null;
		}
	}
	
	public static void leave(String joinNumber){
		joinedPlayers.remove(joinNumber);
	}
	
	public static int getCountPlayers(){
		return joinedPlayers.size();
	}
	
	public static boolean canStart(){
		return getCountPlayers() > 3;
	}
	
	public static void start(){
		if(canStart()){
			WebInit webInit = new WebInit();
			WebGameUserInterface x = new WebGameUserInterface(joinedPlayers);
			webInit.setup(getCountPlayers(), x, x);
		}
	}
}
