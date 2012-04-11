package com.chriscarr.bang;

import java.util.ArrayList;

public class BangDeck {	
	public static ArrayList<Card> makeDeck(){
		ArrayList<Card> cards = new ArrayList<Card>();		
		//Blue cards
		cards.add(new Card(Card.CARDBARREL, Card.SPADES, Card.VALUEQ, Card.TYPEITEM));
		cards.add(new Card(Card.CARDAPPALOOSA, Card.SPADES, Card.VALUEA, Card.TYPEITEM));
		cards.add(new Card(Card.CARDMUSTANG, Card.HEARTS, Card.VALUE8, Card.TYPEITEM));
		cards.add(new Card(Card.CARDMUSTANG, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		cards.add(new Card(Card.CARDJAIL, Card.SPADES, Card.VALUEJ, Card.TYPEITEM));
		cards.add(new Card(Card.CARDJAIL, Card.SPADES, Card.VALUE10, Card.TYPEITEM));
		cards.add(new Card(Card.CARDJAIL, Card.HEARTS, Card.VALUE4, Card.TYPEITEM));			
		cards.add(new Card(Card.CARDDYNAMITE, Card.HEARTS, Card.VALUE2, Card.TYPEITEM));
		cards.add(new Card(Card.CARDSCHOFIELD, Card.SPADES, Card.VALUEK, Card.TYPEGUN));
		cards.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEJ, Card.TYPEGUN));
		cards.add(new Card(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		cards.add(new Card(Card.CARDVOLCANIC, Card.SPADES, Card.VALUE10, Card.TYPEGUN));
		cards.add(new Card(Card.CARDVOLCANIC, Card.CLUBS, Card.VALUE10, Card.TYPEGUN));
		cards.add(new Card(Card.CARDREMINGTON, Card.CLUBS, Card.VALUEK, Card.TYPEGUN));
		cards.add(new Card(Card.CARDWINCHESTER, Card.SPADES, Card.VALUE8, Card.TYPEGUN));
		cards.add(new Card(Card.CARDBARREL, Card.SPADES, Card.VALUEK, Card.TYPEITEM));
		cards.add(new Card(Card.CARDREVCARBINE, Card.CLUBS, Card.VALUEA, Card.TYPEGUN));
		//Brown cards						
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE7, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE3, Card.TYPEPLAY));			
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE4, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE5, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.HEARTS, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE5, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE2, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.HEARTS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.HEARTS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE4, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.CLUBS, Card.VALUE2, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.SPADES, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE3, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBANG, Card.DIAMONDS, Card.VALUE7, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDMISSED, Card.SPADES, Card.VALUE7, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.SPADES, Card.VALUE3, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.SPADES, Card.VALUE5, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.SPADES, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.SPADES, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.CLUBS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.SPADES, Card.VALUE2, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDMISSED, Card.SPADES, Card.VALUE4, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE7, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDBEER, Card.HEARTS, Card.VALUEJ, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDPANIC, Card.DIAMONDS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDPANIC, Card.HEARTS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDPANIC, Card.HEARTS, Card.VALUEQ, Card.TYPEPLAY));			
		cards.add(new Card(Card.CARDPANIC, Card.HEARTS, Card.VALUEA, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDCATBALOU, Card.DIAMONDS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDCATBALOU, Card.DIAMONDS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDCATBALOU, Card.DIAMONDS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDCATBALOU, Card.HEARTS, Card.VALUEK, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDDUEL, Card.CLUBS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDDUEL, Card.DIAMONDS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDDUEL, Card.SPADES, Card.VALUEJ, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDSTAGECOACH, Card.SPADES, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDSTAGECOACH, Card.SPADES, Card.VALUE9, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDINDIANS, Card.DIAMONDS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDINDIANS, Card.DIAMONDS, Card.VALUEA, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDGENERALSTORE, Card.SPADES, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Card(Card.CARDGENERALSTORE, Card.SPADES, Card.VALUEQ, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDGATLING, Card.HEARTS, Card.VALUE10, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDSALOON, Card.HEARTS, Card.VALUE5, Card.TYPEPLAY));
		
		cards.add(new Card(Card.CARDWELLSFARGO, Card.HEARTS, Card.VALUE3, Card.TYPEPLAY));
		return cards;		
	}
}
