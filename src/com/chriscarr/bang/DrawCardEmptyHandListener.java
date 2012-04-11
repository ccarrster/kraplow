package com.chriscarr.bang;

public class DrawCardEmptyHandListener implements EmptyHandListener {

	private Deck deck;
	private Hand hand;
	
	public DrawCardEmptyHandListener(Deck deck, Hand hand){
		this.deck = deck;
		this.hand = hand;
	}
	
	public void handleEmptyHand() {
		hand.add(deck.pull());
	}

}
