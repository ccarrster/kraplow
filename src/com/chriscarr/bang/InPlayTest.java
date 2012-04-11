package com.chriscarr.bang;

import junit.framework.TestCase;

public class InPlayTest extends TestCase {
	public void testDoesNotHaveGun(){
		InPlay inPlay = new InPlay();
		assertFalse(inPlay.hasGun());
	}
	
	public void testDoesHaveGun(){
		InPlay inPlay = new InPlay();
		inPlay.setGun(new Object());
		assertTrue(inPlay.hasGun());
	}
	
	public void testRemoveGun(){
		InPlay inPlay = new InPlay();
		Object gun = new Object();
		inPlay.setGun(gun);
		Object removedGun = inPlay.removeGun();
		assertEquals(removedGun, gun);
	}
	
	public void testRemovedGun(){
		InPlay inPlay = new InPlay();
		Object gun = new Object();
		inPlay.setGun(gun);
		inPlay.removeGun();
		assertFalse(inPlay.hasGun());
	}
	
	public void testAddCard(){
		InPlay inPlay = new InPlay();
		Object toAdd = new Object();
		inPlay.add(toAdd);
		Object peeked = inPlay.peek(0);
		assertEquals(toAdd, peeked);
	}
	
	public void testRemoveCard(){
		InPlay inPlay = new InPlay();
		Object toAdd = new Object();
		inPlay.add(toAdd);
		Object removed = inPlay.remove(0);
		assertEquals(toAdd, removed);
	}
	
	public void testCountInPlay(){
		InPlay inPlay = new InPlay();
		assertTrue(inPlay.count() == 0);
	}
	
	public void testCountInPlayAdd(){
		InPlay inPlay = new InPlay();
		inPlay.add(new Object());
		assertTrue(inPlay.count() == 1);
	}
	
	public void testSameNameReject(){
		InPlay inPlay = new InPlay();
		assertFalse(inPlay.hasItem(Card.CARDBARREL));
		inPlay.add(new Card(Card.CARDBARREL, Card.SPADES, Card.VALUEQ, Card.TYPEITEM));
		assertTrue(inPlay.hasItem(Card.CARDBARREL));
		assertFalse(inPlay.hasItem(Card.CARDAPPALOOSA));
	}
}
