package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	private List<Object> cards = new ArrayList<Object>();
	private Discard discard;
	
	public void add(Object card) {
		cards.add(card);
	}

	public Object pull() {
		if(cards.size() == 0){
			while(!discard.isEmpty()){
				cards.add(discard.remove());
			}
			shuffle();
		}
		return cards.remove(cards.size() - 1);
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public void setDiscard(Discard discard) {
		this.discard = discard;
	}

	public int size() {
		return cards.size();
	}

}
