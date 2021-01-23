package com.chriscarr.bang.test;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;

import junit.framework.TestCase;

public class DeckTest extends TestCase {
	public void testDeckAdd(){
		Deck deck = new Deck();
		Object card = new Object();
		deck.add(card);
		Object outCard = deck.pull();
		assertEquals(card, outCard);
	}
	
	public void testDeckRunOut(){
		Deck deck = new Deck();
		assertTrue(deck.isEmpty());
	}
	
	public void testDeckAddRunOut(){
		Deck deck = new Deck();
		deck.add(new Object());
		assertFalse(deck.isEmpty());
	}
	
	public void testDeckAddPullRunOut(){
		Deck deck = new Deck();
		deck.add(new Object());
		deck.pull();
		assertTrue(deck.isEmpty());
	}
	
	public void testDeckPullOrder(){
		Deck deck = new Deck();
		Object card1 = new Object();
		Object card2 = new Object();
		deck.add(card1);
		deck.add(card2);
		Object pulled = deck.pull();
		assertEquals(pulled, card2);
	}
	
	public void testShuffle(){	
		boolean sameOrder = false;
		boolean reverseOrder = false;
		for(int i = 0; i < 100 && (!sameOrder || !reverseOrder); i ++){
			Deck deck = new Deck();
			Object card1 = new Object();
			Object card2 = new Object();
			deck.add(card1);
			deck.add(card2);		
			deck.shuffle();
			Object pulled1 = deck.pull();
			Object pulled2 = deck.pull();
			if(pulled1.equals(card1) && pulled2.equals(card2)){
				sameOrder = true;
			} else {
				reverseOrder = true;
			}
		}
		assertTrue(sameOrder && reverseOrder);
	}
	
	public void testEmptyDeckDiscardShuffle(){
		Deck deck = new Deck();
		Discard discard = new Discard();
		Object discarded = new Object();
		discard.add(discarded);
		deck.setDiscard(discard);
		Object pulled = deck.pull();
		assertEquals(pulled, discarded);
	}
}
