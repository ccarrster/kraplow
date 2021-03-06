package com.chriscarr.bang.cards;

import java.util.ArrayList;


public class BangDeck {
	public static ArrayList<Card> makeDeck(){
		ArrayList<Card> cards = new ArrayList<Card>();
		//Blue cards
		cards.add(new Card(Card.CARDBARREL, Card.SPADES, Card.VALUEQ, Card.TYPEITEM));
		cards.add(new Card(Card.CARDSCOPE, Card.SPADES, Card.VALUEA, Card.TYPEITEM));
		cards.add(new Card(Card.CARDMUSTANG, Card.HEARTS, Card.VALUE8, Card.TYPEITEM));
		cards.add(new Card(Card.CARDMUSTANG, Card.HEARTS, Card.VALUE9, Card.TYPEITEM));
		cards.add(new Jail(Card.CARDJAIL, Card.SPADES, Card.VALUEJ, Card.TYPEITEM));
		cards.add(new Jail(Card.CARDJAIL, Card.SPADES, Card.VALUE10, Card.TYPEITEM));
		cards.add(new Jail(Card.CARDJAIL, Card.HEARTS, Card.VALUE4, Card.TYPEITEM));
		cards.add(new Card(Card.CARDDYNAMITE, Card.HEARTS, Card.VALUE2, Card.TYPEITEM));
		cards.add(new Gun(Card.CARDSCHOFIELD, Card.SPADES, Card.VALUEK, Card.TYPEGUN));
		cards.add(new Gun(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEJ, Card.TYPEGUN));
		cards.add(new Gun(Card.CARDSCHOFIELD, Card.CLUBS, Card.VALUEQ, Card.TYPEGUN));
		cards.add(new Gun(Card.CARDVOLCANIC, Card.SPADES, Card.VALUE10, Card.TYPEGUN));
		cards.add(new Gun(Card.CARDVOLCANIC, Card.CLUBS, Card.VALUE10, Card.TYPEGUN));
		cards.add(new Gun(Card.CARDREMINGTON, Card.CLUBS, Card.VALUEK, Card.TYPEGUN));
		cards.add(new Gun(Card.CARDWINCHESTER, Card.SPADES, Card.VALUE8, Card.TYPEGUN));
		cards.add(new Card(Card.CARDBARREL, Card.SPADES, Card.VALUEK, Card.TYPEITEM));
		cards.add(new Gun(Card.CARDREVCARBINE, Card.CLUBS, Card.VALUEA, Card.TYPEGUN));
		//Brown cards
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE7, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE3, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE4, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE5, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.HEARTS, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE5, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE2, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.HEARTS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.HEARTS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE4, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE2, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.SPADES, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE3, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.DIAMONDS, Card.VALUE7, Card.TYPEPLAY));

		cards.add(new Missed(Card.CARDMISSED, Card.SPADES, Card.VALUE7, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.SPADES, Card.VALUE3, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.SPADES, Card.VALUE5, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.CLUBS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.CLUBS, Card.VALUEA, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.SPADES, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.CLUBS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.CLUBS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.SPADES, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.CLUBS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.SPADES, Card.VALUE2, Card.TYPEPLAY));
		cards.add(new Missed(Card.CARDMISSED, Card.SPADES, Card.VALUE4, Card.TYPEPLAY));

		cards.add(new Beer(Card.CARDBEER, Card.HEARTS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Beer(Card.CARDBEER, Card.HEARTS, Card.VALUE7, Card.TYPEPLAY));
		cards.add(new Beer(Card.CARDBEER, Card.HEARTS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Beer(Card.CARDBEER, Card.HEARTS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Beer(Card.CARDBEER, Card.HEARTS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new Beer(Card.CARDBEER, Card.HEARTS, Card.VALUEJ, Card.TYPEPLAY));

		cards.add(new Panic(Card.CARDPANIC, Card.DIAMONDS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Panic(Card.CARDPANIC, Card.HEARTS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new Panic(Card.CARDPANIC, Card.HEARTS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Panic(Card.CARDPANIC, Card.HEARTS, Card.VALUEA, Card.TYPEPLAY));

		cards.add(new CatBalou(Card.CARDCATBALOU, Card.DIAMONDS, Card.VALUE10, Card.TYPEPLAY));
		cards.add(new CatBalou(Card.CARDCATBALOU, Card.DIAMONDS, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new CatBalou(Card.CARDCATBALOU, Card.DIAMONDS, Card.VALUEJ, Card.TYPEPLAY));
		cards.add(new CatBalou(Card.CARDCATBALOU, Card.HEARTS, Card.VALUEK, Card.TYPEPLAY));

		cards.add(new Duel(Card.CARDDUEL, Card.CLUBS, Card.VALUE8, Card.TYPEPLAY));
		cards.add(new Duel(Card.CARDDUEL, Card.DIAMONDS, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new Duel(Card.CARDDUEL, Card.SPADES, Card.VALUEJ, Card.TYPEPLAY));

		cards.add(new Stagecoach(Card.CARDSTAGECOACH, Card.SPADES, Card.VALUE9, Card.TYPEPLAY));
		cards.add(new Stagecoach(Card.CARDSTAGECOACH, Card.SPADES, Card.VALUE9, Card.TYPEPLAY));

		cards.add(new Indians(Card.CARDINDIANS, Card.DIAMONDS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Indians(Card.CARDINDIANS, Card.DIAMONDS, Card.VALUEA, Card.TYPEPLAY));

		cards.add(new GeneralStore(Card.CARDGENERALSTORE, Card.SPADES, Card.VALUEQ, Card.TYPEPLAY));
		cards.add(new GeneralStore(Card.CARDGENERALSTORE, Card.SPADES, Card.VALUEQ, Card.TYPEPLAY));

		cards.add(new Gatling(Card.CARDGATLING, Card.HEARTS, Card.VALUE10, Card.TYPEPLAY));

		cards.add(new Saloon(Card.CARDSALOON, Card.HEARTS, Card.VALUE5, Card.TYPEPLAY));

		cards.add(new WellsFargo(Card.CARDWELLSFARGO, Card.HEARTS, Card.VALUE3, Card.TYPEPLAY));
		return cards;
	}

	public static ArrayList<Card> makeSidestepDeck(){
		ArrayList<Card> cards = new ArrayList<Card>();
		//Blue cards
		cards.add(new Gun(Card.CARDREMINGTON, Card.DIAMONDS, Card.VALUE6, Card.TYPEGUN));
		cards.add(new Gun(Card.CARDREVCARBINE, Card.SPADES, Card.VALUE5, Card.TYPEGUN));
		cards.add(new Card(Card.CARDBARREL, Card.CLUBS, Card.VALUEA, Card.TYPEITEM));
		cards.add(new Card(Card.CARDDYNAMITE, Card.CLUBS, Card.VALUE10, Card.TYPEITEM));
		cards.add(new Card(Card.CARDMUSTANG, Card.HEARTS, Card.VALUE5, Card.TYPEITEM));
		cards.add(new Card(Card.CARDSILVER, Card.DIAMONDS, Card.VALUE10, Card.TYPEITEM));
		cards.add(new Card(Card.CARDHIDEOUT, Card.DIAMONDS, Card.VALUEK, Card.TYPEITEM));
		cards.add(new Card(Card.CARDHIDEOUT, Card.DIAMONDS, Card.VALUEK, Card.TYPEITEM));

		//Green cards
		cards.add(new Conestoga(Card.CARDCONESTOGA, Card.DIAMONDS, Card.VALUE9, Card.TYPESINGLEUSEITEM));
		cards.add(new BuffaloRifle(Card.CARDBUFFALORIFLE, Card.CLUBS, Card.VALUEQ, Card.TYPESINGLEUSEITEM));
		cards.add(new CanCan(Card.CARDCANCAN, Card.CLUBS, Card.VALUEJ, Card.TYPESINGLEUSEITEM));
		cards.add(new Howitzer(Card.CARDHOWITZER, Card.SPADES, Card.VALUE9, Card.TYPESINGLEUSEITEM));
		cards.add(new Canteen(Card.CARDCANTEEN, Card.HEARTS, Card.VALUE7, Card.TYPESINGLEUSEITEM));
		cards.add(new Knife(Card.CARDKNIFE, Card.HEARTS, Card.VALUE8, Card.TYPESINGLEUSEITEM));
		cards.add(new Pepperbox(Card.CARDPEPPERBOX, Card.CLUBS, Card.VALUE10, Card.TYPESINGLEUSEITEM));
		cards.add(new Derringer(Card.CARDDERRINGER, Card.SPADES, Card.VALUE7, Card.TYPESINGLEUSEITEM));
		cards.add(new PonyExpress(Card.CARDPONYEXPRESS, Card.DIAMONDS, Card.VALUEQ, Card.TYPESINGLEUSEITEM));
		cards.add(new SingleUseMissed(Card.CARDSOMBRERO, Card.CLUBS, Card.VALUE7, Card.TYPESINGLEUSEITEM));
		cards.add(new SingleUseMissed(Card.CARDBIBLE, Card.HEARTS, Card.VALUE10, Card.TYPESINGLEUSEITEM));
		cards.add(new SingleUseMissed(Card.CARDIRONPLATE, Card.DIAMONDS, Card.VALUEA, Card.TYPESINGLEUSEITEM));
		cards.add(new SingleUseMissed(Card.CARDIRONPLATE, Card.SPADES, Card.VALUEQ, Card.TYPESINGLEUSEITEM));
		cards.add(new SingleUseMissed(Card.CARDTENGALLONHAT, Card.DIAMONDS, Card.VALUEJ, Card.TYPESINGLEUSEITEM));

		//Brown cards
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE5, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUEK, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.CLUBS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Bang(Card.CARDBANG, Card.SPADES, Card.VALUE8, Card.TYPEPLAY));

		cards.add(new Missed(Card.CARDMISSED, Card.DIAMONDS, Card.VALUE8, Card.TYPEPLAY));

		cards.add(new Beer(Card.CARDBEER, Card.HEARTS, Card.VALUE6, Card.TYPEPLAY));
		cards.add(new Beer(Card.CARDBEER, Card.SPADES, Card.VALUE6, Card.TYPEPLAY));

		cards.add(new Panic(Card.CARDPANIC, Card.HEARTS, Card.VALUEJ, Card.TYPEPLAY));

		cards.add(new CatBalou(Card.CARDCATBALOU, Card.CLUBS, Card.VALUE8, Card.TYPEPLAY));

		cards.add(new Indians(Card.CARDINDIANS, Card.DIAMONDS, Card.VALUE5, Card.TYPEPLAY));

		cards.add(new RagTime(Card.CARDRAGTIME, Card.HEARTS, Card.VALUE9, Card.TYPEPLAY));

		cards.add(new Dodge(Card.CARDDODGE, Card.DIAMONDS, Card.VALUE7, Card.TYPEPLAY));
		cards.add(new Dodge(Card.CARDDODGE, Card.HEARTS, Card.VALUEK, Card.TYPEPLAY));

		cards.add(new Whisky(Card.CARDWHISKY, Card.HEARTS, Card.VALUEQ, Card.TYPEPLAY));

		cards.add(new Punch(Card.CARDPUNCH, Card.SPADES, Card.VALUE10, Card.TYPEPLAY));

		cards.add(new Tequila(Card.CARDTEQUILA, Card.CLUBS, Card.VALUE9, Card.TYPEPLAY));

		cards.add(new Brawl(Card.CARDBRAWL, Card.SPADES, Card.VALUEJ, Card.TYPEPLAY));

		cards.add(new Springfield(Card.CARDSPRINGFIELD, Card.SPADES, Card.VALUEJ, Card.TYPEPLAY));




		return cards;
	}
}
