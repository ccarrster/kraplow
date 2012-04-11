package com.chriscarr.bang;

import junit.framework.TestCase;

public class HandTest extends TestCase{
	public void testHandAddCard(){
		Hand hand = new Hand();
		Object card = new Object();
		hand.add(card);
		Object gotCard = hand.get(0);
		assertEquals(card, gotCard);
	}
	
	public void testHandSize(){
		Hand hand = new Hand();
		assertEquals(hand.size(), 0);
	}
	
	public void testHandSizeOne(){
		Hand hand = new Hand();
		hand.add(new Object());
		assertEquals(hand.size(), 1);
	}
}
