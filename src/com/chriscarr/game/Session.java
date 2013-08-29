package com.chriscarr.game;

public class Session {
	public Long lastUpdated;
	public String handle;
	public Session(Long lastUpdated, String handle){
		this.lastUpdated = lastUpdated;
		this.handle = handle;
	}	
}
