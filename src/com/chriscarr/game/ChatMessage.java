package com.chriscarr.game;

import java.util.Date;

public class ChatMessage {
	String message;
	Date timestamp;
	
	public ChatMessage(String message) {
		this.message = message;
		timestamp = new Date(System.currentTimeMillis());
	}
}
