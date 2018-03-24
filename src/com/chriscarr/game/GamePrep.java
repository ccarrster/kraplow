package com.chriscarr.game;

import java.util.ArrayList;
import java.util.List;

public class GamePrep {
	private List<String> joinedPlayers;
	private Long lastUpdated;
	private int counter;
	private String visibility;
	private boolean sidestep;
	
	public GamePrep(String visibility, boolean sidestep){
		this.visibility = visibility;
		this.sidestep = sidestep;
		joinedPlayers = new ArrayList<String>();
		lastUpdated = System.currentTimeMillis();
	}
	
	public boolean getSidestep(){
		return this.sidestep;
	}

	public boolean canJoin(){
		if(joinedPlayers.size() < 7){
			return true;
		} else {
			for(int i = 0; i < joinedPlayers.size(); i++) {
				if(joinedPlayers.get(i).substring(joinedPlayers.get(i).length() - 2).equals("AI")){
					return true;
				}
			}
			return false;
		}
	}

	public boolean canJoinAI(){
		if(joinedPlayers.size() < 7){
			return true;
		} else {
			return false;
		}
	}
	
	public String join(String handle){
		if(canJoin()){
			lastUpdated = System.currentTimeMillis();
			if(joinedPlayers.size() == 7){
				for(int i = 0; i < joinedPlayers.size(); i++) {
					if(joinedPlayers.get(i).substring(joinedPlayers.get(i).length() - 2).equals("AI")){
						joinedPlayers.remove(i);
						break;
					}
				}	
			}
			joinedPlayers.add(handle);
			counter = counter + 1;
			return handle;
		} else {
			return null;
		}
	}
	
	public String joinAI(String handle){
		if(canJoinAI()){
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
