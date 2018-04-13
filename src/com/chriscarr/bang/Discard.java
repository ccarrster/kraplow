package com.chriscarr.bang;

import java.util.ArrayList;
import java.util.List;

public class Discard {

	List<Object> cards = new ArrayList<Object>();
	
	public void add(Object object) {
		cards.add(object);
	}

	public Object peek() {
		return cards.get(cards.size() - 1);
	}

	public Object remove() {
		return cards.remove(cards.size() - 1);
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}
	
	public int size() {
		return cards.size();
	}

}
