package com.chriscarr.bang;

public class Figure {

	
	public static String CALAMITYJANET = "Calamity Janet";
	public static String JOURDONNAIS = "Jourdonnais";
	public static String PAULREGRET = "Paul Regret";
	public static String KITCARLSON = "Kit Carlson";
	public static String BARTCASSIDY = "Bart Cassidy";
	public static String JESSEJONES = "Jesse Jones";
	public static String PEDRORAMIREZ = "Pedro Ramirez";
	public static String ELGRINGO = "El Gringo";
	public static String ROSEDOOLAN = "Rose Doolan";
	public static String SUZYLAFAYETTE = "Suzy Lafayette";
	public static String BLACKJACK = "Black Jack";
	public static String SIDKETCHUM = "Sid Ketchum";
	public static String SLABTHEKILLER = "Slab the Killer";
	public static String LUCKYDUKE = "Lucky Duke";
	public static String VULTURESAM = "Vulture Sam";
	public static String WILLYTHEKID = "Willy the Kid";
	public static String[] CHARACTERS = {CALAMITYJANET, JOURDONNAIS, PAULREGRET, KITCARLSON, BARTCASSIDY, JESSEJONES, PEDRORAMIREZ, ELGRINGO, ROSEDOOLAN, SUZYLAFAYETTE, BLACKJACK, SIDKETCHUM, SLABTHEKILLER, LUCKYDUKE, VULTURESAM, WILLYTHEKID};
	
	public static int PLAYBANG = 0;
	public static int PLAYMISSED = 1;
	public static int GETSHOT = 2;
	public static int PLAYONEEACH = 3;
	
	private String name;
	private String description;
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static int getStartingHealth(String name) {
		if(name.equals(PAULREGRET) || name.equals(ELGRINGO)){
			return 3;
		} else {
			return 4;
		}
	}

}
