package com.chriscarr.bang;

import junit.framework.TestCase;

public class GunTest extends TestCase {
	public void testGunSetGetDistance(){
		Gun gun = new Gun();
		gun.setRange(2);
		int gotRange = gun.getRange();
		assertEquals(gotRange, 2);
	}
	
	public void testGunLimitlessBang(){
		Gun gun = new Gun();
		gun.setLimitlessBangs(true);
		boolean limitlessBangs = gun.getLimitlessBangs();
		assertTrue(limitlessBangs);
	}
	
	public void testGunNotLimitlessBang(){
		Gun gun = new Gun();
		boolean limitlessBangs = gun.getLimitlessBangs();
		assertFalse(limitlessBangs);
	}
}
