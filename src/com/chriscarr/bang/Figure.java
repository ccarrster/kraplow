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
	
	public static String CHUCKWENGAM = "Chuck Wengam";
	public static String ELENAFUENTE = "Elena Fuente";
	public static String BELLESTAR = "Belle Star";
	public static String JOSEDELGADO = "Jose Delgado";
	public static String GREGDIGGER = "Greg Digger";
	public static String DOCHOLYDAY = "Doc Holyday";
	public static String PIXIEPETE = "Pixie Pete";
	public static String MOLLYSTARK = "Molly Stark";
	public static String APACHEKID = "Apache Kid";
	public static String HERBHUNTER = "Herb Hunter";
	public static String BILLNOFACE = "Bill Noface";
	public static String PATBRENNAN = "Pat Brennan";
	public static String TEQUILAJOE = "Tequila Joe";
	public static String VERACUSTER = "Vera Custer";
	public static String SEANMALLORY = "Sean Mallory";
	public static String[] CHARACTERSSIDESTEP = {CHUCKWENGAM, ELENAFUENTE, BELLESTAR, JOSEDELGADO, GREGDIGGER, DOCHOLYDAY, PIXIEPETE, MOLLYSTARK, APACHEKID, HERBHUNTER, BILLNOFACE, PATBRENNAN, TEQUILAJOE, VERACUSTER, SEANMALLORY};
	public static int PLAYBANG = 0;
	public static int PLAYMISSED = 1;
	public static int GETSHOT = 2;
	public static int PLAYONEEACH = 3;
	
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static int getStartingHealth(String name) {
		if(name.equals(PAULREGRET) || name.equals(ELGRINGO) || name.equals(VERACUSTER) || name.equals(APACHEKID) || name.equals(PIXIEPETE) || name.equals(ELENAFUENTE) || name.equals(SEANMALLORY)){
			return 3;
		} else {
			return 4;
		}
	}
	
	public static String getSpecialAbilityText(String name){
		if(name.equals(CALAMITYJANET)){
			return "Shoots can be misses and misses can be shoots";
		} else if(name.equals(JOURDONNAIS)){
			return "Has a barrel at all times";
		} else if(name.equals(PAULREGRET)){
			return "Has a mustang at all times";
		} else if(name.equals(KITCARLSON)){
			return "Draws 3 cards, returns 1 to deck";
		} else if(name.equals(BARTCASSIDY)){
			return "When damaged draws from the deck";
		} else if(name.equals(JESSEJONES)){
			return "Can draw first card from other player or deck";
		} else if(name.equals(PEDRORAMIREZ)){
			return "Can draw first card from discard";
		} else if(name.equals(ELGRINGO)){
			return "Draws a card from the hand of the player that damaged him";
		} else if(name.equals(ROSEDOOLAN)){
			return "Has an Scope at all times";
		} else if(name.equals(SUZYLAFAYETTE)){
			return "Draws a card when hand is empty";
		} else if(name.equals(BLACKJACK)){
			return "Shows second draw card, if Diamond or Heart, draws another card";
		} else if(name.equals(SIDKETCHUM)){
			return "Can discard 2 cards to gain 1 life";
		} else if(name.equals(SLABTHEKILLER)){
			return "2 misses required to cancel his Shoots";
		} else if(name.equals(LUCKYDUKE)){
			return "Chooses between 2 drawn cards instead of 1";
		} else if(name.equals(VULTURESAM)){
			return "Takes dead players cards";
		} else if(name.equals(WILLYTHEKID)){
			return "Not restricted to 1 Shoot";
		} else if(name.equals(CHUCKWENGAM)){
			return "Change one life for 2 cards";
		} else if(name.equals(ELENAFUENTE)){
			return "Any card can be a miss";
		} else if(name.equals(BELLESTAR)){
			return "On her turn others cards do not work";
		} else if(name.equals(JOSEDELGADO)){
			return "On his turn discard blue card to draw 2";
		} else if(name.equals(GREGDIGGER)){
			return "When other player killed, get 2 life";
		} else if(name.equals(DOCHOLYDAY)){
			return "On his turn, discard 2 cards to Shoot";
		} else if(name.equals(PIXIEPETE)){
			return "Draws 4 cards";
		} else if(name.equals(MOLLYSTARK)){
			return "When playing cards out of turn, draw 1";
		} else if(name.equals(APACHEKID)){
			return "Others diamond cards do not work";
		} else if(name.equals(HERBHUNTER)){
			return "When other dies, draw 2 cards";
		} else if(name.equals(BILLNOFACE)){
			return "Draws 1 card and an extra for each missing health";
		} else if(name.equals(PATBRENNAN)){
			return "Can draw a card infront of another player";
		} else if(name.equals(TEQUILAJOE)){ 
			return "Beer gives him 2 life";
		} else if(name.equals(VERACUSTER)){
			return "Copies abilities of others";
		} else if(name.equals(SEANMALLORY)){
			return "Has no handsize limit";
		} else {
			throw new RuntimeException("Invalid player name");
		}
	}

}
