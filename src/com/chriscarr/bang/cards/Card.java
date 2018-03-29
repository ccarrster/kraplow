package com.chriscarr.bang.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import com.chriscarr.bang.Deck;
import com.chriscarr.bang.Discard;
import com.chriscarr.bang.Player;
import com.chriscarr.bang.Turn;
import com.chriscarr.bang.Hand;
import com.chriscarr.bang.Figure;
import com.chriscarr.bang.InPlay;
import com.chriscarr.bang.CancelPlayer;
import com.chriscarr.bang.userinterface.UserInterface;

public class Card implements Playable{

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
	public static final int TYPESINGLEUSEITEM = 3;
	public static final String CARDBARREL = "Barrel";
	public static final String CARDSCOPE = "Scope";
	public static final String CARDMUSTANG = "Mustang";
	public static final String CARDJAIL = "Jail";
	public static final String CARDDYNAMITE = "Dynamite";
	public static final String CARDSCHOFIELD = "Schofield";
	public static final String CARDVOLCANIC = "Volcanic";
	public static final String CARDREMINGTON = "Remington";
	public static final String CARDWINCHESTER = "Winchester";
	public static final String CARDREVCARBINE = "Rev. Carbine";
	public static final String CARDBANG = "Shoot";
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
	public static final String CARDRAGTIME = "Rag Time";
	public static final String CARDDODGE = "Dodge";
	public static final String CARDWHISKY = "Whisky";
	public static final String CARDHIDEOUT = "Hideout";
	public static final String CARDSILVER = "Silver";
	public static final String CARDPUNCH = "Punch";
	public static final String CARDBRAWL = "Brawl";
	public static final String CARDTEQUILA = "Tequila";
	public static final String CARDSPRINGFIELD = "Springfield";
	public static final String CARDCONESTOGA = "Conestoga";
	public static final String CARDBUFFALORIFLE = "Buffalo Rifle";
	public static final String CARDCANCAN = "Can Can";
	public static final String CARDHOWITZER = "Howitzer";
	public static final String CARDSOMBRERO = "Sombrero";
	public static final String CARDBIBLE = "Bible";
	public static final String CARDCANTEEN = "Canteen";
	public static final String CARDIRONPLATE = "Iron Plate";
	public static final String CARDKNIFE = "Knife";
	public static final String CARDPEPPERBOX = "Pepperbox";
	public static final String CARDDERRINGER = "Derringer";
	public static final String CARDTENGALLONHAT = "Ten Gallon Hat";
	public static final String CARDPONYEXPRESS = "Pony Express";

	
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

	@Override
	public boolean canPlay(Player player, List<Player> players, int bangsPlayed) {
		return !player.isInPlay(name);
	}

	@Override
	public boolean play(Player currentPlayer, List<Player> players,
			UserInterface userInterface, Deck deck, Discard discard, Turn turn) {
		currentPlayer.addInPlay(this);
		return true;
	}

	@Override
	public List<Player> targets(Player player, List<Player> players) {
		List<Player> targets = new ArrayList<Player>();
		targets.add(player);
		return targets;
	}

	public static String typeToString(int type) {
		if(TYPEGUN == type || TYPEITEM == type || TYPESINGLEUSEITEM == type){
			return "Item";
		} else {
			return "Play";
		}		
	}

	public boolean shoot(Player currentPlayer, List<Player> players, UserInterface userInterface, Deck deck, Discard discard, Turn turn, boolean skipDiscard){
		if(skipDiscard){
			discard.add(this);
		}
		Player otherPlayer = null;
		if(this.getName() != CARDGATLING && this.getName() != CARDHOWITZER){
			otherPlayer = Turn.getValidChosenPlayer(currentPlayer, targets(currentPlayer, players), userInterface);
		} else {
			otherPlayer = players.get(0);
		}
		if(!(otherPlayer instanceof CancelPlayer)){
			userInterface.printInfo(currentPlayer.getName() + " Shoots " + otherPlayer.getName());
			if(Figure.APACHEKID.equals(otherPlayer.getAbility()) && this.getSuit() == Card.DIAMONDS){
				userInterface.printInfo(otherPlayer.getName() + " is unaffected by diamond "+this.getName());
				return true;
			}
			int missesRequired = 1;
			if(this.getName() == Card.CARDBANG && Figure.SLABTHEKILLER.equals(currentPlayer.getAbility())){
				missesRequired = 2;
			}
			int barrelMisses = Turn.isBarrelSave(otherPlayer, deck, discard, userInterface, missesRequired, currentPlayer);
			missesRequired = missesRequired - barrelMisses;
			if(missesRequired <= 0){
				return true;
			} else if(missesRequired == 1){
				int missPlayed = Turn.validPlayMiss(otherPlayer, userInterface); 
				if(missPlayed == -1){
					turn.damagePlayer(otherPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
					userInterface.printInfo(otherPlayer.getName() + " is loses a health.");
				} else {
					for(int i = 0; i < missesRequired; i++){
						if(missPlayed < otherPlayer.getHand().size()){
							Card missCard = (Card)otherPlayer.getHand().remove(missPlayed);
							discard.add(missCard);
							if(missCard.getName().equals(CARDDODGE)){
								Hand otherHand = otherPlayer.getHand();
								otherHand.add(deck.pull());
								userInterface.printInfo(otherPlayer.getName() + " dodged " + currentPlayer.getName() + "'s " + Card.CARDBANG + " and draws a card");
							} else {
								userInterface.printInfo(otherPlayer.getName() + " plays a Missed!");
							}
							if(Figure.MOLLYSTARK.equals(otherPlayer.getAbility())){
								Hand otherHand = otherPlayer.getHand();
								otherHand.add(deck.pull());
								userInterface.printInfo(otherPlayer.getName() + " draws a card");
							}
						} else {
							missPlayed -= otherPlayer.getHand().size();
							InPlay inPlay = otherPlayer.getInPlay();
							SingleUseMissed sum = (SingleUseMissed)inPlay.remove(missPlayed);
							if(sum.getName().equals(Card.CARDBIBLE)){
								otherPlayer.getHand().add(deck.pull());
							}
							userInterface.printInfo(otherPlayer.getName() + " plays a "+sum.getName());
							discard.add(sum);
						}
					}
				}
			} else if(missesRequired == 2){
				Hand hand = otherPlayer.getHand();
				List<Object> cardsToDiscard = null;			
				cardsToDiscard = Turn.validRespondTwoMiss(otherPlayer, userInterface);			
				if(cardsToDiscard.size() == 0){
					turn.damagePlayer(otherPlayer, players, currentPlayer, 1, currentPlayer, deck, discard, userInterface);
					userInterface.printInfo(otherPlayer.getName() + " is loses a health.");
				} else {
					for(Object card : cardsToDiscard){
						hand.remove(card);
						discard.add(card);
						userInterface.printInfo(otherPlayer.getName() + " plays a Missed!");
						if(Figure.MOLLYSTARK.equals(otherPlayer.getAbility())){
							Hand otherHand = otherPlayer.getHand();
							otherHand.add(deck.pull());
							userInterface.printInfo(otherPlayer.getName() + " draws a card");
						}
					}
				}	
			}
			return true;
		} else {
			if(!skipDiscard){
				currentPlayer.getHand().add(this);
			}
			return false;
		}		
	}
}
