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
		return cards.remove(i);
	}

	public int count() {
		return cards.size();
	}

	public Object removeGun() {
		Object tempGun = gun;
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
}
