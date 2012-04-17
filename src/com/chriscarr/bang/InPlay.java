package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class InPlay {

	Object gun = null;
	List<Object> cards = new ArrayList<Object>();
	
	public boolean hasGun() {
		return gun != null;
	}

	public void setGun(Object object) {
		gun = object;
	}

	public void add(Object toAdd) {
		cards.add(toAdd);
	}

	public Object peek(int i) {
		return cards.get(i);
	}

	public Object remove(int i) {		
		Object removed = cards.remove(i);
		return removed;
	}

	public int count() {
		return cards.size();
	}

	public Object removeGun() {
		Object tempGun = gun;
		gun = new Card();
		gun = null;
		return tempGun;
	}

	public boolean hasItem(String cardName) {
		for(Object card : cards){
			if(((Card)card).getName().equals(cardName)){
				return true;
			}
		}
		return false;
	}

	public int getGunRange() {
		if(hasGun()){
			return Card.getRange(((Card)gun).getName());
		} else {
			return 1;
		}
	}

	public boolean isGunVolcanic() {
		return ((Card)gun).getName().equals(Card.CARDVOLCANIC);
	}

	public Object removeDynamite() {
		for(Object card : cards){
			if(((Card)card).getName().equals(Card.CARDDYNAMITE)){
				cards.remove(card);
				return card;
			}
		}
		return null;
	}

	public Object removeJail() {
		for(Object card : cards){
			if(((Card)card).getName().equals(Card.CARDJAIL)){
				cards.remove(card);
				return card;
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public int size() {
		return cards.size();
	}

	public Object get(int i) {
		return cards.get(i);
	}

	public String getGunName() {
		if(hasGun()){
			return ((Card)gun).getName();
		} else {
			return "Colt .45";
		}
	}

	public Object getGun() {
		return gun;
	}

	public List<GameStateCard> getGameStateInPlay() {
		List<GameStateCard> gameStateCards = new ArrayList<GameStateCard>();
		for(Object card : cards){
			gameStateCards.add(Turn.cardToGameStateCard(((Card)card)));
		}
		return gameStateCards;
	}
}
