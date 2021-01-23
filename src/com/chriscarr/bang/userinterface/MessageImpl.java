package com.chriscarr.bang.userinterface;

public class MessageImpl implements Message {

	static int nextId = 0;
	private String message;
	private int id;
	
	public MessageImpl(String info) {
		setMessage(info);
		this.id = nextId++;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getId(){
		return id;
	}

}
