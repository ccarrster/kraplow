package com.chriscarr.bang;

import junit.framework.TestCase;

public class DistanceTest extends TestCase {
	public void testSetup(){
		assertEquals(AlivePlayers.getDistance(0, 1, 2), 1);
	}
	
	public void testSetupReverse(){		
		assertEquals(AlivePlayers.getDistance(1, 0, 2), 1);
	}
	
	public void testSetupThree(){
		assertEquals(AlivePlayers.getDistance(0, 2, 3), 1);
	}
		
	public void testSetupFour(){
		assertEquals(AlivePlayers.getDistance(0, 2, 4), 2);
	}
	
	public void testSetupSeven(){
		assertEquals(AlivePlayers.getDistance(0, 6, 7), 1);
	}
	public void testSetupSevenTwo(){
		assertEquals(AlivePlayers.getDistance(0, 5, 7), 2);
	}
}
