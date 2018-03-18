package com.chriscarr.game;

import java.util.ArrayList;
import java.util.List;

public class GamePrep {
	private List<String> joinedPlayers;
	private Long lastUpdated;
	private int counter;
	private String visibility;
	
	public GamePrep(String visibility){
		this.visibility = visibility;
		joinedPlayers = new ArrayList<String>();
		lastUpdated = System.currentTimeMillis();
	}
	
	public boolean canJoin(){
		if(joinedPlayers.size() < 7){
			return true;
		} else {
			return false;
		}
	}
	
	public String join(String handle){
		if(canJoin()){
			lastUpdated = System.currentTimeMillis();
			joinedPlayers.add(handle);
			counter = counter + 1;
			return handle;
		} else {
			for(int i = 0; i < joinedPlayers.size(); i++) {
				if(joinedPlayers.get(i).substring(joinedPlayers.get(i).length() - 2).equals("AI")){
					joinedPlayers.remove(i);
					return join(handle);
				}
			}
			return null;
		}
	}
	
	public String joinAI(String handle){
		if(canJoin()){
			lastUpdated = System.currentTimeMillis();
			handle = handle + "AI";
			joinedPlayers.add(handle);
			counter = counter + 1;
			return handle;
		} else {
			return null;
		}
	}
	
	public void leave(String joinNumber){
		joinedPlayers.remove(joinNumber);
		lastUpdated = System.currentTimeMillis();
	}
	
	public int getCountPlayers(){
		return joinedPlayers.size();
	}
	
	public boolean canStart(){
		return joinedPlayers.size() > 3;
	}
	
	public List<String> getJoinedPlayers(){
		return joinedPlayers;
	}
	
	public Long getLastUpdated(){
		return lastUpdated;
	}
	
	public String getVisibility() {
		return this.visibility;
	}
}
