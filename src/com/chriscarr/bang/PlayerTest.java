package com.chriscarr.bang;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {
	public void testPlayer() {
		Player player = new Player();
		Figure setFigure = new Figure();
		player.setFigure(setFigure);
		int setRole = Player.SHERIFF;
		player.setRole(setRole);
		Hand setHand = new Hand();
		player.setHand(setHand);
		InPlay setInPlay = new InPlay();
		player.setInPlay(setInPlay);
		
		Figure gotFigure = player.getFigure();
		int gotRole = player.getRole();
		Hand gotHand = player.getHand();
		InPlay gotInPlay = player.getInPlay();
		
		assertEquals(gotFigure, setFigure);
		assertEquals(gotRole, setRole);
		assertEquals(gotHand, setHand);
		assertEquals(gotInPlay, setInPlay);
	}
}
