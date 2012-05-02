package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class Hand {

	List<Object> cards = new ArrayList<Object>();
	private EmptyHandListener emptyListener;
	
	public Hand(){
		emptyListener = new DoNothingEmptyHandListener();
	}
	
	public void add(Object object) {
		cards.add(object);
	}

	public Object get(int i) {		
		return cards.get(i);
	}

	public int size() {
		return cards.size();
	}

	public Object remove(int card) {
		Object removedCard = cards.remove(card);
		if(cards.isEmpty()){
			emptyListener.handleEmptyHand();
		}
		return removedCard;
	}

	public int countBangs() {
		int bangs = 0;
		for(Object card : cards){
			if(((Card)card).getName().equals(Card.CARDBANG)){
				bangs = bangs + 1;
			}
		}
		return bangs;
	}

	public int countMisses() {
		int bangs = 0;
		for(Object card : cards){
			if(((Card)card).getName().equals(Card.CARDMISSED)){
				bangs = bangs + 1;
			}
		}
		return bangs;
	}

	public Object removeMiss() {
		for(Object card : cards){
			if(((Card)card).getName().equals(Card.CARDMISSED)){
				cards.remove(card); 
				if(cards.isEmpty()){
					emptyListener.handleEmptyHand();
				}
				return card;
			}
		}
		return null;
	}

	public Object removeRandom() {
		Object removedCard = cards.remove((int)(Math.random() * cards.size()));
		if(cards.isEmpty()){
			emptyListener.handleEmptyHand();
		}
		return removedCard;
	}

	public int countBeers() {
		int beers = 0;
		for(Object card : cards){
			if(((Card)card).getName().equals(Card.CARDBEER)){
				beers = beers + 1;
			}
		}
		return beers;
	}

	public Object removeBeer() {
		for(Object card : cards){
			if(((Card)card).getName().equals(Card.CARDBEER)){
				cards.remove(card); 
				if(cards.isEmpty()){
					emptyListener.handleEmptyHand();
				}
				return card;
			}
		}
		return null;
	}

	public void setEmptyListener(EmptyHandListener emptyListener) {
		this.emptyListener = emptyListener;
	}

	public void remove(Object card) {
		cards.remove(card);
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

}
