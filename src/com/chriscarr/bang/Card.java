package com.chriscarr.bang;

public class Card {

	public static final int HEARTS = 0;
	public static final int CLUBS = 1;
	public static final int SPADES = 2;
	public static final int DIAMONDS = 3;
	public static final int VALUE2 = 0;
	public static final int VALUE3 = 1;
	public static final int VALUE4 = 2;
	public static final int VALUE5 = 3;
	public static final int VALUE6 = 4;
	public static final int VALUE7 = 5;
	public static final int VALUE8 = 6;
	public static final int VALUE9 = 7;
	public static final int VALUE10 = 8;
	public static final int VALUEJ = 9;
	public static final int VALUEQ = 10;
	public static final int VALUEK = 11;
	public static final int VALUEA = 12;
	public static final int TYPEGUN = 0;
	public static final int TYPEITEM = 1;
	public static final int TYPEPLAY = 2;
	public static final String CARDBARREL = "Barrel";
	public static final String CARDAPPALOOSA = "Appaloosa";
	public static final String CARDMUSTANG = "Mustang";
	public static final String CARDJAIL = "Jail";
	public static final String CARDDYNAMITE = "Dynamite";
	public static final String CARDSCHOFIELD = "Schofield";
	public static final String CARDVOLCANIC = "Volcanic";
	public static final String CARDREMINGTON = "Remington";
	public static final String CARDWINCHESTER = "Winchester";
	public static final String CARDREVCARBINE = "Rev. Carbine";
	public static final String CARDBANG = "Bang!";
	public static final String CARDMISSED = "Missed!";
	public static final String CARDBEER = "Beer";
	public static final String CARDPANIC = "Panic!";
	public static final String CARDCATBALOU = "Cat Balou";
	public static final String CARDDUEL = "Duel";
	public static final String CARDSTAGECOACH = "Stagecoach";
	public static final String CARDINDIANS = "Indians!";
	public static final String CARDGENERALSTORE = "General Store";
	public static final String CARDGATLING = "Gatling";
	public static final String CARDSALOON = "Saloon";
	public static final String CARDWELLSFARGO = "Wells Fargo";
	
	private String name;
	private int suit;
	private int value;
	private int type;
	
	public Card(){
		
	}
	
	public Card(String name, int suit, int value, int type) {
		this.name = name;
		this.suit = suit;
		this.value = value;		
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	public int getSuit() {
		return suit;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public static int getRange(String gunName){
		if(gunName.equals(CARDREVCARBINE)){
			return 5;
		} else if(gunName.equals(CARDWINCHESTER)){
			return 4;
		} else if(gunName.equals(CARDREMINGTON)){
			return 3;
		} else if(gunName.equals(CARDSCHOFIELD)){
			return 2;
		} else {
			return 1;
		}
	}

	public static boolean multiBang(String gunName) {
		return gunName.equals(CARDVOLCANIC);
	}

	public static boolean isExplode(Card drawnCard) {
		if(drawnCard.suit == SPADES){
			if(drawnCard.value < 8){
				return true;
			}
		}
		return false;
	}
	
	public static String suitToString(int suit){
		if(suit == HEARTS){
			return "Hearts";
		} else if(suit == CLUBS){
			return "Clubs";
		} else if(suit == SPADES){
			return "Spades";
		} else if(suit == DIAMONDS){
			return "Diamonds";
		} else {
			throw new RuntimeException("Invalid Suit");
		}
	}
	
	public static String valueToString(int value){
		if(value == VALUE2){
			return "2";
		} else if(value == VALUE3){
			return "3";
		} else if(value == VALUE4){
			return "4";
		} else if(value == VALUE5){
			return "5";
		} else if(value == VALUE6){
			return "6";
		} else if(value == VALUE7){
			return "7";
		} else if(value == VALUE8){
			return "8";
		} else if(value == VALUE9){
			return "9";
		} else if(value == VALUE10){
			return "10";
		} else if(value == VALUEJ){
			return "J";
		} else if(value == VALUEQ){
			return "Q";
		} else if(value == VALUEK){
			return "K";
		} else if(value == VALUEA){
			return "A";
		} else {
			throw new RuntimeException("Invalid value");
		}
	}
}
