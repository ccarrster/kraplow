package com.chriscarr.bang;

import junit.framework.TestCase;

public class DiscardTest extends TestCase{
	public void testDiscardCard(){
		Discard discard = new Discard();
		Object toAdd = new Object();
		discard.add(toAdd);
		Object peeked = discard.peek();
		assertEquals(toAdd, peeked);
	}
	
	public void testRemove(){
		Discard discard = new Discard();
		Object toAdd = new Object();
		discard.add(toAdd);
		Object removed = discard.remove();
		assertEquals(toAdd, removed);
	}

	public void testRemoveTwo(){
		Discard discard = new Discard();
		Object toAdd1 = new Object();
		Object toAdd2 = new Object();
		discard.add(toAdd1);
		discard.add(toAdd2);
		discard.remove();
		Object removed = discard.remove();
		assertEquals(toAdd1, removed);
	}
}
