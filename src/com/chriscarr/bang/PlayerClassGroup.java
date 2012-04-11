package com.chriscarr.bang;

public class PlayerClassGroup {

	private boolean dead = false;
	
	public void kill() {
		dead = true;
	}

	public boolean isDead() {
		return dead;
	}

}
