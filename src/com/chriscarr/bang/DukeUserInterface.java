package com.chriscarr.bang;

import java.util.List;

public class DukeUserInterface extends TestUserInterface {
	boolean returnedInvalidLess = false;
	boolean returnedInvalidGreat = false;
	public int chooseDrawCard(Player player, List<Object> cards) {
		if(!returnedInvalidLess){
			returnedInvalidLess = true;
			return -1;	
		}else if(!returnedInvalidGreat){
			returnedInvalidGreat = true;
			return 3;
		} else {
			return 0;
		}
		
	}
}
